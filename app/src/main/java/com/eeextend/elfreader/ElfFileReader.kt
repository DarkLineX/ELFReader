package com.eeextend.elfreader

import android.R.attr.path
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import net.fornwall.jelf.ElfFile
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.Exception


class ElfFileReader(fileUrlPath: String?) {
    private val fileUrlPath = fileUrlPath

    fun getFileInfoList():ArrayList<FileInfo>{
        val infoLists = ArrayList<FileInfo>()
        if(FileUtils.isFileExists(fileUrlPath)){
            val file = File(fileUrlPath)
            val fileMd5 = FileUtils.getFileMD5ToString(file)
            infoLists.add(FileInfo("ELF File Path",fileUrlPath))
            infoLists.add(FileInfo("ELF File MD5",fileMd5))
            infoLists.add(FileInfo("ELF File Size",FileUtils.getSize(file)))
            //val elfFile = ElfFile.from(file)
            //infoLists.add(FileInfo("ELF Type",elfFile.is32Bits.toString()))
        }else{
            ToastUtils.showLong("file not exists $fileUrlPath")
        }
        return infoLists
    }
}