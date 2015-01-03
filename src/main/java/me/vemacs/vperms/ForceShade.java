package me.vemacs.vperms;

import org.apache.commons.pool2.impl.DefaultEvictionPolicy;

@SuppressWarnings("unused")
public class ForceShade {
    static {
        Class<?>[] classes = new Class<?>[]{
                DefaultEvictionPolicy.class,
        };
    }
}
