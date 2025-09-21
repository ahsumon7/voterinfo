package com.example.demovoteinfo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.demovoteinfo.R // Ensure R is imported if not already

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cvVoterInfo: CardView = findViewById(R.id.cvVoterInfo)
        cvVoterInfo.setOnClickListener {
            val intent = Intent(this, VoterListActivity::class.java)
            startActivity(intent)
        }
    }
}
