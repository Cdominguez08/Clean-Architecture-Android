package com.cd.cleanarchitecture.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.cd.cleanarchitecture.R
import com.cd.cleanarchitecture.adapters.HomeNavigationStateAdapter
import com.cd.cleanarchitecture.domain.Character
import com.cd.cleanarchitecture.parcelable.toCharacterParcelable
import com.cd.cleanarchitecture.utils.Constants
import com.cd.cleanarchitecture.utils.startActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , BottomNavigationView.OnNavigationItemSelectedListener,
    CharacterListFragment.OnCharacterListFragmentListener,
    FavoriteListFragment.OnFavoriteListFragmentListener {

    //region Fields

    private val homeStatePageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            viewPager.currentItem = position
            when(position) {
                0 -> bottomNavigation.menu.findItem(R.id.navigation_list).isChecked = true
                1 -> bottomNavigation.menu.findItem(R.id.navigation_favorites).isChecked = true
            }
        }
    }

    //endregion

    //region Override Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation.setOnNavigationItemSelectedListener(this)

        viewPager.adapter = HomeNavigationStateAdapter(this)
        viewPager.registerOnPageChangeCallback(homeStatePageChangeCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.unregisterOnPageChangeCallback(homeStatePageChangeCallback)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_list -> {
                viewPager.currentItem = 0
                true
            }
            R.id.navigation_favorites -> {
                viewPager.currentItem = 1
                true
            }
            else -> false
        }
    }

    override fun openCharacterDetail(character: Character) {
        startActivity<CharacterDetailActivity> {
            putExtra(Constants.EXTRA_CHARACTER, character.toCharacterParcelable())
        }
        overridePendingTransition(R.anim.entry, R.anim.exit)
    }

    //endregion
}