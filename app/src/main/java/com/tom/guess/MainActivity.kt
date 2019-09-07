package com.tom.guess

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_function.view.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_CAMERA = 100
    val TAG = MainActivity::class.java.simpleName
    var cacheService : Intent? = null

    val functions = listOf<String>(
        "Camera",
        "Guess game",
        "Record list",
        "Download coupons",
        "News",
        "Maps")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //RecyclerView

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        recycler.adapter = FunctionAdapter()
        //spinner
        val colors = arrayOf("Red", "Green", "Blue")
        val adapter = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, colors)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                Log.d(TAG, "onItemSelected: ${colors[position]}");
            }

        }
    }

    inner class FunctionAdapter() : RecyclerView.Adapter<FunctionHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_function, parent, false)
            val holder = FunctionHolder(view)
            return holder
        }

        override fun getItemCount(): Int {
            return functions.size
        }

        override fun onBindViewHolder(holder: FunctionHolder, position: Int) {
            holder.nameText.text = functions.get(position)
            holder.itemView.setOnClickListener {
                functionClicked(position)
            }
        }
    }

    private fun functionClicked(position: Int) {
        when(position) {
            0 -> {
                val permission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    takePhoto()
                } else {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA)
                }
            }
            1 -> startActivity(Intent(this, MaterialActivity::class.java))
            2 -> startActivity(Intent(this, RecordListActivity::class.java))
            else -> return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            }
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivity(intent)
    }

    class FunctionHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameText: TextView = view.name
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_cache) {
            Log.d(TAG, "Cache selected");
            cacheService = Intent(this, CacheService::class.java)
            startService(cacheService)
            startService(Intent(this, CacheService::class.java))
            startService(Intent(this, CacheService::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        stopService(cacheService)
    }
}
