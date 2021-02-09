package cn.leo.aplugin

import cn.leo.aplugin.tools.APluginState
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

    @Override
    void apply(Project project) {
        isAppModule = project.plugins.hasPlugin(AppPlugin)
        println(" isAppModule =" + isAppModule)

        BaseExtension extension = project.extensions.getByType(BaseExtension)
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
                        println("isAppModule $isAppModule isAppDebugBuild set false")
                        APluginState.isDebugApk = false
                        appVariant.mergeAssetsProvider.get().outputDir.get().asFileTree.each { file ->
                            println("file name=" + file.name)
                            if (file.name == 'OkHttpMock.json') {
                                file.delete()
                            }
                        }
                    }
                } else if (buildName.contains('debug')) {
                    appVariant.mergeAssetsProvider.get().doLast {
                        println("isAppModule $isAppModule isAppDebugBuild set true")
                        APluginState.isDebugApk = true

                    }
                }
            }
        }

        APluginState.readConfig()
        if (APluginState.isPluginEnable) {
            extension.registerTransform(new ATransform(isAppModule))
        } else {
            return
        }

        project.repositories {
            maven { url 'https://jitpack.io' }
        }
        if (APluginState.isRouterEffect) {
            project.dependencies {
                implementation Constant.FUNCTION_ROUTER_LIBRARY
            }
        }
        if (APluginState.isMockEffect && isAppModule) {
            project.dependencies {
                implementation Constant.FUNCTION_MOCK_LIBRARY
            }
        }
        println("APluginState.isMockEffect =" + APluginState.isMockEffect + " isAppModule=" + isAppModule)
    }

}
