package com.amirhosseinemadi.neumorphism.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.annotation.ColorRes;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.amirhosseinemadi.neumorphism.NeumorphismImpl;
import com.amirhosseinemadi.neumorphism.R;
import com.amirhosseinemadi.neumorphism.Utilities;

public class NeuImageView extends AppCompatImageView implements NeumorphismImpl {

    private final DisplayMetrics metrics;
    private final Utilities utilities;
    private boolean isInitDrawn;

    private RectF backgroundRect;
    private Paint lightPaint;
    private Paint darkPaint;

    private float neuElevation;
    private float neuRadius;
    private int backgroundColor;
    private int lightShadowColor;
    private int darkShadowColor;

    public NeuImageView(@NonNull Context context) {
        super(context);
        metrics = getContext().getResources().getDisplayMetrics();
        utilities = new Utilities(metrics);

        neuElevation = utilities.dpToPx(6f);
        neuRadius = utilities.dpToPx(8f);
        backgroundColor = R.color.md_grey_100;
        lightShadowColor = R.color.white;
        darkShadowColor = R.color.md_grey_300;

        init(neuElevation,neuRadius,backgroundColor,lightShadowColor,darkShadowColor);
    }

    public NeuImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        metrics = getContext().getResources().getDisplayMetrics();
        utilities = new Utilities(metrics);

        TypedArray attrArray = context.obtainStyledAttributes(attrs,R.styleable.NeuImageView);
        neuElevation = attrArray.getDimension(R.styleable.NeuImageView_neu_elevation,(int) utilities.dpToPx(6f));
        neuRadius = attrArray.getDimension(R.styleable.NeuImageView_neu_radius,utilities.dpToPx(8f));
        backgroundColor = attrArray.getColor(R.styleable.NeuImageView_neu_background_color, ContextCompat.getColor(getContext(),R.color.md_grey_100));
        lightShadowColor = attrArray.getColor(R.styleable.NeuImageView_neu_light_shadow_color,ContextCompat.getColor(getContext(),R.color.white));
        darkShadowColor = attrArray.getColor(R.styleable.NeuImageView_neu_dark_shadow_color,ContextCompat.getColor(getContext(),R.color.md_grey_300));
        attrArray.recycle();

