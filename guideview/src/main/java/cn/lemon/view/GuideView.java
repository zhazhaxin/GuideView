package cn.lemon.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import static android.content.Context.MODE_PRIVATE;

/**
 * 参考了https://github.com/laxian/GuideView
 * GuideView 重要函数调用顺序 onMeasure -- onGlobalLayout -- createGuideView -- onMeasure --
 * onGlobalLayout -- onDraw -- drawMaskLayer
 * 然后查找TargetView的位置，给画笔Paint设置PorterDuff.Mode模式画出透明的圆或椭圆
 * <p>
 * Created by linlongxin on 2016/7/22.
 */

public class GuideView extends RelativeLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    private final String TAG = "GuideView";

    private boolean isMeasure = false;
    private boolean isShowOnce = false;
    private int mHintViewSpace = 0; //hintView和TargetView间距,默认20px
    private int mTransparentPadding;
    private int mTransparentPaddingLeft;
    private int mTransparentPaddingTop;
    private int mTransparentPaddingRight;
    private int mTransparentPaddingBottom;
    private int mTransparentMarginLeft;
    private int mTransparentMarginRight;
    private int mTransparentMarginTop;
    private int mTransparentMarginBottom;
    private int[] mTargetViewLocation = new int[2];
    private int mTargetViewWidth;
    private int mTargetViewHeight;
    private int mHintViewDirection;
    private
    @ColorInt
    int MASK_LAYER_COLOR = 0xcc1D1C1C;  //遮罩层默认颜色

    private LayoutParams mHintLayoutParams;

    private Paint mBackgroundPaint;  //遮罩层画笔
    private Paint mTransparentPaint;  //透明椭圆画笔

    private View mHintView;
    private Context mContext;
    private View mTargetView;
    private FrameLayout mDecorView;


    public GuideView(Context context) {
        this(context, null);
    }

    public GuideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.i(TAG, " --- GuideView");
        mContext = context;
        mDecorView = (FrameLayout) ((Activity) getContext()).getWindow().getDecorView();
        mBackgroundPaint = new Paint();
        mTransparentPaint = new Paint();
        mBackgroundPaint.setColor(MASK_LAYER_COLOR);
    }

    private void setMaskBackgroundColor(@ColorInt int color) {
        mBackgroundPaint.setColor(color);
    }

    private void setTransparentPadding(int padding) {
        mTransparentPadding = padding;
    }

    private void setTransparentPaddingLeft(int mTransparentPaddingLeft) {
        this.mTransparentPaddingLeft = mTransparentPaddingLeft;
    }

    private void setTransparentPaddingTop(int mTransparentPaddingTop) {
        this.mTransparentPaddingTop = mTransparentPaddingTop;
    }

    private void setTransparentPaddingRight(int mTransparentPaddingRight) {
        this.mTransparentPaddingRight = mTransparentPaddingRight;
    }

    private void setTransparentPaddingBottom(int mTransparentPaddingBottom) {
        this.mTransparentPaddingBottom = mTransparentPaddingBottom;
    }

    public void setTransparentMarginLeft(int mTransparentMarginLeft) {
        this.mTransparentMarginLeft = mTransparentMarginLeft;
    }

    public void setTransparentMarginRight(int mTransparentMarginRight) {
        this.mTransparentMarginRight = mTransparentMarginRight;
    }

    public void setTransparentMarginTop(int mTransparentMarginTop) {
        this.mTransparentMarginTop = mTransparentMarginTop;
    }

    public void setTransparentMarginBottom(int mTransparentMarginBottom) {
        this.mTransparentMarginBottom = mTransparentMarginBottom;
    }

    public void setHintLayoutParams(LayoutParams mHintLayoutParams) {
        this.mHintLayoutParams = mHintLayoutParams;
    }

    private void setHintViewSpace(int mHintViewSpace) {
        this.mHintViewSpace = mHintViewSpace;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, " --- onMeasure");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, " --- onDraw");

        if (!isMeasure || mTargetView == null) {
            return;
        }

        drawMaskLayer(canvas);
    }

    /**
     * 绘制遮罩层
     *
     * @param canvas
     */
    private void drawMaskLayer(Canvas canvas) {
        Log.i(TAG, " --- drawMaskLayer");

        //先绘制遮罩层
        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mTemp = new Canvas(bitmap);
        mTemp.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mBackgroundPaint);

        PorterDuffXfermode mDrawMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
        mTransparentPaint.setXfermode(mDrawMode);
        mTransparentPaint.setAntiAlias(true);

        /**
         * 透明区域padding设置
         */
        if (mTransparentPadding != 0) {
            mTransparentPaddingLeft = mTransparentPadding;
            mTransparentPaddingRight = mTransparentPadding;
            mTransparentPaddingTop = mTransparentPadding;
            mTransparentPaddingBottom = mTransparentPadding;
        }

        RectF rectF = new RectF(mTargetViewLocation[0] - mTransparentPaddingLeft + mTransparentMarginLeft,
                mTargetViewLocation[1] - mTransparentPaddingTop + mTransparentMarginTop,
                mTargetViewLocation[0] + mTargetViewWidth + mTransparentPaddingRight - mTransparentMarginRight,
                mTargetViewLocation[1] + mTargetViewHeight + mTransparentPaddingBottom - mTransparentMarginBottom);
        mTemp.drawOval(rectF, mTransparentPaint);

        //绘制到GuideView的画布上
        canvas.drawBitmap(bitmap, 0, 0, mBackgroundPaint);
    }

    /**
     * 添加HintView
     */
    private void addHintView() {
        Log.i(TAG, " --- addHintView");
        if (mHintView != null) {
            int screenWidth = this.getWidth();
            int screenHeight = this.getHeight();

            LayoutParams layoutParams;
            if (mHintLayoutParams != null) {
                layoutParams = mHintLayoutParams;
            } else {
                layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            switch (mHintViewDirection) {
                /**
                 * FrameLayout没有setGravity()方法
                 */
                case 0:
                    break;
                case Direction.LEFT:
                    this.setGravity(Gravity.RIGHT);
                    layoutParams.setMargins(0, mTargetViewLocation[1],
                            screenWidth - mTargetViewLocation[0] + mHintViewSpace, 0);
                    break;
                case Direction.RIGHT:
                    this.setGravity(Gravity.LEFT);
                    layoutParams.setMargins(mTargetViewLocation[0] + mTargetViewWidth + mHintViewSpace,
                            mTargetViewLocation[1], 0, 0);
                    break;
                case Direction.ABOVE:
                    this.setGravity(Gravity.BOTTOM);
                    layoutParams.setMargins(0, mTargetViewLocation[0],
                            0, screenHeight - mTargetViewLocation[1] + mHintViewSpace);
                    break;
                case Direction.BOTTOM_ALIGN_LEFT:
                    this.setGravity(Gravity.TOP);
                    layoutParams.setMargins(mTargetViewLocation[0],
                            mTargetViewLocation[1] + mTargetViewHeight + mHintViewSpace, 0, 0);
                    break;
                case Direction.BOTTOM_ALIGN_RIGHT:
                    this.setGravity(Gravity.TOP);
                    layoutParams.setMargins(0, mTargetViewLocation[1] + mTargetViewHeight + mHintViewSpace,
                            screenWidth - mTargetViewLocation[0] - mTargetViewWidth, 0);
                case Direction.LEFT_BOTTOM:
                    this.setGravity(Gravity.RIGHT | Gravity.TOP);
                    layoutParams.setMargins(0, mTargetViewLocation[1] + mTargetViewHeight + mHintViewSpace,
                            screenWidth - mTargetViewLocation[0] + mHintViewSpace, 0);
                    break;
                case Direction.RIGHT_BOTTOM:
                    this.setGravity(Gravity.LEFT | Gravity.TOP);
                    layoutParams.setMargins(mTargetViewLocation[0] + mTargetViewWidth + mHintViewSpace,
                            mTargetViewLocation[1] + mTargetViewHeight + mHintViewSpace, 0, 0);
                    break;
                case Direction.LEFT_ABOVE:
                    this.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                    layoutParams.setMargins(0, 0, screenWidth - mTargetViewLocation[0] + mHintViewSpace,
                            screenHeight - mTargetViewLocation[1] + mHintViewSpace);
                    break;
                case Direction.RIGHT_ABOVE:
                    this.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                    layoutParams.setMargins(mTargetViewWidth + mTargetViewLocation[0] + mHintViewSpace, 0,
                            0, screenHeight - mTargetViewLocation[1] + mHintViewSpace);
                    break;
            }

            if (mHintView.getParent() != null) {
                mHintView.setLayoutParams(layoutParams);
                Log.i(TAG, "hintView 有爸爸啦。。。");
//                ((ViewGroup) mHintView.getParent()).removeView(mHintView);
            } else {
                this.addView(mHintView, layoutParams);
            }
        }
    }


    public void hide() {
        Log.i(TAG, "hide");
        if (isShowOnce) {
            if (mTargetView != null) {
                getContext().getSharedPreferences(TAG, MODE_PRIVATE).edit()
                        .putBoolean(TAG + +mTargetView.getId(), true);
                if (Build.VERSION.SDK_INT >= 16) {
                    mTargetView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        }
        this.removeAllViews();
        mDecorView.removeView(this);
    }

    public void show() {
        Log.i(TAG, "show");
        if (hasShow()) {
            return;
        }
        mTargetView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.setBackgroundColor(Color.TRANSPARENT);

        mDecorView.addView(this);
    }

    private void showOnce(boolean isShowOnce) {
        this.isShowOnce = isShowOnce;
    }

    private boolean hasShow() {
        if (mTargetView == null) {
            Log.i(TAG, "mTargetView == null");
            return true;
        }
        return getContext().getSharedPreferences(TAG, MODE_PRIVATE).getBoolean(TAG + mTargetView.getId(), false);
    }

    private void setTargetView(View targetView) {
        mTargetView = targetView;
    }

    private void setTargetView(@IdRes int resId) {
        setTargetView(((Activity) getContext()).findViewById(resId));
    }

    private void setTargetView(@IdRes int resId, View view) {
        setTargetView(view.findViewById(resId));
    }

    private void setHintView(View hintView) {
        mHintView = hintView;
    }

    private void setHintViewDirection(int direction) {
        mHintViewDirection = direction;
    }

    @Override
    public void onGlobalLayout() {
        Log.i(TAG, " --- onGlobalLayout");
        if (mTargetView == null) {
            Log.i(TAG, "mTargetView == null");
            return;
        }
        if (isMeasure) {
            return;
        }
        /**
         * 说明测量了targetView
         */
        if (mTargetView.getWidth() > 0 && mTargetView.getHeight() > 0) {
            isMeasure = true;
            if (mTargetViewWidth == 0 || mTargetViewHeight == 0) {
                mTargetViewWidth = mTargetView.getWidth();
                mTargetViewHeight = mTargetView.getHeight();
                mTargetView.getLocationInWindow(mTargetViewLocation);
            }

            addHintView();
        } else {
            Log.i(TAG, "targetView 不能被测量。。。。。");
        }

    }


    /**
     * 通过Builder构建
     */
    public static class Builder {
        private GuideView mGuideView;

        public Builder(Context ctx) {
            mGuideView = new GuideView(ctx);
        }

        public Builder setTargetView(View targetView) {
            mGuideView.setTargetView(targetView);
            return this;
        }

        public Builder setTargetView(@IdRes int resId) {
            mGuideView.setTargetView(resId);
            return this;
        }

        public Builder showOnce(boolean isShowOnce) {
            mGuideView.showOnce(isShowOnce);
            return this;
        }

        public Builder setHintView(View hintView) {
            mGuideView.setHintView(hintView);
            return this;
        }

        public Builder setHintViewDirection(int direction) {
            mGuideView.setHintViewDirection(direction);
            return this;
        }

        public Builder setTransparentOvalPadding(int px) {
            mGuideView.setTransparentPadding(px);
            return this;
        }

        public Builder setTransparentOvalPaddingLeft(int px) {
            mGuideView.setTransparentPaddingLeft(px);
            return this;
        }

        public Builder setTransparentOvalPaddingRight(int px) {
            mGuideView.setTransparentPaddingRight(px);
            return this;
        }

        public Builder setTransparentOvalPaddingTop(int px) {
            mGuideView.setTransparentPaddingTop(px);
            return this;
        }

        public Builder setTransparentOvalPaddingBottom(int px) {
            mGuideView.setTransparentPaddingBottom(px);
            return this;
        }

        public Builder setTransparentMarginLeft(int mTransparentMarginLeft) {
            mGuideView.setTransparentMarginLeft(mTransparentMarginLeft);
            return this;
        }

        public Builder setTransparentMarginRight(int mTransparentMarginRight) {
            mGuideView.setTransparentMarginRight(mTransparentMarginRight);
            return this;
        }

        public Builder setTransparentMarginTop(int mTransparentMarginTop) {
            mGuideView.setTransparentMarginTop(mTransparentMarginTop);
            return this;
        }

        public Builder setTransparentMarginBottom(int mTransparentMarginBottom) {
            mGuideView.setTransparentMarginBottom(mTransparentMarginBottom);
            return this;
        }

        public Builder setHintViewSpace(int px) {
            mGuideView.setHintViewSpace(px);
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int color) {
            mGuideView.setMaskBackgroundColor(color);
            return this;
        }

        public Builder setHintLayoutParams(LayoutParams mHintLayoutParams) {
            mGuideView.setHintLayoutParams(mHintLayoutParams);
            return this;
        }

        public Builder setOnClickListener(OnClickListener listener) {
            mGuideView.setOnClickListener(listener);
            return this;
        }

        public GuideView create() {
            Log.i("GuideView", "Builder.create()");
            return mGuideView;
        }


    }
}
