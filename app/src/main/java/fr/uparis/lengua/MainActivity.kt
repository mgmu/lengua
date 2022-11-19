package fr.uparis.lengua

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import fr.uparis.lengua.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val model: TranslationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        /* Page names */
        val names = listOf("Research", "Save", "Cleaning")

        /* Fragments creation */
        val saveSearchFragment = SaveSearchFragment.newInstance()
        val translationResearchFragment = TranslationResearchFragment.newInstance()
        val dbCleaningFragment = DbCleaningFragment.newInstance()

        /* Pager Adapter */
        val pagerAdapter = ScreenSlidePagerAdapter(
            this,
            mutableListOf(translationResearchFragment, saveSearchFragment, dbCleaningFragment)
        )

        /* Attach pager adapter to pager */
        binding.pager.adapter = pagerAdapter

        /* Attach page name to tab */
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = names[position]
        }.attach()

        /* Launch Notification Service */
        val i = Intent(this, LearningService::class.java)
        Log.d("logLENGUA","before startService in main")
        applicationContext.startService(i)
        Log.d("logLENGUA","after startService in main")


    }

    /* Associates a fragment to a page of the ViewPager */
    class ScreenSlidePagerAdapter(fa: FragmentActivity,
                                  var fragments: MutableList<Fragment>): FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}
