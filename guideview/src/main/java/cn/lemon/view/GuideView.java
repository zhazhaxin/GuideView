package cn.lemon.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * 参考了https://github.com/laxian/GuideView  现在性能和功能完爆它
 * <p>
 * 方法回调：创建GuideView -- initParams(初始化参数) -- getTargetViewPosition(获取TargetView位置核心方法) -- show(添加GuideView进DecorView)
 *  -- addHintView -- GuideView.onMeasure -- GuideView.onLayout -- GuideView.onDraw
 * <p>
 * view.post(new Runnable(){
 * public void run(){
 * new GuideView.Builder()
 * .setTargetView()
 * .create().show();
 * }
 * });
 * <p>
 * Created by linlongxin on 2016/7/22.
 */

public class GuideView extends RelativeLayout {

    private final String TAG = "GuideView";

    private boolean hasMeasure = false;
    private boolean hasAddHintView = false;
    private boolean isShowing = false;
    private int[] mTargetViewLocation = new int[2];
    private int mTargetViewWidth;
    private int mTargetViewHeight;

    private int mScreenWidth;
    private int mScreenHeight;

    //遮罩层画笔
    private Paint mBackgroundPaint;
    //透明椭圆画笔
    private Paint mTransparentPaint;

    private ViewGroup mDecorView;

    private Builder.GuideViewParams mParams;

    public GuideView(Context context) {
        this(context, null);
    }

