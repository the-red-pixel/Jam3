package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.constant.ConstantTag;
import com.theredpixelteam.jam3.constant.ConstantTagUTF8;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.AEADBadTagException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AttributeEntry {
    AttributeEntry(@Nonnull String name,
                   @Nonnull AttributePool owner,
                   @Nonnegative int index,
                   @Nonnull ConstantPool constantPool,
                   @Nonnull ConstantPool.TagRef nameTagRef)
    {
        Objects.requireNonNull(name, "name");

        this.index = index;
        this.owner = Objects.requireNonNull(owner, "owner");
        this.constantPool = Objects.requireNonNull(constantPool, "constantPool");

        setNameTagRef(nameTagRef);

        this.name = Objects.requireNonNull(nameTagRef.get(ConstantTag.Type.UTF8, ConstantTagUTF8.class)).getString();

        if (!name.endsWith(this.name))
            throw new ClassFormatError("Found \"" + this.nameTagRef + "\" in UTF8 constant tag, "
                    + name + " expected");
    }

    public static @Nonnull AttributeEntry from(@Nonnull AttributePool dstPool,
                                               @Nonnull ConstantPool constantPool,
                                               @Nonnull ByteBuffer byteBuffer)
    {
        ConstantPool.TagRef nameTagRef
                = constantPool.new TagRef(byteBuffer.getShort() & 0xFFFF);

        if (!nameTagRef.available())
            throw new IllegalStateException("Tag ref not available: " + nameTagRef.getRefIndex());

        ConstantTagUTF8 nameTag = nameTagRef.get(ConstantTag.Type.UTF8, ConstantTagUTF8.class);

        if (nameTag == null)
            throw new ClassFormatError("Null attribute name tag ref");

        String name = nameTag.getString();

        Type type = getType(name);

        int len = byteBuffer.getInt();

        if (type == null)
            return AttributeEntryUnknown.from(dstPool, constantPool, len, nameTagRef, byteBuffer);

        return type.getInterpreter().from(dstPool, constantPool, len, nameTagRef, byteBuffer);
    }

    public static void skip(@Nonnull ByteBuffer byteBuffer)
    {
        byteBuffer.position(byteBuffer.position() + 2);

        int len = byteBuffer.getInt();

        byteBuffer.position(byteBuffer.position() + len);
    }

    public abstract @Nonnull byte[] toBytes();

    ConstantPool.TagRef checkRef(@Nonnull ConstantPool.TagRef tagRef)
    {
        return ConstantTag.checkRef(constantPool, tagRef);
    }

    static void checkLen(int len, int expected)
    {
        if (len != expected)
            throw new ClassFormatError("Readed len: " + len + ", " + expected + " expected");
    }

    public @Nonnull String getName()
    {
        return name;
    }

    public void setNameTagRef(@Nonnull ConstantPool.TagRef nameTagRef)
    {
        this.nameTagRef = checkRef(Objects.requireNonNull(nameTagRef));
    }

    public @Nonnull ConstantPool getConstantPool()
    {
        return constantPool;
    }

    public @Nonnull AttributePool getOwner()
    {
        return owner;
    }

    public int getIndex()
    {
        return index;
    }

    public @Nonnull ConstantPool.TagRef getNameTagRef()
    {
        return nameTagRef;
    }

    public static @Nullable Type getType(@Nonnull String name)
    {
        if (!TYPES_INITIALIZED)
            initTypes();

        return TYPES.get(Objects.requireNonNull(name));
    }

    static synchronized void initTypes()
    {
        if (TYPES_INITIALIZED)
            return;

        regType(AttributeEntryAnnotationDefault.NAME, AttributeEntryAnnotationDefault::from);
        regType(AttributeEntryBootstrapMethods.NAME, AttributeEntryBootstrapMethods::from);
        regType(AttributeEntryCode.NAME, AttributeEntryCode::from);
        regType(AttributeEntryConstantValue.NAME, AttributeEntryConstantValue::from);
        regType(AttributeEntryDeprecated.NAME, AttributeEntryDeprecated::from);
        regType(AttributeEntryEnclosingMethod.NAME, AttributeEntryEnclosingMethod::from);
        regType(AttributeEntryExceptions.NAME, AttributeEntryExceptions::from);
        regType(AttributeEntryInnerClasses.NAME, AttributeEntryInnerClasses::from);
        regType(AttributeEntryLineNumberTable.NAME, AttributeEntryLineNumberTable::from);
        regType(AttributeEntryLocalVariableTable.NAME, AttributeEntryLocalVariableTable::from);
        regType(AttributeEntryLocalVariableTypeTable.NAME, AttributeEntryLocalVariableTypeTable::from);
        regType(AttributeEntryMethodParameters.NAME, AttributeEntryMethodParameters::from);
        regType(AttributeEntryRuntimeInvisibleAnnotations.NAME, AttributeEntryRuntimeInvisibleAnnotations::from);
        regType(AttributeEntryRuntimeInvisibleParameterAnnotations.NAME, AttributeEntryRuntimeInvisibleParameterAnnotations::from);
        regType(AttributeEntryRuntimeInvisibleTypeAnnotations.NAME, AttributeEntryRuntimeInvisibleTypeAnnotations::from);
        regType(AttributeEntryRuntimeVisibleAnnotations.NAME, AttributeEntryRuntimeVisibleAnnotations::from);
        regType(AttributeEntryRuntimeVisibleParameterAnnotations.NAME, AttributeEntryRuntimeVisibleParameterAnnotations::from);
        regType(AttributeEntryRuntimeVisibleTypeAnnotations.NAME, AttributeEntryRuntimeVisibleTypeAnnotations::from);
        regType(AttributeEntrySignature.NAME, AttributeEntrySignature::from);
        regType(AttributeEntrySourceDebugExtension.NAME, AttributeEntrySourceDebugExtension::from);
        regType(AttributeEntrySourceFile.NAME, AttributeEntrySourceFile::from);
        regType(AttributeEntryStackMapTable.NAME, AttributeEntryStackMapTable::from);
        regType(AttributeEntrySynthetic.NAME, AttributeEntrySynthetic::from);

        TYPES_INITIALIZED = true;
    }

    public static void registerAttributeType(@Nonnull String name,
                                             @Nonnull Type.AttributeInterpreter interpreter)
    {
        regType(name, interpreter);
    }

    static void regType(String name, Type.AttributeInterpreter interpreter)
    {
        regType(new Type(name, interpreter));
    }

    public static void registerAttributeType(@Nonnull Type type)
    {
        regType(Objects.requireNonNull(type));
    }

    static void regType(Type type)
    {
        TYPES.put(type.getName(), type);
    }

    private static volatile boolean TYPES_INITIALIZED = false;

    private final String name;

    private ConstantPool.TagRef nameTagRef;

    private final ConstantPool constantPool;

    private final int index;

    private final AttributePool owner;

    private static final Map<String, Type> TYPES = new HashMap<>();

    public static class Type
    {
        public Type(@Nonnull String name,
                    @Nonnull AttributeInterpreter interpreter)
        {
            this.name = Objects.requireNonNull(name);
            this.interpreter = Objects.requireNonNull(interpreter);
        }

        static Type of(String name,
                       AttributeInterpreter interpreter)
        {
            return new Type(name, interpreter);
        }

        public @Nonnull String getName()
        {
            return name;
        }

        public @Nonnull AttributeInterpreter getInterpreter()
        {
            return interpreter;
        }

        private final String name;

        private final AttributeInterpreter interpreter;

        public static interface AttributeInterpreter
        {
            public @Nonnull AttributeEntry from(@Nonnull AttributePool attributePool,
                                                @Nonnull ConstantPool constantPool,
                                                @Nonnegative int index,
                                                @Nonnull ConstantPool.TagRef nameTagRef,
                                                @Nonnull ByteBuffer byteBuffer);
        }
    }
}
