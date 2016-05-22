package top.wuhaojie.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import top.wuhaojie.netview.R;

/**
 * Created by wuhaojie on 2016/5/22 11:16.
 */
public class NetView extends View {

    /**
     * 默认视图大小
     */
    private static final int DEF_VIEW_SIZE_DIP = 240;

    /**
     * 数据键值对列表
     */
    private List<Pair<String, Float>> mList = new ArrayList<>();

    /**
     * 外接圆半径
     */
    private float mRadius;

    /**
     * 蜘网图中心X坐标
     */
    private float mCenterX;

    /**
     * 蜘网图中心Y坐标
     */
    private float mCenterY;

    /**
     * 内网数量
     */
    private int mNetCount = 5;

    /**
     * 标签个数
     */
    private int mTagCount;

    /**
     * 单位角度值
     */
    private float mAngle;

    /**
     * 网格线画笔
     */
    private Paint mNetPaint;

    /**
     * 覆盖区画笔
     */
    private Paint mOverlayPaint;

    /**
     * 文字画笔
     */
    private Paint mTextPaint;

    /**
     * 覆盖区圆圈画笔
     */
    private Paint mOverlayCirclePaint;

    /**
     * 全局路径
     */
    private Path mPath;

    /**
     * 标签文本字体大小
     */
    private int mTextSize;

    /**
     * 覆盖区透明度
     */
    private int mOverlayAlpha;

    /**
     * 覆盖区圆圈颜色
     */
    private int mCircleColor;

    /**
     * 标签文本字体颜色
     */
    private int mTextColor;

    /**
     * 覆盖区颜色
     */
    private int mOverlayColor;

    /**
     * 网格线颜色
     */
    private int mNetColor;

    /**
     * 覆盖区圆圈半径
     */
    private int mOverlayCircleRadius = 5;

    /**
     * 上下文
     */
    private Context mContext;

    /* 调试使用

    {
        mList.add(new Pair<>("A", 0.8f));
        mList.add(new Pair<>("B", 0.5f));
        mList.add(new Pair<>("C", 0.6f));
        mList.add(new Pair<>("D", 0.6f));
        mList.add(new Pair<>("E", 0.7f));
        mList.add(new Pair<>("F", 0.6f));
    }

    */

    public NetView(Context context) {
        this(context, null, 0);
    }

    public NetView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化上下文
        mContext = context;

