package com.tyler_hietanen.ygotas_companion.ui.welcome

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tyler_hietanen.ygotas_companion.R

class WelcomeFragment : Fragment()
{

    companion object
    {
        fun newInstance() = WelcomeFragment()
    }

    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }
}