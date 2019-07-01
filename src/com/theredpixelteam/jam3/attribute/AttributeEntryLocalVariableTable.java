package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

@SuppressWarnings("all")
public class AttributeEntryLocalVariableTable extends AttributeEntry {
    public AttributeEntryLocalVariableTable(@Nonnull AttributePool owner,
                                            @Nonnegative int index,
                                            @Nonnull ConstantPool constantPool,
                                            @Nonnull ConstantPool.TagRef nameTagRef,
                                            @Nonnull LocalVariableInfo[] localVariableTable)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setLocalVariableTable(localVariableTable);
    }

    public static @Nonnull AttributeEntryLocalVariableTable from(@Nonnull AttributePool pool,
                                                                 @Nonnull ConstantPool constantPool,
                                                                 @Nonnegative int length,
                                                                 @Nonnull ConstantPool.TagRef nameTagRef,
                                                                 @Nonnull ByteBuffer byteBuffer)
    {
        int localVariableTableSize = byteBuffer.getShort() & 0xFFFF;

        checkLen(length, 2 + localVariableTableSize * 10);

        LocalVariableInfo[] localVariableTable = new LocalVariableInfo[localVariableTableSize];
        for (int i = 0; i < localVariableTableSize; i++)
            localVariableTable[i] = LocalVariableInfo.from(constantPool, byteBuffer);

        return pool.addEntry(
                new AttributeEntryLocalVariableTable(pool, pool.nextIndex(), constantPool,
                        nameTagRef,
                        localVariableTable));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2 + localVariableTable.length * 10];

        BigEndian.pushU2(byts, localVariableTable.length, 0);

        for (int i = 0; i < localVariableTable.length; i++)
            localVariableTable[i].push(byts, 2 + i * 10);

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

    public static final String NAME = "LocalVariableTable";

    public static class LocalVariableInfo
    {
        public LocalVariableInfo(@Nonnegative int startPC,
                                 @Nonnegative int length,
                                 @Nonnull ConstantPool constantPool,
                                 @Nonnull ConstantPool.TagRef nameTagRef,
                                 @Nonnull ConstantPool.TagRef descriptorTagRef,
                                 @Nonnegative int index)
        {
            this.constantPool = Objects.requireNonNull(constantPool);

            setStartPC(startPC);
            setLength(length);
            setNameTagRef(nameTagRef);
            setDescriptorTagRef(descriptorTagRef);
            setIndex(index);
        }

        public static @Nonnull LocalVariableInfo from(@Nonnull ConstantPool constantPool,
                                                      @Nonnull ByteBuffer byteBuffer)
        {
            int startPC = byteBuffer.getShort() & 0xFFFF;

            int length = byteBuffer.getShort() & 0xFFFF;

            ConstantPool.TagRef nameTagRef
                    = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

            ConstantPool.TagRef descriptorTagRef
                    = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

            int index = byteBuffer.getShort() & 0xFFFF;

            return new LocalVariableInfo(startPC, length, constantPool, nameTagRef, descriptorTagRef, index);
        }

        public void push(@Nonnull byte[] byts, @Nonnegative int off)
        {
            BigEndian.pushU2(byts, startPC, off);
            BigEndian.pushU2(byts, length, off + 2);
            BigEndian.pushU2(byts, nameTagRef.getRefIndex(), off + 4);
            BigEndian.pushU2(byts, descriptorTagRef.getRefIndex(), off + 6);
            BigEndian.pushU2(byts, index, off + 8);
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

        public @Nonnull ConstantPool.TagRef getNameTagRef()
        {
            return nameTagRef;
        }

        public void setNameTagRef(@Nonnull ConstantPool.TagRef nameTagRef)
        {
            this.nameTagRef = checkRef(Objects.requireNonNull(nameTagRef));
        }

        public @Nonnull ConstantPool.TagRef getDescriptorTagRef()
        {
            return descriptorTagRef;
        }

        public void setDescriptorTagRef(@Nonnull ConstantPool.TagRef descriptorTagRef)
        {
            this.descriptorTagRef = checkRef(Objects.requireNonNull(descriptorTagRef));
        }

        public @Nonnull ConstantPool getConstantPool()
        {
            return constantPool;
        }

        ConstantPool.TagRef checkRef(ConstantPool.TagRef tagRef)
        {
            return ConstantTag.checkRef(constantPool, tagRef);
        }

        public @Nonnegative int getIndex()
        {
            return index;
        }

        public void setIndex(@Nonnegative int index)
        {
            this.index = index;
        }

        private int startPC;

        private int length;

        private ConstantPool.TagRef nameTagRef;

        private ConstantPool.TagRef descriptorTagRef;

        private int index;

        private final ConstantPool constantPool;
    }
}
