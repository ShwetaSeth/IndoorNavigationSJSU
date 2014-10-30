package com.sample.compass;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DrawSurfaceView extends View {
	Point point = new Point(0d, 0d, "Location");
	Paint paint = new Paint();
	Paint paintRect = new Paint();
	Matrix matrixRect = new Matrix();
	Matrix matrixArrow = new Matrix();
	
	private float azimuth = 0f; 
	private float pitch = 0f; 
	private float roll = 0f;
	private String val = "";
	
	private Camera camera;
	
	private double screenWidth, screenHeight = 0d;
	private Bitmap arrow;
	private Bitmap compass;
	private RectF rect = new RectF();
	private RectF arrowRect = new RectF();
	
	public DrawSurfaceView(Context c, Paint paint) {
		super(c);
	}

	public DrawSurfaceView(Context context, AttributeSet set) {
		super(context, set);
		camera = new Camera();
		paint.setColor(Color.GREEN);
		paint.setTextSize(30);
		paint.setStrokeWidth(DpiUtils.getPxFromDpi(getContext(), 2));
		paint.setAntiAlias(true);
		
		paintRect.setColor(Color.BLACK);
		paintRect.setStrokeWidth(DpiUtils.getPxFromDpi(getContext(), 2));
		paintRect.setAntiAlias(true);
		
		arrow = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow);
		compass = BitmapFactory.decodeResource(context.getResources(), R.drawable.compass200);
		rect.set(0, 0, compass.getWidth(), compass.getHeight());		
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Log.d("onSizeChanged", "in here w=" + w + " h=" + h);
		screenWidth = (double) w;
		screenHeight = (double) h;

	}

	@TargetApi(12)
	@Override
	protected void onDraw(Canvas canvas) {
		
//		if(azimuth < 0)
//			canvas.drawText((azimuth + 360) + " \u00B0", ((float)compass.getWidth()/2 - 20), ((float)compass.getHeight() + 30), paint);
//		else
//			canvas.drawText((azimuth) + " \u00B0", ((float)compass.getWidth()/2 - 20), ((float)compass.getHeight() + 30), paint);
//		canvas.drawRect(rect, paintRect);
//		matrixRect.setRotate(-azimuth, (float)compass.getWidth()/2, (float)compass.getHeight()/2);
//		canvas.drawBitmap(compass, matrixRect, paint);
		
		canvas.drawText(val, 0, 30, paint);
		
		float leftPos = (float) ((screenWidth/2) - (arrow.getWidth()/2));
		float topPos = (float) ((screenHeight/2) - (arrow.getHeight()/2));			
		float rightPos = (float) ((screenWidth/2) + (arrow.getWidth()/2));
		float bottomPos = (float) ((screenHeight/2) + (arrow.getHeight()/2));			
		arrowRect.set(leftPos, topPos, rightPos, bottomPos);
		
		
//		camera.save();
//		if(pitch > 0)
//			camera.rotateX(-pitch);
//		if(-90 < roll && roll < 90)
//			camera.rotateY(-roll);
//		camera.rotateX(0);
//		camera.rotateY(0);
//		camera.rotateZ(-azimuth);
//		camera.getMatrix(matrixArrow);
//		camera.restore();
//
//		matrixArrow.preTranslate(-leftPos, -topPos);		
//		matrixArrow.postTranslate(2*leftPos, 2*topPos);		
		
		matrixArrow.setTranslate(leftPos, topPos);
		matrixArrow.postRotate(-azimuth, (float)screenWidth/2, (float)screenHeight/2);
		canvas.drawBitmap(arrow, matrixArrow, paint);
	}

	public void setRotationValues(float azimuth, float pitch, float roll) {
		this.azimuth = azimuth;
		this.pitch = pitch;
		this.roll = roll;
	}

	public void setMyLocation(double latitude, double longitude) {
		point.latitude = latitude;
		point.longitude = longitude;
	}
	
	public void setDistance(String val){
		this.val = val;
	}

}
