package cn.leo.aplugin.parser

import cn.leo.aplugin.tools.Utils
import groovy.io.FileType
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/20 14:56
 * description:
 */
class ClassParser {

    static void process(File inputDir, File destDir, File temporaryDir) {
        def sourcePath = inputDir.absolutePath
        def destPath = destDir.absolutePath

        inputDir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) { File file ->
            if (Utils.isNeedClassFile(file)) {
                FileOutputStream outputStream = null
                try {
                    def className = file.name
                    println("class name = " + className)
                    def inputStream = new FileInputStream(file)
                    // new modifiedFile
                    File modifiedFile = new File(temporaryDir, className)
                    if (modifiedFile.exists()) {
                        modifiedFile.delete()
                    }
                    modifiedFile.createNewFile()
                    // write new data to modifiedFile
                    outputStream = new FileOutputStream(modifiedFile)
                    ClassReader classReader = new ClassReader(inputStream)
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    ClassVisitor classVisitor = new ClassParserVisitor(classWriter)
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    outputStream.write(classWriter.toByteArray())
                    // modifiedFile replace destPath file
                    def target = new File(file.absolutePath.replace(sourcePath, destPath))
                    if (target.exists()) {
                        target.delete()
                    }
                    FileUtils.copyFile(modifiedFile, target)
                    modifiedFile.delete()
                } catch (Exception exception) {
                    println("---EnhancementsTransform: file handle error = " + exception.toString())
                } finally {
                    outputStream.close()
                }
            }
        }
    }
}