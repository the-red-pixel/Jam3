package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public class AnnotationInfo {
    public AnnotationInfo(@Nonnull ConstantPool constantPool,
                          @Nonnull ConstantPool.TagRef typeTagRef,
                          @Nonnull ElementInfo[] elements)
    {
        this.constantPool = constantPool;

        setTypeTagRef(typeTagRef);
        setElements(elements);
    }

    public static @Nonnull AnnotationInfo from(@Nonnull ConstantPool constantPool,
                                               @Nonnull ByteBuffer byteBuffer)
    {
        ConstantPool.TagRef typeTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        int elementCount = byteBuffer.getShort() & 0xFFFF;

        ElementInfo[] elements = new ElementInfo[elementCount];
        for (int i = 0; i < elementCount; i++)
            elements[i] = ElementInfo.from(constantPool, byteBuffer);

        return new AnnotationInfo(constantPool, typeTagRef, elements);
    }

    public @Nonnull byte[] toBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            BigEndian.pushU2(baos, typeTagRef.getRefIndex());

            BigEndian.pushU2(baos, elements.length);

            for (int i = 0; i < elements.length; i++)
                baos.write(elements[i].toBytes());
        } catch (IOException e) {
            // unused
            throw new Error(e);
        }

        return baos.toByteArray();
    }

    public @Nonnull ConstantPool.TagRef getTypeTagRef()
    {
        return typeTagRef;
    }

    public void setTypeTagRef(@Nonnull ConstantPool.TagRef typeTagRef)
    {
        this.typeTagRef = checkRef(Objects.requireNonNull(typeTagRef));
    }

    public @Nonnull ElementInfo[] getElements()
    {
        return elements;
    }

    public void setElements(@Nonnull ElementInfo[] elements)
    {
        this.elements = Objects.requireNonNull(elements);
    }

    public @Nonnull ConstantPool getConstantPool()
    {
        return constantPool;
    }

    ConstantPool.TagRef checkRef(ConstantPool.TagRef tagRef)
    {
        return ConstantTag.checkRef(constantPool, tagRef);
    }

    private final ConstantPool constantPool;

    private ConstantPool.TagRef typeTagRef;

    private ElementInfo[] elements;
}