        // 获取属性数组
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.netView, defStyleAttr, 0);

        mNetColor = attributes.getColor(R.styleable.netView_netColor, ContextCompat.getColor(context, R.color.defNetColor));
        mOverlayColor = attributes.getColor(R.styleable.netView_overlayColor, ContextCompat.getColor(context, R.color.defOverlayColor));
        mTextColor = attributes.getColor(R.styleable.netView_textColor, ContextCompat.getColor(context, R.color.defTextColor));
        mCircleColor = attributes.getColor(R.styleable.netView_overlayCircleColor, ContextCompat.getColor(context, R.color.defOverlayCircleColor));

        mOverlayAlpha = attributes.getInteger(R.styleable.netView_overlayAlpha, 100);
        mTextSize = attributes.getInteger(R.styleable.netView_textSize, 24);
        mNetCount = attributes.getInteger(R.styleable.netView_intervalCount, mNetCount) + 1;
        mOverlayCircleRadius = attributes.getInteger(R.styleable.netView_overlayCircleRadius, mOverlayCircleRadius);

        // 回收数组
        attributes.recycle();

        // 网格画笔
        mNetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNetPaint.setColor(mNetColor);
        mNetPaint.setStyle(Paint.Style.STROKE);

        // 覆盖区画笔
        mOverlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOverlayPaint.setColor(mOverlayColor);
        mOverlayPaint.setAlpha(mOverlayAlpha);
        mOverlayPaint.setStyle(Paint.Style.FILL);

        // 覆盖区圆圈画笔
        mOverlayCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOverlayCirclePaint.setColor(mCircleColor);
        mOverlayCirclePaint.setStyle(Paint.Style.FILL);

        // 文本画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setStyle(Paint.Style.STROKE);

        // 初始化路径
        mPath = new Path();

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 外接圆半径为宽高除(2*0.7)
        mRadius = Math.min(w, h) / 2 * 0.7f;

        // 初始化X和Y
        mCenterX = w / 2;
        mCenterY = h / 2;


        // 刷新界面
        postInvalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewSizePX = dip2px(mContext, DEF_VIEW_SIZE_DIP);
        int wmode = MeasureSpec.getMode(widthMeasureSpec);
        int wsize = MeasureSpec.getSize(widthMeasureSpec);
        int hmode = MeasureSpec.getMode(heightMeasureSpec);
        int hsize = MeasureSpec.getSize(heightMeasureSpec);
        if (wmode == MeasureSpec.AT_MOST && hmode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(viewSizePX, viewSizePX);
        } else if (widthMeasureSpec == MeasureSpec.AT_MOST) {
            setMeasuredDimension(viewSizePX, hsize);
        } else if (heightMeasureSpec == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wsize, viewSizePX);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mList.size() <= 0) {
            String text = "请添加数据后在真机上查看效果!";
            canvas.drawText(text, mCenterX - mTextPaint.measureText(text) / 2, mCenterY, mTextPaint);
            return;
        }

        // 标签个数
        mTagCount = mList.size();

        // 单位角度
        mAngle = (float) (2 * Math.PI / mTagCount);

        // 绘制网格
        // 每次递增的高度
        float intervalHeight = mRadius / (mNetCount - 1);

        // 当前绘制网格的半径
        float currRadius;

        for (int i = 0; i < mNetCount; i++) {
            currRadius = intervalHeight * i;
            mPath.reset();

            for (int j = 0; j < mTagCount; j++) {
                if (j == 0) {
                    mPath.moveTo(mCenterX + currRadius, mCenterY);
                } else {
                    float x = (float) (mCenterX + currRadius * Math.cos(mAngle * j));
                    float y = (float) (mCenterY + currRadius * Math.sin(mAngle * j));
                    mPath.lineTo(x, y);
                }
            }

            // 闭合路径并绘制
            mPath.close();
            canvas.drawPath(mPath, mNetPaint);

        }


        // 绘制轴线
        for (int i = 1; i < mTagCount + 1; i++) {
            mPath.reset();
            mPath.moveTo(mCenterX, mCenterY);
            float x = (float) (mCenterX + mRadius * Math.cos(mAngle * i));
            float y = (float) (mCenterY + mRadius * Math.sin(mAngle * i));
            mPath.lineTo(x, y);
            canvas.drawPath(mPath, mNetPaint);
        }


        // 绘制文本

        // 获取文本高度
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;

        for (int i = 0; i < mTagCount; i++) {
            // 文本内容
            String tag = mList.get(i).first;

            float x = (float) (mCenterX + (mRadius + fontHeight / 2) * Math.cos(mAngle * i));
            float y = (float) (mCenterY + (mRadius + fontHeight / 2) * Math.sin(mAngle * i));
            float fontWidth = mTextPaint.measureText(tag);

            // 修正文本位置
            if (mAngle * i > 0 && mAngle * i < Math.PI) {
                canvas.drawText(tag, x - fontWidth / 2, y + fontHeight, mTextPaint);
            } else if (mAngle * i >= Math.PI && mAngle * i < 3 * Math.PI / 2) {
                canvas.drawText(tag, x - fontWidth, y, mTextPaint);
            } else {
                canvas.drawText(tag, x, y, mTextPaint);
            }


        }


        // 绘制覆盖区
        mPath.reset();
        for (int i = 0; i < mTagCount; i++) {
            // 属性值
            float value = mList.get(i).second;

            float x = (float) (mCenterX + mRadius * Math.cos(mAngle * i) * value);
            float y = (float) (mCenterY + mRadius * Math.sin(mAngle * i) * value);

            if (i == 0) {
                mPath.moveTo(x, mCenterY);
            } else {
                mPath.lineTo(x, y);
            }

            // 绘制覆盖区圆圈
            canvas.drawCircle(x, y, mOverlayCircleRadius, mOverlayCirclePaint);

        }
        mPath.close();
        canvas.drawPath(mPath, mOverlayPaint);


    }


    /**
     * 添加标签数据
     *
     * @param tag   标签字符串
     * @param value 标签权重(0.0 ~ 1.0)
     * @return 操作是否成功
     */
    public boolean addData(String tag, float value) {
        if (mList != null) {
            mList.add(new Pair<>(tag, value));
            postInvalidate();
            return true;
        }
        return false;
    }

    /**
     * 添加标签数据
     *
     * @param tag   标签字符串
     * @param value 标签权重(0 ~ 100)
     * @return 操作是否成功
     */
    public boolean addData(String tag, int value) {
        return addData(tag, value / 100);
    }

    /**
     * 添加标签数据
     *
     * @param tag      标签字符串
     * @param value    标签价值
     * @param maxValue 最大价值
     * @return 操作是否成功
     */
    public boolean addData(String tag, int value, int maxValue) {
        return addData(tag, value / maxValue);
    }

    /**
     * 移除标签数据
     *
     * @param index 索引
     * @return 操作是否成功
     */
    public boolean removeData(int index) {
        mList.remove(index);
        return true;
    }

    /**
     * @return 内网数量
     */
    public int getNetCount() {
        return mNetCount;
    }

    /**
     * 设置内网数量
     *
     * @param netCount 内网数目
     */
    public void setNetCount(int netCount) {
        mNetCount = netCount;
    }

    /**
     * @return 标签数量
     */
    public int getTagCount() {
        return mTagCount;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public int getOverlayAlpha() {
        return mOverlayAlpha;
    }

    /**
     * 设置覆盖区透明度
     *
     * @param overlayAlpha 透明度(0 ~ 255)
     */
    public void setOverlayAlpha(int overlayAlpha) {
        mOverlayAlpha = overlayAlpha;
    }

    public int getCircleColor() {
        return mCircleColor;
    }

    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getOverlayColor() {
        return mOverlayColor;
    }

    public void setOverlayColor(int overlayColor) {
        mOverlayColor = overlayColor;
    }

    public int getNetColor() {
        return mNetColor;
    }

    public void setNetColor(int netColor) {
        mNetColor = netColor;
    }

    public int getOverlayCircleRadius() {
        return mOverlayCircleRadius;
    }

    public void setOverlayCircleRadius(int overlayCircleRadius) {
        mOverlayCircleRadius = overlayCircleRadius;
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
