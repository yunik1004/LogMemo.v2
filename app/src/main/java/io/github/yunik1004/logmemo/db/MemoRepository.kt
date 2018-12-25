package io.github.yunik1004.logmemo.db

import android.content.Context
import io.github.yunik1004.logmemo.MEMO_TABLE_NAME
import io.github.yunik1004.logmemo.model.Memo
import org.jetbrains.anko.db.*
import java.text.SimpleDateFormat
import java.util.*

class MemoRepository(private val context: Context) {
    fun findAll(): ArrayList<Memo> {
        val memos = ArrayList<Memo>()

        context.memoDatabase.use {
            select(MEMO_TABLE_NAME)
                .orderBy("id", SqlOrderDirection.DESC)
                .parseList(object: MapRowParser<List<Memo>>{
                    override fun parseRow(columns: Map<String, Any?>): List<Memo> {
                        val id = columns.getValue("id").toString().toInt()
                        val date = columns.getValue("date").toString()
                        val time = columns.getValue("time").toString()
                        val text = columns.getValue("text").toString()
                        val memo = Memo(id, date, time, text)

                        memos.add(memo)
                        return memos
                    }
                })
        }

        return memos
    }

    fun insert(text: String): Memo? {
        val current = Calendar.getInstance().time
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(current)
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(current)

        var id: Long = -1

        context.memoDatabase.use {
            id = insert(MEMO_TABLE_NAME,
                "date" to currentDate,
                "time" to currentTime,
                "text" to text
            )
        }

        if (id == -1L) {
            return null
        }

        return Memo(id.toInt(), currentDate, currentTime, text)
    }

    fun update() {
        context.memoDatabase.use {
            //
        }
    }

    fun delete(id: Int) {
        context.memoDatabase.use {
            delete(MEMO_TABLE_NAME,
                "id = {memoID}", "memoID" to id
            )
        }
    }

    fun deleteAll() {
        context.memoDatabase.use {
            delete(MEMO_TABLE_NAME, null, null)
        }
    }
}
