package fr.uparis.lengua

import android.app.AlarmManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import fr.uparis.lengua.databinding.FragmentDbCleaningBinding
import fr.uparis.lengua.databinding.FragmentSettingsBinding
import fr.uparis.lengua.databinding.FragmentTranslationResearchBinding


/**
 * This fragment represents the setting page
 */
class SettingsFragment : Fragment() {
    private var binding: FragmentSettingsBinding by lazy {  }

    private val REMAINING_NOTIFICATIONS = "number of notifications to send for next course"
    private val HOUR_ = "hour"
    private val MINUTE_ = "minute"
    private val RECAP_FREQUENCY_ = "repeatingInterval"
    private val possibleFrequencies = listOf<Long>(
        AlarmManager.INTERVAL_FIFTEEN_MINUTES,
        AlarmManager.INTERVAL_HALF_HOUR,
        AlarmManager.INTERVAL_DAY
    )

    /**
     * Getting sharedprefrences to know how many notification we should send.
     */
    private lateinit var sharedPref:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = (context?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)) as SharedPreferences

        binding = FragmentTranslationRe:.bind(v!!)
        val freqSpinner: Spinner = binding.chooseLessonFrequency
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.db_lists_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // layout of dropdown
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            freqSpinner.adapter = adapter
            freqSpinner.setSelection(0)
        }

        val nbOfWordsSpinner: Spinner = binding.chooseWordsQuantity
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.db_lists_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // layout of dropdown
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            freqSpinner.adapter = adapter
            freqSpinner.setSelection(0)
        }
        binding.saveBtn.setOnClickListener(
            {

                val chosenFrequence = freqSpinner.selectedItem.toString()
                Log.d("logLENGUA", chosenFrequence)

                sharedPref.edit().putLong(RECAP_FREQUENCY_,
                    getFrequency(chosenFrequence)).apply()

                sharedPref.edit().putInt(REMAINING_NOTIFICATIONS,
                    Integer.valueOf(nbOfWordsSpinner.selectedItem.toString())).apply()
            }
        )
    }
    fun getFrequency(label:String):Long{
       when(label){
           "Every 15 minutes" -> return AlarmManager.INTERVAL_FIFTEEN_MINUTES
           "Every 30 minutes" -> return AlarmManager.INTERVAL_HALF_HOUR
           "Every hour" -> return AlarmManager.INTERVAL_HOUR
           "Everyday" -> return AlarmManager.INTERVAL_DAY
       }
        return AlarmManager.INTERVAL_DAY /*Unreachable statement.*/
    }
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