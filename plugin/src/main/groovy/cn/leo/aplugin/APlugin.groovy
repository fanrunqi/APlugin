package cn.leo.aplugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
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
        isAppModule = project.plugins.hasPlugin(AppPlugin.class)
        def extension = project.extensions.getByType(BaseExtension)
        extension.registerTransform(new ATransform(isAppModule))

        dependencyManagement(project, isAppModule)
    }

    void dependencyManagement(Project project, boolean isAppModule) {
        if (isAppModule) {
            project.dependencies {
//                implementation project(':lib_router')
            }
        }
    }
}