package com.example.factgeek

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facts_table")
class Fact(@ColumnInfo(name = "fact") val fact:String) {
    @PrimaryKey(autoGenerate = true) var id = 0
}