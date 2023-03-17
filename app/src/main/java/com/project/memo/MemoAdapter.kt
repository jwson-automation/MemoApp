package com.project.memo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class MemoAdapter(val context: Context, val memoList : ArrayList<MemoDto>) : BaseAdapter() {
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_listview,null)

        val title = view.findViewById<TextView>(R.id.item_A)
        val date = view.findViewById<TextView>(R.id.item_B)

        val memo = memoList[p0]
        title.text = memo.title
        date.text = memo.date

        return view
    }

    override fun getCount(): Int {
    return memoList.size
    }

    override fun getItem(p0: Int): Any {
    return memoList[p0]
    }

    override fun getItemId(p0: Int): Long {
    return 0
    }
}