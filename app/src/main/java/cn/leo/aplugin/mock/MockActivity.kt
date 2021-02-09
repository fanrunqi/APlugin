package cn.leo.aplugin.mock

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import cn.leo.aplugin.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("SetTextI18n")
class MockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock)

        val textView = findViewById<TextView>(R.id.tv_response)
        val request = RetrofitInstance.get().create(RequestService::class.java)
        val call: Call<BaseResponse<String>> = request.getRightApi()
        call.enqueue(object : Callback<BaseResponse<String>> {
            override fun onResponse(call: Call<BaseResponse<String>>, response: Response<BaseResponse<String>>) {
                runOnUiThread {
                    textView.text = "getRightApi: = ${response.body().toString()}"
                }
            }

            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                Log.e("APlugin", t.message)
                Toast.makeText(applicationContext, "on failed: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}