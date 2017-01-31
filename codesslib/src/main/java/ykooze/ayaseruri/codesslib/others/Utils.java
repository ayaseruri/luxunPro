package ykooze.ayaseruri.codesslib.others;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Patterns;
import android.util.TypedValue;
import ykooze.ayaseruri.codesslib.io.FileUtils;

/**
 * Created by wufeiyang on 16/6/18.
 */
public class Utils {
    /**
     * 判断是否是合法的邮箱地址
     */
    public static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher mc = pattern.matcher(email);
        return mc.matches();
    }

    /**
     * 判断是否是合法的URL
     *
     * @param url
     * @return
     */
    public static boolean isValidURL(String url) {
        Pattern patterna = Patterns.WEB_URL;
        Matcher mca = patterna.matcher(url);
        return mca.matches();
    }

    /**
     * 校验身份证号码是否合法
     *
     * @param idCardNo
     * @return
     */
    public static boolean isValidIdCardNo(String idCardNo) {
        Pattern pattern = Pattern.compile("^((1[1-5])|(2[1-3])|(3[1-7])|(4[1-6])|(5[0-4])|" +
                "(6[1-5])|71|(8[12])|91)\\d{4}((19\\d{2}(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|" +
                "(19\\d{2}(0[13578]|1[02])31)|(19\\d{2}02(0[1-9]|1\\d|2[0-8]))|(19([13579][26]|" +
                "[2468][048]|0[48])0229))\\d{3}(\\d|X|x)?$");
        Matcher matcher = pattern.matcher(idCardNo);
        return matcher.matches();
    }

    /**
     * 判断是否是合法的手机号码
     *
     * @param phone
     * @return
     * @note 如果运营商发布新号段，需要更新该方法
     */
    public static boolean isValidMobilePhoneNumber(String phone) {
        Pattern pattern = Pattern
                .compile("^1[34578]\\d{9}$");
        Matcher mc = pattern.matcher(phone);
        return mc.matches();
    }

    /**
     * 获取app的版本数versionCode,比如38
     * @return
     */
    public static int getVerCode(Context context) {
        int result = 0;
        String packageName = context.getPackageName();
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            result = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError(e);
        }
        return result;
    }

    /**
     * 获取app的版本名versionName,比如0.6.9
     * @return
     */
    public static String getVerName(Context context) {
        String result = null;
        String packageName = context.getPackageName();
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            result = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError(e);
        }
        return result;
    }

    /**
     * 获取app的名称
     * @return
     */
    public static String getAppName(Context context) {
        String result = null;
        String packageName = context.getPackageName();
        ApplicationInfo applicationInfo;
        try {
            PackageManager packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            result = packageManager.getApplicationLabel(applicationInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取应用图标
     * @param context
     * @return
     */
    public static Drawable getAppIcon(Context context){
        String packageName = context.getPackageName();
        PackageManager packageManager;
        PackageInfo packageInfo;
        try {
            packageManager = context.getPackageManager();
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError(e);
        }
        return packageInfo.applicationInfo.loadIcon(packageManager);
    }

    /**
     * 删除应用数据： cache, file, share prefs, databases
     * @param context
     */
    public static void clear(Context context) {
        clearCache(context);
        clearFiles(context);
        clearSharedPreference(context);
        clearDatabase(context);
    }

    /**
     * 删除应用缓存目录
     * @param context
     */
    public static void clearCache(Context context) {
        FileUtils.delFile(context.getCacheDir(), true);
        FileUtils.delFile(context.getExternalCacheDir(), true);
    }

    /**
     * 删除应用文件目录
     * @param context
     */
    public static void clearFiles(Context context) {
        FileUtils.delFile(context.getFilesDir(), true);
    }

    /**
     * 删除应用Shared Prefrence目录
     * @param context
     */
    public static void clearSharedPreference(Context context) {
        FileUtils.delFile(new File("/data/data/" + context.getPackageName() + "/shared_prefs"), true);
    }

    /**
     * 删除应用数据库目录
     * @param context
     */
    public static void clearDatabase(Context context) {
        FileUtils.delFile(new File("/data/data/" + context.getPackageName() + "/databases"), true);
    }

    /**
     * 从color res id 转化为 color
     * @param context
     * @param color
     * @return
     */

    public static int getColor(Context context, int color){
        return ContextCompat.getColor(context, color);
    }

    /**
     * 获取cpu核心数
     * @return
     */
    public static int getNumberOfCores() {
        int num;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            num = Runtime.getRuntime().availableProcessors();
        } else {
            try {
                File dir = new File("/sys/devices/system/cpu/");
                File[] files = dir.listFiles(new CpuFilter());
                num = files.length;
            } catch(Exception e) {
                num = 1;
            }
        }
        return num;
    }

    private static class CpuFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            if(Pattern.matches("cpu[0-9]+", pathname.getName())) {
                return true;
            }
            return false;
        }
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取主题中toolbar高度
     * @param context
     * @return
     */
    public static int getThemeToolbarHeight(Context context){
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return 0;
    }

    /**
     * 重启应用
     * @param c
     */
    public static void doRestart(Context c) {
        if (c != null) {
            PackageManager pm = c.getPackageManager();
            if (pm != null) {
                Intent mStartActivity = pm.getLaunchIntentForPackage(
                        c.getPackageName()
                );
                if (mStartActivity != null) {
                    mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    int mPendingIntentId = 223344;
                    PendingIntent mPendingIntent = PendingIntent
                            .getActivity(c, mPendingIntentId, mStartActivity,
                                    PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                    System.exit(0);
                }
            }
        }
    }

    /**
     * 获取带透明度的颜色
     * @param alpha 【0，1】
     * @param baseColor
     * @return
     */
    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    /**
     * 打开app设置页面
     * @param context
     */
    public static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * 安装指定路径下的app
     * @param context
     * @param appPath
     */
    public static void installApp(Context context, String appPath){
        Uri uri = Uri.fromFile(new File(appPath)); //这里是APK路径
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void userLocalBorwer(Context context, String url){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
    }
}
