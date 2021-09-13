package com.example.factgeek

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var layout: LinearLayout

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if(id == R.id.my_button){
            //Code to trigger new Activity
            val intent = Intent(this, SavedFactActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layout = findViewById(R.id.linearLayout)

        //Animated BG
        val animationDrawable = layout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        //Alert for instructions
        var builder = AlertDialog.Builder(this)
        builder.setMessage("Swipe left <- for new fact\nSwipe right -> to save fact\n" +
                "Use the menu to share/copy/delete your saved facts\n\n" +
                "Thank you for installing the app! Enjoy!")
            .setCancelable(true)
            .setTitle("Instructions")
        builder.setPositiveButton("GOT IT"){dialogInterface, which ->
            dialogInterface.cancel()
        }
        val alert: AlertDialog = builder.create()
        alert.show()

        //Fetch and present data on detected Swipe Action
        apiCall()
        layout.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                apiCall()
            }
            override fun onSwipeRight() {
                super.onSwipeRight()
                saveFact()
                apiCall()
            }

        })
    }

    //API Call + O/P [Exceptions Handled]
    @RequiresApi(Build.VERSION_CODES.M)
    private fun apiCall() {
        if(!isOnline(this)) return
        val url = "https://useless-facts.sameerkumar.website/api"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "API let us down bro", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val jsonObject = JSONObject(response.body!!.string())

                    runOnUiThread {
                        val tv: TextView = findViewById(R.id.fact)
                        tv.text = jsonObject.getString("data")
                    }

                }
            }
        })
    }

    //Check Connection
    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager?.let {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            capabilities?.let {
                return true
            }
        }
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this@MainActivity, "No Internet Access\nPlease Check Connection", Toast.LENGTH_SHORT).show()
        }
        return false
    }


    //Push fact to SavedFactActivity
    private fun saveFact() {
        val str:String = findViewById<TextView>(R.id.fact).text.toString()
        val intent = Intent(this, SavedFactActivity::class.java)
        intent.putExtra("Send this", str)
        startActivity(intent)
    }


}

