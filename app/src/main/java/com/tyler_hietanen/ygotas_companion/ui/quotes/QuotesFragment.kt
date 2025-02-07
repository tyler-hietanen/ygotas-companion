package com.tyler_hietanen.ygotas_companion.ui.quotes

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tyler_hietanen.ygotas_companion.R

class QuotesFragment : Fragment()
{

    companion object
    {
        fun newInstance() = QuotesFragment()
    }

    private val viewModel: QuotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        return inflater.inflate(R.layout.fragment_quotes, container, false)
    }
}