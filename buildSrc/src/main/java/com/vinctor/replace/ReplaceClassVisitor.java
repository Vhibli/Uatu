package com.vinctor.replace;

import com.vinctor.ReplaceConfig;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class ReplaceClassVisitor extends ClassVisitor {
    private ReplaceConfig config;
    private boolean isNeedReplace = true;

    public ReplaceClassVisitor(ClassVisitor cv, ReplaceConfig config) {
        super(Opcodes.ASM5, cv);
        this.config = config;
    }

    public ReplaceClassVisitor(int api) {
        super(api);
    }

    public ReplaceClassVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        List<String> needToExclude = config.getNeedToExclude();
        if (needToExclude != null) {
            for (String item : needToExclude) {
                if (name.equals(item)) {
                    isNeedReplace = false;
                    break;
                }
            }
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (!isNeedReplace) {
            return methodVisitor;
        }
        return new ReplaceMethodVisitor(Opcodes.ASM5, methodVisitor, access, name, desc, config);
    }
}
