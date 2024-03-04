package com.eeextend.elfreader

import android.R.attr.path
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import java.io.FileInputStream
import java.io.IOException
import java.lang.Exception


class ElfFileReader(fileUrlPath: String?) {
    private val fileUrlPath = fileUrlPath

    fun getFileInfoList():ArrayList<FileInfo>{
        val infoLists = ArrayList<FileInfo>()
        if(FileUtils.isFileExists(fileUrlPath)){
            val fileMd5 = FileUtils.getFileMD5ToString(fileUrlPath)
            infoLists.add(FileInfo("ELF File Path",fileUrlPath))
            infoLists.add(FileInfo("ELF File MD5",fileMd5))
            infoLists.add(FileInfo("ELF File Size",FileUtils.getSize(fileUrlPath)))
        }else{
            ToastUtils.showLong("file not exists $fileUrlPath")
        }
        return infoLists
    }
    fun eflParser(path: String){
        try {
            val inputStream: FileInputStream = FileInputStream(path)
        }catch (e: IOException){

        }

    }
}