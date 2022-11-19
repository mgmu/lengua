package fr.uparis.lengua

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import fr.uparis.lengua.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val model: TranslationViewModel by lazy { TranslationViewModel((application as TranslationApplication)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        /* Page names */
        val names = listOf("Research")

        /* Fragments creation */
        val translationResearchFragment = TranslationResearchFragment.newInstance(
            DictionaryRecyclerAdapter(model)
        )

        /* Pager Adapter */
        val pagerAdapter = ScreenSlidePagerAdapter(
            this,
            mutableListOf(translationResearchFragment)
        )

        /* Attach pager adapter to pager */
        binding.pager.adapter = pagerAdapter

        /* Attach page name to tab */
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = names[position]
        }.attach()
        val i = Intent(this,LearningService::class.java)
        Log.d("service","before startService in main")
        applicationContext.startService(i)
        Log.d("service","after startService in main")


    }

    /* Associates a fragment to a page of the ViewPager */
    class ScreenSlidePagerAdapter(fa: FragmentActivity,
                                  var fragments: MutableList<Fragment>): FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}