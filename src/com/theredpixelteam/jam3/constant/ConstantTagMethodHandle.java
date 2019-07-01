package com.theredpixelteam.jam3.constant;

import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ConstantTagMethodHandle extends ConstantTag {
    ConstantTagMethodHandle(@Nonnull ConstantPool pool,
                            @Nonnegative int index)
    {
        super(Type.METHOD_HANDLE, pool, index);
    }

    public ConstantTagMethodHandle(@Nonnull ConstantPool pool,
                                   @Nonnegative int index,
                                   @Nonnull RefType refType,
                                   @Nonnull ConstantPool.TagRef tagRef)
    {
        this(pool, index);

        setRefType(refType);
        setTagRef(tagRef);
    }

    public static @Nonnull ConstantTagMethodHandle from(@Nonnull ConstantPool pool,
                                                        @Nonnull ByteBuffer byteBuffer)
    {
        int refTypeCode = byteBuffer.get() & 0xFF;
        int tagRef = byteBuffer.getShort() & 0xFFFF;

        RefType refType = RefType.getRefType(refTypeCode);

        if (refType == null)
            throw new ClassFormatError("Unknown method handle ref type: " + refTypeCode);

        return pool.addTag(
                new ConstantTagMethodHandle(pool, pool.nextIndex(),
                        refType,
                        pool.new TagRef(tagRef)));
    }

    public static void skip(@Nonnull ByteBuffer byteBuffer)
    {
        byteBuffer.position(byteBuffer.position() + 3);
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[3];

        BigEndian.pushU1(byts, refType.getCode(), 0);
        BigEndian.pushU2(byts, tagRef.getRefIndex(), 1);

        return byts;
    }

    public @Nonnull RefType getRefType()
    {
        return refType;
    }

    public void setRefType(@Nonnull RefType refType)
    {
        this.refType = Objects.requireNonNull(refType);
    }

    public @Nonnull ConstantPool.TagRef getTagRef()
    {
        return tagRef;
    }

    public @Nullable ConstantTagAccessibleObjectRef getTag()
    {
        refType.checkType(tagRef);

        return tagRef.get(null, ConstantTagAccessibleObjectRef.class);
    }

    public void setTagRef(@Nonnull ConstantPool.TagRef tagRef)
    {
        this.tagRef = checkRef(Objects.requireNonNull(tagRef));
    }

    private RefType refType;

    private ConstantPool.TagRef tagRef;

    public static enum RefType
    {
        GETFIELD(1, Type.FIELD_REF),
        GETSTATIC(2, Type.FIELD_REF),
        PUTFIELD(3, Type.FIELD_REF),
        PUTSTATIC(4, Type.FIELD_REF),
        INVOKEVIRTUAL(5, Type.METHOD_REF),
        INVOKESTATIC(6, Type.METHOD_REF),
        INVOKESPECIAL(7, Type.METHOD_REF),
        NEW_INVOKESPECIAL(8, Type.METHOD_REF),
        INVOKEINTERFACE(9, Type.METHOD_REF);

        RefType(int code, ConstantTag.Type expectingTagType)
        {
            this.code = code;
            this.expectingTagType = expectingTagType;
        }

        public static @Nullable RefType getRefType(int code)
        {
            switch(code)
            {
                case 1:     return GETFIELD;
                case 2:     return GETSTATIC;
                case 3:     return PUTFIELD;
                case 4:     return PUTSTATIC;
                case 5:     return INVOKEVIRTUAL;
                case 6:     return INVOKESTATIC;
                case 7:     return INVOKESPECIAL;
                case 8:     return NEW_INVOKESPECIAL;
                case 9:     return INVOKEINTERFACE;
                default:    return null;
            }
        }

        public void checkType(@Nonnull ConstantPool.TagRef tagRef)
        {
            ConstantTag tag;
            if ((tag = tagRef.get()) == null)
                return;

            if (!expectingTagType.equals(tag.getTagType()))
                throw new ClassFormatError("Found tag " + tag.getTagType()
                        + " under METHOD_HANDLE(" + this + "), " + expectingTagType + " expected");
        }

        public @Nonnull Type getExpectingTagType()
        {
            return expectingTagType;
        }

        public int getCode()
        {
            return code;
        }

        private final ConstantTag.Type expectingTagType;

        private final int code;
    }
}
