package com.eeextend.elfreader

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.xuexiang.xui.adapter.recyclerview.DividerItemDecoration


class MainActivity : AppCompatActivity() {

    private val APP_STORAGE_ACCESS_REQUEST_CODE = 501
    private val REQUEST_STORAGE_PERMISSIONS = 123
    private val REQUEST_MEDIA_PERMISSIONS = 456
    private val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
    private val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

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

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //As the device is Android 13 and above so I want the permission of accessing Audio, Images, Videos
            //You can ask permission according to your requirements what you want to access.
            val audioPermission = Manifest.permission.READ_MEDIA_AUDIO
            val imagesPermission = Manifest.permission.READ_MEDIA_IMAGES
            val videoPermission = Manifest.permission.READ_MEDIA_VIDEO
            // Check for permissions and request them if needed
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    audioPermission
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    imagesPermission
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    videoPermission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // You have the permissions, you can proceed with your media file operations.
                //Showing dialog when Show Dialog button is clicked.

                openSystemFile()


            } else {
                // You don't have the permissions. Request them.
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf<String>(audioPermission, imagesPermission, videoPermission),
                    REQUEST_MEDIA_PERMISSIONS
                )
            }
        } else {
            //Android version is below 13 so we are asking normal read and write storage permissions
            // Check for permissions and request them if needed
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    readPermission
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    writePermission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // You have the permissions, you can proceed with your file operations.
                // Show the file picker dialog when needed





            } else {
                // You don't have the permissions. Request them.
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf<String>(readPermission, writePermission),
                    REQUEST_STORAGE_PERMISSIONS
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_STORAGE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions were granted. You can proceed with your file operations.
                //Showing dialog when Show Dialog button is clicked.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    //Android version is 11 and above so to access all types of files we have to give
                    //special permission so show user a dialog..
                    //accessAllFilesPermissionDialog("1")
                } else {
                    //Android version is 10 and below so need of special permission...
                    //openSystemFile()
                }
            } else {
                // Permissions were denied. Show a rationale dialog or inform the user about the importance of these permissions.
                //showRationaleDialog("1")
            }
        }

        //This conditions only works on Android 13 and above versions
        if (requestCode == REQUEST_MEDIA_PERMISSIONS) {
            if (grantResults.isNotEmpty() && areAllPermissionsGranted(grantResults)) {
                // Permissions were granted. You can proceed with your media file operations.
                //Showing dialog when Show Dialog button is clicked.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    //Android version is 11 and above so to access all types of files we have to give
                    //special permission so show user a dialog..
                    //accessAllFilesPermissionDialog("2")
                }
            } else {
                // Permissions were denied. Show a rationale dialog or inform the user about the importance of these permissions.
                //showRationaleDialog("2")
            }
        }
    }

    private fun areAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun showRationaleDialog(string: String) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, readPermission) ||
            ActivityCompat.shouldShowRequestPermissionRationale(this, writePermission)
        ) {
            // Show a rationale dialog explaining why the permissions are necessary.
            AlertDialog.Builder(this)
                .setTitle(string+"Permission Needed")
                .setMessage("This app needs storage permissions to read and write files.")
                .setPositiveButton("OK") { dialog, which ->
                    // Request permissions when the user clicks OK.
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf<String>(readPermission, writePermission),
                        REQUEST_STORAGE_PERMISSIONS
                    )
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .show()
        } else {
            // Request permissions directly if no rationale is needed.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(readPermission, writePermission),
                REQUEST_STORAGE_PERMISSIONS
            )
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    open fun accessAllFilesPermissionDialog(string: String) {
        AlertDialog.Builder(this)
            .setTitle("Permission Needed")
            .setMessage(string+"This app needs all files access permissions to view files from your storage. Clicking on OK will redirect you to new window were you have to enable the option.")
            .setPositiveButton("OK") { dialog, which ->
                // Request permissions when the user clicks OK.
                val intent = Intent(
                    Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                    Uri.parse("package:com.eeextend.elfreader")
                )
                startActivityForResult(intent, APP_STORAGE_ACCESS_REQUEST_CODE)
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
                // Handle the case where the user cancels the permission request.
                openSystemFile()
            }
            .show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_STORAGE_ACCESS_REQUEST_CODE) {
            // Permission granted. Now resume your workflow.
            openSystemFile()
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

        button.setOnClickListener {
            checkPermissions()
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