package com.example.factgeek

import androidx.lifecycle.LiveData

class FactRepository(private val factDao: FactDao) {
    val allFacts: LiveData<List<Fact>> = factDao.getAllFacts()

    suspend fun insert(fact: Fact) {
        factDao.insert(fact)
    }

    suspend fun delete(fact:Fact){
        factDao.delete(fact)
    }
}