package com.winnie.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * @author : winnie
 * @date : 2018/10/24
 * @desc 屏幕宽高获取方法
 * <p>
 * getSize/getMetrics获取到的是App逻辑显示尺寸，而getRealMetrics/getRealSize获取到的是实际屏幕显示尺寸。
 * getSize/getMetrics返回的尺寸，会除去那些一直显示的系统装饰元素，例如虚拟导航栏
 * getSize/getMetrics返回的尺寸，为了兼容那些为更小显示尺寸的旧应用，会被等比例缩放至合适的值。
 * <p>
 * getResources()用在有context的地方，没有context的地方和静态类中是不能用的（也有开发者通过一些方式对context进行封装用在静态类中）
 * 而且getResources()只能用于获取应用本身的资源
 * Resources.getSystem() 可以在任何地方进行使用，但是有一个局限，只能获取系统本身的资源
 */
public class ScreenUtils {
    /**
     * 通过Resources获取屏幕宽度
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * 通过Resources获取屏幕高度
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * 通过WindowManager获取屏幕宽度像素
     */
    public static int getScreenWidthPx(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getSize(point);
        } else {
            display.getRealSize(point);
        }
        return point.x;
    }

    /**
     * 通过WindowManager获取屏幕高度像素
     */
    public static int getScreenHeightPx(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getSize(point);
        } else {
            display.getRealSize(point);
        }
        return point.y;
    }

    /**
     * 通过context.getResources()获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 通过context.getResources()获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 通过activity.getWindowManager()获取屏幕宽度
     */
    public static int getScreenWidthPx(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = activity.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getMetrics(metrics);
        } else {
            display.getRealMetrics(metrics);
        }
        return metrics.widthPixels;
    }

    /**
     * 通过activity.getWindowManager()获取屏幕高度
     */
    public static int getScreenHeightPx(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = activity.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getMetrics(metrics);
        } else {
            display.getRealMetrics(metrics);
        }
        return metrics.heightPixels;
    }

    /**
     * 通过activity.getWindowManager()获取屏幕宽度dp
     */
    public static int getScreenWidthDp(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = activity.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getMetrics(metrics);
        } else {
            display.getRealMetrics(metrics);
        }
        // 屏幕宽度（像素）
        int width = metrics.widthPixels;
        // 屏幕密度（0.75 / 1.0 / 1.5）
        float density = metrics.density;
        // 屏幕密度dpi（120 / 160 / 240）
        int densityDpi = metrics.densityDpi;
        return width / densityDpi;
    }

    /**
     * 通过activity.getWindowManager()获取屏幕高度dp
     */
    public static int getScreenHeightDp(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = activity.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getMetrics(metrics);
        } else {
            display.getRealMetrics(metrics);
        }
        // 屏幕高度（像素）
        int height = metrics.heightPixels;
        // 屏幕密度（0.75 / 1.0 / 1.5）
        float density = metrics.density;
        // 屏幕密度dpi（120 / 160 / 240）
        int densityDpi = metrics.densityDpi;
        return height / densityDpi;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            return resources.getDimensionPixelSize(resId);
        }
        return dp2px(25);
    }

    /**
     * 获取状态栏高度
     */
    public static int getStateBar2() {
        Class c;
        int statusBarHeight = 0;
        Resources resources = Resources.getSystem();
        try {
            c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = resources.getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取虚拟按钮高度
     */
    public static int getNavigationBarHeight() {
        Resources resources = Resources.getSystem();
        int navigationBarHeight = 0;
        int id = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * dp转换成px
     * +0.5的作用是为了适用于程序中的四舍五入
     * 例如5.6f在java中转换成int类型的话是5，所以加上0.5从而变成6，提高了精度。
     */
    public static int dp2px(float dpValue) {
        Resources resources = Resources.getSystem();
        float scale = resources.getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转换成dp
     */
    public static int px2dp(float pxValue) {
        Resources resources = Resources.getSystem();
        float scale = resources.getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转换成px
     */
    public static int sp2px(float spValue) {
        Resources resources = Resources.getSystem();
        float fontScale = resources.getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转换成sp
     */
    public static int px2sp(float pxValue) {
        Resources resources = Resources.getSystem();
        float fontScale = resources.getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(Context context, int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, context.getResources().getDisplayMetrics());
    }
}
