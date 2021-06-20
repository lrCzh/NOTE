package com.czh.note.db

import com.czh.note.R
import com.czh.note.config.AppConfig
import com.thegrizzlylabs.sardineandroid.Sardine
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import java.io.File
import java.io.FileOutputStream

/**
 * @Description: webdav备份管理类
 * @Author: czh
 * @CreateDate: 2021/2/2 19:09
 */
object SyncManager {

    private const val SERVER_URL = "https://dav.jianguoyun.com/dav/"
    private const val DIR_PATH = "FileOfDaily/"
    private const val BACKUP_DIR = "backup"

    private var alreadyInitAccount: Boolean = false

    private val sardine: Sardine by lazy {
        OkHttpSardine()
    }

    fun initAccount(username: String, password: String) {
        sardine.setCredentials(username, password)
        alreadyInitAccount = true
    }

    /**
     * @description: 是否已经有该App对应的文件备份目录
     * @params: Unit
     * @return: 是否存在
     * @author: czh
     */
    private fun existDir(): Boolean {
        checkInit()
        return sardine.exists("$SERVER_URL$DIR_PATH")
    }

    /**
     * @description: 创建App的文件备份目录
     * @params: Unit
     * @return: Unit
     * @author: czh
     */
    private fun createDir() {
        checkInit()
        sardine.createDirectory("$SERVER_URL$DIR_PATH")
    }

    /**
     * @description: 上传文件
     * @params: 待上传的文件
     * @return: Unit
     * @author: czh
     */
    private fun upload(file: File) {
        checkInit()
        if (!existDir()) {
            createDir()
        }
        val bytes = file.readBytes()
        sardine.put("$SERVER_URL$DIR_PATH${file.name}", bytes)
    }

    /**
     * @description: 下载文件
     * @params: 待下载的文件名
     * @return: Unit
     * @author: czh
     */
    private fun download(fileName: String): File {
        checkInit()
        val file = File(AppConfig.mContext.getExternalFilesDir(BACKUP_DIR), fileName)
        if (file.exists()) {
            file.delete()
        }
        val inputStream = sardine.get("$SERVER_URL$DIR_PATH$fileName")
        val outputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } != -1) {
            outputStream.write(buffer, 0, length)
        }
        outputStream.flush()
        inputStream.close()
        outputStream.close()
        return file
    }

    /**
     * @description: 删除备份文件
     * @params: 待删除的文件名
     * @return: Unit
     * @author: czh
     */
    private fun delete(fileName: String) {
        checkInit()
        if (sardine.exists("$SERVER_URL$DIR_PATH$fileName")) {
            sardine.delete("$SERVER_URL$DIR_PATH$fileName")
        }
    }

    private fun checkInit() {
        check(alreadyInitAccount) {
            AppConfig.mContext.resources.getString(R.string.sync_uninitialized_webdev_account)
        }
    }
}