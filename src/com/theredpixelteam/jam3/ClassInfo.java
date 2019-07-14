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
import java.util.LinkedList;
import java.util.ListIterator;
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
                     @Nonnegative int fieldCount,
                     @Nonnull FieldInfo[] fields,
                     @Nonnegative int methodCount,
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
        setFieldCount(fieldCount);
        setFields(fields);
        setMethodCount(methodCount);
        setMethods(methods);
        setAttributes(attributes);
    }

    private static void sortAndInsert(LinkedList<Integer> list,
                                      int value)
    {
        ListIterator<Integer> iter = list.listIterator();

        while (iter.hasNext())
        {
            int val = iter.next();

            if (val > value)
            {
                iter.set(value);
                iter.add(val);

                return;
            }
        }

        list.addLast(value);
    }

    public static @Nonnull ClassInfo from(@Nonnull ByteBuffer byteBuffer)
    {
        return from(byteBuffer, 0);
    }

    public static @Nonnull ClassInfo from(@Nonnull ByteBuffer byteBuffer,
                                          int options)
    {
        int magicValue = byteBuffer.getInt();

        if (magicValue != MAGICVALUE)
            throw new ClassFormatError("Corrupt magicvalue header (Should be 0xCAFEBABE)");

        boolean nonStrictByteFlowPtr = is(options, NON_STRICT_BYTE_FLOW_POINTER);


        // Load class file information

        int minorVersion = byteBuffer.getShort() & 0xFFFF;
        int majorVersion = byteBuffer.getShort() & 0xFFFF;


        // Load constants

        int constantPoolLength = byteBuffer.getShort() & 0xFFFF;

        ConstantPool constantPool = new ConstantPool();

        if (is(options, BIT_SKIP_CONSTANTS))
            if (!nonStrictByteFlowPtr || is(options, LOAD_FIELD_COUNT) || is(options, LOAD_METHOD_COUNT))
                ConstantPool.skip(constantPoolLength, byteBuffer);
            else
                return new ClassInfo(
                        minorVersion,
                        majorVersion,
                        constantPool,
                        0,
                        constantPool.nullTagRef,
                        constantPool.nullTagRef,
                        new ConstantPool.TagRef[0],
                        0,
                        new FieldInfo[0],
                        0,
                        new MethodInfo[0],
                        new AttributePool()
                );
        else
            ConstantPool.from(constantPool, constantPoolLength - 1, byteBuffer);


        // Load class type information

        int modifiers = byteBuffer.getShort() & 0xFFFF;

        ConstantPool.TagRef thisClassTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        ConstantPool.TagRef superClassTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        int interfaceTagRefCount = byteBuffer.getShort() & 0xFFFF;

        ConstantPool.TagRef[] interfaceTagRefs = new ConstantPool.TagRef[interfaceTagRefCount];
        for (int i = 0; i < interfaceTagRefCount; i++)
            interfaceTagRefs[i] = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);


        // Load fields

        int fieldCount = byteBuffer.getShort() & 0xFFFF;

        FieldInfo[] fields;
        if (is(options, BIT_SKIP_FIELDS))
        {
            fields = new FieldInfo[0];

            if (!nonStrictByteFlowPtr || !is(options, BIT_SKIP_METHODS) || !is(options, BIT_SKIP_CLASS_ATTRIBUTES))
                for (int i = 0; i < fieldCount; i++)
                    FieldInfo.skip(byteBuffer);
        }
        else
        {
            boolean skipFieldAttributes = is(options, BIT_SKIP_FIELD_ATTRIBUTES);

            fields = new FieldInfo[fieldCount];
            for (int i = 0; i < fieldCount; i++)
                fields[i] = FieldInfo.from(constantPool, byteBuffer, skipFieldAttributes);
        }


        // Load methods

        int methodCount = byteBuffer.getShort() & 0xFFFF;

        MethodInfo[] methods;
        if (is(options, BIT_SKIP_METHODS))
        {
            methods = new MethodInfo[0];

            if (!nonStrictByteFlowPtr || !is(options, BIT_SKIP_CLASS_ATTRIBUTES))
                for (int i = 0; i < methodCount; i++)
                    MethodInfo.skip(byteBuffer);
        }
        else
        {
            boolean skipMethodAttributes = is(options, BIT_SKIP_METHOD_ATTRIBUTES);

            methods = new MethodInfo[methodCount];
            for (int i = 0; i < methodCount; i++)
                methods[i] = MethodInfo.from(constantPool, byteBuffer, skipMethodAttributes);
        }


        // Load class attributes

        int attributeCount = byteBuffer.getShort() & 0xFFFF;
        AttributePool attributes = new AttributePool();

        if (!is(options, BIT_SKIP_CLASS_ATTRIBUTES))
            AttributePool.from(attributes, attributeCount, constantPool, byteBuffer);
        else if (!nonStrictByteFlowPtr)
            AttributePool.skip(attributeCount, byteBuffer);

        return new ClassInfo(
                minorVersion,
                majorVersion,
                constantPool,
                modifiers,
                thisClassTagRef,
                superClassTagRef,
                interfaceTagRefs,
                fieldCount,
                fields,
                methodCount,
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

    public @Nonnegative int getFieldCount()
    {
        return fieldCount;
    }

    public void setFieldCount(@Nonnegative int fieldCount)
    {
        this.fieldCount = fieldCount;
    }

    public @Nonnull FieldInfo[] getFields()
    {
        return fields;
    }

    public void setFields(@Nonnull FieldInfo[] fields)
    {
        this.fields = Objects.requireNonNull(fields);
    }

    public @Nonnegative int getMethodCount()
    {
        return methodCount;
    }

    public void setMethodCount(@Nonnegative int methodCount)
    {
        this.methodCount = methodCount;
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

    static boolean is(int flags, int bit)
    {
        return (flags & bit) != 0;
    }

    private int minorVersion;

    private int majorVersion;

    private final ConstantPool constantPool;

    private int modifiers;

    private ConstantPool.TagRef thisClassTagRef;

    private ConstantPool.TagRef superClassTagRef;

    private ConstantPool.TagRef[] interfaceTagRefs;

    private int fieldCount;

    private FieldInfo[] fields;

    private int methodCount;

    private MethodInfo[] methods;

    private AttributePool attributes;

    public static final int MAGICVALUE = 0xCAFEBABE;

    public static final int LOAD_FIELD_COUNT                    = 0b00000001 << 8;

    public static final int LOAD_METHOD_COUNT                   = 0b00000010 << 8;

    public static final int NON_STRICT_BYTE_FLOW_POINTER        = 0b10000000;

    public static final int SKIP_CONSTANTS_N_ATTRIBUTES         = 0b00111111;

    public static final int SKIP_ATTRIBUTES                     = 0b00001011;

    public static final int SKIP_METHODS                        = 0b00011000;

    public static final int SKIP_METHOD_ATTRIBUTES              = 0b00001000;

    public static final int SKIP_FIELDS                         = 0b00000110;

    public static final int SKIP_FIELD_ATTRIBUTES               = 0b00000010;

    public static final int SKIP_CLASS_ATTRIBUTES               = 0b00000001;

    private static final int BIT_SKIP_CONSTANTS                 = 0b00100000;

    private static final int BIT_SKIP_METHODS                   = 0b00010000;

    private static final int BIT_SKIP_METHOD_ATTRIBUTES         = 0b00001000;

    private static final int BIT_SKIP_FIELDS                    = 0b00000100;

    private static final int BIT_SKIP_FIELD_ATTRIBUTES          = 0b00000010;

    private static final int BIT_SKIP_CLASS_ATTRIBUTES          = 0b00000001;
}
