package cn.leo.aplugin.parser


import cn.leo.aplugin.tools.Constant
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/20 14:57
 * description:
 */
class JarParser {

    static void process(File jarInput, File destJar, File temporaryDir) {
        println("destJar = " + destJar.getAbsolutePath())
        def jarWrapFile = new JarFile(jarInput)
        def tempFile = new File(temporaryDir, System.currentTimeMillis() + jarInput.name)

        final JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tempFile))
        final Enumeration enumeration = jarWrapFile.entries()
        while (enumeration.hasMoreElements()) {
            final JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            final String entryName = jarEntry.getName()
            final ZipEntry zipEntry = new ZipEntry(entryName)
            jarOutputStream.putNextEntry(zipEntry)

            final InputStream inputStream = jarWrapFile.getInputStream(jarEntry)
            if (entryName.endsWith(Constant.JAR_MODIFY_CLASS_ENTRY_NAME_SUFFIX)) {
                ClassReader classReader = new ClassReader(inputStream)
                ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                ClassVisitor classVisitor = new JarParserVisitor(classWriter)
                classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                jarOutputStream.write(classWriter.toByteArray())
            } else {
                try {
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                } catch (Exception ignore) {
                }
            }
            jarOutputStream.write(IOUtils.toByteArray(inputStream))
            inputStream.close()
            jarOutputStream.closeEntry()
        }
        jarOutputStream.close()
        jarWrapFile.close()

        if (tempFile != null) {
            FileUtils.copyFile(tempFile, destJar)
            tempFile.delete()
        } else {
            FileUtils.copyFile(jarInput, destJar)
        }
    }

}