    public GuideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (context instanceof Activity) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            mScreenWidth = displayMetrics.widthPixels;
            mScreenHeight = displayMetrics.heightPixels;
            mDecorView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
            setWillNotDraw(false);
            Log.i(TAG, "screenWidth : " + mScreenWidth + "  screenHeight : " + mScreenHeight);
        } else {
            throw new IllegalArgumentException("context must be activity");
        }
    }

    public void initParams(Builder.GuideViewParams params) {
        mParams = params;
        mBackgroundPaint = new Paint();
        mTransparentPaint = new Paint();
        mBackgroundPaint.setColor(mParams.maskLayerColor);
        setOnClickListener(mParams.mClickListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //先绘制遮罩层
        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mTemp = new Canvas(bitmap);
        mTemp.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mBackgroundPaint);

        PorterDuffXfermode mDrawMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
        mTransparentPaint.setXfermode(mDrawMode);
        mTransparentPaint.setAntiAlias(true);

        @SuppressLint("DrawAllocation") RectF rectF = new RectF(mTargetViewLocation[0] - mParams.mTransparentPaddingLeft + mParams.mTransparentMarginLeft,
                mTargetViewLocation[1] - mParams.mTransparentPaddingTop + mParams.mTransparentMarginTop,
                mTargetViewLocation[0] + mTargetViewWidth + mParams.mTransparentPaddingRight - mParams.mTransparentMarginRight,
                mTargetViewLocation[1] + mTargetViewHeight + mParams.mTransparentPaddingBottom - mParams.mTransparentMarginBottom);
        mTemp.drawOval(rectF, mTransparentPaint);

        //绘制到GuideView的画布上
        canvas.drawBitmap(bitmap, 0, 0, mBackgroundPaint);
    }

    /**
     * 添加HintView
     */
    @SuppressLint("RtlHardcoded")
    private void addHintView() {
        if (hasAddHintView) {
            return;
        }
        if (mParams.mHintView != null) {

            LayoutParams layoutParams;
            if (mParams.mHintLayoutParams != null) {
                layoutParams = mParams.mHintLayoutParams;
            } else {
                layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
            }

            switch (mParams.mDirection) {
                /*
                 * FrameLayout没有setGravity()方法
                 */
                //左边相关
                case Direction.LEFT:
                    this.setGravity(Gravity.RIGHT);
                    layoutParams.setMargins(0, mTargetViewLocation[1],
                            mScreenWidth - mTargetViewLocation[0] + mParams.mHintViewSpace + mParams.mHintViewMarginRight, 0);
                    break;
                case Direction.LEFT_BOTTOM:
                    this.setGravity(Gravity.RIGHT | Gravity.TOP);
                    layoutParams.setMargins(0, mTargetViewLocation[1] + mTargetViewHeight + mParams.mHintViewSpace + mParams.mHintViewMarginTop,
                            mScreenWidth - mTargetViewLocation[0] + mParams.mHintViewSpace + mParams.mHintViewMarginRight, 0);
                    break;
                case Direction.LEFT_ABOVE:
                    this.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                    layoutParams.setMargins(0, 0, mScreenWidth - mTargetViewLocation[0] + mParams.mHintViewSpace + mParams.mHintViewMarginRight,
                            mScreenHeight - mTargetViewLocation[1] + mParams.mHintViewSpace + mParams.mHintViewMarginBottom);
                    break;
                case Direction.LEFT_ALIGN_BOTTOM:
                    this.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                    layoutParams.setMargins(0, mTargetViewLocation[1],
                            mScreenWidth - mTargetViewLocation[0] + mParams.mHintViewSpace + mParams.mHintViewMarginRight,
                            mScreenHeight - mTargetViewLocation[1] - mTargetViewHeight);
                    break;

                //右边相关
                case Direction.RIGHT:
                    this.setGravity(Gravity.LEFT);
                    layoutParams.setMargins(mTargetViewLocation[0] + mTargetViewWidth + mParams.mHintViewSpace + mParams.mHintViewMarginLeft,
                            mTargetViewLocation[1], 0, 0);
                    break;
                case Direction.RIGHT_ABOVE:
                    this.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                    layoutParams.setMargins(mTargetViewWidth + mTargetViewLocation[0] + mParams.mHintViewSpace + mParams.mHintViewMarginLeft, 0,
                            0, mScreenHeight - mTargetViewLocation[1] + mParams.mHintViewSpace + mParams.mHintViewMarginBottom);
                    break;
                case Direction.RIGHT_BOTTOM:
                    this.setGravity(Gravity.LEFT | Gravity.TOP);
                    layoutParams.setMargins(mTargetViewLocation[0] + mTargetViewWidth + mParams.mHintViewSpace + mParams.mHintViewMarginLeft,
                            mTargetViewLocation[1] + mTargetViewHeight + mParams.mHintViewSpace + mParams.mHintViewMarginTop, 0, 0);
                    break;
                case Direction.RIGHT_ALIGN_BOTTOM:
                    this.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                    layoutParams.setMargins(mTargetViewLocation[0] + mTargetViewWidth + mParams.mHintViewSpace + mParams.mHintViewMarginLeft,
                            0, 0, mScreenHeight - mTargetViewLocation[1] - mTargetViewHeight + mParams.mHintViewMarginBottom);
                    break;

                //上方相关
                case Direction.ABOVE:
                    this.setGravity(Gravity.BOTTOM);
                    layoutParams.setMargins(0, 0,
                            0, mScreenHeight - mTargetViewLocation[1] + mParams.mHintViewSpace + mParams.mHintViewMarginBottom);
                    break;
                case Direction.ABOVE_ALIGN_LEFT:
                    this.setGravity(Gravity.BOTTOM | Gravity.LEFT);
                    layoutParams.setMargins(mTargetViewLocation[0] + mParams.mHintViewMarginLeft, 0, 0,
                            mScreenHeight - mTargetViewLocation[1] + mParams.mHintViewSpace + mParams.mHintViewMarginBottom);
                    break;
                case Direction.ABOVE_ALIGN_RIGHT:
                    this.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
                    layoutParams.setMargins(0, 0, mScreenWidth - mTargetViewLocation[0] - mTargetViewWidth + mParams.mHintViewMarginRight,
                            mScreenHeight - mTargetViewLocation[1] + mParams.mHintViewSpace + mParams.mHintViewMarginBottom);
                    break;

                //下方相关
                case Direction.BOTTOM:
                    this.setGravity(Gravity.TOP);
                    layoutParams.setMargins(0, mTargetViewLocation[1] + mTargetViewHeight + mParams.mHintViewMarginTop, 0, 0);
                    break;
                case Direction.BOTTOM_ALIGN_LEFT:
                    this.setGravity(Gravity.TOP | Gravity.LEFT);
                    layoutParams.setMargins(mTargetViewLocation[0] + mParams.mHintViewMarginLeft,
                            mTargetViewLocation[1] + mTargetViewHeight + mParams.mHintViewSpace + mParams.mHintViewMarginTop, 0, 0);
                    break;
                case Direction.BOTTOM_ALIGN_RIGHT:
                    this.setGravity(Gravity.TOP | Gravity.RIGHT);
                    layoutParams.setMargins(0, mTargetViewLocation[1] + mTargetViewHeight + mParams.mHintViewSpace + mParams.mHintViewMarginTop,
                            mScreenWidth - mTargetViewLocation[0] - mTargetViewWidth + mParams.mHintViewMarginRight, 0);
                default:
                    break;
            }
            addView(mParams.mHintView, layoutParams);
            hasAddHintView = true;
        }
    }

    /**
     * 获取TargetView位置
     */
    private void getTargetViewPosition() {
        Log.i(TAG, "getTargetViewPosition");
        if (mParams.mTargetView.getWidth() > 0 && mParams.mTargetView.getHeight() > 0) {
            mParams.mTargetView.getLocationInWindow(mTargetViewLocation);
            if (mTargetViewWidth == 0 || mTargetViewHeight == 0) {
                mTargetViewWidth = mParams.mTargetView.getWidth();
                mTargetViewHeight = mParams.mTargetView.getHeight();
            }
            if (mTargetViewLocation[0] >= 0 && mTargetViewLocation[1] > 0) {
                hasMeasure = true;
            }
        } else {
            hasMeasure = false;
            Log.i(TAG, "targetView is not measured, please user view.post(Runnable run) initialize GuideView");
        }
    }

    public void show() {
        if (isShowing || !hasMeasure) {
            return;
        }
        addHintView();
        mDecorView.addView(this);
        isShowing = true;
    }

    public void hide() {
        this.removeAllViews();
        mDecorView.removeView(this);
    }

    public boolean isShowing() {
        return isShowing;
    }

    /**
     * 通过Builder构建
     */
    public static class Builder {

        private static class GuideViewParams {
            View mTargetView;
            View mHintView;
            int mDirection;
            //hintView和TargetView间距,默认20px
            int mHintViewSpace = 20;
            int mTransparentPadding;
            int mTransparentPaddingLeft;
            int mTransparentPaddingTop;
            int mTransparentPaddingRight;
            int mTransparentPaddingBottom;
            int mTransparentMargin;
            int mTransparentMarginLeft;
            int mTransparentMarginRight;
            int mTransparentMarginTop;
            int mTransparentMarginBottom;
            //mHintViewSpace和它意义相同
            int mHintViewMargin;
            int mHintViewMarginLeft;
            int mHintViewMarginRight;
            int mHintViewMarginTop;
            int mHintViewMarginBottom;
            //遮罩层默认颜色
            @ColorInt
            int maskLayerColor = 0xcc1D1C1C;
            LayoutParams mHintLayoutParams;
            OnClickListener mClickListener;
        }

        private GuideViewParams mParams;
        private Context mContext;

        public Builder(Context ctx) {
            if (ctx instanceof Activity) {
                mParams = new GuideViewParams();
                mContext = ctx;
            } else {
                throw new IllegalArgumentException("context must be activity and not null");
            }
        }

        public Builder setTargetView(View targetView) {
            if (targetView == null) {
                throw new NullPointerException("targetView is null");
            }
            mParams.mTargetView = targetView;
            return this;
        }

        public Builder setTargetView(@IdRes int resId) {
            mParams.mTargetView = ((Activity) mContext).findViewById(resId);
            return this;
        }

        public Builder setHintView(View hintView) {
            mParams.mHintView = hintView;
            return this;
        }

        public Builder setHintViewDirection(int direction) {
            mParams.mDirection = direction;
            return this;
        }

        public Builder setTransparentOvalPadding(int px) {
            mParams.mTransparentPadding = px;
            return this;
        }

        public Builder setTransparentOvalPaddingLeft(int px) {
            mParams.mTransparentPaddingLeft = px;
            return this;
        }

        public Builder setTransparentOvalPaddingRight(int px) {
            mParams.mTransparentPaddingRight = px;
            return this;
        }

        public Builder setTransparentOvalPaddingTop(int px) {
            mParams.mTransparentPaddingTop = px;
            return this;
        }

        public Builder setTransparentOvalPaddingBottom(int px) {
            mParams.mTransparentPaddingBottom = px;
            return this;
        }

        public Builder setTransparentMargin(int px) {
            mParams.mTransparentMargin = px;
            return this;
        }

        public Builder setTransparentMarginLeft(int mTransparentMarginLeft) {
            mParams.mTransparentMarginLeft = mTransparentMarginLeft;
            return this;
        }

        public Builder setTransparentMarginRight(int mTransparentMarginRight) {
            mParams.mTransparentMarginRight = mTransparentMarginRight;
            return this;
        }

        public Builder setTransparentMarginTop(int mTransparentMarginTop) {
            mParams.mTransparentMarginTop = mTransparentMarginTop;
            return this;
        }

        public Builder setTransparentMarginBottom(int mTransparentMarginBottom) {
            mParams.mTransparentMarginBottom = mTransparentMarginBottom;
            return this;
        }

        public Builder setHintViewMargin(int px) {
            mParams.mHintViewMargin = px;
            return this;
        }

        public Builder setHintViewMarginLeft(int px) {
            mParams.mHintViewMarginLeft = px;
            return this;
        }

        public Builder setHintViewMarginRight(int px) {
            mParams.mHintViewMarginRight = px;
            return this;
        }

        public Builder setHintViewMarginTop(int px) {
            mParams.mHintViewMarginTop = px;
            return this;
        }

        public Builder setHintViewMarginBottom(int px) {
            mParams.mHintViewMarginBottom = px;
            return this;
        }

        public Builder setHintViewSpace(int px) {
            mParams.mHintViewSpace = px;
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int color) {
            mParams.maskLayerColor = color;
            return this;
        }

        public Builder setHintLayoutParams(LayoutParams mHintLayoutParams) {
            mParams.mHintLayoutParams = mHintLayoutParams;
            return this;
        }

        public Builder setOnClickListener(OnClickListener listener) {
            mParams.mClickListener = listener;
            return this;
        }

        public GuideView create() {
            if (mParams.mTargetView == null) {
                throw new RuntimeException("please set a targetView");
            }
            GuideView guideView = new GuideView(mContext);
            guideView.initParams(mParams);
            guideView.getTargetViewPosition();
            return guideView;
        }

        public void show() {
            create().show();
        }
    }
}
