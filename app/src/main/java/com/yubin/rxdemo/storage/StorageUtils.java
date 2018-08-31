package com.yubin.rxdemo.storage;

import android.content.Context;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *     author : Haitao
 *     e-mail : xxx@xx
 *     time   : 2017/05/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */


public class StorageUtils {
    public static File getCacheDirectory(Context context) {
        return getCacheDirectory(context, true);
    }

    public static File getCacheDirectory(Context context, String cacheFolder) {
        boolean sdCardEnable = SDCardUtils.isSDCardEnable();
        return getCacheDirectory(context, cacheFolder, sdCardEnable);
    }

    public static File getCacheDirectory(Context context, String cacheFolder, boolean isMounted) {
        File file = null;
        if (isMounted) {
            file = getExternalCacheDir(context, cacheFolder);
        }
        if (file == null) {
            file = context.getCacheDir();
        }
        if (file == null) {
            String string = "/data/data/" + context.getPackageName() + "/cache/" + cacheFolder;
            Log.w("Can't define system cache directory! '%s' will be used.", string);
            file = new File(string);
        }
        return file;
    }

    public static File getCacheDirectory(Context context, boolean isMounted) {
        File file = null;
        if (isMounted) {
            file = getExternalCacheDir(context);
        }
        if (file == null) {
            file = context.getCacheDir();
        }
        if (file == null) {
            String string = "/data/data/" + context.getPackageName() + "/cache/";
            Log.w("Can't define system cache directory! '%s' will be used.", string);
            file = new File(string);
        }
        return file;
    }

    private static File getExternalCacheDir(Context context) {
        File file = new File(new File(new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data"), context.getPackageName()), "cache");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                file = null;
            } else {
                try {
                    new File(file, ".nomedia").createNewFile();
                    return file;
                } catch (IOException ex) {
                    return file;
                }
            }
        }
        return file;
    }

    private static File getExternalCacheDir(Context context, String cacheFolder) {
        context.getExternalCacheDir();
        File file = new File(new File(new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data"), context.getPackageName()), cacheFolder);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                file = null;
            } else {
                try {
                    new File(file, ".nomedia").createNewFile();
                    return file;
                } catch (IOException ex) {
                    return file;
                }
            }
        }
        return file;
    }

    public static File getIndividualCacheDirectory(Context context, String s) {
        File cacheDirectory = getCacheDirectory(context);
        File file = new File(cacheDirectory, s);
        if (!file.exists() && !file.mkdir()) {
            file = cacheDirectory;
        }
        return file;
    }

    public static File getOwnCacheDirectory(Context context, String s) {
        boolean equals = SDCardUtils.isSDCardEnable();
        File cacheDir = null;
        if (equals) {
            cacheDir = new File(Environment.getExternalStorageDirectory(), s);
        }
        if (cacheDir == null || (!cacheDir.exists() && !cacheDir.mkdirs())) {
            cacheDir = context.getCacheDir();
        }
        return cacheDir;
    }
}

