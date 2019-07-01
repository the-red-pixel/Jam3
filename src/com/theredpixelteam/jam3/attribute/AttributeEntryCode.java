package com.theredpixelteam.jam3.attribute;

import com.theredpixelteam.jam3.constant.ConstantPool;
import com.theredpixelteam.jam3.util.BigEndian;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public class AttributeEntryCode extends AttributeEntry {
    public AttributeEntryCode(@Nonnull AttributePool owner,
                              @Nonnegative int index,
                              @Nonnull ConstantPool constantPool,
                              @Nonnull ConstantPool.TagRef nameTagRef,
                              @Nonnegative int maxStack,
                              @Nonnegative int maxLocals,
                              @Nonnull byte[] code,
                              @Nonnull ExceptionInfo[] exceptionTable,
                              @Nonnull AttributePool attributes)
    {
        super(NAME, owner, index, constantPool, nameTagRef);

        setMaxStack(maxStack);
        setMaxLocals(maxLocals);
        setCode(code);
        setExceptionTable(exceptionTable);
        setAttributes(attributes);
    }

    public static @Nonnull AttributeEntryCode from(@Nonnull AttributePool attributePool,
                                                   @Nonnull ConstantPool constantPool,
                                                   @Nonnegative int length,
                                                   @Nonnull ConstantPool.TagRef nameTagRef,
                                                   @Nonnull ByteBuffer byteBuffer)
    {
        int maxStack = byteBuffer.getShort() & 0xFFFF;
        int maxLocals = byteBuffer.getShort() & 0xFFFF;

        byte[] code = new byte[byteBuffer.getInt()];
        byteBuffer.get(code);

        int exceptionTableSize = byteBuffer.getShort() & 0xFFFF;

        ExceptionInfo[] exceptionTable = new ExceptionInfo[exceptionTableSize];
        for (int i = 0; i < exceptionTableSize; i++)
            exceptionTable[i] = ExceptionInfo.from(constantPool, byteBuffer);

        int attributePoolSize = byteBuffer.getShort() & 0xFFFF;
        AttributePool attributes = new AttributePool();
        AttributePool.from(attributePool, attributePoolSize, constantPool, byteBuffer);

        return attributePool.addEntry(
                new AttributeEntryCode(attributePool, attributePool.nextIndex(), constantPool,
                        nameTagRef,
                        maxStack,
                        maxLocals,
                        code,
                        exceptionTable,
                        attributes));
    }

    @Override
    public @Nonnull byte[] toBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeShort(maxStack);
            dos.writeShort(maxLocals);

            dos.writeInt(code.length);
            dos.write(code);

            dos.writeShort(exceptionTable.length);
            for (ExceptionInfo exceptionInfo : exceptionTable)
                dos.write(exceptionInfo.toBytes());

            dos.writeShort(attributes.size());
            dos.write(attributes.toBytes());
        } catch (IOException e) {
            throw new Error(e);

            // unused
        }

        return baos.toByteArray();
    }

    public int getMaxStack()
    {
        return maxStack;
    }

    public void setMaxStack(@Nonnegative int maxStack)
    {
        this.maxStack = maxStack;
    }

    public int getMaxLocals()
    {
        return maxLocals;
    }

    public void setMaxLocals(@Nonnegative int maxLocals)
    {
        this.maxLocals = maxLocals;
    }

    public @Nonnull AttributePool getAttributes()
    {
        return attributes;
    }

    public void setAttributes(@Nonnull AttributePool attributes)
    {
        this.attributes = Objects.requireNonNull(attributes);
    }

    public @Nonnull byte[] getCode()
    {
        return code;
    }

    public void setCode(@Nonnull byte[] code)
    {
        this.code = Objects.requireNonNull(code);
    }

    public @Nonnull ExceptionInfo[] getExceptionTable()
    {
        return exceptionTable;
    }

    public void setExceptionTable(@Nonnull ExceptionInfo[] exceptionTable)
    {
        this.exceptionTable = Objects.requireNonNull(exceptionTable);
    }

    private int maxStack;

    private int maxLocals;

    private byte[] code;

    private ExceptionInfo[] exceptionTable;

    private AttributePool attributes = new AttributePool();

    public static final String NAME = "Code";

    public static class ExceptionInfo
    {
        public ExceptionInfo(@Nonnegative int startPC,
                             @Nonnegative int endPC,
                             @Nonnegative int handlerPC,
                             @Nonnull ConstantPool.TagRef catchType)
        {
            this.startPC = startPC;
            this.endPC = endPC;
            this.handlerPC = handlerPC;
            this.catchType = Objects.requireNonNull(catchType, "catchType");
        }

        public static @Nonnull ExceptionInfo from(@Nonnull ConstantPool constantPool,
                                                  @Nonnull ByteBuffer byteBuffer)
        {
            int startPC = byteBuffer.getShort() & 0xFFFF;
            int endPC = byteBuffer.getShort() & 0xFFFF;
            int handlerPC = byteBuffer.getShort() & 0xFFFF;
            int catchTypeTagRef = byteBuffer.getShort() & 0xFFFF;

            return new ExceptionInfo(startPC, endPC, handlerPC, constantPool.new TagRef(catchTypeTagRef));
        }

        public @Nonnull byte[] toBytes()
        {
            byte[] byts = new byte[8];

            BigEndian.pushU2(byts, startPC, 0);
            BigEndian.pushU2(byts, endPC, 2);
            BigEndian.pushU2(byts, handlerPC, 4);
            BigEndian.pushU2(byts, catchType.getRefIndex(), 6);

            return byts;
        }

        public int getStartPC()
        {
            return startPC;
        }

        public void setStartPC(int startPC)
        {
            this.startPC = startPC;
        }

        public int getEndPC()
        {
            return endPC;
        }

        public void setEndPC(int endPC)
        {
            this.endPC = endPC;
        }

        public int getHandlerPC()
        {
            return handlerPC;
        }

        public void setHandlerPC(int handlerPC)
        {
            this.handlerPC = handlerPC;
        }

        public @Nonnull ConstantPool.TagRef getCatchType()
        {
            return catchType;
        }

        public void setCatchType(@Nonnull ConstantPool.TagRef catchType)
        {
            this.catchType = Objects.requireNonNull(catchType);
        }

        private int startPC;

        private int endPC;

        private int handlerPC;

        private ConstantPool.TagRef catchType;
    }
}
