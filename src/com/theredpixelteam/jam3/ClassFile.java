package com.theredpixelteam.jam3;

import com.theredpixelteam.jam3.attribute.AttributePool;
import com.theredpixelteam.jam3.constant.ConstantPool;

public class ClassFile {
    private short minorVersion;

    private short majorVersion;

    private short modifiers;

    private ConstantPool constantPool;

    private AttributePool attributePool;
}
