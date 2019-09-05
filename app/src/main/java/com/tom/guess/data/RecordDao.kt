package com.tom.guess.data

import androidx.room.*

@Dao
interface RecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: Record)

    @Query("select * from record")
    suspend fun getAll() : List<Record>

    @Delete
    suspend fun delete(record: Record)
}