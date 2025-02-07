//====================================================================================================================//
//  Source:         HostActivity.kt
//  Description:    The code-behind the Host Activity, which contains a bottom navigation bar and a navigation host
//                  fragment.
//====================================================================================================================//
package com.tyler_hietanen.ygotas_companion.ui.host

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.tyler_hietanen.ygotas_companion.R
import com.tyler_hietanen.ygotas_companion.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Inflates the binding.
        val binding: ActivityHostBinding = DataBindingUtil.setContentView(this, R.layout.activity_host)
        binding.lifecycleOwner = this

        // Setup listeners for each navigation bar menu item.
        // TODO Move to ViewModel.
        binding.bottomNavViewActivityMain.setOnItemSelectedListener { item ->
            when (item.itemId)
            {
                R.id.navigation_quotes      ->
                {
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                    navHostFragment.navController.navigate(R.id.quotesFragment)
                    true
                }
                R.id.navigation_duel        ->
                {
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                    navHostFragment.navController.navigate(R.id.duelFragment)
                    true
                }
                R.id.navigation_house_rules ->
                {
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                    navHostFragment.navController.navigate(R.id.houseRulesFragment)
                    true
                }
                R.id.navigation_settings    ->
                {
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                    navHostFragment.navController.navigate(R.id.settingsFragment)
                    true
                }
                else                        ->false
            }
        }
    }
}