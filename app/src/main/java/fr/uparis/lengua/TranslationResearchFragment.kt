package fr.uparis.lengua

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uparis.lengua.databinding.FragmentTranslationResearchBinding

class TranslationResearchFragment:
    Fragment(R.layout.fragment_translation_research) {

    private val HTTP = "http://"
    private val HTTPS = "https://"
    private val GOOGLE_URI = "www.google.fr/search?q="

    private lateinit var binding: FragmentTranslationResearchBinding
    private lateinit var adapter: DictionaryRecyclerAdapter
    private val model: TranslationViewModel by activityViewModels()

    companion object {
        @JvmStatic
        fun newInstance() = TranslationResearchFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = DictionaryRecyclerAdapter(model)
        Log.d("logLengua", "initialized adapter")
    }

    override fun onStart() {

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
                        "$HTTPS$GOOGLE_URI$wordToSearch"
                if (searchUri.startsWith(HTTP) || searchUri.startsWith(HTTPS)) {
                    val webIntent = Intent(Intent.ACTION_VIEW)
                    webIntent.data = Uri.parse(searchUri)
                    startActivity(webIntent)
                } else
                    Toast.makeText(context, "Invalid URI...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
