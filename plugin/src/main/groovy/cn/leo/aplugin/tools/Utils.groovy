package cn.leo.aplugin.tools

import com.android.SdkConstants

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/2/8 16:49
 * description:
 */
class Utils{

    static boolean isNeedClassFile(File file) {
        def className = file.name
        if (!className.endsWith(SdkConstants.DOT_CLASS)) {
            return false
        }

        if (className.contains("R\$") ||
                className.contains("R2\$") ||
                className.endsWith("R.class") ||
                className.endsWith("R2.class") ||
                className.endsWith("BuildConfig.class")) {
            return false
        }

        def filePackagePath = file.getAbsolutePath().replace(File.separator, ".")
        if (filePackagePath.contains('android.support') ||
                filePackagePath.contains('androidx.') ||
                filePackagePath.contains('android.arch')) {
            return false
        }

        return true
    }
}