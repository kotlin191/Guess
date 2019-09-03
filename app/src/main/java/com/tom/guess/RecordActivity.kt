package com.tom.guess

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.tom.guess.data.GameDatabase
import com.tom.guess.data.Record
import kotlinx.android.synthetic.main.activity_record.*

class RecordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        val count = intent.getIntExtra("COUNTER", -1)
        counter.setText(count.toString())
        // OnClickListener
        save.setOnClickListener { view ->
            val nick = nickname.text.toString()
            getSharedPreferences("guess", Context.MODE_PRIVATE)
                .edit()
                .putInt("REC_COUNTER", count)
                .putString("REC_NICKNAME", nick)
                .apply()
            // insert to Room
            Thread(){
                GameDatabase.getInstance(this)?.recordDao()?.
                    insert(Record(nick, count))
            }.start()

            val intent = Intent()
            intent.putExtra("NICK", nick)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
