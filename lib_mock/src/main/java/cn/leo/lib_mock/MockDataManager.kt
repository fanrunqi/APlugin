package cn.leo.lib_mock

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.os.Environment
import android.util.Log
import org.json.JSONArray
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.lang.reflect.Method

/**
 * Author: leo
 * Date: 2020/12/16 10:58
 * Description:
 */

object MockDataManager {
    private val TAG = "APlugin->Mock"

    private val mMap = HashMap<String, String>()
    private var mAppContext: Context? = null
    private var mSourceType: DataSourceType? = null

    fun getMockResponseJson(requestUrl: String?): String {
        loadData()
        if (requestUrl.isNullOrEmpty() || mMap.isEmpty()) {
            return ""
        }
        for ((key, value) in mMap) {
            if (requestUrl.contains(key)) {
                return value
            }
        }
        return ""
    }

    private fun loadData() {
        // Identify data sources
        var jsonResponse = ""
        if (mSourceType == null) {
            jsonResponse = getAssetResponse()
            if (jsonResponse.isEmpty()) {
                mSourceType = DataSourceType.TYPE_FILE
                jsonResponse = getFileResponse()
                if (jsonResponse.isEmpty()) {
                    return
                }
            } else {
                mSourceType = DataSourceType.TYPE_ASSET
            }
        }
        //Processing data updates
        if (mSourceType == DataSourceType.TYPE_FILE) {
            //check file
            jsonResponse = getFileResponse()
            if (jsonResponse.isEmpty()) {
                return
            }
        }

        try {
            val jsonArray = JSONArray(jsonResponse)
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                mMap[item.getString(Constant.MOCK_JSON_KEY)] =
                    item.getString(Constant.MOCK_JSON_VALUE)
            }
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }
    }

    private fun getAssetResponse(): String {
        if (mAppContext == null) {
            getContext()
        }
        var jsonResponse = ""
        var bufferedReader: BufferedReader? = null
        try {
            val stringBuilder = StringBuilder()
            val inputStream = mAppContext?.assets?.open(String.format(Constant.MOCK_FILE_NAME))
            bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var str: String?
            while (bufferedReader.readLine().also { str = it } != null) {
                stringBuilder.append(str)
            }
            jsonResponse = stringBuilder.toString()
        } catch (e: Exception) {
        } finally {
            bufferedReader?.apply {
                try {
                    bufferedReader.close()
                } catch (e: Exception) {
                }
            }
        }
        return jsonResponse
    }

    private fun getFileResponse(): String {
        if (mAppContext == null) {
            getContext()
        }
        var jsonResponse = ""
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
            || !Environment.isExternalStorageRemovable()
        ) {
            val path = mAppContext?.getExternalFilesDir(null)?.path
            val file = File(path, Constant.MOCK_FILE_NAME)
            if (!file.exists()) {
                Log.e(TAG, "The ${file.absolutePath} does not exit")
            } else {
                var bufferedReader: BufferedReader? = null
                try {
                    val stringBuilder = StringBuilder()
                    val inputStream = FileInputStream(file)
                    bufferedReader = BufferedReader(InputStreamReader(inputStream))
                    var str: String?
                    while (bufferedReader.readLine().also { str = it } != null) {
                        stringBuilder.append(str)
                    }
                    jsonResponse = stringBuilder.toString()
                } catch (e: Exception) {
                } finally {
                    bufferedReader?.apply {
                        try {
                            bufferedReader.close()
                        } catch (e: Exception) {
                        }
                    }
                }
            }
        } else {
            Log.e(TAG, "The external memory card does not exist")
        }
        return jsonResponse
    }

    @SuppressLint("PrivateApi")
    private fun getContext() {
        try {
            val mActivityThread = Class.forName("android.app.ActivityThread")
            val currentActivityThread: Any? =
                mActivityThread.getMethod("currentActivityThread").invoke(mActivityThread)
            mAppContext = currentActivityThread?.javaClass?.getMethod("getApplication")
                ?.invoke(currentActivityThread) as Context?
            Log.i(TAG, mAppContext.toString())
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }
    }
}