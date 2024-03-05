package com.eeextend.elfreader

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.xuexiang.xui.adapter.recyclerview.DividerItemDecoration
import com.xuexiang.xui.utils.XToastUtils.toast
import java.lang.String.format


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
        val intent = Intent(Intent.ACTION_GET_CONTENT);
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivity.launch(Intent.createChooser(intent, "select a elf file"))
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
        button.setOnClickListener {
            var delayMillis: Long = 0
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                delayMillis = 2000
            }
            Handler().postDelayed( Runnable  {
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

    private fun runPermissions() {
        XXPermissions.with(this@MainActivity) // 适配分区存储应该这样写
            .permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .interceptor(PermissionInterceptor())
            .request(OnPermissionCallback { permissions, allGranted ->
                if (!allGranted) {
                    return@OnPermissionCallback
                }
                openSystemFile()
            })
    }
}