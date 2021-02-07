package cn.leo.aplugin

import cn.leo.aplugin.tools.Constant
import cn.leo.aplugin.tools.Logger
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/20 14:54
 * description:
 */
class APlugin implements Plugin<Project> {
    boolean isAppModule
    boolean isAppDebugRelease

    @Override
    void apply(Project project) {
        isAppModule = project.plugins.hasPlugin(AppPlugin.class)
        def extension = project.extensions.getByType(BaseExtension)
        if (extension == null) {
            Logger.error("APlugin Only support Android project")
            return
        }
        if (isAppModule) {
            ((AppExtension) extension).applicationVariants.all { variant ->
                def appVariant = variant as ApplicationVariant
                def buildName = appVariant.buildType.name.toLowerCase()
                if (buildName.contains('release')) {
                    appVariant.mergeAssetsProvider.get().doLast {
                        appVariant.mergeAssetsProvider.get().outputDir.listFiles().each { file ->
                            if (file.name == 'RetrofitMock.json') {
                                file.delete()
                            }
                        }
                    }
                } else if (buildName.contains('debug')) {
                    isAppDebugRelease = true
                }
            }
        }

        def disableFunctionsStr = System.properties['APlugin.disableFunctions'].toString()
        boolean pluginEnable = false
        project.repositories {
            maven { url 'https://jitpack.io' }
        }
        if (!disableFunctionsStr.contains(Constant.FUNCTION_ROUTER)) {
            pluginEnable = true
            project.dependencies {
                implementation Constant.FUNCTION_ROUTER_LIBRARY
            }
        }

        if (pluginEnable) {
            extension.registerTransform(new ATransform(isAppModule))
        }
    }

}
