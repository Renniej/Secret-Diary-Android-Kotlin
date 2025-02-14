package org.hyperskill.secretdiary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    val STORED_PIN = "1234"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val etPin = findViewById<EditText>(R.id.etPin)

       loginBtn.setOnClickListener {
           val enteredPin = etPin.text.toString()

           if (enteredPin != STORED_PIN) {
               etPin.error = "Wrong PIN!"
           } else {
               val intent = Intent(this, MainActivity::class.java)
               intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
               startActivity(intent)
               finish()
           }
       }

    }
}