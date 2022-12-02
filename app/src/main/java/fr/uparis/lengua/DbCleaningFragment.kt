package fr.uparis.lengua

import android.app.Activity
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
import fr.uparis.lengua.databinding.FragmentDbCleaningBinding

class DbCleaningFragment : Fragment(), OnItemSelectedListener {

    private lateinit var binding: FragmentDbCleaningBinding

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

        /* According to spinner selection, display corresponding fragment. */
        val containerViewId = R.id.list_fragment_container_view
        val fragment =
            if (spinner.selectedItem.toString() == "Dictionaries")
                DictionaryListFragment.newInstance()
            else
                WordListFragment.newInstance()

        // Place fragment
        childFragmentManager.beginTransaction()
            .replace(
                containerViewId,
                fragment,
                null
            )
            .addToBackStack(null)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DbCleaningFragment().apply {
                arguments = Bundle() /* no arguments */
            }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val selected = parent.getItemAtPosition(pos) as String
        /* change list fragment displayed */
        Log.d("logLENGUA", "cleaning $selected")
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}