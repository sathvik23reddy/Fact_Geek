package com.example.factgeek

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

lateinit var viewModel: FactViewModel

class SavedFactActivity : AppCompatActivity(), IFactsRVAdapter {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_fact)

        //RV Adapter Setup
        val recyclerView =
            this.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = FactsRVAdapter(this, this)
        recyclerView.adapter = adapter

        //ViewModel Config to observe
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(FactViewModel::class.java)
        viewModel.allFacts.observe(this, Observer { list ->
            list?.let {
                adapter.updateList(it)
            }
        })

        //Push fact to DB through ViewModel
        val str :String? = intent.getStringExtra("Send this")
        str?.let {
            saveThis(str)
        }

    }

    //Deletes Fact
    override fun onItemClicked(fact: Fact) {
        viewModel.deleteFact(fact)
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this@SavedFactActivity, "Fact Deleted!", Toast.LENGTH_SHORT).show()
        }
    }

    //Saves Fact
    override fun onSaveClicked(fact: Fact){
        val str: String = fact.fact
        var clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var clip = ClipData.newPlainText("label", str)
        clipboard.setPrimaryClip(clip)
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this@SavedFactActivity, "Copied to Clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    //Launch ShareSheet to Share Fact
    override fun onShareClicked(fact: Fact) {
        val str: String = fact.fact
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Did you know?\n" + str + "\n\nFind out more facts using Sathvik's new app\n" +
                    "https://drive.google.com/drive/u/1/folders/1KzXKqmM3Ri9zCxMlUG7dsIy1SI7R-ii8")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    //Util to push fact to DB through ViewModel
    fun saveThis(str: String){
        viewModel.insertFact(Fact(str))
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this, "Fact Saved!", Toast.LENGTH_SHORT).show()
        }
        finish()
    }
}