package com.example.bottompopupview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bottompopupview.R;

import java.util.ArrayList;
import java.util.List;

public class BottomPopupView extends RelativeLayout {
    public static final int ACTION_LIVE = 1;
    public static final int ACTION_VIDEO = 2;
    public static final int ACTION_RADIO = 3;
    public static final int ACTION_DYNAMIC = 4;

    private ImageView mMainBackground;
    private ImageView mCloseImage;

    private RelativeLayout mLiveContainer;
    private ImageView mLiveBackground;
    private ImageView mLiveImage;
    private TextView mLiveName;

    private RelativeLayout mVideoContainer;
    private ImageView mVideoBackground;
    private ImageView mVideoImage;
    private TextView mVideoName;

    private RelativeLayout mRadioContainer;
    private ImageView mRadioBackground;
    private ImageView mRadioImage;
    private TextView mRadioName;

    private RelativeLayout mDynamicContainer;
    private ImageView mDynamicBackground;
    private ImageView mDynamicImage;
    private TextView mDynamicName;

    private OnItemClickListenr mItemClickListener;

    private float mMainBackgroundHeight;
    private float mIconBackgroundHeight;
    private final int ANIM_DURATION = 400;
    private final int ANIM_DELAY = 50;
    private final int ANIM_INTERVAL = 50;

    private List<ImageView> mBgImageList = new ArrayList<>();
    private List<ImageView> mImageList = new ArrayList<>();
    private List<TextView> mTextList = new ArrayList<>();

    private AnimatorSet mEnterAnim;
    private AnimatorSet mExitAnim;


    public interface OnItemClickListenr{
        void onLiveClickListener();
        void onVideoClickListener();
        void onRadioClickListener();
        void onDynamicClickListener();
    }


    public BottomPopupView(Context context) {
        this(context, null);
    }

    public BottomPopupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomPopupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        View.inflate(getContext(), R.layout.popup_layout, this);

        mMainBackground = (ImageView) findViewById(R.id.main_bg);
        mCloseImage = (ImageView) findViewById(R.id.close_iv);
        mCloseImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        mLiveContainer = (RelativeLayout) findViewById(R.id.live_container);
        mLiveBackground = (ImageView) findViewById(R.id.bg_live_iv);
        mLiveImage = (ImageView) findViewById(R.id.live_iv);
        mLiveName = (TextView) findViewById(R.id.live_text);
        mBgImageList.add(mLiveBackground);
        mImageList.add(mLiveImage);
        mTextList.add(mLiveName);

        mVideoContainer = (RelativeLayout) findViewById(R.id.video_container);
        mVideoBackground = (ImageView) findViewById(R.id.bg_video_iv);
        mVideoImage = (ImageView) findViewById(R.id.video_iv);
        mVideoName = (TextView) findViewById(R.id.video_text);
        mBgImageList.add(mVideoBackground);
        mImageList.add(mVideoImage);
        mTextList.add(mVideoName);

        mRadioContainer = (RelativeLayout) findViewById(R.id.radio_container);
        mRadioBackground = (ImageView) findViewById(R.id.bg_radio_iv);
        mRadioImage = (ImageView) findViewById(R.id.radio_iv);
        mRadioName = (TextView) findViewById(R.id.radio_text);
        mBgImageList.add(mRadioBackground);
        mImageList.add(mRadioImage);
        mTextList.add(mRadioName);

        mDynamicContainer = (RelativeLayout) findViewById(R.id.feed_container);
        mDynamicBackground = (ImageView) findViewById(R.id.bg_feed_iv);
        mDynamicImage = (ImageView) findViewById(R.id.feed_iv);
        mDynamicName = (TextView) findViewById(R.id.feed_text);
        mBgImageList.add(mDynamicBackground);
        mImageList.add(mDynamicImage);
        mTextList.add(mDynamicName);

        mLiveImage.setOnTouchListener(new ImageOnTouchListener());
        mLiveBackground.setOnTouchListener(new ImageOnTouchListener());
        mVideoImage.setOnTouchListener(new ImageOnTouchListener());
        mVideoBackground.setOnTouchListener(new ImageOnTouchListener());
        mRadioImage.setOnTouchListener(new ImageOnTouchListener());
        mRadioBackground.setOnTouchListener(new ImageOnTouchListener());
        mDynamicImage.setOnTouchListener(new ImageOnTouchListener());
        mDynamicBackground.setOnTouchListener(new ImageOnTouchListener());


        mLiveContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onLiveClickListener();
                }
            }
        });

        mVideoContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onVideoClickListener();
                }
            }
        });

        mRadioContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onRadioClickListener();
                }
            }
        });

        mDynamicContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onDynamicClickListener();
                }
            }
        });

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        float density = getResources().getDisplayMetrics().density;
        mMainBackgroundHeight = (196.66f * density + 0.5f);
        mIconBackgroundHeight = (72.0f * density + 0.5f);

        prepareAnimator();

        exit(); // 防止闪烁
    }


    private void prepareAnimator() {
        mEnterAnim = new AnimatorSet();
        mEnterAnim.playTogether(genBgEnterAnimator());
        mEnterAnim.playTogether(genIconBgEnterAnimator(mBgImageList));
        mEnterAnim.playTogether(genIconEnterAnimator(mImageList));
        mEnterAnim.playTogether(genTextEnterAnimator(mTextList));
        mEnterAnim.playTogether(genCloseEnterAnimator());
        mEnterAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setVisibility(VISIBLE);
            }
        });

        mExitAnim = new AnimatorSet();
        mExitAnim.playTogether(genBgExitAnimator());
        mExitAnim.playTogether(genIconBgExitAnimator(mBgImageList));
        mExitAnim.playTogether(genIconExitAnimator(mImageList));
        mExitAnim.playTogether(genTextExitAnimator(mTextList));
        mExitAnim.playTogether(genCloseExitAnimator());
        mExitAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(INVISIBLE);
            }
        });
    }


    public void setOnItemClickListener(OnItemClickListenr listener) {
        this.mItemClickListener = listener;
    }


    private class ImageOnTouchListener implements OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setAlpha(0.5f);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1.0f);
                    exit();
                    return true;
                default:
            }
            return false;
        }
    }

    public void exit(){
        if (isAnimating()) {
            return;
        }
        mExitAnim.start();
    }

    public void start() {
        if (isAnimating()) {
            return;
        }
        mEnterAnim.start();
    }


    // 动画
    // main背景入场动画
    private Animator genBgEnterAnimator() {
        Animator animator = ObjectAnimator.ofFloat(mMainBackground, "translationY", mMainBackgroundHeight, 0f)
                .setDuration(ANIM_DURATION);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    // main背景出场动画
    private Animator genBgExitAnimator() {
        Animator animator = ObjectAnimator.ofFloat(mMainBackground, "translationY", 0, mMainBackgroundHeight)
                .setDuration(ANIM_DURATION);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    // icon背景入场动画
    private List<Animator> genIconBgEnterAnimator(List<ImageView> icons) {
        List<Animator> allAnimator = new ArrayList<>();

        for (int i = 0; i < icons.size(); i++) {
            Animator transAnim = ObjectAnimator.ofFloat(icons.get(i), "translationY", mIconBackgroundHeight, 0)
                    .setDuration(ANIM_DURATION);
            transAnim.setStartDelay(ANIM_DELAY * i);
            transAnim.setInterpolator(new OvershootInterpolator(3));
            Animator alphaAnim = ObjectAnimator.ofFloat(icons.get(i), "alpha", 0, 1.0f)
                    .setDuration(ANIM_DURATION);
            alphaAnim.setStartDelay(ANIM_DELAY * i);
            alphaAnim.setInterpolator(new LinearInterpolator());
            allAnimator.add(transAnim);
            allAnimator.add(alphaAnim);
        }
        return allAnimator;
    }

    // icon背景出场动画
    private List<Animator> genIconBgExitAnimator(List<ImageView> icons) {
        List<Animator> allAnimator = new ArrayList<>();

        for (int i = 0; i < icons.size(); i++) {
            Animator transAnim = ObjectAnimator.ofFloat(icons.get(i), "translationY", 0, mIconBackgroundHeight)
                    .setDuration(ANIM_DURATION);
            transAnim.setStartDelay(ANIM_DELAY * i);
            transAnim.setInterpolator(new OvershootInterpolator(3));
            Animator alphaAnim = ObjectAnimator.ofFloat(icons.get(i), "alpha", 1.0f, 0f)
                    .setDuration(ANIM_DURATION);
            alphaAnim.setStartDelay(ANIM_DELAY * i);
            alphaAnim.setInterpolator(new LinearInterpolator());
            allAnimator.add(transAnim);
            allAnimator.add(alphaAnim);
        }
        return allAnimator;
    }

    // icon入场动画
    private List<Animator> genIconEnterAnimator(List<ImageView> icons) {
        List<Animator> allAnimator = new ArrayList<>();

        for (int i = 0; i < icons.size(); i++) {
            Animator transXAnim = ObjectAnimator.ofFloat(icons.get(i), "translationX", -mIconBackgroundHeight / 3, 0)
                    .setDuration(ANIM_DURATION);
            transXAnim.setInterpolator(new OvershootInterpolator(3));
            transXAnim.setStartDelay(ANIM_DELAY * i);
            Animator transYAnim = ObjectAnimator.ofFloat(icons.get(i), "translationY", mIconBackgroundHeight, 0)
                    .setDuration(ANIM_DURATION);
            transYAnim.setStartDelay(ANIM_DELAY * i);
            transYAnim.setInterpolator(new OvershootInterpolator());
            Animator alphaAnim = ObjectAnimator.ofFloat(icons.get(i), "alpha", 0, 1.0f)
                    .setDuration(ANIM_DURATION);
            alphaAnim.setStartDelay(ANIM_DELAY * i);
            alphaAnim.setInterpolator(new LinearInterpolator());
            allAnimator.add(transXAnim);
            allAnimator.add(transYAnim);
            allAnimator.add(alphaAnim);
        }
        return allAnimator;
    }


    // icon出场动画
    private List<Animator> genIconExitAnimator(List<ImageView> icons) {
        List<Animator> allAnimator = new ArrayList<>();

        for (int i = 0; i < icons.size(); i++) {
            Animator transYAnim = ObjectAnimator.ofFloat(icons.get(i), "translationY", 0, mIconBackgroundHeight)
                    .setDuration(ANIM_DURATION);
            transYAnim.setStartDelay(ANIM_DELAY * i);
            transYAnim.setInterpolator(new OvershootInterpolator());
            Animator alphaAnim = ObjectAnimator.ofFloat(icons.get(i), "alpha", 1.0f, 0f)
                    .setDuration(ANIM_DURATION);
            alphaAnim.setStartDelay(ANIM_DELAY * i);
            alphaAnim.setInterpolator(new LinearInterpolator());
            allAnimator.add(transYAnim);
            allAnimator.add(alphaAnim);
        }
        return allAnimator;
    }

    // 文字入场动画
    private List<Animator> genTextEnterAnimator(List<TextView> list) {
        List<Animator> allAnimator = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Animator transYAnim = ObjectAnimator.ofFloat(list.get(i), "translationY", mIconBackgroundHeight, 0)
                    .setDuration(ANIM_DURATION);
            transYAnim.setInterpolator(new LinearInterpolator());
            transYAnim.setStartDelay(i * ANIM_DELAY);
            Animator alphaAnim = ObjectAnimator.ofFloat(list.get(i), "alpha", 0, 1.0f)
                    .setDuration(ANIM_DURATION);
            alphaAnim.setInterpolator(new LinearInterpolator());
            alphaAnim.setStartDelay(i * ANIM_DELAY);
            allAnimator.add(transYAnim);
            allAnimator.add(alphaAnim);
        }
        return allAnimator;
    }

    // 文字出场动画
    private List<Animator> genTextExitAnimator(List<TextView> list) {
        List<Animator> allAnimator = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Animator transYAnim = ObjectAnimator.ofFloat(list.get(i), "translationY", 0, mIconBackgroundHeight)
                    .setDuration(ANIM_DURATION);
            transYAnim.setInterpolator(new LinearInterpolator());
            transYAnim.setStartDelay(i * ANIM_DELAY);
            Animator alphaAnim = ObjectAnimator.ofFloat(list.get(i), "alpha", 1.0f, 0)
                    .setDuration(ANIM_DURATION);
            alphaAnim.setInterpolator(new LinearInterpolator());
            alphaAnim.setStartDelay(i * ANIM_DELAY);
            allAnimator.add(transYAnim);
            allAnimator.add(alphaAnim);
        }
        return allAnimator;
    }

    // 结束图标入场动画
    private List<Animator> genCloseEnterAnimator() {
        List<Animator> allAnimator = new ArrayList<>();
        Animator rotate = ObjectAnimator.ofFloat(mCloseImage, "rotation", 0, -90f)
                .setDuration(ANIM_DURATION);
        rotate.setInterpolator(new LinearInterpolator());
        Animator alphaAnim = ObjectAnimator.ofFloat(mCloseImage, "alpha", 0.5f, 1)
                .setDuration(ANIM_DURATION);
        alphaAnim.setInterpolator(new LinearInterpolator());
        allAnimator.add(rotate);
        allAnimator.add(alphaAnim);
        return allAnimator;
    }

    // 结束图标出场动画
    private List<Animator> genCloseExitAnimator() {
        List<Animator> allAnimator = new ArrayList<>();
        Animator rotate = ObjectAnimator.ofFloat(mCloseImage, "rotation", -90f, 0)
                .setDuration(ANIM_DURATION);
        rotate.setInterpolator(new LinearInterpolator());
        Animator alphaAnim = ObjectAnimator.ofFloat(mCloseImage, "alpha", 1, 0.5f)
                .setDuration(ANIM_DURATION);
        alphaAnim.setInterpolator(new LinearInterpolator());
        allAnimator.add(rotate);
        allAnimator.add(alphaAnim);
        return allAnimator;
    }

    private boolean isAnimating() {
        return mEnterAnim.isRunning() || mExitAnim.isRunning();
    }


}
