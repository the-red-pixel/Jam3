package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public class AttributeEntryBootstrapMethods extends AttributeEntry {
    public AttributeEntryBootstrapMethods(@Nonnull AttributePool owner,
                                          @Nonnegative int index,
                                          @Nonnull ConstantPool constantPool,
                                          @Nonnull ConstantPool.TagRef nameTagRef,
                                          @Nonnull BootstrapMethodInfo[] bootstrapMethods)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setBootstrapMethods(bootstrapMethods);
    }

    public static @Nonnull AttributeEntryBootstrapMethods from(@Nonnull AttributePool attributePool,
                                                               @Nonnull ConstantPool constantPool,
                                                               @Nonnegative int length,
                                                               @Nonnull ConstantPool.TagRef nameTagRef,
                                                               @Nonnull ByteBuffer byteBuffer)
    {
        int bootstrapMethodCount = byteBuffer.getShort() & 0xFFFF;

        BootstrapMethodInfo[] bootstrapMethods = new BootstrapMethodInfo[bootstrapMethodCount];

        for (int i = 0; i < bootstrapMethodCount; i++)
            bootstrapMethods[i] = BootstrapMethodInfo.from(constantPool, byteBuffer);

        return attributePool.addEntry(
                new AttributeEntryBootstrapMethods(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        bootstrapMethods));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        BigEndian.pushU2(baos, bootstrapMethods.length);

        for (int i = 0; i < bootstrapMethods.length; i++)
            bootstrapMethods[i].push(baos);

        return baos.toByteArray();
    }

    public @Nonnull BootstrapMethodInfo[] getBootstrapMethods()
    {
        return bootstrapMethods;
    }

    public void setBootstrapMethods(@Nonnull BootstrapMethodInfo[] bootstrapMethods)
    {
        this.bootstrapMethods = Objects.requireNonNull(bootstrapMethods);
    }

    private BootstrapMethodInfo[] bootstrapMethods;

    public static final String NAME = "BootstrapMethods";

    public static class BootstrapMethodInfo
    {
        public BootstrapMethodInfo(@Nonnull ConstantPool constantPool,
                                   @Nonnull ConstantPool.TagRef bootstrapMethodTagRef,
                                   @Nonnull ConstantPool.TagRef[] parameterTagRefs)
        {
            this.constantPool = Objects.requireNonNull(constantPool);

            setBootstrapMethodTagRef(bootstrapMethodTagRef);
            setParameterTagRefs(parameterTagRefs);
        }

        public static @Nonnull BootstrapMethodInfo from(@Nonnull ConstantPool constantPool,
                                                        @Nonnull ByteBuffer byteBuffer)
        {
            ConstantPool.TagRef bootstrapMethodTagRef
                    = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

            int parameterCount = byteBuffer.getShort() & 0xFFFF;

            ConstantPool.TagRef[] parameterTagRefs = new ConstantPool.TagRef[parameterCount];
            for (int i = 0; i < parameterCount; i++)
                parameterTagRefs[i] = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

            return new BootstrapMethodInfo(constantPool, bootstrapMethodTagRef, parameterTagRefs);
        }

        public void push(@Nonnull ByteArrayOutputStream baos)
        {
            BigEndian.pushU2(baos, bootstrapMethodTagRef.getRefIndex());

            BigEndian.pushU2(baos, parameterTagRefs.length);

            for (int i = 0; i < parameterTagRefs.length; i++)
                BigEndian.pushU2(baos, parameterTagRefs[i].getRefIndex());
        }

        public @Nonnull ConstantPool.TagRef getBootstrapMethodTagRef()
        {
            return bootstrapMethodTagRef;
        }

        public void setBootstrapMethodTagRef(@Nonnull ConstantPool.TagRef bootstrapMethodTagRef)
        {
            this.bootstrapMethodTagRef = checkRef(Objects.requireNonNull(bootstrapMethodTagRef));
        }

        public @Nonnull ConstantPool.TagRef[] getParameterTagRefs()
        {
            return parameterTagRefs;
        }

        public void setParameterTagRefs(@Nonnull ConstantPool.TagRef[] parameterTagRefs)
        {
            this.parameterTagRefs = Objects.requireNonNull(parameterTagRefs);
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

        private ConstantPool.TagRef bootstrapMethodTagRef;

        private ConstantPool.TagRef[] parameterTagRefs;
    }
}
