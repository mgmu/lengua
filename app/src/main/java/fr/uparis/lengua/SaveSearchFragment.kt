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

    private val URI_KEY = "URI_KEY"
    private lateinit var binding: FragmentSaveSearchBinding
    private val model: TranslationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save_search, container, false)
    }

    fun removeAfterLastSlashContent(uri: String): String {
        return uri.replaceAfterLast("/", "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSaveSearchBinding.bind(view)

        /* Get translation uri value if present */
        val translationUri = requireArguments().getString(URI_KEY, "")
        if (translationUri.isNotBlank()) {
            binding.translationUriEditText.setText(translationUri)
            binding.dictionaryUriEditText.setText(translationUri)
        }

        /* On Save Word button click, add word DB */
        binding.saveWordButton.setOnClickListener {

            /* Word entity fields */
            val word = binding.wordToSaveEditText.text.toString()
            val srcL = binding.sourceLanguageEditText.text.toString()
            val destL = binding.destinationLanguageEditText.text.toString()
            val uriWord = binding.translationUriEditText.text.toString()

            /* Do nothing if mandatory fields are missing */
            if (word.isBlank() || srcL.isBlank() || destL.isBlank() || uriWord.isBlank())
                return@setOnClickListener

            /* Insert word in DB */
            model.insertWord(Word(word, srcL, destL, uriWord))

            /* Reset fields */
            with (binding) {
                wordToSaveEditText.text.clear()
                sourceLanguageEditText.text.clear()
                destinationLanguageEditText.text.clear()
                translationUriEditText.text.toString()
            }
        }

        /* On Save dictionary button click, add dictionary to DB */
        binding.saveDictionaryButton.setOnClickListener {

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
            Log.d("logLENGUA", "triming...")
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
    }

    companion object {

        @JvmStatic
        fun newInstance(): SaveSearchFragment {
            return SaveSearchFragment().apply {
                arguments = Bundle() /* no arguments */
            }
        }

        @JvmStatic
        fun newInstance(uri: String): SaveSearchFragment {
            return SaveSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(URI_KEY, uri)
                }
            }
        }
    }
}