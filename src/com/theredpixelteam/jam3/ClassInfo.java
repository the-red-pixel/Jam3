package com.theredpixelteam.jam3;

import com.theredpixelteam.jam3.attribute.AttributePool;
import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

@SuppressWarnings("all")
public class ClassInfo {
    public ClassInfo(@Nonnegative int minorVersion,
                     @Nonnegative int majorVersion,
                     @Nonnull ConstantPool constantPool,
                     @Nonnegative int modifiers,
                     @Nonnull ConstantPool.TagRef thisClassTagRef,
                     @Nonnull ConstantPool.TagRef superClassTagRef,
                     @Nonnull ConstantPool.TagRef[] interfaceTagRefs,
                     @Nonnull FieldInfo[] fields,
                     @Nonnull MethodInfo[] methods,
                     @Nonnull AttributePool attributes)
    {
        this.constantPool = Objects.requireNonNull(constantPool);

        setMinorVersion(minorVersion);
        setMajorVersion(majorVersion);
        setModifiers(modifiers);
        setThisClassTagRef(thisClassTagRef);
        setSuperClassTagRef(superClassTagRef);
        setInterfaceTagRefs(interfaceTagRefs);
        setFields(fields);
        setMethods(methods);
        setAttributes(attributes);
    }

    public static @Nonnull ClassInfo from(@Nonnull ByteBuffer byteBuffer)
    {
        int magicValue = byteBuffer.getInt();

        if (magicValue != MAGICVALUE)
            throw new ClassFormatError("Corrupt magicvalue header (Should be 0xCAFEBABE)");

        int minorVersion = byteBuffer.getShort() & 0xFFFF;
        int majorVersion = byteBuffer.getShort() & 0xFFFF;

        int constantPoolLength = byteBuffer.getShort() & 0xFFFF;
        ConstantPool constantPool = new ConstantPool();
        ConstantPool.from(constantPool, constantPoolLength - 1, byteBuffer);

        int modifiers = byteBuffer.getShort() & 0xFFFF;

        ConstantPool.TagRef thisClassTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        ConstantPool.TagRef superClassTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        int interfaceTagRefCount = byteBuffer.getShort() & 0xFFFF;

        ConstantPool.TagRef[] interfaceTagRefs = new ConstantPool.TagRef[interfaceTagRefCount];
        for (int i = 0; i < interfaceTagRefCount; i++)
            interfaceTagRefs[i] = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        int fieldCount = byteBuffer.getShort() & 0xFFFF;

        FieldInfo[] fields = new FieldInfo[fieldCount];
        for (int i = 0; i < fieldCount; i++)
            fields[i] = FieldInfo.from(constantPool, byteBuffer);

        int methodCount = byteBuffer.getShort() & 0xFFFF;

        MethodInfo[] methods = new MethodInfo[methodCount];
        for (int i = 0; i < methodCount; i++)
            methods[i] = MethodInfo.from(constantPool, byteBuffer);

        int attributeCount = byteBuffer.getShort() & 0xFFFF;
        AttributePool attributes = new AttributePool();
        AttributePool.from(attributes, attributeCount, constantPool, byteBuffer);

        return new ClassInfo(
                minorVersion,
                majorVersion,
                constantPool,
                modifiers,
                thisClassTagRef,
                superClassTagRef,
                interfaceTagRefs,
                fields,
                methods,
                attributes
        );
    }

    public @Nonnull byte[] toBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            BigEndian.pushU4(baos, MAGICVALUE);

            BigEndian.pushU2(baos, minorVersion);
            BigEndian.pushU2(baos, majorVersion);

            BigEndian.pushU2(baos, constantPool.getLength());
            baos.write(constantPool.toBytes());

            BigEndian.pushU2(baos, modifiers);

            BigEndian.pushU2(baos, thisClassTagRef.getRefIndex());
            BigEndian.pushU2(baos, superClassTagRef.getRefIndex());

            BigEndian.pushU2(baos, interfaceTagRefs.length);
            for (int i = 0; i < interfaceTagRefs.length; i++)
                BigEndian.pushU2(baos, interfaceTagRefs[i].getRefIndex());

            BigEndian.pushU2(baos, fields.length);
            for (int i = 0; i < fields.length; i++)
                baos.write(fields[i].toBytes());

            BigEndian.pushU2(baos, methods.length);
            for (int i = 0; i < methods.length; i++)
                baos.write(methods[i].toBytes());

            BigEndian.pushU2(baos, attributes.size());
            baos.write(attributes.toBytes());
        } catch (IOException e) {
            // unused
            throw new Error(e);
        }

        return baos.toByteArray();
    }

    public @Nonnegative int getMinorVersion()
    {
        return minorVersion;
    }

    public void setMinorVersion(@Nonnegative int minorVersion)
    {
        this.minorVersion = minorVersion;
    }

    public @Nonnegative int getMajorVersion()
    {
        return majorVersion;
    }

    public void setMajorVersion(@Nonnegative int majorVersion)
    {
        this.majorVersion = majorVersion;
    }

    public @Nonnull ConstantPool getConstantPool()
    {
        return constantPool;
    }

    public @Nonnegative int getModifiers()
    {
        return modifiers;
    }

    public void setModifiers(@Nonnegative int modifiers)
    {
        this.modifiers = modifiers;
    }

    public @Nonnull ConstantPool.TagRef getThisClassTagRef()
    {
        return thisClassTagRef;
    }

    public void setThisClassTagRef(@Nonnull ConstantPool.TagRef thisClassTagRef)
    {
        this.thisClassTagRef = checkRef(Objects.requireNonNull(thisClassTagRef));
    }

    public @Nonnull ConstantPool.TagRef getSuperClassTagRef()
    {
        return superClassTagRef;
    }

    public void setSuperClassTagRef(@Nonnull ConstantPool.TagRef superClassTagRef)
    {
        this.superClassTagRef = checkRef(Objects.requireNonNull(superClassTagRef));
    }

    public @Nonnull ConstantPool.TagRef[] getInterfaceTagRefs()
    {
        return interfaceTagRefs;
    }

    public void setInterfaceTagRefs(@Nonnull ConstantPool.TagRef[] interfaceTagRefs)
    {
        this.interfaceTagRefs = Objects.requireNonNull(interfaceTagRefs);
    }

    public @Nonnull FieldInfo[] getFields()
    {
        return fields;
    }

    public void setFields(@Nonnull FieldInfo[] fields)
    {
        this.fields = Objects.requireNonNull(fields);
    }

    public @Nonnull MethodInfo[] getMethods()
    {
        return methods;
    }

    public void setMethods(@Nonnull MethodInfo[] methods)
    {
        this.methods = Objects.requireNonNull(methods);
    }

    public @Nonnull AttributePool getAttributes()
    {
        return attributes;
    }

    public void setAttributes(@Nonnull AttributePool attributes)
    {
        this.attributes = Objects.requireNonNull(attributes);
    }

    ConstantPool.TagRef checkRef(ConstantPool.TagRef tagRef)
    {
        return ConstantTag.checkRef(constantPool, tagRef);
    }

    private int minorVersion;

    private int majorVersion;

    private final ConstantPool constantPool;

    private int modifiers;

    private ConstantPool.TagRef thisClassTagRef;

    private ConstantPool.TagRef superClassTagRef;

    private ConstantPool.TagRef[] interfaceTagRefs;

    private FieldInfo[] fields;

    private MethodInfo[] methods;

    private AttributePool attributes;

    public static final int MAGICVALUE = 0xCAFEBABE;
}
