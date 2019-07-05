package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ElementInfoArray extends ElementInfo {
    public ElementInfoArray(@Nonnull ConstantPool constantPool,
                            @Nonnull ConstantPool.TagRef nameTagRef,
                            @Nonnull ElementInfo[] elementInfos)
    {
        super(constantPool, nameTagRef, Type.ARRAY);

        setElementInfos(elementInfos);
    }

    public static @Nonnull ElementInfoArray from(@Nonnull ConstantPool constantPool,
                                                 @Nonnull ConstantPool.TagRef nameTagRef,
                                                 @Nonnull Type type,
                                                 @Nonnull ByteBuffer byteBuffer)
    {
        int arrayLength = byteBuffer.getShort() & 0xFFFF;

        ElementInfo[] elementInfos = new ElementInfo[arrayLength];
        for (int i = 0; i < arrayLength; i++)
            elementInfos[i] = ElementInfo.from(constantPool, byteBuffer);

        return new ElementInfoArray(constantPool, nameTagRef, elementInfos);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            BigEndian.pushU2(baos, elementInfos.length);

            for (int i = 0; i < elementInfos.length; i++)
                baos.write(elementInfos[i].toBytes());
        } catch (IOException e) {
            // unused
            throw new Error(e);
        }

        return baos.toByteArray();
    }

    public @Nonnull ElementInfo[] getElementInfos()
    {
        return elementInfos;
    }

    public void setElementInfos(@Nonnull ElementInfo[] elementInfos)
    {
        this.elementInfos = Objects.requireNonNull(elementInfos);
    }

    private ElementInfo[] elementInfos;
}
