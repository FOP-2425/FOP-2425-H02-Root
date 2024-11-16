package h02;

import org.objectweb.asm.*;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

import static org.objectweb.asm.Opcodes.*;

public class SubmissionTransformer implements ClassTransformer {

    @Override
    public String getName() {
        return "MethodInjectingTransformer";
    }

    @Override
    public void transform(ClassReader reader, ClassWriter writer) {
        if (reader.getClassName().equals("h02/FourWins")) {
            reader.accept(new FourWinsClassVisitor(writer), 0);
        } else {
            reader.accept(writer, 0);
        }
    }

    private static class FourWinsClassVisitor extends ClassVisitor {

        private FourWinsClassVisitor(ClassWriter classWriter) {
            super(Opcodes.ASM9, classWriter);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            return new MethodVisitor(api, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                @Override
                public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                    if (owner.equals("h02/template/InputHandler") && name.equals("install") && descriptor.equals("()V")) {
                        super.visitInsn(POP);
                    } else {
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    }
                }
            };
        }

        @Override
        public void visitEnd() {
            Label startLabel = new Label();
            Label endLabel = new Label();
            MethodVisitor mv = super.visitMethod(ACC_PUBLIC,
                "getInputHandler",
                "()Lh02/template/InputHandler;",
                null,
                null);
            mv.visitLabel(startLabel);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, "h02/FourWins", "inputHandler", "Lh02/template/InputHandler;");
            mv.visitInsn(ARETURN);
            mv.visitLabel(endLabel);
            mv.visitLocalVariable("this", "Lh02/FourWins;", null, startLabel, endLabel, 0);
            mv.visitMaxs(1, 1);

            super.visitEnd();
        }
    }
}
