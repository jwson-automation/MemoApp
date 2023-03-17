package com.project.memo

import android.app.Activity
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.project.memo.databinding.ActivityMemoEditBinding

class MemoEditActivity : AppCompatActivity() {
    lateinit var binding: ActivityMemoEditBinding

    // db 추가
    private lateinit var dbHelper: MemoDBHelper
    private lateinit var database: SQLiteDatabase
    //

    var isUpdate = false
    var idx = -1
    val currentTime = "2023-03-17"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMemoEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 어떤 경로로 들어왔는지 검사해서 '저장모드','수정모드'로 분류합니다.
        typeCheck()

        initDB()

        binding.saveButton.setOnClickListener {
            saveButtonMethod()
        }

        binding.updateButton.setOnClickListener {
            updateButtonMethod()
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun initDB(){
        dbHelper = MemoDBHelper(this, "new.db", null, 1)
        database = dbHelper.writableDatabase
    }

    private fun saveButtonMethod(){
        var todo = binding.toDoListInput.text
        var description = binding.descriptionInput.text

        if (binding.second10.isChecked){
            intent.putExtra("second10", true)
        }else{
            intent.putExtra("second10", false)
        }
        if (binding.second30.isChecked){
            intent.putExtra("second30", true)
        }else{
            intent.putExtra("second30", false)
        }

        if (todo.isNotEmpty() && description.isNotEmpty()) {
            dbHelper.insert(
                    todo.toString(),
                    description.toString(),
                    currentTime.toString()
            )
            //

            intent.putExtra("title", todo.toString())
            setResult(Activity.RESULT_OK,intent)
            finish()
        } else {
            Toast.makeText(this, "입력값을 확인해주세요", Toast.LENGTH_SHORT).show()

        }
    }

    private fun typeCheck(){
        idx = intent.getIntExtra("idx", -1)
        isUpdate = idx != -1

        var title = intent.getStringExtra("title")
        var content = intent.getStringExtra("content")

        if (isUpdate) {
            binding.saveButton.isVisible = false
            binding.toDoListInput.setText(title)
            binding.toDoListInput.isEnabled = false
            binding.descriptionInput.setText(content)
            binding.dateInput.setText(currentTime)
            binding.dateInput.isEnabled = false
        } else {
            binding.updateButton.isVisible = false
            binding.dateInput.setText(currentTime)
            binding.dateInput.isEnabled = false
        }
    }

    private fun updateButtonMethod(){
        var todo = binding.toDoListInput.text
        var description = binding.descriptionInput.text

        if (description.isNotEmpty()) {

            dbHelper.update(
                idx + 1,
                todo.toString(),
                description.toString(),
                currentTime.toString()
            )
            //

            if (binding.second10.isChecked){
                intent.putExtra("second10", true)
            }else{
                intent.putExtra("second10", false)
            }
            if (binding.second30.isChecked){
                intent.putExtra("second30", true)
            }else{
                intent.putExtra("second30", false)
            }
            intent.putExtra("title", todo.toString())
            setResult(Activity.RESULT_OK,intent)
            finish()
        } else {
            Toast.makeText(this, "입력값을 확인해주세요", Toast.LENGTH_SHORT).show()
        }
    }

}