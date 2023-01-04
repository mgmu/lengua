package fr.uparis.lengua

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import fr.uparis.lengua.databinding.FragmentSettingsBinding

/**
 * This fragment represents the settings page
 */
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = (context?.getSharedPreferences(
            getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )) as SharedPreferences

        binding = FragmentSettingsBinding.bind(requireView())

        val nbOfWordsSpinner: Spinner = binding.chooseWordsQuantity
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.words_quantity,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // layout of dropdown
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            nbOfWordsSpinner.adapter = adapter
            nbOfWordsSpinner.setSelection(0)
        }

        binding.saveBtn.setOnClickListener {

            val hoursContent = binding.hours.text
            val minutesContent = binding.minutes.text
            if (hoursContent.isNotEmpty() && minutesContent.isNotEmpty()) {
                val hours = Integer.parseInt(hoursContent.toString())
                val minutes = Integer.parseInt(minutesContent.toString())
                val chosenFrequency = getFrequency(hours, minutes)
                val nbOfWords = nbOfWordsSpinner.selectedItem.toString()

                with (sharedPref.edit()) {
                    putInt(getString(R.string.recap_frequency), chosenFrequency)
                    putInt(getString(R.string.words_per_lesson), Integer.parseInt(nbOfWords))
                    apply()
                }

                Toast.makeText(requireContext(), "Updated !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "You have to fill hours and minutes fields.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getFrequency(hours: Int, minutes: Int): Int = (hours * 3600 + minutes * 60) * 1000

    companion object {

        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    /* No arguments. */
                }
            }
    }
}