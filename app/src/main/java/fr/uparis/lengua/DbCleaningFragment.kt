package fr.uparis.lengua

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import fr.uparis.lengua.databinding.FragmentDbCleaningBinding

class DbCleaningFragment : Fragment(), OnItemSelectedListener {
    private lateinit var binding: FragmentDbCleaningBinding
    private lateinit var displayedTag: String
    private val model: TranslationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_db_cleaning, container, false)
        binding = FragmentDbCleaningBinding.bind(v!!)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Initialize spinner value. */
        val spinner: Spinner = binding.listSelectionSpinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.db_lists_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // layout of dropdown
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(0)
        }

        /* Set action performed on spinner item click */
        spinner.onItemSelectedListener = this

        /* According to spinner first selection, display corresponding fragment. */
        displayFragmentOfTag(spinner.selectedItem.toString())

        /* When clicked, removes the selected word from the database. */
        binding.deleteButton.setOnClickListener {
            if (model.isDictionarySelected() && displayedTag == DICT_TAG)
                model.deleteDictionary(model.selectedDictionary.value!!)

            if (model.isWordSelected() && displayedTag == WORD_TAG)
                model.deleteWord(model.selectedWord.value!!)
        }
    }

    private fun displayFragmentOfTag(tag: String) {
        Log.d("logLENGUA", "display fragment of : $tag")
        val containerId = R.id.list_fragment_container_view
        val tag2: String
        val fragment: Fragment

        if (tag == DICT_TAG) {
            fragment = DictionaryListFragment.newInstance()
            tag2 = tag
        } else { /* Display Word list if invalid tag */
            fragment = WordListFragment.newInstance()
            tag2 = WORD_TAG
        }

        childFragmentManager.beginTransaction()
            .replace(
                containerId,
                fragment,
                tag2
            )
            .addToBackStack(null)
            .commit()

        displayedTag = tag2
    }

    companion object {
        private val DICT_TAG = "Dictionaries"
        private val WORD_TAG = "Words"

        @JvmStatic
        fun newInstance() =
            DbCleaningFragment().apply {
                arguments = Bundle() /* no arguments */
            }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val selected = parent.getItemAtPosition(pos) as String

        Log.d("logLENGUA", "Selected: $selected")

        /* Check if selected element is already displayed */
        if (selected != displayedTag) {/* Update list fragment if not already displayed */
            Log.d("logLENGUA", "not already displayed")
            displayFragmentOfTag(selected)
        } else {
            Log.d("logLENGUA", "already displayed")
        }

        /* Reset selected data on list change. */
        model.resetSelected()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        /* do nothing */
    }
}