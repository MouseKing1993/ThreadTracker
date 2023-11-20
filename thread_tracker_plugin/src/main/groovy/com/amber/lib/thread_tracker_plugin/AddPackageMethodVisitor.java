package com.amber.lib.thread_tracker_plugin;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddPackageMethodVisitor extends MethodVisitor {

    AddPackageMethodVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == Opcodes.ARETURN || opcode == Opcodes.RETURN) {
            for (String path : PluginUtils.getClassList()) {
                mv.visitFieldInsn(Opcodes.GETSTATIC, "com/amber/lib/thread_tracker/UserPackage", "packageList", "Ljava/util/ArrayList;");
                mv.visitLdcInsn(path);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false);
            }
        }
        super.visitInsn(opcode);
    }
}
