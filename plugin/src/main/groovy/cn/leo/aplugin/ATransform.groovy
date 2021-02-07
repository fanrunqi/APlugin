package cn.leo.aplugin

import cn.leo.aplugin.parser.ClassParser
import cn.leo.aplugin.parser.JarParser
import cn.leo.aplugin.tools.Constant
import cn.leo.aplugin.tools.Logger
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.io.FileUtils
import java.util.function.Consumer

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/20 14:55
 * description:
 */
class ATransform extends Transform {
    private Set scopes

    ATransform(boolean isAppModule) {
        scopes = isAppModule ? TransformManager.SCOPE_FULL_PROJECT : TransformManager.PROJECT_ONLY
    }

    @Override
    String getName() {
        return "EnhancementsTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return scopes
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) {
        def outputProvider = transformInvocation.outputProvider
        def transformInputs = transformInvocation.inputs
        def temporaryDir = transformInvocation.context.temporaryDir
        File mJarInput = null
        File mDestFile = null

        transformInputs.forEach(new Consumer<TransformInput>() {
            @Override
            void accept(TransformInput transformInput) {

                transformInput.directoryInputs.forEach(new Consumer<DirectoryInput>() {
                    @Override
                    void accept(DirectoryInput directoryInput) {
                        def inputDir = directoryInput.file
                        def destDir = outputProvider.getContentLocation(
                                directoryInput.name,
                                directoryInput.contentTypes,
                                directoryInput.scopes,
                                Format.DIRECTORY
                        )
                        FileUtils.copyDirectory(inputDir, destDir)
                        ClassParser.process(inputDir, destDir, temporaryDir)
                    }
                })

                transformInput.jarInputs.forEach(new Consumer<JarInput>() {
                    @Override
                    void accept(JarInput jarInput) {
                        def jarInputFile = jarInput.file
                        def jarInputFileName = jarInputFile.name
                        def destJar = outputProvider.getContentLocation(
                                jarInput.name,
                                jarInput.contentTypes,
                                jarInput.scopes,
                                Format.JAR
                        )

                        if (jarInputFileName.contains(Constant.JAR_INPUT_MATCH_SUFFIX)) {
                            mDestFile = destJar
                            mJarInput = jarInputFile
                        } else {
                            FileUtils.copyFile(jarInputFile, destJar)
                        }
                    }
                })
            }
        })

        if (mJarInput != null) {
            Logger.printCopyright()
            JarParser.process(mJarInput, mDestFile, temporaryDir)
        }
    }
}
