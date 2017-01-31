package ykooze.ayaseruri.codesslib.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wufeiyang on 16/6/16.
 * 对于原始的bitmap需要自己手动recycle！！！
 */
public class BitmapUtils {
    private static Bitmap.Config sInPreferredConfig = Bitmap.Config.RGB_565;

    //原比例尺寸获得bitmap
    public static Bitmap getBitmap(String imgPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPreferredConfig = sInPreferredConfig;
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }

    //仅获取图片宽度
    public static int getWidth(String imgPath){
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = sInPreferredConfig;
        return BitmapFactory.decodeFile(imgPath, newOpts).getWidth();
    }

    //仅获取图片高度
    public static int getHeight(String imgPath){
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = sInPreferredConfig;
        return BitmapFactory.decodeFile(imgPath, newOpts).getHeight();
    }

    //保证最长的边不超过制定的最大长度
    public static Bitmap getBitmap(String imgPath, int maxLenth) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = sInPreferredConfig;

        BitmapFactory.decodeFile(imgPath, newOpts);

        int orgWidth = newOpts.outWidth;
        int orgHeight = newOpts.outHeight;

        newOpts.inSampleSize = Math.max(orgWidth/maxLenth, orgHeight/maxLenth);
        newOpts.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(imgPath, newOpts);
    }

    //当format为 png 的时候 quality 参数会自动被忽略
    public static void storeBitmap(Bitmap bitmap, Bitmap.CompressFormat format, int quality, String outPath, String name){
        try {
            File file = new File(outPath + "/" + name);
            if(!file.exists()){
                if(!file.createNewFile()){
                    throw new IOException("can not create bitmap file!");
                }
            }

            FileOutputStream os = new FileOutputStream(file);
            bitmap.compress(format, quality, os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap scale(Bitmap bitmap, int pixelW, int pixelH) {
        return Bitmap.createScaledBitmap(bitmap, pixelW, pixelH, true);
    }

    public static Bitmap.Config getInPreferredConfig() {
        return sInPreferredConfig;
    }

    public static void setInPreferredConfig(Bitmap.Config inPreferredConfig) {
        sInPreferredConfig = inPreferredConfig;
    }
}
