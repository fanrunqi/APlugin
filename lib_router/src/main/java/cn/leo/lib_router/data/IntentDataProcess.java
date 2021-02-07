package cn.leo.lib_router.data;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import cn.leo.lib_router.process.RouteStatus;
import cn.leo.lib_router.process.RouterRequest;
import cn.leo.lib_router.process.RouterResponse;

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/29 12:52
 * description:
 */
public class IntentDataProcess {

    public static RouterResponse convertDataIn(RouterRequest request) {
        RouterResponse response = new RouterResponse();
        Map<String, Object> map = request.getExtraMap();
        if (map.size() == 0) {
            return response;
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(entry.getValue());
                String objectVal = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
                request.getIntent().putExtra(entry.getKey(), objectVal);
            } catch (Exception e) {
                response.setContent("extra data convert fail :" + e.toString(), RouteStatus.FAILED);
            } finally {
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                    if (objectOutputStream != null) {
                        objectOutputStream.close();
                    }
                } catch (IOException ignore) {
                }
            }
        }
        return response;
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertDataOut(@NonNull Activity context, String key, T defaultValue) {
        Intent intent = context.getIntent();
        if (intent == null) {
            return defaultValue;
        }

        Object object = defaultValue;
        if (defaultValue instanceof Integer) {
            object = intent.getIntExtra(key, (Integer) defaultValue);
        } else if (defaultValue instanceof String) {
            String value = intent.getStringExtra(key);
            if (!TextUtils.isEmpty(value)) {
                object = value;
            }
        } else if (defaultValue instanceof Boolean) {
            object = intent.getBooleanExtra(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Double) {
            object = intent.getDoubleExtra(key, (Double) defaultValue);
        } else if (defaultValue instanceof Long) {
            object = intent.getLongExtra(key, (Long) defaultValue);
        } else if (defaultValue instanceof Short) {
            object = intent.getShortExtra(key, (Short) defaultValue);
        } else if (defaultValue instanceof Float) {
            object = intent.getFloatExtra(key, (Float) defaultValue);
        } else if (defaultValue instanceof Byte) {
            object = intent.getByteExtra(key, (Byte) defaultValue);
        } else if (defaultValue instanceof Character) {
            object = intent.getCharExtra(key, (Character) defaultValue);
        } else {
            String data = intent.getStringExtra(key);
            if (TextUtils.isEmpty(data)) {
                return defaultValue;
            }
            byte[] buffer = Base64.decode(data, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
            ObjectInputStream objectInputStream = null;
            try {
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                object = objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                Log.e("ByteRouter:", "get intent data failed with " + e.toString());
            } finally {
                try {
                    if (byteArrayInputStream != null) {
                        byteArrayInputStream.close();
                    }
                    if (objectInputStream != null) {
                        objectInputStream.close();
                    }
                } catch (IOException ignore) {
                }
            }
        }
        return (T) object;
    }
}