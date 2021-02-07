package cn.leo.module_a

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.leo.lib_router.Router
import cn.leo.lib_router.annotation.Route
import cn.leo.lib_router.annotation.RouteParam
import java.util.ArrayList
import java.util.HashMap


@Route("NeedLogin/RouterKotlinActivity")
class RouterKotlinActivity : AppCompatActivity() {

    @RouteParam
    var mIntValue: Int = 0

    @RouteParam
    var mBooleanValue: Boolean = false

    @RouteParam
    var mLongValue: Long = 0L

    @RouteParam
    var mFloatValue: Float = 0.0f

    @RouteParam
    var mDoubleValue: Double = 0.001

    @RouteParam
    var mByteValue: Byte? = null

    @RouteParam
    var mCharValue: Char? = null

    @RouteParam
    var mShortValue: Short? = null

    @RouteParam("mStringValue")
    var mShowText = "text"

    @RouteParam
    var mListValue: List<String> = ArrayList()

    @RouteParam
    var mMapValue: Map<String, String> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_router_kotlin)

        val textView = findViewById<TextView>(R.id.textView)
        val str = """intValue = $mIntValue
            mBooleanValue = $mBooleanValue
            mLongValue = $mLongValue
            mFloatValue = $mFloatValue
            mShowText = $mShowText
            list[0].value = ${mListValue[0]}
            map = ${mMapValue["key"]}
            """.trimIndent()
        textView.text = str

        findViewById<Button>(R.id.button).setOnClickListener {
            Router.Builder()
                .setPath("/MainActivity")
                .build()
                .start(this@RouterKotlinActivity)
        }
    }
}