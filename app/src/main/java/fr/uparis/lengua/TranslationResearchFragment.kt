package fr.uparis.lengua

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uparis.lengua.databinding.FragmentTranslationResearchBinding

class TranslationResearchFragment:
    Fragment(R.layout.fragment_translation_research) {

    private val GOOGLE_URI = "https://www.google.fr/search?q="

    private lateinit var binding: FragmentTranslationResearchBinding
    private lateinit var adapter: DictionaryRecyclerAdapter
    private val model: TranslationViewModel by activityViewModels()

    companion object {
        @JvmStatic
        fun newInstance() = TranslationResearchFragment()
    }

    override fun onStart() {
        adapter = DictionaryRecyclerAdapter(model)

        /* Observe changes in dictionaries of database */
        model.allDictionaries.observe(viewLifecycleOwner) {
            adapter.dictionaries = it
            adapter.notifyDataSetChanged()
        }

        /* load all dictionaries */
        model.loadAllDictionaries()

        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTranslationResearchBinding.bind(view)
        binding.dictionaryRecycler.layoutManager = LinearLayoutManager(context)
        binding.dictionaryRecycler.adapter = adapter

        /* Launch search in web browser on click */
        binding.searchWordButton.setOnClickListener {
            val wordToSearch = binding.searchWordEditText.text.toString()
            if (wordToSearch.isNotBlank()) {
                val searchUri =
                    if (model.isDictionarySelected())
                        "${model.selectedDictionary.value!!.link}/$wordToSearch"
                    else
                        "$GOOGLE_URI$wordToSearch"

                val webIntent = Intent(Intent.ACTION_VIEW)
                webIntent.data = Uri.parse(searchUri)
                startActivity(webIntent)
            }
        }
    }
}