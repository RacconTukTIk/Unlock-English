package com.example.application

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Загружаем макет фрагмента
        val view = inflater.inflate(R.layout.activity_calendar, container, false)

        // Находим кнопку
        val btnOpenDatePicker = view.findViewById<Button>(R.id.btnOpenDatePicker)

        // Обработка нажатия на кнопку
        btnOpenDatePicker.setOnClickListener {
            // Создание MaterialDatePicker
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .build()

            // Обработка выбора даты
            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                // selectedDate — это timestamp в миллисекундах
                Toast.makeText(requireContext(), "Выбранная дата: $selectedDate", Toast.LENGTH_SHORT).show()
            }

            // Показ MaterialDatePicker
            datePicker.show(childFragmentManager, "DATE_PICKER")
        }

        return view
    }
}