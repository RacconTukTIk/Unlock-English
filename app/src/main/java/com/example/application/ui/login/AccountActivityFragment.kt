package com.example.application.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.application.MenuFragment
import com.example.application.R

class AccountActivityFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating layout for this fragment
        return inflater.inflate(R.layout.activity_account, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = AccountActivityFragment()
    }
}