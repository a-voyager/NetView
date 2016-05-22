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
    private Paint mNetPaint;
    private Paint mOverlayPaint;
    private Paint mTextPaint;
    private Paint mOverlayCirclePaint;

    {
        mList.add(new Pair<>("A", 0.1f));
        mList.add(new Pair<>("B", 0.7f));
        mList.add(new Pair<>("C", 0.4f));
        mList.add(new Pair<>("D", 0.9f));
        mList.add(new Pair<>("E", 0.3f));
        mList.add(new Pair<>("F", 0.4f));
    }

    public NetView(Context context) {
        this(context, null, 0);
    }

    public NetView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 获取属性数组
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.netView, defStyleAttr, 0);

        int netColor = attributes.getColor(R.styleable.netView_netColor, ContextCompat.getColor(context, R.color.defNetColor));
        int overlayColor = attributes.getColor(R.styleable.netView_overlayColor, ContextCompat.getColor(context, R.color.defOverlayColor));
        int textColor = attributes.getColor(R.styleable.netView_textColor, ContextCompat.getColor(context, R.color.defTextColor));
        int circleColor = attributes.getColor(R.styleable.netView_overlayCircleColor, ContextCompat.getColor(context, R.color.defOverlayCircleColor));

        int overlayAlpha = attributes.getInteger(R.styleable.netView_overlayAlpha, 100);
        int textSize = attributes.getInteger(R.styleable.netView_textSize, 24);
        mNetCount = attributes.getInteger(R.styleable.netView_intervalCount, 5) + 1;

        // 回收数组
        attributes.recycle();

        // 网格画笔
        mNetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNetPaint.setColor(netColor);
        mNetPaint.setStyle(Paint.Style.STROKE);

        // 覆盖区画笔
        mOverlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOverlayPaint.setColor(overlayColor);
        mOverlayPaint.setAlpha(overlayAlpha);
        mOverlayPaint.setStyle(Paint.Style.FILL);

        // 覆盖区圆圈画笔
        mOverlayCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOverlayCirclePaint.setColor(circleColor);
        mOverlayCirclePaint.setStyle(Paint.Style.FILL);

        // 文本画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setStyle(Paint.Style.STROKE);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 外接圆半径为宽高除(2*0.7)
        mRadius = Math.min(w, h) / 2 * 0.7f;

        // 初始化X和Y
        mCenterX = w / 2;
        mCenterY = h / 2;

        // 标签个数
        mTagCount = mList.size();

        // 单位角度
        mAngle = (float) (2 * Math.PI / mTagCount);

        // 刷新界面
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path path = new Path();

        // 绘制网格
        // 每次递增的高度
        float intervalHeight = mRadius / (mNetCount - 1);

        // 当前绘制网格的半径
        float currRadius;

        for (int i = 0; i < mNetCount; i++) {
            currRadius = intervalHeight * i;
            path.reset();

            for (int j = 0; j < mTagCount; j++) {
                if (j == 0) {
                    path.moveTo(mCenterX + currRadius, mCenterY);
                } else {
                    float x = (float) (mCenterX + currRadius * Math.cos(mAngle * j));
                    float y = (float) (mCenterY + currRadius * Math.sin(mAngle * j));
                    path.lineTo(x, y);
                }
            }

            // 闭合路径并绘制
            path.close();
            canvas.drawPath(path, mNetPaint);

        }


        // 绘制轴线
        for (int i = 1; i < mTagCount + 1; i++) {
            path.reset();
            path.moveTo(mCenterX, mCenterY);
            float x = (float) (mCenterX + mRadius * Math.cos(mAngle * i));
            float y = (float) (mCenterY + mRadius * Math.sin(mAngle * i));
            path.lineTo(x, y);
            canvas.drawPath(path, mNetPaint);
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
        path.reset();
        for (int i = 0; i < mTagCount; i++) {
            // 属性值
            float value = mList.get(i).second;

            float x = (float) (mCenterX + mRadius * Math.cos(mAngle * i) * value);
            float y = (float) (mCenterY + mRadius * Math.sin(mAngle * i) * value);

            if (i == 0) {
                path.moveTo(x, mCenterY);
            } else {
                path.lineTo(x, y);
            }

            // 绘制覆盖区圆圈
            canvas.drawCircle(x, y, 5, mOverlayCirclePaint);

        }
        path.close();
        canvas.drawPath(path, mOverlayPaint);


    }
}
