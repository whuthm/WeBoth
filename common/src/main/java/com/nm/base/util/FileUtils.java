package com.nm.base.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class FileUtils {
    
    public static final String TAG = "FileUtils";
    
    public static final String UTF_8 = "utf-8";
    
    private FileUtils() {}
    
    public static boolean createNewFile(File file) {
        if (file != null) {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return file.exists();
        }
        return false;
    }
    
    /**
     * 清空文件
     */
    public static void clearFile(File file) {
        if (file != null) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void writeFile(File file, String value) {
        writeFile(file.getPath(), value);
    }
    
    public static void writeFile(String fileName, String value) {
        try {
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(fileName), UTF_8));
            bw.append(value);
            bw.close();
        } catch (IOException e) {
            Log.w(TAG, fileName + " failed to write!! ");
            e.printStackTrace();
        }
    }
    
    public static String readFileAsString(File file) {
        StringBuilder sb = new StringBuilder("");
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, UTF_8);
            br = new BufferedReader(isr);
            String line = br.readLine();
            int index = 0;
            while (line != null) {
                if (index > 0) {
                    sb.append("\n");
                }
                else {
                    index = 1;
                }
                sb.append(line);
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
    
    public static String readFileAsString(String filePath) {
        return readFileAsString(new File(filePath));
    }
    
    public static File getFileCacheDir(Context context) {
        return getFileCacheDir(context, "");
    }
    
    public static File getFileCacheDir(Context context, String dirName) {
        if (TextUtils.isEmpty(dirName)) {
            dirName = "default";
        }
        File dir = new File(getRootCacheFile(context).getPath() + "/" + dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        return dir;
    }
    
    public static File getRootCacheFile(Context context) {
        if (isSDCardExists()) {
            return context.getExternalCacheDir();
        }
        else {
            return context.getCacheDir();
        }
    }
    
    public static boolean isSDCardExists() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }
    
}
