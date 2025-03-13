package com.example.application

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // Создание MaterialDatePicker
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Выберите дату")
            .build()

        // Обработка выбора даты
        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            // selectedDate — это timestamp в миллисекундах
            Toast.makeText(this, "Выбранная дата: $selectedDate", Toast.LENGTH_SHORT).show()
        }

        // Показ MaterialDatePicker
        datePicker.show(supportFragmentManager, "DATE_PICKER")
    }
}