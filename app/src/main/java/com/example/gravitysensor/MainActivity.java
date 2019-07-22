package com.example.gravitysensor;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * @author Created by LittleGreens
 * @data on 2019/7/19
 * @describe TODO
 */
public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private static final String TAG = "MainActivity";

    ImageView ivClose;
    ImageView more;
    ImageView ivSwitch;
    ImageView ivPic;
    ImageView ivBeauty;
    ImageView ivFace;
    ImageView ivFilter;
    private TextureView textureView;
    private OpenCamera openCamera;
    private ArrayList<View> rotateViews = new ArrayList<>();
    private RecordParamDialog dialog = null;
    private int mRotation;
    private int curRotateCode = 0;
    private int angle;
    private int curAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_main);
        requsetPermission(Manifest.permission.CAMERA);
        initVIew();
        addViews();
        startAnim();

        more.setOnClickListener(view -> {
            if (dialog == null) {
                dialog = new RecordParamDialog(MainActivity.this);
            }
            Log.e(TAG, "onCreate: mRotation:"+mRotation );
            dialog.setRotation(mRotation);
            dialog.showDialog();
        });

    }

    private void requsetPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int ret = PermissionChecker.checkSelfPermission(MainActivity.this, permission);
            if (ret == PermissionChecker.PERMISSION_DENIED) {
                requestPermissions(new String[]{permission}, 110);
            }else if(ret == PermissionChecker.PERMISSION_GRANTED){
                openCamera();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 110) {
            Log.e(TAG,"onRequestPermissionsResult");
            openCamera();
        }

    }

    private void initVIew() {
        ivClose = findViewById(R.id.iv_close);
        more = findViewById(R.id.more);
        ivSwitch = findViewById(R.id.iv_switch);
        ivPic = findViewById(R.id.iv_pic);
        ivBeauty = findViewById(R.id.iv_beauty);
        ivFace = findViewById(R.id.iv_face);
        ivFilter = findViewById(R.id.iv_filter);
    }

    private void startAnim() {

        OrientationEventListener orientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {

                if (orientation > 350 || orientation < 10) {
                    orientation = 0;
                } else if (orientation > 80 && orientation < 100) {
                    orientation = 90;
                } else if (orientation > 170 && orientation < 190) {
                    orientation = 180;
                } else if (orientation > 260 && orientation < 280) {
                    orientation = 270;
                }
                switch (orientation) {
                    case 0:
                        mRotation = Surface.ROTATION_0;
                        angle = 0;
                        break;
                    case 90:
                        mRotation = Surface.ROTATION_270;
                        angle = 270;
                        break;
                    case 180:
                        mRotation = Surface.ROTATION_180;
                        angle = 180;
                        break;
                    case 270:
                        mRotation = Surface.ROTATION_90;
                        angle = 90;
                        break;
                }
                if (curAngle != angle) {
                    for (int i = 0; i < rotateViews.size(); i++) {
                        startRotateAnim(rotateViews.get(i), 300, curAngle, angle);
                    }
                    curAngle = angle;
                }

                if (curRotateCode == mRotation) {
                    return;
                }
                curRotateCode = mRotation;
                if (dialog != null && dialog.isShowing()) {
                    Log.e(TAG, "valueRotateAnim: orientation" + orientation + " mRotation:" + mRotation);
                    dialog.switchRoation(mRotation);
                }
            }
        };

        if (orientationEventListener.canDetectOrientation()) {//判断设备是否支持
            orientationEventListener.enable();
        } else {
            orientationEventListener.disable();//注销
            Log.e(TAG, "当前设备不支持手机旋转");
        }
    }

    public void startRotateAnim(View view, long time, int fromAngle, float toAngle) {
        ObjectAnimator animRotate = ObjectAnimator.ofFloat(view, "rotation", fromAngle, toAngle);
        animRotate.setDuration(time);
        animRotate.start();
    }

    private void addViews() {
        rotateViews.add(ivClose);
        rotateViews.add(more);
        rotateViews.add(ivSwitch);
        rotateViews.add(ivPic);
        rotateViews.add(ivBeauty);
        rotateViews.add(ivFace);
        rotateViews.add(ivFilter);
    }

    private void openCamera() {
        textureView = (TextureView) findViewById(R.id.textureview);
        openCamera = new OpenCamera(getApplicationContext(), textureView);
        openCamera.startCameraThread();
        textureView.setVisibility(View.VISIBLE);
        if (!textureView.isAvailable()) {
            Log.e(TAG, "openCamera: 1" );
            textureView.setSurfaceTextureListener(this);
        } else {
            Log.e(TAG, "openCamera: 2" );
            openCamera.startPreview();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.e(TAG, "onSurfaceTextureAvailable" );
        openCamera.setupCamera(width, height);  //设置相机参数,回调的是textureview的宽高
        openCamera.openCamera();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

}
