package com.theredpixelteam.jam3.constant;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public abstract class ConstantTag {
    ConstantTag(@Nonnull Type tagType,
                @Nonnull ConstantPool pool,
                @Nonnegative int index)
    {
        this.tagType = Objects.requireNonNull(tagType, "tagType");
        this.owner = Objects.requireNonNull(pool, "owner");
        this.index = index;
    }

    public @Nonnull Type getTagType()
    {
        return tagType;
    }

    public @Nonnegative int getIndex()
    {
        return index;
    }

    public @Nonnull ConstantPool getOwner()
    {
        return owner;
    }

    public abstract @Nonnull byte[] toBytes();

    public static ConstantPool.TagRef checkRef(@Nonnull ConstantPool owner,
                                               @Nonnull ConstantPool.TagRef tagRef)
    {
        if (tagRef.getOwner() != owner)
            throw new IllegalArgumentException("Cross pool tag ref");

        return tagRef;
    }

    ConstantPool.TagRef checkRef(@Nonnull ConstantPool.TagRef tagRef)
    {
        return checkRef(owner, tagRef);
    }

    public static ConstantTag from(@Nonnull ConstantPool constantPool,
                                   @Nonnull ByteBuffer byteBuffer)
    {
        int tag = byteBuffer.get() & 0xFF;

        Type type = Type.getTagType(tag);

        if (type == null)
            throw new ClassFormatError("Unknown tag type: " + tag);

        return type.getInterpreter().from(constantPool, byteBuffer);
    }

    private final Type tagType;

    private final int index;

    private final ConstantPool owner;

    public static enum Type
    {
        UTF8                    (1,     ConstantTagUTF8::from,              ConstantTagUTF8::skip),
        INTEGER                 (3,     ConstantTagInteger::from,           ConstantTagInteger::skip),
        FLOAT                   (4,     ConstantTagFloat::from,             ConstantTagFloat::skip),
        LONG                    (5,     ConstantTagLong::from,              ConstantTagLong::skip),
        DOUBLE                  (6,     ConstantTagDouble::from,            ConstantTagDouble::skip),
        CLASS                   (7,     ConstantTagClass::from,             ConstantTagClass::skip),
        STRING                  (8,     ConstantTagString::from,            ConstantTagString::skip),
        FIELD_REF               (9,     ConstantTagFieldRef::from,          ConstantTagFieldRef::skip),
        METHOD_REF              (10,    ConstantTagMethodRef::from,         ConstantTagMethodRef::skip),
        INTERFACE_METHOD_REF    (11,    ConstantTagInterfaceMethodRef::from,ConstantTagInterfaceMethodRef::skip),
        NAME_N_TYPE             (12,    ConstantTagNameNType::from,         ConstantTagNameNType::skip),
        METHOD_HANDLE           (15,    ConstantTagMethodHandle::from,      ConstantTagMethodHandle::skip),
        METHOD_TYPE             (16,    ConstantTagMethodType::from,        ConstantTagMethodType::skip),
        INVOKEDYNAMIC           (18,    ConstantTagInvokeDynamic::from,     ConstantTagInvokeDynamic::skip);

        Type(int tag,
             TagInterpreter interpreter,
             Blank blank)
        {
            this.tag = tag;
            this.interpreter = interpreter;
            this.blank = blank;
        }

        public int getTag()
        {
            return tag;
        }

        public static @Nullable Type getTagType(int tag)
        {
            if (tag < 0 || tag > 18)
                return null;

            return TYPES[tag];
        }

        private static void reg(Type tagType)
        {
            if (TYPES == null)
                TYPES = new Type[19];

            TYPES[tagType.tag] = tagType;
        }

        public @Nonnull TagInterpreter getInterpreter()
        {
            return interpreter;
        }

        public @Nonnull Blank getBlank()
        {
            return blank;
        }

        private final int tag;

        private final TagInterpreter interpreter;

        private final Blank blank;

        private static Type[] TYPES;

        public static interface TagInterpreter
        {
            public ConstantTag from(@Nonnull ConstantPool constantPool,
                                    @Nonnull ByteBuffer byteBuffer);
        }

        public static interface Blank
        {
            public void skip(@Nonnull ByteBuffer byteBuffer);
        }
    }
}
