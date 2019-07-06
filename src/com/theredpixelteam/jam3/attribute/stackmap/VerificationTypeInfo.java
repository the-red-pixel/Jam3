package com.theredpixelteam.jam3.attribute.stackmap;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public class VerificationTypeInfo {
    public VerificationTypeInfo(@Nonnull Type type)
    {
        this.type = Objects.requireNonNull(type);
    }

    static Type getType(@Nonnull ByteBuffer byteBuffer)
    {
        int tag = byteBuffer.get() & 0xFF;
        Type type = Type.getType(tag);

        if (type == null)
            throw new ClassFormatError("Unknown verification type tag: " + tag);

        return type;
    }

    public static @Nonnull VerificationTypeInfo from(@Nonnull ConstantPool constantPool,
                                                     @Nonnull ByteBuffer byteBuffer)
    {
        return from(constantPool, getType(byteBuffer), byteBuffer);
    }

    public static @Nonnull VerificationTypeInfo from(@Nonnull ConstantPool constantPool,
                                                     @Nonnull Type type,
                                                     @Nonnull ByteBuffer byteBuffer)
    {
        return new VerificationTypeInfo(type);
    }

    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[1];

        BigEndian.pushU1(byts, type.getTag(), 0);

        return byts;
    }

    public @Nonnull Type getType()
    {
        return type;
    }

    private final Type type;

    public static enum Type
    {
        TOP                 (0, VerificationTypeInfo::from),
        INT                 (1, VerificationTypeInfo::from),
        FLOAT               (2, VerificationTypeInfo::from),
        NULL                (5, VerificationTypeInfo::from),
        UNINITIALIZED_THIS  (6, VerificationTypeInfo::from),
        OBJECT              (7, VerificationTypeInfoObject::from),
        UNINITIALIZED       (8, VerificationTypeInfoUninitialized::from),
        LONG                (4, VerificationTypeInfo::from),
        DOUBLE              (3, VerificationTypeInfo::from);

        private Type(int tag,
                     VerificationTypeInterpreter interpreter)
        {
            this.tag = tag;
            this.interpreter = interpreter;

            reg(this);
        }

        public @Nonnegative int getTag()
        {
            return tag;
        }

        public @Nonnull VerificationTypeInterpreter getInterpreter()
        {
            return interpreter;
        }

        public static @Nullable Type getType(int tag)
        {
            if (tag < 0 || tag > 0x0F)
                return null;

            return TYPES[tag];
        }

        static void reg(Type type)
        {
            if (TYPES == null)
                TYPES = new Type[0x10];

            TYPES[type.getTag()] = type;
        }

        private final int tag;

        private final VerificationTypeInterpreter interpreter;

        private static Type[] TYPES;

        public static interface VerificationTypeInterpreter
        {
            public @Nonnull VerificationTypeInfo from(@Nonnull ConstantPool constantPool,
                                                      @Nonnull Type type,
                                                      @Nonnull ByteBuffer byteBuffer);
        }
    }
}
