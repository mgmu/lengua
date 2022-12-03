package fr.uparis.lengua

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import fr.uparis.lengua.databinding.FragmentWordListListBinding

/**
 * A fragment representing a list of words.
 */
class WordListFragment : Fragment() {
    private val model: TranslationViewModel by activityViewModels()
    private lateinit var binding: FragmentWordListListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_word_list_list,
            container,
            false
        )

        binding = FragmentWordListListBinding.bind(view)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = WordRecyclerViewAdapter(model)
            }
        }
        return view
    }

    override fun onStart() {

        /* Update recycler when dictionaries in DB change. */
        model.allWords.observe(viewLifecycleOwner) {
            with (binding.list.adapter) {
                if (this != null) {
                    (this as WordRecyclerViewAdapter)
                    this.words = it
                    this.notifyDataSetChanged()
                }
            }
        }

        /* load all dictionaries */
        model.loadAllWords()

        super.onStart()

    }

    companion object {
        @JvmStatic
        fun newInstance() = WordListFragment()
    }
}