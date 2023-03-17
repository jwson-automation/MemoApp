package com.project.memo

import java.io.Serializable

class MemoDto : Serializable {
    var _id : Int
    var title : String
    var content : String
    var date : String

    constructor(_id : Int, title: String, content: String, date: String) {
        this._id = _id
        this.title = title
        this.content = content
        this.date = date
    }

    override fun toString(): String {
        return "MemoDto(_id='$_id', title='$title', content='$content', date='$date')"
    }

}