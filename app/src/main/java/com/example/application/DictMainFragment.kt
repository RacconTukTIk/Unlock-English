package com.example.dicti

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.application.R

class DictMainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_dict_main, container, false)
        val buttonDictTechFragment: Button = view.findViewById(R.id.TechButton)

        buttonDictTechFragment.setOnClickListener {
            val intent = Intent(requireActivity(),DictTechActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}