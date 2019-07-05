package com.theredpixelteam.jam3.attribute.annotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public abstract class TargetInfo {
    public TargetInfo(@Nonnull Type type)
    {
        this.type = Objects.requireNonNull(type);
    }

    public abstract @Nonnull byte[] toBytes();

    public @Nonnull Type getType()
    {
        return type;
    }

    private final Type type;

    public static enum Type
    {
        CLASS_TYPE_PARAMETER                (0x00, TargetInfoTypeParameter::from),
        METHOD_TYPE_PARAMETER               (0x01, TargetInfoTypeParameter::from),
        SUPERCLASS                          (0x10, TargetInfoSuperClass::from),
        CLASS_TYPE_PARAMETER_BOUND          (0x11, TargetInfoTypeParameterBound::from),
        METHOD_TYPE_PARAMETER_BOUND         (0x12, TargetInfoTypeParameterBound::from),
        FIELD                               (0x13, TargetInfoEmpty::from),
        METHOD_RETURN                       (0x14, TargetInfoEmpty::from),
        METHOD_RECEIVER                     (0x15, TargetInfoEmpty::from),
        METHOD_FORMAL_PARAMETER             (0x16, TargetInfoFormalParameter::from),
        THROWS                              (0x17, TargetInfoThrows::from),
        LOCAL_VARIABLE                      (0x40, TargetInfoLocalVariable::from),
        RESOURCE_VARIABLE                   (0x41, TargetInfoLocalVariable::from),
        EXCEPTION_VARIABLE                  (0x42, TargetInfoCatch::from),
        INSTANCEOF                          (0x43, TargetInfoOffset::from),
        NEW                                 (0x44, TargetInfoOffset::from),
        CONSTRUCTOR_REFERENCE               (0x45, TargetInfoOffset::from),
        METHOD_REFERENCE                    (0x46, TargetInfoOffset::from),
        CAST                                (0x47, TargetInfoTypeArgument::from),
        CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT(0x48, TargetInfoTypeArgument::from),
        METHOD_INVOCATION_TYPE_ARGUMENT     (0x49, TargetInfoTypeArgument::from),
        CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT (0x4A, TargetInfoTypeArgument::from),
        METHOD_REFERENCE_TYPE_ARGUMENT      (0x4B, TargetInfoTypeArgument::from);

        private Type(int tag, TargetInfoInterpreter interpreter)
        {
            this.tag = tag;
            this.interpreter = interpreter;

            reg(this);
        }

        public int getTag()
        {
            return tag;
        }

        public @Nonnull TargetInfoInterpreter getInterpreter()
        {
            return interpreter;
        }

        public static @Nullable Type getType(int tag)
        {
            if (tag < 0 || tag > 0xFF)
                return null;

            return TYPES[tag];
        }

        static void reg(Type type)
        {
            if (TYPES == null)
                TYPES = new Type[0x100];

            TYPES[type.getTag()] = type;
        }

        private static Type[] TYPES;

        private final int tag;

        private final TargetInfoInterpreter interpreter;

        public static interface TargetInfoInterpreter
        {
            public @Nonnull TargetInfo from(@Nonnull Type type,
                                            @Nonnull ByteBuffer byteBuffer);
        }
    }
}
