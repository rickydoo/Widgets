package net.rickydoo.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Ricky DOO on Oct. 17, 2015.
 */
public class CustomImageView extends ImageView {
	public static final int TYPE_SQUARE = 1;
	public static final int TYPE_CIRCLE = 2;
	public static final int TYPE_WITH_TEXT = 3;
	private int type;
	private int borderColor;
	private int backgroundColor;
	private float borderWidth;
	private float radius;
	private String textContent;

	private Paint paint;
	private Paint paintUserName;
	private Paint paintBorder;
	private Paint paintBackground;

	public CustomImageView(Context context) {
		this(context, null, 0);
	}

	public CustomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyle, 0);
		type = a.getInt(R.styleable.CustomImageView_civType, TYPE_SQUARE);
		borderColor = a.getColor(R.styleable.CustomImageView_civBorderColor, Color.WHITE);
		backgroundColor = a.getColor(R.styleable.CustomImageView_civBackground,
				context.getResources().getColor(R.color.main_color));
		borderWidth = a.getDimension(R.styleable.CustomImageView_civBorderWidth, 0);
		radius = a.getDimension(R.styleable.CustomImageView_civRadius, 0);
		textContent = a.getString(R.styleable.CustomImageView_civTextContent);
		preCheck();
		a.recycle();
		paint = new Paint();
		paint.setAntiAlias(true);
		paintUserName = new Paint();
		paintUserName.setAntiAlias(true);
		paintBorder = new Paint();
		paintBorder.setAntiAlias(true);
		paintBorder.setColor(borderColor);
		paintBackground = new Paint();
		paintBackground.setAntiAlias(true);
		paintBackground.setColor(backgroundColor);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable drawable = getDrawable();
		if (drawable != null) {
			int width = MeasureSpec.getSize(widthMeasureSpec);
			setMeasuredDimension(width, width);
		} else
			super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas) {
		Bitmap image = drawableToBitmap(getDrawable());
		int canvasSize;
		canvasSize = canvas.getWidth();
		if (canvas.getHeight() < canvasSize)
			canvasSize = canvas.getHeight();
		// init shader
		if (type == TYPE_WITH_TEXT) {
			canvas.drawCircle(canvasSize / 2, canvasSize / 2, canvasSize / 2, paintBackground);
			Paint.FontMetrics fm = new Paint.FontMetrics();
			paintUserName.setTextSize(canvasSize * 0.52f);

			paintUserName.getFontMetrics(fm);
			paintUserName.setColor(Color.WHITE);

			canvas.drawText(textContent,
					canvasSize / 2 - paintUserName.measureText(textContent) / 2,
					canvasSize / 2 + (int) (0.16 * canvasSize),
					paintUserName);
		} else if (image != null) {

			BitmapShader shader = new BitmapShader(Bitmap.createScaledBitmap(image, canvasSize, canvasSize, false),
					Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			if (type == TYPE_SQUARE) {
				canvas.drawRoundRect(new RectF(0, 0, canvasSize, canvasSize), radius, radius, paintBorder);

				paint.setShader(shader);
				float mainRadius = radius - borderWidth;
				canvas.drawRoundRect(new RectF(borderWidth, borderWidth, canvasSize
						- borderWidth, canvasSize - borderWidth), mainRadius, mainRadius, paint);
			} else {
				canvas.drawCircle(canvasSize / 2, canvasSize / 2, canvasSize / 2, paintBorder);

				paint.setShader(shader);
				canvas.drawCircle(canvasSize / 2, canvasSize / 2, canvasSize / 2 - borderWidth, paint);
			}
		}
	}

	public Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable == null) {
			return null;
		} else if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	public int getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		invalidate();
	}

	public float getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
		invalidate();
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
		invalidate();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		invalidate();
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
		preCheck();
		invalidate();
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
		invalidate();
	}

	private void preCheck() {
		if (TextUtils.isEmpty(textContent)) {
			textContent = "C";
		}
		textContent = textContent.charAt(0) + "";
		textContent = textContent.toUpperCase();
	}
}