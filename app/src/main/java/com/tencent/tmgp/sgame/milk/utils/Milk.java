package com.tencent.tmgp.sgame.milk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Milk {

    public static String Post(String ur, String byteString) {
        String fh = "";
        try {
            URL url = new URL(ur);
            HttpURLConnection HttpURLConnection = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setReadTimeout(5000);
            HttpURLConnection.setRequestMethod("POST");
            OutputStream outputStream = HttpURLConnection.getOutputStream();

            outputStream.write(byteString.getBytes());
            BufferedReader BufferedReader = new BufferedReader(new InputStreamReader(HttpURLConnection.getInputStream()));
            String String = "";
            StringBuffer StringBuffer = new StringBuffer();
            while ((String = BufferedReader.readLine()) != null) {
                StringBuffer.append(String);
            }
            fh = StringBuffer.toString();


        } catch (IOException e) {
        }
        return fh;
    }

    public static String Post(String string) throws IOException {
        URL url = new URL(string);
        HttpURLConnection HttpURLConnection = (HttpURLConnection) url.openConnection();
        HttpURLConnection.setReadTimeout(5000);
        HttpURLConnection.setRequestMethod("POST");
        BufferedReader BufferedReader = new BufferedReader(new InputStreamReader(HttpURLConnection.getInputStream()));
        String String = "";
        StringBuffer StringBuffer = new StringBuffer();
        while ((String = BufferedReader.readLine()) != null) {
            StringBuffer.append(String);
        }
        return StringBuffer.toString();
    }

    public static boolean WriteAssets(Context context, String outPath, String fileName) {
        File file = new File(outPath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("--Method--", "copyAssetsSingleFile: cannot create directory.");
                return false;
            }
        }
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            File outFile = new File(file, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(outFile);
            // Transfer bytes from inputStream to fileOutputStream
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = inputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            inputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void ShellJni(Context context, int ZTSZ, String strshell) {
        if (ZTSZ == 1) {
            try {
                Runtime.getRuntime().exec("chmod 777 " + context.getFilesDir() + strshell, null, null);
                Runtime.getRuntime().exec("su -c " + context.getFilesDir() + strshell, null, null);
                Runtime.getRuntime().exec(context.getFilesDir() + strshell, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ZTSZ == 2) {
            try {
                Runtime.getRuntime().exec("chmod 777 " + context.getFilesDir() + strshell, null, null);
                Runtime.getRuntime().exec(context.getFilesDir() + strshell, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MyToast.showLong(context, "??????" + ZTSZ);
        }

    }

    public static String WriteData(File file) {
        String content = "";
        if (!file.isDirectory()) {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader
                        = new InputStreamReader(instream, StandardCharsets.UTF_8);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line = "";
                    while ((line = buffreader.readLine()) != null) {
                        content += line;
                    }
                    instream.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public static Bitmap getBitmapFromPath(String path) {
        if (!new File(path).exists()) {
            System.err.println("Error ???????????????");
            return null;
        }
        byte[] buf = new byte[1024 * 1024];
        Bitmap bitmap = null;
        try {
            FileInputStream fis = new FileInputStream(path);
            int len = fis.read(buf, 0, buf.length);
            bitmap = BitmapFactory.decodeByteArray(buf, 0, len);
            int bw = bitmap.getWidth();
            int bh = bitmap.getHeight();
            float scale = Math.min(1f * 43 / bw, 1f * 43 / bh);
            bitmap = scaleBitmap(bitmap, scale);
            if (bitmap == null) {
                System.out.println("len= " + len);
                System.err.println("??????: " + path + "  ????????????!!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return bitmap;
    }

    public static Bitmap scaleBitmap(Bitmap origin, float scale) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    public static int AdaptationPermissions(int Permissions) {
        boolean Milk1 = false;
        boolean Milk2 = false;
        boolean Milk3 = true;
        boolean Milk4 = false;
        if (!Milk1) {
            Permissions |= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        } else {
            Permissions &= ~WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        }
        if (!Milk2) {
            Permissions |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        } else {
            Permissions &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }
        if (!Milk3) {
            Permissions |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        } else {
            Permissions &= ~WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        }
        if (!Milk4) {
            Permissions |= WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        } else {
            Permissions &= ~WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        }

        return Permissions;
    }

    public static Boolean CopyAssets(Context context, String oldPath, String newPath) {
        boolean jyztshow=false;
        try {
            String fileNames[] = context.getAssets().list(oldPath);// ??????assets????????????????????????????????????
            if (fileNames.length > 0) {// ???????????????
                File file = new File(newPath);
                file.mkdirs();// ????????????????????????????????????
                for (String fileName : fileNames) {
                    CopyAssets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {// ???????????????
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// ????????????????????????
                    fos.write(buffer, 0, byteCount);// ???????????????????????????????????????

                }
                jyztshow = true;
                fos.flush();// ???????????????
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jyztshow;
    }

    public static boolean isHorizontalScreen(Context context) {
        int angle = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        Log.e("Milk", "??????????????? Angle ?????? " + angle);
        if (angle == Surface.ROTATION_90 || angle == Surface.ROTATION_270) {       
            //??????????????????90????????270????????????????????????????????????????????
            return true;
        }
        return false;
    }

    /*
     *???????????????????????????
     */
    public static boolean isNet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /*
     * ???????????? ????????????????????????
     * */
    public static boolean isWifiProxy(Context context) {
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    public static boolean iswifivpzt(Context context) {
        Boolean qpd = false;
        if (isNet(context)) {
            if (isWifiProxy(context)) {
                return qpd = false;
            } else {
                return qpd = true;
            }
        } else {
            return qpd = false;
        }
    }


    /**
     * ????????????Bitmap
     *
     * @param filePath      ????????????
     * @param requestWidth  ????????????
     * @param requestHeight ????????????
     */
    public static Bitmap decodeSampleBitmap(String filePath, int requestWidth, int requestHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //?????????????????????????????????????????????????????????????????????
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        //???????????????inSampleSize
        options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHeight);
        //??????????????????????????????????????????
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }


    /**
     * ???????????????
     *
     * @param options   ????????????
     * @param reqWidth  ????????????
     * @param reqHeight ????????????
     * @return ?????????
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            //????????????????????????????????????????????????
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            //???????????????????????????????????????inSampleSize
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    

}
