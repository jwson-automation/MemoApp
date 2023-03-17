package com.project.memo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MemoReceiver : BroadcastReceiver()  {
    private val TAG = "jwson"
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d(TAG, "Received intent : $p1")
        Log.d(TAG, "onReceive: ${p1?.getStringExtra("msg")}")
        Log.d(TAG, "onReceive: ${p1?.toString()}")
        Toast.makeText(p0, p1!!.getStringExtra("msg").toString(),Toast.LENGTH_SHORT).show()
    }
}