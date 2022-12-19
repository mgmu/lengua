package fr.uparis.lengua

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import fr.uparis.lengua.databinding.ActivitySaveSearchBinding

class SaveSearchActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySaveSearchBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /* Get URI from web intent */
        if (intent.action.equals("android.intent.action.SEND")) {
            if (intent.extras != null) {
                val txt = intent.extras!!.getString("android.intent.extra.TEXT")
                if (txt != null) {

                    /* Place fragment with uri */
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_slot,
                            SaveSearchFragment.newInstance(txt),
                            null
                        ).commit()
                }
            }
        }

        Log.d("logLengua", "finished onCreate SaveSearchActivity")
    }
}