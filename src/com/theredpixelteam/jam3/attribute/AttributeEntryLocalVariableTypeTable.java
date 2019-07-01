package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

@SuppressWarnings("all")
public class AttributeEntryLocalVariableTypeTable extends AttributeEntry {
    public AttributeEntryLocalVariableTypeTable(@Nonnull AttributePool owner,
                                                @Nonnegative int index,
                                                @Nonnull ConstantPool constantPool,
                                                @Nonnull ConstantPool.TagRef nameTagRef,
                                                @Nonnull LocalVariableTypeInfo[] localVariableTypeTable)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setLocalVariableTypeTable(localVariableTypeTable);
    }

    public static @Nonnull AttributeEntryLocalVariableTypeTable from(@Nonnull AttributePool attributePool,
                                                                     @Nonnull ConstantPool constantPool,
                                                                     @Nonnegative int length,
                                                                     @Nonnull ConstantPool.TagRef nameTagRef,
                                                                     @Nonnull ByteBuffer byteBuffer)
    {
        int localVariableTypeTableSize = byteBuffer.getShort() & 0xFFFF;

        checkLen(length, 2 + localVariableTypeTableSize * 10);

        LocalVariableTypeInfo[] localVariableTypeTable = new LocalVariableTypeInfo[localVariableTypeTableSize];
        for (int i = 0; i < localVariableTypeTableSize; i++)
            localVariableTypeTable[i] = LocalVariableTypeInfo.from(constantPool, byteBuffer);

        return attributePool.addEntry(
                new AttributeEntryLocalVariableTypeTable(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        localVariableTypeTable));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2 + localVariableTypeTable.length * 10];

        BigEndian.pushU2(byts, localVariableTypeTable.length, 0);

        for (int i = 0; i < localVariableTypeTable.length; i++)
            localVariableTypeTable[i].push(byts, 2 + i * 10);

        return byts;
    }

    public @Nonnull LocalVariableTypeInfo[] getLocalVariableTypeTable()
    {
        return localVariableTypeTable;
    }

    public void setLocalVariableTypeTable(@Nonnull LocalVariableTypeInfo[] localVariableTypeTable)
    {
        this.localVariableTypeTable = Objects.requireNonNull(localVariableTypeTable);
    }

    private LocalVariableTypeInfo[] localVariableTypeTable;

    public static final String NAME = "LocalVariableTypeTable";

    public static class LocalVariableTypeInfo
    {
        public LocalVariableTypeInfo(@Nonnegative int startPC,
                                     @Nonnegative int length,
                                     @Nonnull ConstantPool constantPool,
                                     @Nonnull ConstantPool.TagRef nameTagRef,
                                     @Nonnull ConstantPool.TagRef signatureTagRef,
                                     @Nonnegative int index)
        {
            this.constantPool = Objects.requireNonNull(constantPool);

            setStartPC(startPC);
            setLength(length);
            setNameTagRef(nameTagRef);
            setSignatureTagRef(signatureTagRef);
            setIndex(index);
        }

        public static @Nonnull LocalVariableTypeInfo from(@Nonnull ConstantPool constantPool,
                                                          @Nonnull ByteBuffer byteBuffer)
        {
            int startPC = byteBuffer.getShort() & 0xFFFF;

            int length = byteBuffer.getShort() & 0xFFFF;

            ConstantPool.TagRef nameTagRef
                    = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

            ConstantPool.TagRef signatureTagRef
                    = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

            int index = byteBuffer.getShort() & 0xFFFF;

            return new LocalVariableTypeInfo(startPC, length, constantPool, nameTagRef, signatureTagRef, index);
        }

        public void push(@Nonnull byte[] byts, int off)
        {
            BigEndian.pushU2(byts, startPC, off);
            BigEndian.pushU2(byts, length, off + 2);
            BigEndian.pushU2(byts, nameTagRef.getRefIndex(), off + 4);
            BigEndian.pushU2(byts, signatureTagRef.getRefIndex(), off + 6);
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

        public @Nonnull ConstantPool.TagRef getSignatureTagRef()
        {
            return signatureTagRef;
        }

        public void setSignatureTagRef(@Nonnull ConstantPool.TagRef signatureTagRef)
        {
            this.signatureTagRef = checkRef(Objects.requireNonNull(signatureTagRef));
        }

        public @Nonnegative int getIndex()
        {
            return index;
        }

        public void setIndex(@Nonnegative int index)
        {
            this.index = index;
        }

        public @Nonnull ConstantPool getConstantPool()
        {
            return constantPool;
        }

        ConstantPool.TagRef checkRef(ConstantPool.TagRef tagRef)
        {
            return ConstantTag.checkRef(constantPool, tagRef);
        }

        private int startPC;

        private int length;

        private ConstantPool.TagRef nameTagRef;

        private ConstantPool.TagRef signatureTagRef;

        private int index;

        private final ConstantPool constantPool;
    }
}
