package com.project.memo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.memo.databinding.ActivityMemoInfoBinding

class MemoInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityMemoInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMemoInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.returnButton.setOnClickListener {
            finish()
        }
    }
}