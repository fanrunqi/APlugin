package cn.leo.aplugin.router;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leo.aplugin.R;
import cn.leo.lib_router.Router;
import cn.leo.lib_router.annotation.Route;
import cn.leo.lib_router.annotation.RouteParam;


@Route("NeedLogin/RouterJavaActivity")
public class RouterJavaActivity extends AppCompatActivity {

    @RouteParam
    int mIntValue = 0;
    @RouteParam
    Boolean mBooleanValue = false;
    @RouteParam
    long mLongValue = 0L;
    @RouteParam
    float mFloatValue = 0.0f;

    @RouteParam
    double mDoubleValue;

    @RouteParam
    byte mByteValue;

    @RouteParam
    char mCharValue;

    @RouteParam
    short mShortValue;

    @RouteParam("mStringValue")
    String mShowText = "text";
    @RouteParam
    List<String> mListValue = new ArrayList<>();
    @RouteParam
    Map<String, String> mMapValue = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router_java);

        TextView textView = findViewById(R.id.textView);
        String str = "intValue = " + mIntValue + "\n"
                + "mBooleanValue = " + mBooleanValue + "\n"
                + "mLongValue = " + mLongValue + "\n"
                + "mFloatValue = " + mFloatValue + "\n"
                + "mShowText = " + mShowText + "\n"
                + "list[0].value = " + mListValue.get(0) + "\n"
                + "map = " + mMapValue.get("key");
        textView.setText(str);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.Builder()
                        .setPath("NeedLogin/RouterKotlinActivity")
                        .putExtra("mIntValue", mIntValue)
                        .putExtra("mBooleanValue", mBooleanValue)
                        .putExtra("mLongValue", mLongValue)
                        .putExtra("mFloatValue", mFloatValue)
                        .putExtra("mStringValue", mShowText)
                        .putExtra("mListValue", mListValue)
                        .putExtra("mMapValue", mMapValue)
                        .build()
                        .start(RouterJavaActivity.this);
            }
        });
    }

}

