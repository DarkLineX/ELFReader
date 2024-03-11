package com.eeextend.elfreader

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.xuexiang.xui.adapter.recyclerview.DividerItemDecoration


class MainActivity : AppCompatActivity() {

    private val startActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val intent = it.data
                val uri = intent!!.data
                if (uri!!.path!!.endsWith(".so")) {
                    val path = PathUtils.getPath(this,uri)
                    ToastUtils.showLong("select file $path")
                    readFile(path)
                } else {
                    ToastUtils.showLong("select file format error !!!" + uri!!.encodedPath!!)
                }
            } }

    private fun openSystemFile(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivity.launch(Intent.createChooser(intent, "select a elf file"))
    }

    private lateinit var recycler: RecyclerView
    private lateinit var button: Button
    private lateinit var adapter: SimpleRecyclerAdapter

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        recycler = findViewById(R.id.recycler)
        button.setOnClickListener {
            var delayMillis: Long = 0
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                delayMillis = 2000
            }
            Handler().postDelayed({
                runPermissions()
            }, delayMillis);
        }
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(DividerItemDecoration(this,1))
    }

    private fun readFile(urlPath:String?){
       val fileInfoList =  ElfFileReader(urlPath).getFileInfoList()
        adapter = SimpleRecyclerAdapter(fileInfoList)
        recycler.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun runPermissions() {
        XXPermissions.with(this@MainActivity)
            .permission(listOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO))
            .interceptor(PermissionInterceptor())
            .request(OnPermissionCallback { _, allGranted ->
                if (!allGranted) {
                    return@OnPermissionCallback
                }
                openSystemFile()
            })
    }
}