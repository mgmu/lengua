package fr.uparis.lengua

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.uparis.lengua.databinding.ActivitySaveSearchBinding

class SaveSearchActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySaveSearchBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}