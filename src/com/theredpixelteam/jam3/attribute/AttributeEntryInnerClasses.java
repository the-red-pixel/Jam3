package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.constant.ConstantTagClass;
import com.theredpixelteam.jam3.constant.ConstantTagUTF8;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public class AttributeEntryInnerClasses extends AttributeEntry {
    public AttributeEntryInnerClasses(@Nonnull AttributePool owner,
                                      @Nonnegative int index,
                                      @Nonnull ConstantPool constantPool,
                                      @Nonnull ConstantPool.TagRef nameTagRef,
                                      @Nonnull ClassInfo[] classTable)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setClassTable(classTable);
    }

    public static @Nonnull AttributeEntryInnerClasses from(@Nonnull AttributePool attributePool,
                                                           @Nonnull ConstantPool constantPool,
                                                           @Nonnegative int length,
                                                           @Nonnull ConstantPool.TagRef nameTagRef,
                                                           @Nonnull ByteBuffer byteBuffer)
    {
        int classTableSize = byteBuffer.getShort() & 0xFFFF;

        checkLen(length, 2 + (classTableSize << 3));

        ClassInfo[] classTable = new ClassInfo[classTableSize];
        for (int i = 0; i < classTableSize; i++)
            classTable[i] = ClassInfo.from(constantPool, byteBuffer);

        return attributePool.addEntry(
                new AttributeEntryInnerClasses(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        classTable));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2 + (classTable.length << 3)];

        BigEndian.pushU2(byts, classTable.length, 0);
        for (int i = 0; i < classTable.length; i++)
            classTable[i].push(byts, 2 + (i << 3));

        return byts;
    }

    public @Nonnull ClassInfo[] getClassTable()
    {
        return classTable;
    }

    public void setClassTable(@Nonnull ClassInfo[] classTable)
    {
        this.classTable = Objects.requireNonNull(classTable);
    }

    private ClassInfo[] classTable;

    public static final String NAME = "InnerClasses";

    public static class ClassInfo
    {
        public ClassInfo(@Nonnull ConstantPool constantPool,
                         @Nonnull ConstantPool.TagRef innerClassInfoTagRef,
                         @Nonnull ConstantPool.TagRef outerClassInfoTagRef,
                         @Nonnull ConstantPool.TagRef innerNameTagRef,
                         int innerClassModifier)
        {
            this.constantPool = Objects.requireNonNull(constantPool);
            setInnerClassInfoTagRef(innerClassInfoTagRef);
            setOuterClassInfoTagRef(outerClassInfoTagRef);
            setInnerNameTagRef(innerNameTagRef);
            this.innerClassModifier = innerClassModifier;
        }

        public static @Nonnull ClassInfo from(@Nonnull ConstantPool constantPool,
                                              @Nonnull ByteBuffer byteBuffer)
        {
            ConstantPool.TagRef innerClassInfoTagRef
                    = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

            ConstantPool.TagRef outerClassInfoTagRef
                    = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

            ConstantPool.TagRef innerNameTagRef
                    = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

            int innerClassModifier = byteBuffer.getShort() & 0xFFFF;

            return new ClassInfo(constantPool, innerClassInfoTagRef, outerClassInfoTagRef, innerNameTagRef, innerClassModifier);
        }

        public void push(byte[] byts, int off)
        {
            BigEndian.pushU2(byts, innerClassInfoTagRef.getRefIndex(), off);
            BigEndian.pushU2(byts, outerClassInfoTagRef.getRefIndex(), off + 2);
            BigEndian.pushU2(byts, innerNameTagRef.getRefIndex(), off + 4);
            BigEndian.pushU2(byts, innerClassModifier, off + 6);
        }

        public @Nonnull ConstantPool.TagRef getInnerClassInfoTagRef()
        {
            return innerClassInfoTagRef;
        }

        public @Nullable ConstantTagClass getInnerClassInfoTag()
        {
            return innerClassInfoTagRef.get(ConstantTag.Type.CLASS, ConstantTagClass.class);
        }

        public void setInnerClassInfoTagRef(@Nonnull ConstantPool.TagRef innerClassInfoTagRef)
        {
            this.innerClassInfoTagRef = checkRef(Objects.requireNonNull(innerClassInfoTagRef));
        }

        public @Nonnull ConstantPool.TagRef getOuterClassInfoTagRef()
        {
            return outerClassInfoTagRef;
        }

        public @Nullable ConstantTagClass getOuterClassInfoTag()
        {
            return outerClassInfoTagRef.get(ConstantTag.Type.CLASS, ConstantTagClass.class);
        }

        public void setOuterClassInfoTagRef(@Nonnull ConstantPool.TagRef outerClassInfoTagRef)
        {
            this.outerClassInfoTagRef = checkRef(Objects.requireNonNull(outerClassInfoTagRef));
        }

        public @Nonnull ConstantPool.TagRef getInnerNameTagRef()
        {
            return innerNameTagRef;
        }

        public @Nullable ConstantTagUTF8 getInnerNameTag()
        {
            return innerNameTagRef.get(ConstantTag.Type.UTF8, ConstantTagUTF8.class);
        }

        public void setInnerNameTagRef(@Nonnull ConstantPool.TagRef innerNameTagRef)
        {
            this.innerNameTagRef = checkRef(Objects.requireNonNull(innerNameTagRef));
        }

        public int getInnerClassModifier()
        {
            return innerClassModifier;
        }

        public void setInnerClassModifier(int innerClassModifier)
        {
            this.innerClassModifier = innerClassModifier;
        }

        public @Nonnull ConstantPool getConstantPool()
        {
            return constantPool;
        }

        ConstantPool.TagRef checkRef(@Nonnull ConstantPool.TagRef tagRef)
        {
            return ConstantTag.checkRef(constantPool, tagRef);
        }

        private final ConstantPool constantPool;

        private ConstantPool.TagRef innerClassInfoTagRef;

        private ConstantPool.TagRef outerClassInfoTagRef;

        private ConstantPool.TagRef innerNameTagRef;

        private int innerClassModifier;
    }
}
