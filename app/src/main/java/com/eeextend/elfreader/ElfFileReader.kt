package com.eeextend.elfreader

import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.eeextend.elfreader.jelf.ElfFile
import java.io.File


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
            val elfFile = ElfFile.from(file)
            if(elfFile.is32Bits){
                infoLists.add(FileInfo("ELF CPU INFO", "32"))
            }else {
                infoLists.add(FileInfo("ELF CPU INFO","64"))
            }
            infoLists.add(FileInfo("ELF e_ident", elfFile.e_ident.toHex()))
            infoLists.add(FileInfo("ELF e_type",elfFile.e_type.toString() ))
            infoLists.add(FileInfo("ELF e_machine",elfFile.e_machine.toString() ))
            infoLists.add(FileInfo("ELF e_version",elfFile.e_version.toString() ))
            infoLists.add(FileInfo("ELF e_entry",elfFile.e_entry.toString() ))
            infoLists.add(FileInfo("ELF e_phoff",elfFile.e_phoff.toString() ))
            infoLists.add(FileInfo("ELF e_shoff",elfFile.e_shoff.toString() ))
            infoLists.add(FileInfo("ELF e_flags",elfFile.e_flags.toString() ))
            infoLists.add(FileInfo("ELF e_ehsize",elfFile.e_ehsize.toString() ))
            infoLists.add(FileInfo("ELF e_phentsize",elfFile.e_phentsize.toString() ))
            infoLists.add(FileInfo("ELF e_phnum",elfFile.e_phnum.toString() ))
            infoLists.add(FileInfo("ELF e_shentsize",elfFile.e_shentsize.toString() ))
            infoLists.add(FileInfo("ELF e_shnum",elfFile.e_shnum.toString() ))
            infoLists.add(FileInfo("ELF e_shstrndx",elfFile.e_shstrndx.toString() ))

            for (i in 0 until elfFile.e_shnum) {
               val secion =  elfFile.getSection(i)
                infoLists.add(FileInfo("ELF secion",secion.toString() ))
            }

            for (i in 0 until elfFile.e_phnum) {
                val programHeaders =  elfFile.getProgramHeader(i)
                infoLists.add(FileInfo("ELF programHeaders",programHeaders.toString()))
            }

        }else{
            ToastUtils.showLong("file not exists $fileUrlPath")
        }
        return infoLists
    }



    private fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }


}