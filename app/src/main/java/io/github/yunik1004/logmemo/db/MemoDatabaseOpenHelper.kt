package io.github.yunik1004.logmemo.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import io.github.yunik1004.logmemo.MEMO_DB_NAME
import io.github.yunik1004.logmemo.MEMO_TABLE_NAME
import org.jetbrains.anko.db.*

class MemoDatabaseOpenHelper(ctx: Context): ManagedSQLiteOpenHelper(ctx,
    MEMO_DB_NAME, null, 1) {
    companion object {
        private var instance: MemoDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MemoDatabaseOpenHelper {
            if (instance == null) {
                instance = MemoDatabaseOpenHelper(ctx)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            MEMO_TABLE_NAME, true,
            "id" to INTEGER + PRIMARY_KEY + UNIQUE + AUTOINCREMENT,
            "date" to TEXT,
            "time" to TEXT,
            "text" to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(MEMO_TABLE_NAME, true)
    }
}
