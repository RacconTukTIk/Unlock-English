    package com.example.application

    import android.os.Bundle
    import android.widget.ImageView
    import androidx.activity.enableEdgeToEdge
    import androidx.activity.viewModels
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import com.example.application.databinding.ActivityTopicDiscriptionBinding

    class TopicDiscriptionActivity : AppCompatActivity() {
        private lateinit var binding: ActivityTopicDiscriptionBinding
        private val viewModel: TopicsViewModel by viewModels()
        private var topicId: Int = -1

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityTopicDiscriptionBinding.inflate(layoutInflater)
            setContentView(binding.root)

            topicId = intent.getIntExtra("TOPIC_ID", -1)

            viewModel.getTopic(topicId).observe(this) { topic ->
                binding.sectionTitle.text = topic.title
                binding.actionButton.isEnabled = !topic.isCompleted
            }

            binding.actionButton.setOnClickListener {
                viewModel.markTopicCompleted(topicId)
                binding.actionButton.isEnabled = false
            }

            val buttonExit: ImageView = findViewById(R.id.exitTopics)
            buttonExit.setOnClickListener { finish() }
        }

        override fun onBackPressed() {

        }


    }