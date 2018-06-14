package com.flycode.musclemax_app.util

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FileUtils {

    fun saveImage(context: Context, finalBitmap: Bitmap, path: String,name: String): String? {
        val root = context.filesDir.absolutePath
        val myDir = File(root + path)

        return try {
            myDir.mkdirs()

            val file = File(myDir, name)
            if (file.exists())
                file.delete()
            else file.createNewFile()

            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    fun deleteImage(imagePath: String): Boolean {
        val file = File(imagePath)
        return file.exists() && file.delete()
    }

    fun readFileToBytes(path : String): ByteArray{
        val file = File(path)
        //init array with file length
        val bytesArray = ByteArray(file.length().toInt())
        val fis = FileInputStream(file)
        fis.read(bytesArray) //read file into bytes[]
        fis.close()
        return bytesArray
    }
}
