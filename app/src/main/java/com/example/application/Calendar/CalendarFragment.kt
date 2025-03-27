package com.example.application.Calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Загружаем макет для Fragment
        val view = inflater.inflate(R.layout.activity_calendar, container, false)

        // Инициализация RecyclerView
        val calendarRecyclerView: RecyclerView = view.findViewById(R.id.calendarRecyclerView)
        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7) // 7 дней в неделе


        val days = generateDaysForNovember2024()
        val adapter = CalendarAdapter(days) { day ->
            Toast.makeText(requireContext(), "Выбран день: $day", Toast.LENGTH_SHORT).show()
        }
        calendarRecyclerView.adapter = adapter

        // Установка заголовка календаря
        val calendarTitle: TextView = view.findViewById(R.id.calendarTitle)
        calendarTitle.text = "Ноябрь 2024 г."

        return view
    }

    private fun generateDaysForNovember2024(): List<String> {
        // Ноябрь 2024 года начинается с пятницы (1 ноября)
        val days = mutableListOf<String>()

        // Пустые дни для начала месяца (если месяц начинается не с понедельника)
        for (i in 0 until 4) {
            days.add("")
        }

        // Добавляем дни ноября
        for (i in 1..30) {
            days.add(i.toString())
        }

        return days
    }
}