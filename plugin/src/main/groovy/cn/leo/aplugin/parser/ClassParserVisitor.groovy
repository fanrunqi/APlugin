package cn.leo.aplugin.parser

import cn.leo.aplugin.execute.RouteExecute
import cn.leo.aplugin.tools.Constant
import org.apache.http.util.TextUtils
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

/**
 * author: leo
 * email: fanrunqi@qq.com
 * creation time: 2021/1/20 14:58
 * description:
 */
class ClassParserVisitor extends ClassVisitor implements Opcodes {
    private String mClassName
    private String mSuperName
    private boolean mHaveOverrideOnCreateMethod = false
    public List<InjectFieldItem> mFieldList = new ArrayList<>()

    ClassParserVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor)
    }


    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.mClassName = name
        this.mSuperName = superName
        println("mClassName = " + mClassName)
    }

    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(descriptor, visible)
        if (descriptor == Constant.ANNOTATION_DESCRIPTOR_ROUTER_PATHS) {
            return new AnnotationVisitor(ASM7, annotationVisitor) {
                @Override
                AnnotationVisitor visitArray(String name) {
                    return new AnnotationVisitor(ASM7) {
                        @Override
                        void visit(String name0, Object value) {
                            super.visit(name0, value)
                            if (!TextUtils.isEmpty(value)) {
                                RouteExecute.addRouteRule(mClassName, value)
                            }
                        }
                    }
                }
            }
        } else if (descriptor == Constant.ANNOTATION_DESCRIPTOR_ROUTER_INTERCEPTOR) {
            return new AnnotationVisitor(ASM7, annotationVisitor) {
                @Override
                AnnotationVisitor visitArray(String name) {
                    return new AnnotationVisitor(ASM7) {
                        @Override
                        void visit(String name0, Object value) {
                            super.visit(name0, value)
                            if (!TextUtils.isEmpty(value)) {
                                RouteExecute.addInterceptorRule(mClassName, value)
                            }
                        }
                    }
                }
            }
        } else {
            return annotationVisitor
        }
    }

    @Override
    FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        FieldVisitor fieldVisitor = super.visitField(access, name, descriptor, signature, value)
        def fieldItem = new InjectFieldItem()
        fieldItem.mFieldName = name
        fieldItem.mFieldType = descriptor
        return new FieldVisitor(ASM7, fieldVisitor) {
            @Override
            AnnotationVisitor visitAnnotation(String descriptor0, boolean visible) {
                if (descriptor0 == Constant.ANNOTATION_DESCRIPTOR_ROUTER_PARAM) {
                    mFieldList.add(fieldItem)
                    return new AnnotationVisitor(ASM7) {
                        @Override
                        void visit(String name0, Object value0) {
                            super.visit(name0, value0)
                            if (!TextUtils.isEmpty(value0)) {
                                fieldItem.mFieldKey = value0
                            }
                        }
                    }
                } else {
                    return super.visitAnnotation(descriptor0, visible)
                }
            }
        }
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        final MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (name == "onCreate" && descriptor == "(Landroid/os/Bundle;)V") {
            mHaveOverrideOnCreateMethod = true
            return new AdviceAdapter(ASM7, methodVisitor, access, name, descriptor) {

                @Override
                protected void onMethodEnter() {
                    super.onMethodEnter()
                    injectIntentParams(methodVisitor)
                }
            }
        }
        return methodVisitor
    }

    @Override
    void visitEnd() {

        if (!mHaveOverrideOnCreateMethod && (mFieldList.size() > 0)) {
            MethodVisitor methodVisitor = cv.visitMethod(ACC_PROTECTED, "onCreate", "(Landroid/os/Bundle;)V", null, null)
            injectIntentParams(methodVisitor)
            methodVisitor.visitVarInsn(ALOAD, 0)
            methodVisitor.visitVarInsn(ALOAD, 1)
            methodVisitor.visitMethodInsn(INVOKESPECIAL, mSuperName, "onCreate", "(Landroid/os/Bundle;)V", false)
            methodVisitor.visitInsn(RETURN)
            methodVisitor.visitEnd()
        }
        super.visitEnd()
    }

    private List<InjectFieldItem> injectIntentParams(methodVisitor) {
        mFieldList.each { item ->
            if (!item.mFieldType.contains("java")) {
                methodVisitor.visitVarInsn(ALOAD, 0)
                methodVisitor.visitVarInsn(ALOAD, 0)
                methodVisitor.visitLdcInsn(item.getFieldKey())
                methodVisitor.visitVarInsn(ALOAD, 0)
                methodVisitor.visitFieldInsn(GETFIELD, mClassName, item.mFieldName, item.mFieldType)
                println("fieldVisitor name=" + item.mFieldName + " type=" + item.mFieldType)
                switch (item.mFieldType) {
                    case "I":
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false)
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/leo/lib_router/data/IntentDataProcess", "convertDataOut", "(Landroid/app/Activity;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", false)
                        methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Integer")
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false)
                        methodVisitor.visitFieldInsn(PUTFIELD, mClassName, item.mFieldName, "I")
                        break
                    case "Z":
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false)
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/leo/lib_router/data/IntentDataProcess", "convertDataOut", "(Landroid/app/Activity;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", false)
                        methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Boolean")
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false)
                        methodVisitor.visitFieldInsn(PUTFIELD, mClassName, item.mFieldName, "Z")
                        break
                    case "J":
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false)
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/leo/lib_router/data/IntentDataProcess", "convertDataOut", "(Landroid/app/Activity;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", false);
                        methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Long");
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false)
                        methodVisitor.visitFieldInsn(PUTFIELD, mClassName, item.mFieldName, "J")
                        break
                    case "F":
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false)
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/leo/lib_router/data/IntentDataProcess", "convertDataOut", "(Landroid/app/Activity;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", false);
                        methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Float")
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false)
                        methodVisitor.visitFieldInsn(PUTFIELD, mClassName, item.mFieldName, "F")
                        break
                    case "D":
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/leo/lib_router/data/IntentDataProcess", "convertDataOut", "(Landroid/app/Activity;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", false);
                        methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Double");
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false)
                        methodVisitor.visitFieldInsn(PUTFIELD, mClassName, item.mFieldName, "D")
                        break
                    case "B":
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/leo/lib_router/data/IntentDataProcess", "convertDataOut", "(Landroid/app/Activity;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", false);
                        methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Byte");
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B", false)
                        methodVisitor.visitFieldInsn(PUTFIELD, mClassName, item.mFieldName, "B")
                        break
                    case "C":
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/leo/lib_router/data/IntentDataProcess", "convertDataOut", "(Landroid/app/Activity;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", false);
                        methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Character");
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false)
                        methodVisitor.visitFieldInsn(PUTFIELD, mClassName, item.mFieldName, "C")
                        break
                    case "S":
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/leo/lib_router/data/IntentDataProcess", "convertDataOut", "(Landroid/app/Activity;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", false);
                        methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Short");
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false)
                        methodVisitor.visitFieldInsn(PUTFIELD, mClassName, item.mFieldName, "S")
                        break
                }
            } else {
                def castType = item.mFieldType.substring(1, item.mFieldType.length() - 1)
                methodVisitor.visitVarInsn(ALOAD, 0)
                methodVisitor.visitVarInsn(ALOAD, 0)
                methodVisitor.visitLdcInsn(item.getFieldKey())
                methodVisitor.visitVarInsn(ALOAD, 0)
                methodVisitor.visitFieldInsn(GETFIELD, mClassName, item.mFieldName, item.mFieldType)
                methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/leo/lib_router/data/IntentDataProcess", "convertDataOut", "(Landroid/app/Activity;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", false)
                methodVisitor.visitTypeInsn(CHECKCAST, castType)
                methodVisitor.visitFieldInsn(PUTFIELD, mClassName, item.mFieldName, item.mFieldType)

            }
        }
    }

    class InjectFieldItem {
        public String mFieldName
        public String mFieldKey
        public String mFieldType

        String getFieldKey() {
            return (mFieldKey == null) ? mFieldName : mFieldKey
        }
    }

}