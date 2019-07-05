package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public class TypeAnnotationInfo {
    public TypeAnnotationInfo(@Nonnull TargetInfo target,
                              @Nonnull TypePathInfo targetPath,
                              @Nonnull AnnotationInfo annotation)
    {
        setTarget(target);
        setTargetPath(targetPath);
        setAnnotation(annotation);
    }

    public static @Nonnull TypeAnnotationInfo from(@Nonnull ConstantPool constantPool,
                                                   @Nonnull ByteBuffer byteBuffer)
    {
        int targetTypeTag;
        TargetInfo.Type targetType
                = TargetInfo.Type.getType(targetTypeTag = byteBuffer.get() & 0xFF);

        if (targetType == null)
            throw new ClassFormatError("Unknown type annotation target type: " + targetTypeTag);

        TargetInfo target = targetType.getInterpreter().from(targetType, byteBuffer);

        TypePathInfo targetPath = TypePathInfo.from(byteBuffer);

        AnnotationInfo annotation = AnnotationInfo.from(constantPool, byteBuffer);

        return new TypeAnnotationInfo(target, targetPath, annotation);
    }

    public @Nonnull byte[] toBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            BigEndian.pushU1(baos, target.getType().getTag());
            baos.write(target.toBytes());

            baos.write(targetPath.toBytes());

            baos.write(annotation.toBytes());
        } catch (IOException e) {
            // unused
            throw new Error(e);
        }

        return baos.toByteArray();
    }

    public @Nonnull TargetInfo getTarget()
    {
        return target;
    }

    public void setTarget(@Nonnull TargetInfo target)
    {
        this.target = Objects.requireNonNull(target);
    }

    public @Nonnull TypePathInfo getTargetPath()
    {
        return targetPath;
    }

    public void setTargetPath(@Nonnull TypePathInfo targetPath)
    {
        this.targetPath = Objects.requireNonNull(targetPath);
    }

    public @Nonnull AnnotationInfo getAnnotation()
    {
        return annotation;
    }

    public void setAnnotation(@Nonnull AnnotationInfo annotation)
    {
        this.annotation = Objects.requireNonNull(annotation);
    }

    private TargetInfo target;

    private TypePathInfo targetPath;

    private AnnotationInfo annotation;
}
