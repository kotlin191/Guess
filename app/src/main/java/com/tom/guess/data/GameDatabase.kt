package com.tom.guess.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Record::class, Word::class), version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao

}