package io.github.yunik1004.logmemo

import android.content.Context
import io.github.yunik1004.logmemo.db.MemoDatabaseOpenHelper

val Context.memoDatabase: MemoDatabaseOpenHelper
    get() = MemoDatabaseOpenHelper.getInstance(applicationContext)
