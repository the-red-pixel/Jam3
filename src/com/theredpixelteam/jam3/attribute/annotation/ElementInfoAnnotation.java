package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.constant.ConstantPool;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ElementInfoAnnotation extends ElementInfo {
    public ElementInfoAnnotation(@Nonnull ConstantPool constantPool,
                                 @Nonnull ConstantPool.TagRef nameTagRef,
                                 @Nonnull AnnotationInfo annotationInfo)
    {
        super(constantPool, nameTagRef, Type.ANNOTATION);

        setAnnotationInfo(annotationInfo);
    }

    public static @Nonnull ElementInfoAnnotation from(@Nonnull ConstantPool constantPool,
                                                      @Nonnull ConstantPool.TagRef nameTagRef,
                                                      @Nonnull Type type,
                                                      @Nonnull ByteBuffer byteBuffer)
    {
        AnnotationInfo annotationInfo = AnnotationInfo.from(constantPool, byteBuffer);

        return new ElementInfoAnnotation(constantPool, nameTagRef, annotationInfo);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        return annotationInfo.toBytes();
    }

    public @Nonnull AnnotationInfo getAnnotationInfo()
    {
        return annotationInfo;
    }

    public void setAnnotationInfo(@Nonnull AnnotationInfo annotationInfo)
    {
        this.annotationInfo = Objects.requireNonNull(annotationInfo);
    }

    private AnnotationInfo annotationInfo;
}
