package cn.leo.aplugin.parser

import cn.leo.aplugin.execute.RouteExecute
import cn.leo.aplugin.tools.Constant
import org.objectweb.asm.*
import org.objectweb.asm.commons.AdviceAdapter

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/28 10:38
 * description:
 */
class JarParserVisitor extends ClassVisitor {
    private String mClassName
    private String mSuperName

    JarParserVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.mClassName = name
        this.mSuperName = superName
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        final MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (name == Constant.JAR_MODIFY_CLASS_METHOD_NAME_INJECT_ROUTER_LIST) {
            return new AdviceAdapter(Opcodes.ASM7, methodVisitor, access, name, descriptor) {
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)
                    RouteExecute.mRouteMap.each { item ->
                        methodVisitor.visitVarInsn(ALOAD, 0)
                        methodVisitor.visitLdcInsn(item.key)
                        methodVisitor.visitLdcInsn(item.value)
                        methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                                "java/util/Map",
                                "put",
                                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                                true)
                        methodVisitor.visitInsn(POP)
                    }
                }
            }
        } else if (name == Constant.JAR_MODIFY_CLASS_METHOD_NAME_INJECT_INTERCEPTOR_LIST) {
            return new AdviceAdapter(Opcodes.ASM7, methodVisitor, access, name, descriptor) {
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)
                    println("RouteExecute.mInterceptorRules.size = " + RouteExecute.mInterceptorMap.size())
                    RouteExecute.mInterceptorMap.each { item ->
                        methodVisitor.visitVarInsn(ALOAD, 0)
                        methodVisitor.visitLdcInsn(item.key)
                        methodVisitor.visitLdcInsn(item.value)
                        methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                                "java/util/Map",
                                "put",
                                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                                true)
                        methodVisitor.visitInsn(POP)
                    }
                }
            }
        }
        return methodVisitor
    }

    @Override
    void visitEnd() {
        super.visitEnd()
        RouteExecute.clear()
    }
}