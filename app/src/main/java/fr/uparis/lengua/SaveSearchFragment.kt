package fr.uparis.lengua

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SaveSearchFragment : Fragment(R.layout.fragment_save_search) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save_search, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() = SaveSearchFragment()
    }
}