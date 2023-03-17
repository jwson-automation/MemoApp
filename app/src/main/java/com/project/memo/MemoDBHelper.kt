package com.project.memo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

private const val TAG = "DBHelper_싸피"
private const val TABLE = "mytable"

class MemoDBHelper(
    context: Context,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    private lateinit var db: SQLiteDatabase
    override fun onCreate(db: SQLiteDatabase) {
        // 테이블 생성 쿼리
        val query: String =
            "CREATE TABLE if not exists $TABLE ( _id integer primary key autoincrement, title text, content text, date text);"
        db.execSQL(query)
    }

    //  upgrade 가 필요한 경우 기존 테이블 drop 후 onCreate로 새롭게 생성
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql: String = "DROP TABLE if exists $TABLE"
        db.execSQL(sql)
        onCreate(db)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        this.db = db
        Log.d(TAG, "onOpen: database 준비 완료")
    }

    fun selectAllMemo(): ArrayList<MemoDto> {
        var result = ArrayList<MemoDto>()
        db.rawQuery("select * from $TABLE", null).use{
            while (it.moveToNext()) {
                result.add(MemoDto( it.getInt(0), it.getString(1), it.getString(2), it.getString(3)))
            }
        }
        Log.d(TAG, "selectAllMemo: ${result}")
        return result
    }

    fun getCount() : Int {
        var result = 0
        db.rawQuery("select _id $TABLE ORDER BY _ID DESC LIMIT 1 ", null).use {
            result = it.getInt(0)
        }
        return result
    }

    fun selectMemo(id: Int): MemoDto {
        val columns = arrayOf("_id", "txt")
        val cursor = db.query(TABLE, columns, "_id=?", arrayOf(id.toString()), null, null, null)
        var result = MemoDto(0,"","","")
        if (cursor.moveToNext()) {
            result._id = cursor.getInt(0)
            result.title = cursor.getString(1)
            result.content = cursor.getString(2)
            result.date = cursor.getString(3)

        }
        return result
    }

    fun insert(title: String,content: String,date: String) {
        // ContentValues를 이용한 저장
        val contentValues = ContentValues()
        contentValues.put("title", title)
        contentValues.put("content", content)
        contentValues.put("date", date)

        db.beginTransaction()
        val result = db.insert(TABLE, null, contentValues)
        if (result > 0) {
            db.setTransactionSuccessful()
        }
        db.endTransaction()
    }

    fun update(id: Int, title: String,content: String,date: String) {
        // ContentValues를 이용한 수정
        val contentValues = ContentValues()
        contentValues.put("title", title)
        contentValues.put("content", content)
        contentValues.put("date", date)
        db.beginTransaction()
        val result = db.update(TABLE, contentValues, "_id=?", arrayOf(id.toString()))
        if (result > 0) {
            db.setTransactionSuccessful()
        }
        db.endTransaction()
    }

    fun delete(id: Int) {
        db.beginTransaction()
        val result = db.delete(TABLE, "_id=?", arrayOf(id.toString()))
        if (result > 0) {
            db.setTransactionSuccessful()
        }
        db.endTransaction()
    }



}
