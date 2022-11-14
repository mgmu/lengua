package fr.uparis.lengua

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uparis.lengua.databinding.FragmentTranslationResearchBinding

class TranslationResearchFragment(private val adapter: DictionaryRecyclerAdapter):
    Fragment(R.layout.fragment_translation_research) {

    private lateinit var binding: FragmentTranslationResearchBinding
    private val model: TranslationViewModel by activityViewModels()

    companion object {
        @JvmStatic
        fun newInstance(adapter: DictionaryRecyclerAdapter) = TranslationResearchFragment(adapter)
    }

    override fun onStart() {

        /* Observe changes in dictionaries of database */
        model.allDictionaries.observe(this) {
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
    }
}