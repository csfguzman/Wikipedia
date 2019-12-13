package com.example.wikipedia.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wikipedia.R
import com.example.wikipedia.databinding.ActivityMainBinding
import com.example.wikipedia.fragments.ExploreFragment
import com.example.wikipedia.fragments.FavoritesFragment
import com.example.wikipedia.fragments.HistoryFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val exploreFragment : ExploreFragment
    private val favoritesFragment : FavoritesFragment
    private val historyFragment : HistoryFragment

    init{
        exploreFragment = ExploreFragment()
        favoritesFragment = FavoritesFragment()
        historyFragment = HistoryFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_explore, R.id.navigation_favorites, R.id.navigation_history
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navView.setupWithNavController(navController)
    }
}