        init(neuElevation,neuRadius,backgroundColor,lightShadowColor,darkShadowColor);
    }


    private void init(float elevation, float radius, int backgroundColor, int lightShadowColor, int darkShadowColor)
    {
        backgroundRect = new RectF();
        lightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        darkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setNeuElevation(elevation);
        setNeuRadius(radius);
        setBackgroundColor(backgroundColor);
        setLightShadowColor(lightShadowColor);
        setDarkShadowColor(darkShadowColor);
        setPaddingRelative(getPaddingStart(),getPaddingTop(),getPaddingEnd(),getPaddingBottom());
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        backgroundRect.set(0 + neuElevation * 2.7f
                , 0 + neuElevation * 2.7f
                , getWidth() - neuElevation * 2.7f
                , getHeight() - neuElevation * 2.7f);
    }


    /**
     * This function draw neumorphism background for the view
     * @param canvas
     */
    private void drawBackground(Canvas canvas)
    {
        lightPaint.setStyle(Paint.Style.FILL);
        lightPaint.setColor(backgroundColor);
        lightPaint.setShadowLayer(neuElevation*2.1f,-neuElevation,-neuElevation,lightShadowColor);

        darkPaint.setStyle(Paint.Style.FILL);
        darkPaint.setColor(backgroundColor);
        darkPaint.setShadowLayer(neuElevation*2.1f,+neuElevation,+neuElevation,darkShadowColor);

        canvas.drawRoundRect(backgroundRect,neuRadius,neuRadius, lightPaint);
        canvas.drawRoundRect(backgroundRect,neuRadius,neuRadius, darkPaint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        canvas.getMatrix().setRectToRect(new RectF(canvas.getClipBounds()),backgroundRect, Matrix.ScaleToFit.CENTER);
        super.onDraw(canvas);
    }


    /**
     *This method use for setting background color for view and does not apply shape drawable.<br>
     * <br>
     *note : this view does not support gradient background color.
     * @param background background drawable for view
     */
    @Override
    @SuppressLint("ResourceType")
    public void setBackground(Drawable background) {
        if (background instanceof ColorDrawable)
        {
            setNeuBackgroundColor(((ColorDrawable) background).getColor());
        }else if (background instanceof ShapeDrawable)
        {
            setNeuBackgroundColor(((ShapeDrawable) background).getPaint().getColor());
        }
    }

    @Override
    public Drawable getBackground() {
        return new ColorDrawable(backgroundColor);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        left = (int) (left + neuElevation*2.5);
        top = (int) (top + neuElevation*2.5);
        right = (int) (right + neuElevation*2.5);
        bottom = (int) (bottom + neuElevation*2.5);
        super.setPadding(left, top, right, bottom);
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        start = (int) (start + neuElevation*2.5);
        top = (int) (top + neuElevation*2.5);
        end = (int) (end + neuElevation*2.5);
        bottom = (int) (bottom + neuElevation*2.5);
        super.setPadding(start, top, end, bottom);
    }

    /**
     * @return Padding top of view minus default padding
     */
    public int getNuePaddingTop()
    {
        return (int) (getPaddingTop() - neuElevation*2.5);
    }

    /**
     * @return Padding start of view minus default padding
     */
    public int getNuePaddingStart()
    {
        return (int) (getPaddingStart() - neuElevation*2.5);
    }

    /**
     * @return Padding end of view minus default padding
     */
    public int getNuePaddingEnd()
    {
        return (int) (getPaddingEnd() - neuElevation*2.5);
    }

    /**
     * @return Padding bottom of view minus default padding
     */
    public int getNuePaddingBottom()
    {
        return (int) (getPaddingBottom() - neuElevation*2.5);
    }

    /**
     * Set elevation for the view . in fact it's for shadow value
     * @param elevation
     */
    @Override
    public void setNeuElevation(@Dimension float elevation) {
        this.neuElevation = elevation;
        if (isInitDrawn)
        {
            requestLayout();
        }
    }

    /**
     * @return elevation of view
     */
    @Override
    public float getNeuElevation() {
        return neuElevation;
    }

    /**
     * This function set background color for the view
     * @param color ResourceColor background color
     */
    @Override
    public void setNeuBackgroundColor(@ColorRes int color) {
        try
        {
            this.backgroundColor = ContextCompat.getColor(getContext(),color);
        }catch (Resources.NotFoundException exception)
        {
            this.backgroundColor = color;
        }
        if (isInitDrawn)
        {
            invalidate();
        }
    }

    /**
     * @return background color
     */
    @Override
    public int getNeuBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Set light shadow color for the view
     * @param color light shadow color res
     */
    @Override
    public void setLightShadowColor(@ColorRes  int color) {
        try
        {
            this.lightShadowColor = ContextCompat.getColor(getContext(),color);
        }catch (Resources.NotFoundException exception)
        {
            this.lightShadowColor = color;
        }

        if (isInitDrawn)
        {
            invalidate();
        }
    }

    /**
     * @return light shadow color of view
     */
    @Override
    public int getLightShadowColor() {
        return lightShadowColor;
    }

    /**
     * Set dark shadow color for the view
     * @param color dark shadow color res
     */
    @Override
    public void setDarkShadowColor(@ColorRes int color) {
        try
        {
            this.darkShadowColor = ContextCompat.getColor(getContext(),color);
        }catch (Resources.NotFoundException exception)
        {
            this.darkShadowColor = color;
        }

        if (isInitDrawn)
        {
            invalidate();
        }
    }

    /**
     * @return dark shadow color of the view
     */
    @Override
    public int getDarkShadowColor() {
        return darkShadowColor;
    }

    /**
     * Set radius corners for the view
     * @param radius radius value in pixel
     */
    @Override
    public void setNeuRadius(@Dimension float radius) {
        this.neuRadius = radius;
        if (isInitDrawn)
        {
            invalidate();
        }
    }

    /**
     * @return radius of the corners
     */
    @Override
    public float getNeuRadius() {
        return neuRadius;
    }
}

