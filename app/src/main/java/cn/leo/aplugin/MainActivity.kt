package cn.leo.aplugin

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.leo.aplugin.mock.MockActivity
import cn.leo.lib_router.Router
import cn.leo.lib_router.annotation.Route

@Route("/MainActivity")
class MainActivity : AppCompatActivity() {

    @SuppressLint("PrivateApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.bt_mock).setOnClickListener {
            startActivity(Intent(this@MainActivity, MockActivity::class.java))
        }


        findViewById<Button>(R.id.bt_router).setOnClickListener {
            val list = ArrayList<String>()
            list.add("list from main")
            val map = HashMap<String, String>()
            map["key"] = "map from main"
            Router.Builder()
                .setPath("NeedLogin/RouterJavaActivity")
                .putExtra("mIntValue", 123456)
                .putExtra("mBooleanValue", true)
                .putExtra("mLongValue", 987654321L)
                .putExtra("mFloatValue", 100.0f)
                .putExtra("mStringValue", "from main")
                .putExtra("mListValue", list)
                .putExtra("mMapValue", map)
                .setResponseListener {
                    Log.e("Router", it.message)
                }
                .build()
                .start(this@MainActivity)
            finish()
        }

    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
//               申请权限
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            } else {
//              申请过权限，但是用户彻底决绝了或是手机不允许拥有此权限，执行相应的操作：
            }
        }
    }
}