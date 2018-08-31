package com.yubin.rxdemo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.yubin.rxdemo.common.Constant;
import com.yubin.rxdemo.common.Utils;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MsgReceiver msgReceiver;
    private  MainActivity base;
    private Button mbutton;
    private Button miflyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        mbutton = (Button) findViewById(R.id.start_recognize);
        mbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(base,com.yubin.rxdemo.activity.RecognizeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        msgReceiver = new MsgReceiver();
        base = this;

        miflyButton = (Button) findViewById(R.id.iflytek_recognize);
        miflyButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(base,com.yubin.rxdemo.activity.IflyteckActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        checkPermission();
    }

    @Override
    public void onResume(){
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Utils.UPDATE);
        registerReceiver(msgReceiver,intentFilter);
//        Data d1 = new Data(1,"one");
//        Data d2 = new Data(2,"two");
//        List<Data> data = new ArrayList<Data>();
//        data.add(d1);
//        data.add(d2);
//        Observable.just(data)
//                 .subscribeOn(Schedulers.computation())
//                 .map(new Function<List<Data>, String>() {
//                     @Override
//                     public String apply(List<Data> data)throws Exception{
//                         Data d3  =new Data(1,"three");
//                         data.add(d3);
//                         StringBuilder sb = new StringBuilder();
//                         for(int i=0;i<data.size();i++){
//                             sb.append(data.get(i).getName());
//                         }
//                         return sb.toString();
//
//
//                     }
//                 })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                    }
//                });
//        Observable.just("one", "two", "three", "four", "five").subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        //text.setText(s);
//                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
//                    }
//                });

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class MsgReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(context,intent.getStringExtra("title")+intent.getStringExtra("content"),Toast.LENGTH_LONG).show();
        }
    }

    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (AndPermission.hasPermission(base,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                AndPermission.with(base)
                        .requestCode(Constant.REQUEST_PERMISSION)
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                        .callback(new PermissionListener() {
                            @Override
                            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                                if (AndPermission.hasPermission(base, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.RECORD_AUDIO,
                                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                                }
                            }

                            @Override
                            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                                if (AndPermission.hasPermission(base, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.RECORD_AUDIO,
                                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                                    return;
                                }
                                StringBuilder sb = new StringBuilder();
                                if (deniedPermissions != null && deniedPermissions.size() > 0) {
                                    for (String s : deniedPermissions) {
                                        if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(s)) {
                                            sb.append("读写手机存储");
                                        }
                                        if (Manifest.permission.RECORD_AUDIO.equals(s)) {
                                            sb.append("录制音频");
                                        }
                                        if (Manifest.permission.ACCESS_FINE_LOCATION.equals(s)) {
                                            sb.append("手机网络卫星定位");
                                        }

                                    }
                                }
                                AndPermission.defaultSettingDialog(base, Constant.REQUEST_PERMISSION)
                                        .setTitle("权限申请失败")
                                        .setMessage("您已禁用" + sb.toString() + " 权限，请在设置中授权！")
                                        .setPositiveButton("好，去设置")
                                        .show();
                            }
                        })
                        .rationale(new RationaleListener() {
                            @Override
                            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                AndPermission.rationaleDialog(base, rationale).show();
                            }
                        })
                        .start();
            }
        }
    }
}
