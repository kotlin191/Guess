package com.tom.guess.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Record::class, Word::class], version = 0)
abstract class GameDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao

}