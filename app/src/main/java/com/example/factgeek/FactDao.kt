package com.example.factgeek

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fact: Fact)

    @Delete
    suspend fun delete(fact: Fact)

    @Query("SELECT * FROM facts_table ORDER BY ID DESC")
    fun getAllFacts(): LiveData<List<Fact>>
}