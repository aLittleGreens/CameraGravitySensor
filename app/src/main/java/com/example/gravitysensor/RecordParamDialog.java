package com.example.gravitysensor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author Created by LittleGreens
 * @data on 2019/7/19
 * @describe TODO
 */
public class RecordParamDialog {
    private final String TAG = "RecordParamDialog";
    private Context context;
    private final Activity mActivity;
    private Dialog mDeviceDialog;
    private View view;
    String[] mdate = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "L", "M", "N", "O", "P", "Q", "R"};
    private boolean mIsVertial;
    private int width;
    private int height;
    private boolean isover = true;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private View mContent;

    private int mBeginDialogWidth;
    private int mBeginDialogHeight;
    protected boolean isFirstCreateDialog = true; // 表示第一次初始化本DialogFragment
    private int firstRotation = 0;

    public RecordParamDialog(Activity activity) {
        mActivity = activity;
    }

    public void setRotation(int rotation) {
        this.firstRotation = rotation;
    }

    public void showDialog() {
        if (mActivity == null) return;

        view = View.inflate(mActivity, R.layout.dialog_select_device, null);
        listView = view.findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(mActivity, R.layout.layout_list_text, mdate);
        listView.setAdapter(adapter);
        mDeviceDialog = new Dialog(mActivity, R.style.DialogFullScreen);

        mDeviceDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Window window = mDeviceDialog.getWindow();
                if (window != null) {
                    mContent = window.findViewById(android.R.id.content);
                    mBeginDialogWidth = mContent.getWidth();
                    mBeginDialogHeight = mContent.getHeight()/* + dp2px(24)*/;
                    Log.e(TAG, "setOnShowListener: mBeginDialogWidth：" + mBeginDialogWidth + " mBeginDialogHeight:" + mBeginDialogHeight);
                }
            }
        });
        mDeviceDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Window window = mDeviceDialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        Display d = mActivity.getWindowManager().getDefaultDisplay();
        window.setBackgroundDrawableResource(R.color.colorTransparent_black);
        width = d.getWidth();
        height = d.getHeight();
        Log.e(TAG, "width:" + width + " height:" + height);
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;   //宽设为屏幕的100%
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.gravity = Gravity.TOP;
        mDeviceDialog.onWindowAttributesChanged(wl);
        mDeviceDialog.setCanceledOnTouchOutside(true);
        mDeviceDialog.show();
        new Handler().postDelayed(() -> {
            if (firstRotation != 0) {
                switchRoation(firstRotation);
            }
        }, 50);


    }


    public boolean isShowing() {
        return mDeviceDialog.isShowing();
    }


    public void switchRoation(int rotation) {
        Window window = mDeviceDialog.getWindow();
        if (mContent == null) {
            mContent = window.findViewById(android.R.id.content);
            mBeginDialogWidth = mContent.getWidth();
            mBeginDialogHeight = mContent.getHeight();
            Log.e(TAG, "1switchRoation: mBeginDialogWidth：" + mBeginDialogWidth + " mBeginDialogHeight:" + mBeginDialogHeight);
        }
        int w, h;
        int tranX, tranY;
        if (rotation == 1 || rotation == 3) {//横屏
            w = height;
            h = width;
//            boolean b = ScreenUtil.checkDeviceHasNavigationBar(mActivity);
//            Log.e(TAG, "checkDeviceHasNavigationBar: " + b);
//            if (b) {
//                int virtualBarHeigh = ScreenUtil.getVirtualBarHeigh(mActivity);
//                Log.e(TAG, "virtualBarHeigh: " + virtualBarHeigh);
////                w -= virtualBarHeigh;
//            }

            tranX = (h - w) / 2;
            tranY = (w - h) / 2;
            window.setLayout(h /*+ 80*/, w /*+ 100*/);


        } else {
            mContent.setPadding(0, 0, 0, 0);
            w = width;
            h = height;
            tranX = 0;
            tranY = 0;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        mContent.getLayoutParams().width = w;
        mContent.getLayoutParams().height = h;
        mContent.setLayoutParams(mContent.getLayoutParams());

        int duration = isFirstCreateDialog ? 0 : 200;

        mContent.animate()
                .rotation(90 * (rotation))
                .translationX(tranX)
                .translationY(tranY)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isFirstCreateDialog = false;
                    }
                });


    }
}
