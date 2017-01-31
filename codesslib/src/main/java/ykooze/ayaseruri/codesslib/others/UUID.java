package ykooze.ayaseruri.codesslib.others;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by wufeiyang on 16/6/18.
 */
public class UUID {
    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";

    public static String id(Context context) {
        if (null == sID) {
            synchronized (UUID.class){
                if(null == sID){
                    File installation = new File(context.getFilesDir(), INSTALLATION);
                    try {
                        if (!installation.exists())
                            writeInstallationFile(installation);
                        sID = readInstallationFile(installation);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = java.util.UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}
