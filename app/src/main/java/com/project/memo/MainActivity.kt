package com.project.memo

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.project.memo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MemoAdapter
    lateinit var getResultText: ActivityResultLauncher<Intent>
    lateinit var memos: ArrayList<MemoDto>

    val TAG = "jwson"

    //디비 추가
    private lateinit var dbHelper: MemoDBHelper
    private lateinit var database: SQLiteDatabase
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initDB()

        initResultReceiver()

        initAdapter()
    }

    private fun initDB() {
        dbHelper = MemoDBHelper(this, "new.db", null, 1)
        database = dbHelper.writableDatabase
        Log.d(TAG, "onCreate: $database")
    }

    private fun initAdapter() {
        memos = dbHelper.selectAllMemo()
        adapter = MemoAdapter(this, memos)
        binding.memoListView.adapter = adapter
        binding.memoListView.setOnItemClickListener { adapterView, view, i, l ->
            val memo = adapterView.adapter.getItem(i) as MemoDto
            val intent = Intent(this, MemoEditActivity::class.java)

            intent.putExtra("idx", i)
            Log.d(TAG, "initAdapter: $i")
            intent.putExtra("title", memo.title.toString())
            intent.putExtra("content", memo.content.toString())

            getResultText.launch(intent)
        }
        registerForContextMenu(binding.memoListView)
    }

    private fun initResultReceiver() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        var alarmIntent = Intent(this, MemoReceiver::class.java)
        val prefs = getSharedPreferences("alarm", MODE_PRIVATE)

        getResultText = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {

            if (it.resultCode == RESULT_OK) {
                var v = prefs.getInt("KEY", 0)
                val editor = prefs.edit()
                editor.putInt("KEY", v.toInt() + 1)
                editor.apply()

                title = it.data?.getStringExtra("title")
                Log.d(TAG, "onCreate: ${title}")
                alarmIntent.putExtra("msg", title)

                if (it.data!!.getBooleanExtra("second10", false)) {
                    Toast.makeText(this, "10초 알람", Toast.LENGTH_SHORT).show()
                    val triggerTime = (SystemClock.elapsedRealtime() + 3 * 1000) // 10초

                    var pendingIntent =
                        PendingIntent.getBroadcast(this, v, alarmIntent, PendingIntent.FLAG_MUTABLE)
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME, triggerTime, pendingIntent)
                }
                if (it.data!!.getBooleanExtra("second30", false)) {
                    Toast.makeText(this, "30초 알람", Toast.LENGTH_SHORT).show()
                    val triggerTime = (SystemClock.elapsedRealtime() + 5 * 1000) // 30초

                    var pendingIntent =
                        PendingIntent.getBroadcast(this, v, alarmIntent, PendingIntent.FLAG_MUTABLE)
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME, triggerTime, pendingIntent)
                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addButton -> {
                val intent = Intent(this, MemoEditActivity::class.java)
                intent.putExtra("isUpdate", false)
                getResultText.launch(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo

        dbHelper.delete(memos.get(info.position)._id)
        onResume()
        return super.onContextItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {
            memos = dbHelper.selectAllMemo()
            Log.d(TAG, "initResultReceiver: ${memos}")

            adapter = MemoAdapter(this, memos)
            binding.memoListView.adapter = adapter
            adapter.notifyDataSetChanged()

        }
    }
}


