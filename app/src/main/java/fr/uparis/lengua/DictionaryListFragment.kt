package fr.uparis.lengua

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import fr.uparis.lengua.databinding.FragmentDictionaryListBinding
import fr.uparis.lengua.databinding.FragmentDictionaryListListBinding

/**
 * A fragment representing a list of Items.
 */
class DictionaryListFragment : Fragment() {
    private val model: TranslationViewModel by activityViewModels()
    private lateinit var binding: FragmentDictionaryListListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_dictionary_list_list,
            container,
            false
        )

        binding = FragmentDictionaryListListBinding.bind(view)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = DictionaryRecyclerViewAdapter(model)
            }
        }
        return view
    }

    override fun onStart() {

        /* Update recycler when dictionaries in DB change. */
        model.allDictionaries.observe(viewLifecycleOwner) {
            with (binding.list.adapter) {
                if (this != null) {
                    (this as DictionaryRecyclerViewAdapter)
                    this.dictionaries = it
                    this.notifyDataSetChanged()
                }
            }
        }

        /* load all dictionaries */
        model.loadAllDictionaries()

        super.onStart()

    }

    companion object {
        @JvmStatic
        fun newInstance() = DictionaryListFragment()
    }
}