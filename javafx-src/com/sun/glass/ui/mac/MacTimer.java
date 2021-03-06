/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Timer;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * MacOSX platform implementation class for Timer.
 */
final class MacTimer extends Timer {

    static {
        minPeriod = _getMinPeriod();
        maxPeriod = _getMaxPeriod();
        _initIDs();
    }

    private static final int minPeriod, maxPeriod;

    protected MacTimer(Runnable runnable) {
        super(runnable);
    }

    native private static int _getMinPeriod();
    native private static int _getMaxPeriod();

    static int getMinPeriod_impl() {
        return minPeriod;
    }

    static int getMaxPeriod_impl() {
        return maxPeriod;
    }
    
    @Override native protected long _start(Runnable runnable);
    @Override native protected long _start(Runnable runnable, int period);
    @Override native protected void _stop(long timer);
    native private static void _initIDs();
}

