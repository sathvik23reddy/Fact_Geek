package com.example.factgeek

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FactRepository
    val allFacts: LiveData<List<Fact>>

    init {
        val dao = FactDatabase.getDatabase(application).getFactDao()
        repository = FactRepository(dao)
        allFacts = repository.allFacts
    }

    fun deleteFact(fact: Fact) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(fact)
    }

    fun insertFact(fact:Fact) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(fact)
    }
}