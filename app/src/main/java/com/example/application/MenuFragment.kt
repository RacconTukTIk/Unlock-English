package com.example.application

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import com.example.application.R
import com.example.application.MistakesActivity
import com.example.dicti.DictTechActivity


class MenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_menu, container, false)

        val buttonThemes: Button = view.findViewById(R.id.button_themes)
        buttonThemes.setOnClickListener {
            val intent = Intent(requireActivity(), ThemesActivity::class.java)
            startActivity(intent)
        }
        val buttonError: Button = view.findViewById(R.id.button_mistakes)
        buttonError.setOnClickListener {
            val intent = Intent(requireActivity(), MistakesActivity::class.java)
            startActivity(intent)
        }
        return view
    }
    
    companion object {
        @JvmStatic
        fun newInstance() = MenuFragment()
    }
}