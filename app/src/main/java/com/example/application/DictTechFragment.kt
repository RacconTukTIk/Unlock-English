package com.example.dicti

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.application.R
import com.example.application.databinding.ActivityDictMainBinding

class DictTechFragment : Fragment() {
    private  lateinit var binding: ActivityDictMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating layout for this fragment
        return inflater.inflate(R.layout.activity_dict_tech, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance() = DictTechFragment()
    }
}