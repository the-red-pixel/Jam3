package com.theredpixelteam.jam3.attribute.annotation;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public abstract class ElementInfo {
    ElementInfo(@Nonnull ConstantPool constantPool,
                @Nonnull ConstantPool.TagRef nameTagRef,
                @Nonnull Type type)
    {
        this.constantPool = Objects.requireNonNull(constantPool, "constantPool");
        this.type = Objects.requireNonNull(type, "type");

        setNameTagRef(nameTagRef);

    }

    public static @Nonnull ElementInfo from(@Nonnull ConstantPool constantPool,
                                            @Nonnull ByteBuffer byteBuffer)
    {
        ConstantPool.TagRef nameTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        int tag = byteBuffer.get() & 0xFF;

        Type type = Type.getType(tag);

        if (type == null)
            throw new ClassFormatError("Unknown annotation element tag: " + tag);

        return type.getInterpreter().from(constantPool, nameTagRef, type, byteBuffer);
    }

    public abstract @Nonnull byte[] toBytes();

    public @Nonnull Type getType()
    {
        return type;
    }

    public @Nonnull ConstantPool.TagRef getNameTagRef()
    {
        return nameTagRef;
    }

    public void setNameTagRef(@Nonnull ConstantPool.TagRef nameTagRef)
    {
        this.nameTagRef = checkRef(Objects.requireNonNull(nameTagRef));
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

    private ConstantPool.TagRef nameTagRef;

    private final Type type;

    public static enum Type
    {
        BYTE        ('B', ElementInfoConstantValue::from),
        CHAR        ('C', ElementInfoConstantValue::from),
        DOUBLE      ('D', ElementInfoConstantValue::from),
        FLOAT       ('F', ElementInfoConstantValue::from),
        INT         ('I', ElementInfoConstantValue::from),
        LONG        ('J', ElementInfoConstantValue::from),
        SHORT       ('S', ElementInfoConstantValue::from),
        BOOLEAN     ('Z', ElementInfoConstantValue::from),
        STRING      ('s', ElementInfoConstantValue::from),
        ENUM        ('e', ElementInfoEnumConstant::from),
        CLASS       ('c', ElementInfoClass::from),
        ANNOTATION  ('@', ElementInfoAnnotation::from),
        ARRAY       ('[', ElementInfoArray::from);

        private Type(int tag,
                     @Nonnull ElementInfoInterpreter interpreter)
        {
            this.tag = tag;
            this.interpreter = interpreter;

            reg(this);
        }

        public static @Nullable Type getType(int tag)
        {
            if (tag < 0 || tag > 255)
                return null;

            return TYPES[tag];
        }

        private static void reg(Type type)
        {
            if (TYPES == null)
                TYPES = new Type[256];

            TYPES[type.getTag()] = type;
        }

        public int getTag()
        {
            return tag;
        }

        public @Nonnull ElementInfoInterpreter getInterpreter()
        {
            return interpreter;
        }

        private final int tag;

        private final ElementInfoInterpreter interpreter;

        private static Type[] TYPES;

        public static interface ElementInfoInterpreter
        {
            public @Nonnull ElementInfo from(@Nonnull ConstantPool constantPool,
                                             @Nonnull ConstantPool.TagRef nameTagRef,
                                             @Nonnull Type type,
                                             @Nonnull ByteBuffer byteBuffer);
        }
    }
}
