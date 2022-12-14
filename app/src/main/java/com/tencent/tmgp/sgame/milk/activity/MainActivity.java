package com.tencent.tmgp.sgame.milk.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.tmgp.sgame.milk.R;
import com.tencent.tmgp.sgame.milk.adapter.MyDrawingAdapter;
import com.tencent.tmgp.sgame.milk.adapter.MyDrawingsAdapter;
import com.tencent.tmgp.sgame.milk.adapter.MyFwViewPagerAdapter;
import com.tencent.tmgp.sgame.milk.adapter.MySettingsAdapter;
import com.tencent.tmgp.sgame.milk.draw.Drawing;
import com.tencent.tmgp.sgame.milk.utils.DocumentTool;
import com.tencent.tmgp.sgame.milk.utils.Milk;
import com.tencent.tmgp.sgame.milk.utils.MyToast;
import com.tencent.tmgp.sgame.milk.utils.NativeUtils;
import com.tencent.tmgp.sgame.milk.utils.SPUtils;
import com.tencent.tmgp.sgame.milk.utils.zip.CompressStatus;
import com.tencent.tmgp.sgame.milk.utils.zip.ZipUtil;
import com.tencent.tmgp.sgame.milk.view.MyLineGridView;
import com.tencent.tmgp.sgame.milk.view.MyMilkViewPager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String WJLJ = Environment.getExternalStorageDirectory().toString();
    private final String appname = "?????????";
    private final Handler handler_JD = new Handler() {
        public void handleMessage(Message msg) {
            String LOGRZ = "??????";
            switch (msg.what) {
                case CompressStatus.START:
                    Log.i(LOGRZ, "????????????...");
                    break;
                case CompressStatus.HANDLING:
                    Bundle b = msg.getData();
                    Log.i(LOGRZ, "????????????:" + b.getInt(CompressStatus.PERCENT) + "%");
                    break;
                case CompressStatus.COMPLETED:
                    Log.i(LOGRZ, "????????????!");
                    break;
                case CompressStatus.ERROR:
                    Log.i(LOGRZ, "????????????!");
                    break;
            }
        }

    };
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private View milk, drawing, settings;
    private LinearLayout xfc_linear;
    private ImageView xfc_icon;
    private TextView textview_drawing, textview_settings, textview_xfcsx;
    private MyMilkViewPager viewPager;
    private boolean show;
    private String GONGGAO = NativeUtils.GETGONGGAO(), GENGZ;
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setIcon(R.mipmap.ic_launcher)
                            .setCancelable(false)
                            .setTitle(appname + "????????????:")
                            .setMessage(GONGGAO)
                            .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }
                            );
                    dialog.show();
                    break;
                case 2:
                    if (GENGZ.equals("1")) {
                        Toast.makeText(MainActivity.this, "?????????", Toast.LENGTH_SHORT).show();

                    } else if (GENGZ.equals("0")) {
                        Uri uri = Uri.parse("https://wwr.lanzoui.com/b02ccyg9g");
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.setData(uri);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "???????????? [123]", Toast.LENGTH_SHORT).show();
                        android.os.Process.killProcess(android.os.Process.myPid());

                    } else {
                        Toast.makeText(MainActivity.this, GENGZ, Toast.LENGTH_SHORT).show();

                    }
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_it1:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setTitle(appname + "????????????:")
                        .setMessage(GONGGAO)
                        .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }
                        );
                dialog.show();
                break;
            case R.id.main_it2://????????????
                Uri uri = Uri.parse("mailto:3636243192@qq.com");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                //intent.putExtra(Intent.EXTRA_CC, email);
                intent.putExtra(Intent.EXTRA_SUBJECT, "??????BUG??????");
                intent.putExtra(Intent.EXTRA_TEXT, "??????????????????");
                startActivity(Intent.createChooser(intent, "??????????????????????????????"));
                break;
            case R.id.main_it3://????????????
                String lxqq = "3636243192";
                String urlQQ = "mqqwpa://im/chat?chat_type=wpa&uin=" + lxqq + "&version=1";
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlQQ)));
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("?????????");
        setContentView(R.layout.activity_main);
        initui();
        if (Milk.iswifivpzt(this)) {
            DocumentTool.verifyStoragePermissions(MainActivity.this);

            try {
                Runtime.getRuntime().exec("su", null, null);
            } catch (IOException e) {

            }
            new Thread(new MainT()) {
            }.start();

        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setIcon(R.mipmap.ic_launcher)
                    .setTitle(appname + "????????????:")
                    .setMessage("???????????????????????????????????????")
                    .setCancelable(false)
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dia, int which) {
                            dia.dismiss();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                    .create();
            dialog.show();
        }

    }

    private void initui() {
        TextView textview1 = findViewById(R.id.activitymainTextView1);
        textview1.setText(NativeUtils.stringFromJNI());
        Button button1 = findViewById(R.id.activitymainButton1);
        button1.setOnClickListener(this);
        Button button2 = findViewById(R.id.activitymainButton2);
        button2.setOnClickListener(this);

        MyToast.showLong(getApplicationContext(), NativeUtils.stringFromJNI());

        //??????assets??????
        Milk.WriteAssets(MainActivity.this, getFilesDir() + "/assets/", "mytemp.so");

        //???????????????????????????
        Boolean JYshow01 = Milk.CopyAssets(this, "icon.zip", WJLJ + "/Download/icon.zip");
        if (JYshow01) {
            String zipFilePath;
            zipFilePath = WJLJ + "/Download/icon.zip";
            File zipFile = new File(zipFilePath);
            try {
                ZipUtil.unZipFileWithProgress(zipFile, WJLJ + "/Download/", handler_JD, false);

            } catch (net.lingala.zip4j.exception.ZipException e) {
            }
        } else {
            MyToast.showLong(MainActivity.this, "????????????-6");
        }

        for (int i = 0; i <= 536; i++) {
            Drawing.bitmaptx[i] = Milk.getBitmapFromPath("/storage/emulated/0/Download/icon/" + i + ".png");

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activitymainButton1://??????
                if (show) {
                    MyToast.showLong(MainActivity.this, "??????????????????");
                } else {
                    //?????????????????????
                    if (!Settings.canDrawOverlays(MainActivity.this)) {
                        startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
                    }
                    ShowFloatingWindow();

                }
                break;
            case R.id.activitymainButton2://????????????
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setIcon(R.mipmap.ic_launcher)
                        .setTitle(appname + "????????????:")
                        .setMessage("??????????????????!")
                        .setCancelable(false)
                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                MyToast.showLong(MainActivity.this, "(????????????)?????????????????????????????????");

                                dia.dismiss();
                            }
                        })
                        .setPositiveButton("??????", null)
                        .create();
                dialog.show();
                break;
            default:
                break;
        }
    }

    private void ShowFloatingWindow() {
        Drawing.px = this.getResources().getDisplayMetrics().widthPixels;
        Drawing.py = this.getResources().getDisplayMetrics().heightPixels;
        Drawing.?????????????????? = SPUtils.getParam(this, "??????????????????", 0).hashCode();
        Drawing.?????????????????? = SPUtils.getParam(this, "??????????????????", 0).hashCode();
        Drawing.??????????????? = SPUtils.getParam(this, "???????????????", 0).hashCode();
        Drawing.??????????????? = SPUtils.getParam(this, "???????????????", 0).hashCode();

        SxFloatingWindow(-2, -2, 0, 0);
        milk = View.inflate(getApplicationContext(), R.layout.xfc_floatwindow, null);
        windowManager.addView(milk, layoutParams);
        show = true;
        xfc_linear = milk.findViewById(R.id.xfc_fw_linear);
        xfc_linear.setVisibility(View.GONE);
        xfc_icon = milk.findViewById(R.id.xfc_fw_icon);
        xfc_icon.setOnTouchListener(TheWindowMoves(milk, windowManager, layoutParams));
        viewPager = milk.findViewById(R.id.xfc_fw_viewpager_1);
        viewPager.setSliding(false);//??????????????????
        viewPager.setAnimation(false);//????????????
        textview_drawing = milk.findViewById(R.id.xfc_fw_textview_drawing);
        textview_settings = milk.findViewById(R.id.xfc_fw_textview_settings);
        textview_xfcsx = milk.findViewById(R.id.xfc_fw_textview_xfcsx);
        textview_drawing.setTextColor(0xFF00ACFF);
        textview_drawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
                textview_settings.setTextColor(0xFF737373);
                textview_xfcsx.setTextColor(0xFF737373);
                textview_drawing.setTextColor(0xFF00ACFF);
            }
        });
        textview_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                textview_drawing.setTextColor(0xFF737373);
                textview_xfcsx.setTextColor(0xFF737373);
                textview_settings.setTextColor(0xFF00ACFF);
            }
        });
        textview_xfcsx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xfc_linear.setVisibility(View.GONE);
                xfc_icon.setVisibility(View.VISIBLE);

            }
        });

        drawing = View.inflate(getApplicationContext(), R.layout.drawing_floatingwindow, null);
        settings = View.inflate(getApplicationContext(), R.layout.settings_floatingwindow, null);
        ArrayList<View> views = new ArrayList<View>();
        views.add(drawing);
        views.add(settings);
        MyFwViewPagerAdapter myfwViewPagerAdapter = new MyFwViewPagerAdapter();
        myfwViewPagerAdapter.setViews(views);
        viewPager.setAdapter(myfwViewPagerAdapter);
        //??????gridview
        GridView gridview = drawing.findViewById(R.id.drawing_fw_gridview_1);
        gridview.setAdapter(new MyDrawingAdapter(this));
        //?????????????????????gridview
        MyLineGridView gridviews = drawing.findViewById(R.id.drawing_fw_gridview_2);
        gridviews.setAdapter(new MyDrawingsAdapter(this));
        //????????????list
        ListView listview = settings.findViewById(R.id.settings_fw_listview_1);
        listview.setAdapter(new MySettingsAdapter(this));

    }

    private void SxFloatingWindow(int width, int height, int x, int y) {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            layoutParams.flags = Milk.AdaptationPermissions(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.x = x;
        layoutParams.y = y;

    }

    public View.OnTouchListener TheWindowMoves(final View v, final WindowManager manager, final WindowManager.LayoutParams params) {
        return new View.OnTouchListener() {
            int x;
            int y;
            int _x;
            int _y;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View p1, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        _x = params.x;
                        _y = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int nowX = (int) event.getRawX();
                        int nowY = (int) event.getRawY();
                        int movedX = nowX - x;
                        int movedY = nowY - y;
                        x = nowX;
                        y = nowY;
                        params.x = params.x + movedX;
                        params.y = params.y + movedY;
                        manager.updateViewLayout(v, params);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(params.x - _x) < 5.0 && Math.abs(params.y - _y) < 5.0) {
                            xfc_icon.setVisibility(View.GONE);
                            xfc_linear.setVisibility(View.VISIBLE);

                        }
                        break;
                }
                return true;
            }
        };
    }

    class MainT implements Runnable {
        @Override
        public void run() {
            try {
                GONGGAO = Milk.Post(NativeUtils.GETGONGGAOAPI());
                GONGGAO = GONGGAO.replace('@', '\n');
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            } catch (IOException e) {
            }

            String ver = NativeUtils.GETGENGXIN();
            GENGZ = Milk.Post(NativeUtils.GETGENGXINAPI(), "ver=" + ver);
            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);

        }
    }

}
