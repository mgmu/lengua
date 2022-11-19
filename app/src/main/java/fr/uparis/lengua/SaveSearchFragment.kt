package fr.uparis.lengua

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.uparis.lengua.databinding.FragmentSaveSearchBinding

class SaveSearchFragment : Fragment(R.layout.fragment_save_search) {

    private lateinit var binding: FragmentSaveSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSaveSearchBinding.bind(view)

        /* On Save button click, add word to DB */
        binding.saveSearchButton.setOnClickListener {
            val word = binding.wordToSaveEditText.text.toString()
            val src = binding.sourceLanguageEditText.text.toString()
            val dest = binding.destinationLanguageEditText.text.toString()
            val uri = binding.dictionaryUriEditText.text.toString()
            val dict = binding.dictionaryNameEditText.text.toString()

            /* Do nothing if fields are missing */
            if (word.isBlank()
                || src.isBlank()
                || dest.isBlank()
                || uri.isBlank()
                || dict.isBlank()) {
                return@setOnClickListener
            }

            TODO()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = SaveSearchFragment()
    }
}