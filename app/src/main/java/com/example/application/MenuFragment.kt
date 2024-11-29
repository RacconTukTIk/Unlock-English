package com.example.application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import com.example.application.R
import com.example.dicti.DictTechFragment

class MenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating layout for this fragment
        return inflater.inflate(R.layout.activity_menu, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MenuFragment()
    }
}