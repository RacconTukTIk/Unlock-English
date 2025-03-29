package com.example.application.Calendar

import CalendarAdapter
import android.graphics.Color
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import com.example.application.Calendar.DayModel

class CalendarFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var loginListener: ValueEventListener? = null
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var calendarTitle: TextView
    private var currentYearMonth: YearMonth = YearMonth.now()
    private lateinit var tvStreak:TextView
    private lateinit var tvTodayTime:TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_calendar, container, false)
        setupFirebase()
        setupCalendarView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateCalendarTitle()
        fetchLoginData()
    }



    private fun setupFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://unlock-english-22c67-default-rtdb.europe-west1.firebasedatabase.app/")
    }

    private fun setupCalendarView(view: View) {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        calendarTitle = view.findViewById(R.id.calendarTitle)
        tvStreak = view.findViewById(R.id.tvStreak) // Добавлено
        tvTodayTime = view.findViewById(R.id.tvTodayTime) // Добавлено

        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)

        val days = generateDaysForCurrentMonth()
        val adapter = CalendarAdapter(days.map { DayModel(it, false) }) { day ->
            Toast.makeText(requireContext(), "Выбран день: $day", Toast.LENGTH_SHORT).show()
        }
        calendarRecyclerView.adapter = adapter
    }

    private fun updateCalendarTitle() {
        val formatter = DateTimeFormatter.ofPattern("LLLL yyyy г.", Locale("ru"))
        calendarTitle.text = currentYearMonth.format(formatter).replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale("ru")) else it.toString()
        }
    }

    private fun generateDaysForCurrentMonth(): List<String> {
        val firstDayOfMonth = currentYearMonth.atDay(1)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek
        val days = mutableListOf<String>()

        // Пустые дни в начале (если месяц начинается не с понедельника)
        val emptyDaysStart = (firstDayOfWeek.value - DayOfWeek.MONDAY.value + 7) % 7
        repeat(emptyDaysStart) { days.add("") }

        // Добавляем дни месяца
        for (day in 1..currentYearMonth.lengthOfMonth()) {
            days.add(day.toString())
        }

        // Пустые дни в конце
        while (days.size % 7 != 0) {
            days.add("")
        }

        return days
    }

    private fun fetchLoginData() {
        val user = auth.currentUser ?: return

        database.getReference("users/${user.uid}/logins").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sessions = mutableListOf<Session>()
                for (sessionSnapshot in snapshot.children) {
                    val start = sessionSnapshot.child("start").getValue(Long::class.java)
                    val end = sessionSnapshot.child("end").getValue(Long::class.java)
                    if (start != null && end != null) {
                        sessions.add(Session(start, end))
                    }
                }

                updateCalendar(sessions)
                updateStreak(sessions)
                updateTodayTime(sessions)
            }

            override fun onCancelled(error: DatabaseError) {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Ошибка: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }





    data class Session(val start: Long, val end: Long)

    // Подсчет серии дней
    private fun updateStreak(sessions: List<Session>) {
        val dates = sessions.map { Instant.ofEpochMilli(it.start).atZone(ZoneId.systemDefault()).toLocalDate() }
        val sortedDates = dates.sorted()

        var currentStreak = 0
        var maxStreak = 0
        var prevDate: LocalDate? = null

        for (date in sortedDates) {
            if (prevDate == null || prevDate.plusDays(1) == date) {
                currentStreak++
            } else {
                currentStreak = 1
            }
            maxStreak = maxOf(maxStreak, currentStreak)
            prevDate = date
        }

        tvStreak.text = "Серия: $maxStreak дней"
    }

    private fun updateTodayTime(sessions: List<Session>) {
        val today = LocalDate.now()
        val totalMillis = sessions
            .filter { session ->
                val startDate = Instant.ofEpochMilli(session.start)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                startDate == today && session.end > session.start // Исключаем сессии с end = 0
            }
            .sumOf { it.end - it.start }

        val minutes = (totalMillis / 60000).toInt()
        tvTodayTime.text = "За сегодня: $minutes мин"
    }


    private fun updateCalendar(sessions: List<Session>) {
        val loggedDays = sessions
            .map { Instant.ofEpochMilli(it.start).atZone(ZoneId.systemDefault()).toLocalDate() }
            .filter { it.year == currentYearMonth.year && it.month == currentYearMonth.month }
            .map { it.dayOfMonth.toString() }
            .toSet()

        val daysWithStatus = generateDaysForCurrentMonth().map { day ->
            DayModel(day, day in loggedDays)
        }
        (calendarRecyclerView.adapter as CalendarAdapter).updateDays(daysWithStatus)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loginListener?.let {
            database.reference.removeEventListener(it)
        }
    }

}

