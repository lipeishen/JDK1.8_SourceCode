/*
 * Copyright (c) 2010, 2014, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.media.jfxmediaimpl.platform.osx;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.platform.Platform;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Mac OS X Platform implementation. This class implements both the QTKit based
 * platform and the AVFoundation based platforms.
 * 
 * NOTE: The QTKit based platform is deprecated and will be removed in a future
 * release.
 */
public final class OSXPlatform extends Platform {
    /**
     * The MIME types of all supported media.
     */
    private static final String[] CONTENT_TYPES = {
        "audio/x-aiff",
        "audio/mp3",
        "audio/mpeg",
        "audio/x-m4a",
        "video/mp4",
        "video/x-m4v",
        "application/vnd.apple.mpegurl",
        "audio/mpegurl"
    };

    private static final class OSXPlatformInitializer {
        private static final OSXPlatform globalInstance;
        static {
            // Platform is only available if we can load it's native lib
            // Do this early so we can report the correct content types
            boolean isLoaded = false;
            try {
                isLoaded = AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> {
                    boolean avf = false;
                    boolean qtk = false;
                    // attempt to load the AVFoundation based player first
                    // AVFoundation will have precedence
                    try {
                        NativeLibLoader.loadLibrary("jfxmedia_avf");
                        avf = true;
                    } catch (UnsatisfiedLinkError ule) {}
                    try {
                        NativeLibLoader.loadLibrary("jfxmedia_qtkit");
                        qtk = true;
                    } catch (UnsatisfiedLinkError ule) {}

                    return avf || qtk;
                });
            } catch (Exception e) {
                // Ignore
            }
            if (isLoaded) {
                globalInstance = new OSXPlatform();
            } else {
                globalInstance = null;
            }
        }
    }

    public static Platform getPlatformInstance() {
        return OSXPlatformInitializer.globalInstance;
    }

    private OSXPlatform() {
    }

    /**
     * @return false if the platform cannot be loaded
     */
    @Override
    public boolean loadPlatform() {
        if (!HostUtils.isMacOSX()) {
            return false;
        }

        // ULE should not happen here, but just in case
        try {
            return osxPlatformInit();
        } catch (UnsatisfiedLinkError ule) {
            if (Logger.canLog(Logger.DEBUG)) {
                Logger.logMsg(Logger.DEBUG, "Unable to load OSX platform.");
            }
//            MediaUtils.nativeError(OSXPlatform.class, MediaError.ERROR_MANAGER_ENGINEINIT_FAIL);
            return false;
        }
    }

    @Override
    public String[] getSupportedContentTypes() {
        String[] contentTypesCopy = new String[CONTENT_TYPES.length];
        System.arraycopy(CONTENT_TYPES, 0, contentTypesCopy, 0, CONTENT_TYPES.length);
        return contentTypesCopy;
    }

    @Override
    public Media createMedia(Locator source) {
        return new OSXMedia(source);
    }

    @Override
    public MediaPlayer createMediaPlayer(Locator source) {
        try {
            return new OSXMediaPlayer(source);
        } catch (Exception ex) {
            if (Logger.canLog(Logger.DEBUG)) {
                Logger.logMsg(Logger.DEBUG, "OSXPlatform caught exception while creating media player: "+ex);
                ex.printStackTrace();
            }
        }
        return null;
    }

    private static native boolean osxPlatformInit();
}
