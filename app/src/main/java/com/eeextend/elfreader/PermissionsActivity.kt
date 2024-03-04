package com.eeextend.elfreader

import android.Manifest
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

open class PermissionsActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    companion object{
        var READ_WRITE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        const val RC_READ_WRITE = 123
    }

    private fun hasReadWritePermissions(): Boolean {
        return EasyPermissions.hasPermissions(this, *READ_WRITE)
    }


    @AfterPermissionGranted(value = RC_READ_WRITE)
    fun requestAllPermissions() {
        if (hasReadWritePermissions()) {
            //Toast.makeText(this, "apk need read and write permissions", Toast.LENGTH_LONG).show();
        } else {
            EasyPermissions.requestPermissions(this, "", RC_READ_WRITE, *READ_WRITE)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }
}