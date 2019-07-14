package com.theredpixelteam.jam3;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ClassSnapshot {
    public ClassSnapshot(int minorVersion,
                         int majorVersion,
                         int modifier,
                         @Nonnull String className,
                         @Nonnull String superClassName,
                         @Nonnull String[] interfaceNames)
    {
        this.minorVersion = minorVersion;
        this.majorVersion = majorVersion;
        this.modifier = modifier;
        this.className = Objects.requireNonNull(className, "className");
        this.superClassName = Objects.requireNonNull(superClassName, "superClassName");
        this.interfaceNames = Objects.requireNonNull(interfaceNames, "interfaceNames");
    }

    public int getMinorVersion()
    {
        return minorVersion;
    }

    public int getMajorVersion()
    {
        return majorVersion;
    }

    public int getModifier()
    {
        return modifier;
    }

    public @Nonnull String getClassName()
    {
        return className;
    }

    public @Nonnull String getSuperClassName()
    {
        return superClassName;
    }

    public @Nonnull String[] getInterfaceNames()
    {
        return interfaceNames;
    }

    private final int minorVersion;

    private final int majorVersion;

    private final int modifier;

    private final String className;

    private final String superClassName;

    private final String[] interfaceNames;
}
