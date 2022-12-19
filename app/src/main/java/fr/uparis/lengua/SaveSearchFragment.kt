package fr.uparis.lengua

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import fr.uparis.lengua.databinding.FragmentSaveSearchBinding

class SaveSearchFragment : Fragment(R.layout.fragment_save_search) {
    private lateinit var binding: FragmentSaveSearchBinding
    private val model: TranslationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "SaveSearchFragment onCreateView")
        return inflater.inflate(R.layout.fragment_save_search, container, false)
    }

    private fun removeAfterLastSlashContent(uri: String): String {
        return uri.replaceAfterLast("/", "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSaveSearchBinding.bind(view)

        Log.d(TAG, "SaveSearchFragment onViewCreated")

        /* Get translation uri value if present */
        val translationUri = requireArguments().getString(URI_KEY, "")
        if (translationUri.isNotBlank()) {
            Log.d(TAG, "SaveSearchFragment translationUri is not blank")
            binding.translationUriEditText.setText(translationUri)
            binding.dictionaryUriEditText.setText(translationUri)
        }

        /* Restore saved instance state from ViewModel */
        if (model.wordToSave != null)
            binding.wordToSaveEditText.setText(model.wordToSave)
        if (model.srcLanguage != null)
            binding.sourceLanguageEditText.setText(model.srcLanguage)
        if (model.destLanguage != null)
            binding.destinationLanguageEditText.setText(model.destLanguage)
        if (model.dictionaryName != null)
            binding.dictionaryNameEditText.setText(model.dictionaryName)

        /* On Save Word button click, add word DB */
        binding.saveWordButton.setOnClickListener {

            Log.d(TAG, "SaveSearchFragment saveWordButton on click listener")

            /* Word entity fields */
            val word = binding.wordToSaveEditText.text.toString()
            val srcL = binding.sourceLanguageEditText.text.toString()
            val destL = binding.destinationLanguageEditText.text.toString()
            val uriWord = binding.translationUriEditText.text.toString()

            /* Do nothing if mandatory fields are missing */
            if (word.isBlank() || srcL.isBlank() || destL.isBlank() || uriWord.isBlank())
                return@setOnClickListener

            /* Insert word in DB */
            model.insertWord(Word(word, srcL, destL, uriWord, 0))

            /* Reset fields */
            with (binding) {
                Log.d(TAG, "FIELDS RESET")
                wordToSaveEditText.text.clear()
                sourceLanguageEditText.text.clear()
                destinationLanguageEditText.text.clear()
                translationUriEditText.text.toString()
            }
        }

        /* On Save dictionary button click, add dictionary to DB */
        binding.saveDictionaryButton.setOnClickListener {

            Log.d(TAG, "SaveSearchFragment saveDictionaryButton on click listener")

            /* Dictionary entity fields */
            val dictName = binding.dictionaryNameEditText.text.toString()
            val dictUri = binding.dictionaryUriEditText.text.toString()

            /* Do nothing if mandatory fields are missing */
            if (dictName.isBlank() || dictUri.isBlank())
                return@setOnClickListener

            /* Insert dictionary in DB */
            model.insertDictionary(Dictionary(dictName, dictUri))

            /* Reset fields */
            with (binding) {
                dictionaryNameEditText.text.clear()
                dictionaryUriEditText.text.clear()
            }
        }

        /* On Trim Link button click, trim content after last slash it dictionary URI */
        binding.trimLinkButton.setOnClickListener {
            with (binding.dictionaryUriEditText) {
                setText(removeAfterLastSlashContent(text.toString()).removeSuffix("/"))
            }
        }

        /* Display a toast when word or dictionary insertion finishes */
        model.insertWordResult.observe(viewLifecycleOwner) {
            val msg =
                if (it != -1L)
                    "Added new word !"
                else
                    "Could not add word..."
            Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
        }

        model.insertDictionaryResult.observe(viewLifecycleOwner) {
            val msg =
                if (it != -1L)
                    "Added new dictionary !"
                else
                    "Could not add dictionary..."
            Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        private val TAG = "logSAVESEARCH"
        private val URI_KEY = "URI_KEY"

        @JvmStatic
        fun newInstance(): SaveSearchFragment {
            Log.d(TAG, "NEW INSTANCE")
            return SaveSearchFragment().apply {
                arguments = Bundle() /* no arguments */
            }
        }

        @JvmStatic
        fun newInstance(uri: String): SaveSearchFragment {
            Log.d(TAG, "NEW INSTANCE URI")
            return SaveSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(URI_KEY, uri)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "SaveSearchFragment onSaveInstanceState")
        super.onSaveInstanceState(outState)
        val word = binding.wordToSaveEditText.text
        val srcL = binding.sourceLanguageEditText.text
        val destL = binding.destinationLanguageEditText.text
        val dictName = binding.dictionaryNameEditText.text
        if (word.isNotBlank())
            model.wordToSave = word.toString()
        if (srcL.isNotBlank())
            model.srcLanguage = srcL.toString()
        if (destL.isNotBlank())
            model.destLanguage = destL.toString()
        if (dictName.isNotBlank())
            model.dictionaryName = dictName.toString()
    }
}