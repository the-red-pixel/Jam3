package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.attribute.annotation.AnnotationInfo;
import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public abstract class AttributeEntryParameterAnnotationsBase extends AttributeEntry {
    public AttributeEntryParameterAnnotationsBase(@Nonnull String name,
                                                  @Nonnull AttributePool owner,
                                                  @Nonnegative int index,
                                                  @Nonnull ConstantPool constantPool,
                                                  @Nonnull ConstantPool.TagRef nameTagRef,
                                                  @Nonnull AnnotationInfo[][] parameterAnnotations)
    {
        super(name, owner, index, constantPool, nameTagRef);

        setParameterAnnotations(parameterAnnotations);
    }

    static <T extends AttributeEntryParameterAnnotationsBase> T from(@Nonnull AttributePool attributePool,
                                                                     @Nonnull ConstantPool constantPool,
                                                                     @Nonnegative int length,
                                                                     @Nonnull ConstantPool.TagRef nameTagRef,
                                                                     @Nonnull ByteBuffer byteBuffer,
                                                                     @Nonnull Constructor<T> constructor)
    {
        int parameterCount = byteBuffer.get() & 0xFF;

        AnnotationInfo[][] parameterAnnotations = new AnnotationInfo[parameterCount][];
        for (int i = 0; i < parameterCount; i++)
        {
            int annotationCount = byteBuffer.getShort() & 0xFFFF;

            parameterAnnotations[i] = new AnnotationInfo[annotationCount];
            for (int j = 0; j < annotationCount; j++)
                parameterAnnotations[i][j] = AnnotationInfo.from(constantPool, byteBuffer);
        }

        return attributePool.addEntry(
                constructor.construct(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef, parameterAnnotations));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            BigEndian.pushU1(baos, parameterAnnotations.length);

            for (int i = 0; i < parameterAnnotations.length; i++)
            {
                BigEndian.pushU2(baos, parameterAnnotations[i].length);

                for (int j = 0; j < parameterAnnotations[i].length; j++)
                    baos.write(parameterAnnotations[i][j].toBytes());
            }
        } catch (IOException e) {
            // unused
            throw new Error(e);
        }

        return baos.toByteArray();
    }

    public @Nonnull AnnotationInfo[][] getParameterAnnotations()
    {
        return parameterAnnotations;
    }

    public void setParameterAnnotations(@Nonnull AnnotationInfo[][] parameterAnnotations)
    {
        this.parameterAnnotations = Objects.requireNonNull(parameterAnnotations);
    }

    private AnnotationInfo[][] parameterAnnotations;

    static interface Constructor<T extends AttributeEntryParameterAnnotationsBase>
    {
        T construct(@Nonnull AttributePool owner,
                    @Nonnegative int index,
                    @Nonnull ConstantPool constantPool,
                    @Nonnull ConstantPool.TagRef nameTagRef,
                    @Nonnull AnnotationInfo[][] parameterAnnotations);
    }
}
