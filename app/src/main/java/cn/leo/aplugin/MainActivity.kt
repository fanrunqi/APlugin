package cn.leo.aplugin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import cn.leo.lib_router.Router
import cn.leo.lib_router.annotation.Route

@Route("/MainActivity")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
}