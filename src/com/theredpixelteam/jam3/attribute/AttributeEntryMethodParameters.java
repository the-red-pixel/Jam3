package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.Objects;

@SuppressWarnings("all")
public class AttributeEntryMethodParameters extends AttributeEntry {
    public AttributeEntryMethodParameters(@Nonnull AttributePool owner,
                                          @Nonnegative int index,
                                          @Nonnull ConstantPool constantPool,
                                          @Nonnull ConstantPool.TagRef nameTagRef,
                                          @Nonnull ParameterInfo[] parameters)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setParameters(parameters);
    }

    public static @Nonnull AttributeEntryMethodParameters from(@Nonnull AttributePool attributePool,
                                                               @Nonnull ConstantPool constantPool,
                                                               @Nonnegative int length,
                                                               @Nonnull ConstantPool.TagRef nameTagRef,
                                                               @Nonnull ByteBuffer byteBuffer)
    {
        int parameterTableSize = byteBuffer.get() & 0xFF;

        checkLen(length, 2 + (parameterTableSize << 2));

        ParameterInfo[] parameterTable = new ParameterInfo[parameterTableSize];
        for (int i = 0; i < parameterTableSize; i++)
            parameterTable[i] = ParameterInfo.from(constantPool, byteBuffer);

        return attributePool.addEntry(
                new AttributeEntryMethodParameters(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        parameterTable));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        byte[] byts = new byte[2 + (parameters.length << 2)];

        BigEndian.pushU2(byts, parameters.length, 0);
        for (int i = 0; i < parameters.length; i++)
            parameters[i].push(byts, 2 + (i << 2));

        return byts;
    }

    public @Nonnull ParameterInfo[] getParameters()
    {
        return parameters;
    }

    public void setParameters(@Nonnull ParameterInfo[] parameters)
    {
        this.parameters = Objects.requireNonNull(parameters);
    }

    private ParameterInfo[] parameters;

    public static final String NAME = "MethodParameters";

    public static class ParameterInfo
    {
        public ParameterInfo(@Nonnull ConstantPool constantPool,
                             @Nonnull ConstantPool.TagRef nameTagRef,
                             int modifiers)
        {
            this.constantPool = Objects.requireNonNull(constantPool);

            setNameTagRef(nameTagRef);
            setModifiers(modifiers);
        }

        public static @Nonnull ParameterInfo from(@Nonnull ConstantPool constantPool,
                                                  @Nonnull ByteBuffer byteBuffer)
        {
            ConstantPool.TagRef nameTagRef
                    = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

            int modifiers = byteBuffer.getShort() & 0xFFFF;

            return new ParameterInfo(constantPool, nameTagRef, modifiers);
        }

        public void push(@Nonnull byte[] byts, int off)
        {
            BigEndian.pushU2(byts, nameTagRef.getRefIndex(), off);
            BigEndian.pushU2(byts, modifiers, off + 2);
        }

        public @Nonnull ConstantPool.TagRef getNameTagRef()
        {
            return nameTagRef;
        }

        public void setNameTagRef(@Nonnull ConstantPool.TagRef nameTagRef)
        {
            this.nameTagRef = checkRef(Objects.requireNonNull(nameTagRef));
        }

        public int getModifiers()
        {
            return modifiers;
        }

        public void setModifiers(int modifiers)
        {
            this.modifiers = modifiers;
        }

        public @Nonnull ConstantPool getConstantPool()
        {
            return constantPool;
        }

        ConstantPool.TagRef checkRef(ConstantPool.TagRef tagRef)
        {
            return ConstantTag.checkRef(constantPool, tagRef);
        }

        private ConstantPool.TagRef nameTagRef;

        private int modifiers;

        private final ConstantPool constantPool;
    }
}
