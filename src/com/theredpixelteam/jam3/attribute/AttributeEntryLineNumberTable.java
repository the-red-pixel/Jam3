package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

@SuppressWarnings("all")
public class AttributeEntryLineNumberTable extends AttributeEntry {
    public AttributeEntryLineNumberTable(@Nonnull AttributePool owner,
                                         @Nonnegative int index,
                                         @Nonnull ConstantPool constantPool,
                                         @Nonnull ConstantPool.TagRef nameTagRef,
                                         @Nonnull LineNumberInfo[] lineNumberTable)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setLineNumberTable(lineNumberTable);
    }

    public static @Nonnull AttributeEntryLineNumberTable from(@Nonnull AttributePool attributePool,
                                                              @Nonnull ConstantPool constantPool,
                                                              @Nonnegative int length,
                                                              @Nonnull ConstantPool.TagRef nameTagRef,
                                                              @Nonnull ByteBuffer byteBuffer)
    {
        int lineNumberTableSize = byteBuffer.getShort() & 0xFFFF;

        checkLen(length, 2 + (lineNumberTableSize << 2));

        LineNumberInfo[] lineNumberTable = new LineNumberInfo[lineNumberTableSize];

        for (int i = 0; i < lineNumberTableSize; i++)
            lineNumberTable[i] = LineNumberInfo.from(byteBuffer);

        return attributePool.addEntry(
                new AttributeEntryLineNumberTable(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        lineNumberTable));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2 + (lineNumberTable.length << 2)];

        BigEndian.pushU2(byts, lineNumberTable.length, 0);

        for (int i = 0; i < lineNumberTable.length; i++)
            lineNumberTable[i].push(byts, 2 + (i << 2));

        return byts;
    }

    public @Nonnull LineNumberInfo[] getLineNumberTable()
    {
        return lineNumberTable;
    }

    public void setLineNumberTable(@Nonnull LineNumberInfo[] lineNumberTable)
    {
        this.lineNumberTable = Objects.requireNonNull(lineNumberTable);
    }

    private LineNumberInfo[] lineNumberTable;

    public static final String NAME = "LineNumberTable";

    public static class LineNumberInfo
    {
        public LineNumberInfo(@Nonnegative int startPC,
                              @Nonnegative int lineNumber)
        {
            setStartPC(startPC);
            setLineNumber(lineNumber);
        }

        public static @Nonnull LineNumberInfo from(@Nonnull ByteBuffer byteBuffer)
        {
            int startPC = byteBuffer.getShort() & 0xFFFF;
            int lineNumber = byteBuffer.getShort() & 0xFFFF;

            return new LineNumberInfo(startPC, lineNumber);
        }

        public void push(byte[] byts, int off)
        {
            BigEndian.pushU2(byts, startPC, off);
            BigEndian.pushU2(byts, lineNumber, off + 2);
        }

        public @Nonnegative int getStartPC()
        {
            return startPC;
        }

        public void setStartPC(@Nonnegative int startPC)
        {
            this.startPC = startPC;
        }

        public @Nonnegative int getLineNumber()
        {
            return lineNumber;
        }

        public void setLineNumber(@Nonnegative int lineNumber)
        {
            this.lineNumber = lineNumber;
        }

        private int startPC;

        private int lineNumber;
    }
}
