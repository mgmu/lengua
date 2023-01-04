package fr.uparis.lengua

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
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
        val names = listOf("Research", "Save", "Cleaning", "Settings")

        /* Fragments creation */
        val saveSearchFragment = SaveSearchFragment.newInstance()
        val translationResearchFragment = TranslationResearchFragment.newInstance()
        val dbCleaningFragment = DbCleaningFragment.newInstance()
        val settingsFragment = SettingsFragment.newInstance()

        /* Pager Adapter */
        val pagerAdapter = ScreenSlidePagerAdapter(
            this,
            mutableListOf(translationResearchFragment, saveSearchFragment, dbCleaningFragment, settingsFragment)
        )

        /* Attach pager adapter to pager */
        binding.pager.adapter = pagerAdapter

        /* Attach page name to tab */
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = names[position]
        }.attach()

        /* Start learning service 2 */
        val learningIntent = Intent(this, LearningService2::class.java)
        applicationContext.startService(learningIntent)

        /* On Pager page change, reset selected word and dictionary in model to prevent
         * unwanted deletes. */
        val callback = PageChangeCallback(model)
        binding.pager.registerOnPageChangeCallback(callback)
    }

    /* Associates a fragment to a page of the ViewPager */
    class ScreenSlidePagerAdapter(
        fa: FragmentActivity,
        private var fragments: MutableList<Fragment>): FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }

    /* On Page change, resets selected dictionary and word in model. */
    class PageChangeCallback(private val model: TranslationViewModel): OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            model.resetSelected()
            super.onPageSelected(position)
        }
    }
}
