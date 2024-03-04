package com.eeextend.elfreader

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.xuexiang.xui.adapter.recyclerview.DividerItemDecoration

class MainActivity : PermissionsActivity() {

    private val startActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val intent = it.data
                val uri = intent!!.data
                if (uri!!.path!!.endsWith(".so")) {
                    val path = PathUtils.getPath(this,uri)
                    ToastUtils.showLong("select file ${path}")
                    readFile(path)
                } else {
                    ToastUtils.showLong("select file format error !!!")
                }
            }
        }

    private lateinit var recycler: RecyclerView
    private lateinit var button: Button
    private lateinit var adapter: SimpleRecyclerAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        recycler = findViewById(R.id.recycler)

        requestAllPermissions()

        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT);
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivity.launch(Intent.createChooser(intent, "select a elf file"))
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(DividerItemDecoration(this,1))

    }

    private fun readFile(urlPath:String?){
       val fileInfoList =  ElfFileReader(urlPath).getFileInfoList()
        adapter = SimpleRecyclerAdapter(fileInfoList)
        recycler.adapter = adapter
    }
}