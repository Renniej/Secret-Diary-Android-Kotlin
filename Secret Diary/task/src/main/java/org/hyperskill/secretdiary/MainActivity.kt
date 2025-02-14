package org.hyperskill.secretdiary

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.log


class MainActivity : AppCompatActivity() {
    @SuppressLint("DefaultLocale")
    lateinit var diaryLog: TextView

    val ENTRY_SEPERATOR = "\n\n"
    val DIARY_TEXT_KEY = "KEY_DIARY_TEXT"
//
    val PREFERENCES_NAME = "PREF_DIARY"
    lateinit var sharedPreferences : SharedPreferences


    @SuppressLint("DefaultLocale")
    fun addEntry(entryText : String) {
        val instant = Clock.System.now()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dateText = simpleDateFormat.format(instant.toEpochMilliseconds())


        val fullEntryText = "$dateText\n$entryText"

        if (diaryLog.text.isEmpty()) {
            diaryLog.text = fullEntryText
        } else {
            diaryLog.text =  "$fullEntryText$ENTRY_SEPERATOR${diaryLog.text}"
        }


        saveState()
    }

    private fun saveState() {
      sharedPreferences.edit().putString(DIARY_TEXT_KEY, diaryLog.text.toString()).apply()
    }

    private fun undo() {
        val entries = diaryLog.text.toString()
        val logHistory = entries.split(ENTRY_SEPERATOR).toMutableList()
        logHistory.removeAt(0)
        diaryLog.text = logHistory.joinToString(ENTRY_SEPERATOR).removeSuffix(ENTRY_SEPERATOR)
        saveState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      sharedPreferences= getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)

        val saveBtn = findViewById<Button>(R.id.btnSave)
        val undoBtn = findViewById<Button>(R.id.btnUndo)
        val diaryTxt = findViewById<EditText>(R.id.etNewWriting)
         diaryLog = findViewById<TextView>(R.id.tvDiary)



       diaryLog.text = sharedPreferences.getString(DIARY_TEXT_KEY, "") ?: ""



        undoBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.apply {
                title = "Remove last note"
                setNegativeButton("No")  { dialog, which -> }
                setPositiveButton("Yes") {dialog, which ->
                    if ( diaryLog.text.isNotEmpty()) {
                        undo()
                    }
                }

                setMessage("Do you really want to remove the last writing? This operation cannot be undone!")
            }

            builder.create().show()
        }

        saveBtn.setOnClickListener {
            val newText = diaryTxt.text.toString()
            if (newText.isBlank()) {
                Toast.makeText(this,"Empty or blank input cannot be saved",Toast.LENGTH_SHORT ).show()
            } else {
                addEntry(newText)
                diaryTxt.text.clear()
            }
        }

        /*
            Tests for android can not guarantee the correctness of solutions that make use of
            mutation on "static" variables to keep state. You should avoid using those.
            Consider "static" as being anything on kotlin that is transpiled to java
            into a static variable. That includes global variables and variables inside
            singletons declared with keyword object, including companion object.
            This limitation is related to the use of JUnit on tests. JUnit re-instantiate all
            instance variable for each test method, but it does not re-instantiate static variables.
            The use of static variable to hold state can lead to state from one test to spill over
            to another test and cause unexpected results.
            Using mutation on static variables to keep state
            is considered a bad practice anyway and no measure
            attempting to give support to that pattern will be made.
         */
    }
}