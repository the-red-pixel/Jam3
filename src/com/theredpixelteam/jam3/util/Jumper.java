package com.theredpixelteam.jam3.util;

public class Jumper {
    public boolean enabled()
    {
        return enabled;
    }

    public void disable()
    {
        enabled = false;
    }

    public void enable()
    {
        enabled = true;
    }

    private boolean enabled;
}
