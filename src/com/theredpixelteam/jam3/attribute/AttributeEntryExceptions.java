package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class AttributeEntryExceptions extends AttributeEntry {
    public AttributeEntryExceptions(@Nonnull AttributePool owner,
                                    @Nonnegative int index,
                                    @Nonnull ConstantPool constantPool,
                                    @Nonnull ConstantPool.TagRef nameTagRef,
                                    @Nonnull ConstantPool.TagRef[] exceptionTable)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setExceptionTable(exceptionTable);
    }

    public static @Nonnull AttributeEntryExceptions from(@Nonnull AttributePool attributePool,
                                                         @Nonnull ConstantPool constantPool,
                                                         @Nonnegative int length,
                                                         @Nonnull ConstantPool.TagRef nameTagRef,
                                                         @Nonnull ByteBuffer byteBuffer)
    {
        int exceptionTableSize = byteBuffer.getShort() & 0xFFFF;

        checkLen(length, 2 + (exceptionTableSize << 1));

        ConstantPool.TagRef[] exceptionTable = new ConstantPool.TagRef[exceptionTableSize];
        for (int i = 0; i < exceptionTableSize; i++)
            exceptionTable[i] = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        return attributePool.addEntry(
                new AttributeEntryExceptions(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        exceptionTable));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2 + (exceptionTable.length << 1)];

        BigEndian.pushU2(byts, exceptionTable.length, 0);

        for (int i = 0; i < exceptionTable.length; i++)
            BigEndian.pushU2(byts, exceptionTable[i].getRefIndex(), 2 + (i << 1));

        return byts;
    }

    public @Nonnull ConstantPool.TagRef[] getExceptionTable()
    {
        return exceptionTable;
    }

    public void setExceptionTable(@Nonnull ConstantPool.TagRef[] exceptionTable)
    {
        for(ConstantPool.TagRef exceptionInfo : exceptionTable)
            checkRef(exceptionInfo);

        this.exceptionTable = exceptionTable;
    }

    private ConstantPool.TagRef[] exceptionTable;

    public static final String NAME = "Exceptions";
}
