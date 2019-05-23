package com.yiwangrencai.ywkj.jmessage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiwangrencai.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 获取相册
     */
    public static final int REQUEST_PHOTO = 1;
    /**
     * 获取视频
     */
    public static final int REQUEST_VIDEO = 2;
    /**
     * 最小录制时间
     */
    private static final int MIN_RECORD_TIME = 1 * 1000;
    /**
     * 最长录制时间
     */
    private static final int MAX_RECORD_TIME = 10 * 1000;
    /**
     * 刷新进度的间隔时间
     */
    private static final int PLUSH_PROGRESS = 100;

    private Context mContext;
    /**
     * TextureView
     */
    private TextureView mTextureView;
    /**
     * 带手势识别
     */
    private CameraView mCameraView;
    /**
     * 录制按钮
     */
    private CameraProgressBar mProgressbar;
    /**
     * 顶部像机设置
     */
    private RelativeLayout rl_camera;
    /**
     * 关闭,选择,前后置
     */
    private ImageView iv_close, iv_choice, iv_facing;
    /**
     * 闪光
     */
    private TextView tv_flash;
    /**
     * camera manager
     */
    private CameraManager cameraManager;
    /**
     * player manager
     */
    private MediaPlayerManager playerManager;
    /**
     * true代表视频录制,否则拍照
     */
    private boolean isSupportRecord;
    /**
     * 视频录制地址
     */
    private String recorderPath;
    /**
     * 录制视频的时间,毫秒
     */
    private int recordSecond;
    /**
     * 获取照片订阅, 进度订阅
     */
    private Subscription takePhotoSubscription, progressSubscription;
    /**
     * 是否正在录制
     */
    private boolean isRecording;

    /**
     * 是否为点了拍摄状态(没有拍照预览的状态)
     */
    private boolean isPhotoTakingState;
    private TextView mTv_tack;
    private boolean isFlash=true;
    int  cameraFacing;
    public static void lanuchForPhoto(Activity context) {
        Intent intent = new Intent(context, CameraActivity.class);
        context.startActivityForResult(intent, REQUEST_PHOTO);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_camera);


        initView();
        initDatas();

    }


    private void initView() {
        mTextureView = (TextureView) findViewById(R.id.mTextureView);
        mCameraView = (CameraView) findViewById(R.id.mCameraView);
        mProgressbar = (CameraProgressBar) findViewById(R.id.mProgressbar);
        rl_camera = (RelativeLayout) findViewById(R.id.rl_camera);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_choice = (ImageView) findViewById(R.id.iv_choice);
        iv_facing = (ImageView) findViewById(R.id.iv_facing);
        tv_flash = (TextView) findViewById(R.id.tv_flash);
        mTv_tack = (TextView) findViewById(R.id.tv_tack);
        cameraFacing=CameraUtils.getCameraFacing(CameraActivity.this,0);


        iv_close.setOnClickListener(this);
        iv_choice.setOnClickListener(this);
        iv_facing.setOnClickListener(this);
        tv_flash.setOnClickListener(this);
    }

    protected void initDatas() {
        cameraManager = CameraManager.getInstance(getApplication());
        playerManager = MediaPlayerManager.getInstance(getApplication());
        cameraManager.setCameraType(isSupportRecord ? 1 : 0);
        if (cameraFacing==1){
            Log.i("cameraFacingcameraFacing","camer1111111");
            tv_flash.setVisibility(View.GONE);
            isFlash=false;
          //  tv_flash.setVisibility(cameraManager.isSupportFlashCamera() ? View.GONE : View.GONE);
        }else{
            Log.i("cameraFacingcameraFacing","000000000");
            tv_flash.setVisibility(cameraManager.isSupportFlashCamera() ? View.VISIBLE : View.GONE);
            isFlash=true;
        }

        setCameraFlashState();
        iv_facing.setVisibility(cameraManager.isSupportFrontCamera() ? View.VISIBLE : View.GONE);
        rl_camera.setVisibility(cameraManager.isSupportFlashCamera()
                || cameraManager.isSupportFrontCamera() ? View.VISIBLE : View.GONE);

        final int max = MAX_RECORD_TIME / PLUSH_PROGRESS;
        mProgressbar.setMaxProgress(max);

        mProgressbar.setOnProgressTouchListener(new CameraProgressBar.OnProgressTouchListener() {
            @Override
            public void onClick(CameraProgressBar progressBar) {
                mTv_tack.setVisibility(View.GONE);
                cameraManager.takePhoto(callback);
            }

            @Override
            public void onLongClick(CameraProgressBar progressBar) {
                mTv_tack.setVisibility(View.GONE);
                isSupportRecord = true;
                cameraManager.setCameraType(1);
                rl_camera.setVisibility(View.GONE);
                recorderPath = FileUtilsss.getUploadVideoFile(mContext);
                cameraManager.startMediaRecord(recorderPath);
                isRecording = true;
                progressSubscription = Observable.interval(100, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).take(max).subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        stopRecorder(true);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        mProgressbar.setProgress(mProgressbar.getProgress() + 1);
                    }
                });
            }

            @Override
            public void onZoom(boolean zoom) {
                cameraManager.handleZoom(zoom);
            }

            @Override
            public void onLongClickUp(CameraProgressBar progressBar) {
                isSupportRecord = false;
                cameraManager.setCameraType(0);
                stopRecorder(true);
                if (progressSubscription != null) {
                    progressSubscription.unsubscribe();
                }
            }

            @Override
            public void onPointerDown(float rawX, float rawY) {
                if (mTextureView != null) {
                    mCameraView.setFoucsPoint(new PointF(rawX, rawY));
                }
            }
        });

        mCameraView.setOnViewTouchListener(new CameraView.OnViewTouchListener() {
            @Override
            public void handleFocus(float x, float y) {
                if (isFlash){
                    cameraManager.handleFocusMetering(x, y);
                }

            }

            @Override
            public void handleZoom(boolean zoom) {
                cameraManager.handleZoom(zoom);
            }
        });
    }

    /**
     * 设置闪光状态
     */
    private void setCameraFlashState() {
        int flashState = cameraManager.getCameraFlash();
        switch (flashState) {
            case 0: //自动
                tv_flash.setSelected(true);
                tv_flash.setText("自动");
                break;
            case 1://open
                tv_flash.setSelected(true);
                tv_flash.setText("开启");
                break;
            case 2: //close
                tv_flash.setSelected(false);
                tv_flash.setText("关闭");
                break;
        }
    }

    /**
     * 是否显示录制按钮
     *
     * @param isShow
     */
    private void setTakeButtonShow(boolean isShow) {
        if (isShow) {
            mProgressbar.setVisibility(View.VISIBLE);
            rl_camera.setVisibility(cameraManager.isSupportFlashCamera()
                    || cameraManager.isSupportFrontCamera() ? View.VISIBLE : View.GONE);
        } else {
            mProgressbar.setVisibility(View.GONE);
            rl_camera.setVisibility(View.GONE);
        }
    }

    /**
     * 停止拍摄
     */
    private void stopRecorder(boolean play) {
        isRecording = false;
        cameraManager.stopMediaRecord();
        recordSecond = mProgressbar.getProgress() * PLUSH_PROGRESS;//录制多少毫秒
        mProgressbar.reset();
        if (recordSecond < MIN_RECORD_TIME) {//小于最小录制时间作废
            if (recorderPath != null) {
                FileUtilsss.delteFiles(new File(recorderPath));
                recorderPath = null;
                recordSecond = 0;
            }
            setTakeButtonShow(true);
        } else if (play && mTextureView != null && mTextureView.isAvailable()) {
            setTakeButtonShow(false);
            mProgressbar.setVisibility(View.GONE);
            iv_choice.setVisibility(View.VISIBLE);
            cameraManager.closeCamera();
            playerManager.playMedia(new Surface(mTextureView.getSurfaceTexture()), recorderPath);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTextureView.isAvailable()) {
            Log.i("打开射线机","1111111111");
            if (recorderPath != null) {//优先播放视频
                iv_choice.setVisibility(View.VISIBLE);
                setTakeButtonShow(false);
                playerManager.playMedia(new Surface(mTextureView.getSurfaceTexture()), recorderPath);
            } else {
                iv_choice.setVisibility(View.GONE);
                setTakeButtonShow(true);
                Log.i("打开射线机","22222222222");
                cameraManager.openCamera(this, mTextureView.getSurfaceTexture(),
                        mTextureView.getWidth(), mTextureView.getHeight());
            }
        } else {
            mTextureView.setSurfaceTextureListener(listeners);
            Log.i("打开射线机","3333333333");
        }
    }

    @Override
    protected void onPause() {
        if (progressSubscription != null) {
            progressSubscription.unsubscribe();
        }
        if (takePhotoSubscription != null) {
            takePhotoSubscription.unsubscribe();
        }
        if (isRecording) {
            stopRecorder(false);
        }
        cameraManager.closeCamera();
        playerManager.stopMedia();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mCameraView.removeOnZoomListener();
        super.onDestroy();
    }

    private boolean isFace=false;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                mTv_tack.setVisibility(View.VISIBLE);
                if (recorderPath != null) {//有拍摄好的正在播放,重新拍摄
                    FileUtilsss.delteFiles(new File(recorderPath));
                    recorderPath = null;
                    recordSecond = 0;
                    playerManager.stopMedia();
                    setTakeButtonShow(true);
                    iv_choice.setVisibility(View.GONE);
                    cameraManager.openCamera(CameraActivity.this, mTextureView.getSurfaceTexture(), mTextureView.getWidth(), mTextureView.getHeight());
                } else if (isPhotoTakingState) {
                    isPhotoTakingState = false;
                    iv_choice.setVisibility(View.GONE);
                    setTakeButtonShow(true);
                    cameraManager.restartPreview();
                } else {
                    finish();
                }
                break;
            case R.id.iv_choice://选择图片或视频
                //将拍摄的视频路径回传
                if (recorderPath != null) {
                    Intent intent = new Intent();
                    intent.putExtra("video", recorderPath);
                    setResult(RequestCode.TAKE_VIDEO, intent);
                }
                if (photo != null) {
                    backPicture();
                }
                finish();
                break;
            case R.id.tv_flash:
                    cameraManager.changeCameraFlash(mTextureView.getSurfaceTexture(),
                            mTextureView.getWidth(), mTextureView.getHeight());
                setCameraFlashState();
                isFlash=true;
                break;
            case R.id.iv_facing:
                cameraManager.changeCameraFacing(this, mTextureView.getSurfaceTexture(),
                        mTextureView.getWidth(), mTextureView.getHeight());
                int isFacing=CameraUtils.getCameraFacing(this,0);
                if (isFacing==0){
                    tv_flash.setVisibility(View.VISIBLE);
                }else{
                    tv_flash.setVisibility(View.GONE);
                }

                int isFlashs=CameraUtils.getCameraFlash(this);
                if (isFlashs==2){

                }
                isFlash=false;
                break;
        }
    }

    /**
     * camera回调监听
     */
    public TextureView.SurfaceTextureListener listeners = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            Log.i("1ssssssss","texture"+width);
            if (recorderPath != null) {
                iv_choice.setVisibility(View.VISIBLE);
                setTakeButtonShow(false);
                playerManager.playMedia(new Surface(texture), recorderPath);
            } else {
                setTakeButtonShow(true);
                iv_choice.setVisibility(View.GONE);
                cameraManager.openCamera(CameraActivity.this, texture, width, height);
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            Log.i("2ssssssss","texture"+width);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            Log.i("2ssssssss","texture"+texture);
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
            Log.i("2ssssssss","texture"+texture);
        }
    };
    private String photo;

    private void backPicture() {
        //将图片路径intent回传
        Intent intent = new Intent();
        intent.putExtra("take_photo", photo);
        setResult(RequestCode.TAKE_PHOTO, intent);
    }

    private Camera.PictureCallback callback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            setTakeButtonShow(false);
            takePhotoSubscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    if (!subscriber.isUnsubscribed()) {
                        String photoPath = FileUtilsss.getUploadPhotoFile(mContext);
                        //保存拍摄的图片
                        isPhotoTakingState = FileUtilsss.savePhoto(photoPath, data, cameraManager.isCameraFrontFacing());
                        if (isPhotoTakingState) {
                            photo = photoPath;
                        }
                        subscriber.onNext(isPhotoTakingState);
                    }
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (aBoolean != null && aBoolean) {
                        iv_choice.setVisibility(View.VISIBLE);
                    } else {
                        setTakeButtonShow(true);
                    }
                }
            });
        }
    };

}
