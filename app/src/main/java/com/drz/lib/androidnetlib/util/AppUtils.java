package com.drz.lib.androidnetlib.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by drz on 2016/5/29.
 */
public class AppUtils {
    /**
     * 判断手机是否有SD卡。
     *
     * @return 有SD卡返回true，没有返回false。
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获得系统相册路径
     *
     * @return
     */
    public static String getPhotoPath() {
        return Environment.getExternalStorageDirectory() + "/DCIM/";
    }

    public static String getAppImageCachePath(Context context) {
        return new File(context.getCacheDir(), "images").getAbsolutePath();
    }

    public static String getAppCacheDirPath(Context context) {
//        Environment.getDownloadCacheDirectory();
        File cacheDir = context.getCacheDir();
        return cacheDir.getAbsolutePath();

    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File getAppCacheDir(Context context) {
        return context.getCacheDir();
    }


    private static final int DEVICEINFO_UNKNOWN = -1;

    /**
     * 获取DPI
     *
     * @param context
     * @return
     */
    public static float getDpi(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);

        float density = metric.density;  // 密度（0.75 / 1.0 / 1.5）
        return metric.density;

    }

    /**
     * 获取屏幕DPI
     *
     * @param context
     * @return
     */
    public static float getDensity(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int densityDpi = metric.densityDpi;  // 密度DPI（120 / 160 / 240）
        return metric.densityDpi;
    }

    /**
     * 获取屏幕的宽度
     *
     * @param activity
     * @return
     */
    public static int getDeviceWidth(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = activity.getWindowManager();
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;

    }

    /**
     * 获取器屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int getDeviceHeight(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = activity.getWindowManager();
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;

    }

    /**
     * 获取statusBar的高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    /**
     * 获取应用堆内存分配大小
     *
     * @param context
     * @return
     */
    public static int getHeapSize(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int heapSize = manager.getMemoryClass();
        Log.d("debug", getApplicationInfo(context).packageName + ":heapSize=" + heapSize);
        return heapSize;
    }

    /**
     * 获取应用的包名，版本等信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getApplicationInfo(Context context) {
        PackageInfo info = null;
        String packageNames = null;
        String versionName = null;
        int versionCode = -1;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            // 当前应用的版本名称
            versionName = info.versionName;
            // 当前版本的版本号
            versionCode = info.versionCode;
            // 当前版本的包名
            packageNames = info.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("debug", packageNames + ":versionName=" + versionName + "versionCode=" + versionCode);
        return info;
    }

    /**
     * 获取cpu核心数
     * Returns the number of processor cores available to the VM, at least 1
     *
     * @return
     */
    public static int getCpuCoreNumber() {
        int processors = Runtime.getRuntime().availableProcessors();
        Log.d("debug", "- - -CPU 核心数：" + processors);
        return processors;
    }

    /**
     * 判断是否存在SD卡
     *
     * @return
     */
    public static boolean hasSDcard() {
        // 判断SDcard状态
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 判断存储空间是否足够
     *
     * @param minSize
     * @return
     */
    public static boolean spaceEnough(long minSize) {
        File SDCardRoot = Environment.getExternalStorageDirectory();
        if (SDCardRoot.getFreeSpace() < minSize) {
            // 弹出对话框提示用户空间不够
            Log.e("Utils", "存储空间不够");
            return false;
        }
        return true;
    }

    /**
     * 获取CPU核心数
     *
     * @return
     */
    public static int getNumberOfCPUCores() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            // Gingerbread doesn't support giving a single application access to both cores, but a
            // handful of devices (Atrix 4G and Droid X2 for example) were released with a dual-core
            // chipset and Gingerbread; that can let an app in the background run without impacting
            // the foreground application. But for our purposes, it makes them single core.
            return 1;
        }
        int cores;
        try {
            cores = new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
        } catch (SecurityException e) {
            cores = DEVICEINFO_UNKNOWN;
        } catch (NullPointerException e) {
            cores = DEVICEINFO_UNKNOWN;
        }
        return cores;
    }

    private static final FileFilter CPU_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getName();
            //regex is slow, so checking char by char.
            if (path.startsWith("cpu")) {
                for (int i = 3; i < path.length(); i++) {
                    if (path.charAt(i) < '0' || path.charAt(i) > '9') {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    };

    public static void changeViewSize(ImageView view, int width, int height) {
        view.setMinimumWidth(width);
        view.setMinimumHeight(height);

    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断移动网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isMobleNetConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] allNetworks = manager.getAllNetworks();
            for (Network network :
                    allNetworks) {
                NetworkInfo networkInfo = manager.getNetworkInfo(network);
                int type = networkInfo.getType();
                if (ConnectivityManager.TYPE_MOBILE == type) {
                    return networkInfo.isConnected();
                }
            }
        } else {
//            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//            if (networkInfo.isConnected()) {
//                return true;
//            }
        }

        return false;
    }

    /**
     * 判断Wifi是否连接
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] allNetworks = connectivityManager.getAllNetworks();
            for (Network network :
                    allNetworks) {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                int type = networkInfo.getType();
                if (ConnectivityManager.TYPE_WIFI == type) {
                    return networkInfo.isConnected();
                }
            }
        } else {
            NetworkInfo wifiNetworkInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetworkInfo.isConnected()) {
                return true;
            }
        }

        return false;
    }
    /*
     * 判断是否安装目标应用
	 *
	 * @param packageName 目标应用安装后的包名
	 *
	 * @return 是否已安装目标应用
	 */

    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 检查APP权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 获取设备信息
     *
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
