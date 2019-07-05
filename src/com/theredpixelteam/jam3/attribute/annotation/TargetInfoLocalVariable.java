package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

public class TargetInfoLocalVariable extends TargetInfo {
    public TargetInfoLocalVariable(@Nonnull Type type,
                                   @Nonnull LocalVariableInfo[] localVariableTable)
    {
        super(type);

        setLocalVariableTable(localVariableTable);
    }

    public static @Nonnull TargetInfoLocalVariable from(@Nonnull Type type,
                                                        @Nonnull ByteBuffer byteBuffer)
    {
        int localVariableTableLength = byteBuffer.getShort() & 0xFFFF;

        LocalVariableInfo[] localVariableTable = new LocalVariableInfo[localVariableTableLength];
        for (int i = 0; i < localVariableTableLength; i++)
            localVariableTable[i] = LocalVariableInfo.from(byteBuffer);

        return new TargetInfoLocalVariable(type, localVariableTable);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2 + localVariableTable.length * 6];

        BigEndian.pushU2(byts, localVariableTable.length, 0);

        for (int i = 0; i < localVariableTable.length; i++)
            localVariableTable[i].push(byts, 2 + i * 6);

        return byts;
    }

    public @Nonnull LocalVariableInfo[] getLocalVariableTable()
    {
        return localVariableTable;
    }

    public void setLocalVariableTable(@Nonnull LocalVariableInfo[] localVariableTable)
    {
        this.localVariableTable = Objects.requireNonNull(localVariableTable);
    }

    private LocalVariableInfo[] localVariableTable;

    public static class LocalVariableInfo
    {
        public LocalVariableInfo(@Nonnegative int startPC,
                                 @Nonnegative int length,
                                 @Nonnegative int index)
        {
            setStartPC(startPC);
            setLength(length);
            setIndex(index);
        }

        public static @Nonnull LocalVariableInfo from(@Nonnull ByteBuffer byteBuffer)
        {
            int startPC = byteBuffer.getShort() & 0xFFFF;
            int length = byteBuffer.getShort() & 0xFFFF;
            int index = byteBuffer.getShort() & 0xFFFF;

            return new LocalVariableInfo(startPC, length, index);
        }

        public void push(byte[] byts, int off)
        {
            BigEndian.pushU2(byts, startPC, off);
            BigEndian.pushU2(byts, length, off + 2);
            BigEndian.pushU2(byts, index, off + 4);
        }

        public @Nonnegative int getStartPC()
        {
            return startPC;
        }

        public void setStartPC(@Nonnegative int startPC)
        {
            this.startPC = startPC;
        }

        public @Nonnegative int getLength()
        {
            return length;
        }

        public void setLength(@Nonnegative int length)
        {
            this.length = length;
        }

        public @Nonnegative int getIndex()
        {
            return index;
        }

        public void setIndex(@Nonnegative int index)
        {
            this.index = index;
        }

        int startPC;

        int length;

        int index;
    }
}
