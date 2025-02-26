package com.example.application

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.application.databinding.ActivityBottomNavigationBinding
import com.example.application.databinding.ActivityDictMainBinding
import com.example.application.databinding.ActivityDictTechBinding
import com.example.application.ui.login.AccountActivityFragment
import com.example.dicti.DictMainActivity
import com.example.dicti.DictMainFragment
import com.example.dicti.DictTechFragment


class BottomNavigationActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityBottomNavigationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(MenuFragment())
        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId)
            {
                R.id.home -> replaceFragment(MenuFragment())
                R.id.dictionary -> replaceFragment(DictMainFragment())
                R.id.account -> replaceFragment(AccountActivityFragment())

                else -> false
            }
            true

        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    private fun replaceFragment(fragment: Fragment)
    {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}