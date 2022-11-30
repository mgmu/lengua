package fr.uparis.lengua

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        private val HTTP = "http://"
        private val HTTPS = "https://"
        private val GOOGLE_URI = "www.google.fr/search?q="
        private val DICTIONARY_LIST_FRAGMENT_TAG = "tag1"

        @JvmStatic
        fun newInstance(adapter: DictionaryRecyclerAdapter) = TranslationResearchFragment(adapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentTranslationResearchBinding.bind(v!!)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Place dictionary list fragment. */
        childFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container_view,
                DictionaryListFragment.newInstance(),
                DICTIONARY_LIST_FRAGMENT_TAG
            )
            .addToBackStack(DICTIONARY_LIST_FRAGMENT_TAG)
            .commit()

        /* Launch search in web browser with selected URI on click
         * If no link is selected, launches the search with Google URI */
        binding.searchWordButton.setOnClickListener {
            val wordToSearch = binding.searchWordEditText.text.toString()
            if (wordToSearch.isNotBlank()) {
                val searchUri =
                    if (model.isDictionarySelected())
                        "${model.selectedDictionary.value!!.link}/$wordToSearch"
                    else
                        "$HTTPS$GOOGLE_URI$wordToSearch"
                startWebIntent(searchUri)
            } else
                    Toast.makeText(context, "Invalid URI...", Toast.LENGTH_SHORT).show()
        }

        /* Launch search*/
        binding.otherSearchWordButton.setOnClickListener {
            val wordToSearch = binding.searchWordEditText.text.toString()
            if (wordToSearch.isNotBlank()) {
                val searchUri = "$HTTPS$GOOGLE_URI$wordToSearch"
                startWebIntent(searchUri)
            }
        }
    }

    fun startWebIntent(uri: String) {
        if (uri.startsWith(HTTP) || uri.startsWith(HTTPS)) {
            val webIntent = Intent(Intent.ACTION_VIEW)
            webIntent.data = Uri.parse(uri)
            startActivity(webIntent)
        }
    }
}