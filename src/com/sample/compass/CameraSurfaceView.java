package com.sample.compass;

import java.io.IOException;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder holder;
	private Camera camera;

	public CameraSurfaceView(Context context) {
		super(context);

		// Initiate the Surface Holder properly
		this.holder = this.getHolder();
		this.holder.addCallback(this);
	}

	public CameraSurfaceView(Context context, AttributeSet set) {
		super(context, set);

		// Initiate the Surface Holder properly
		this.holder = this.getHolder();
		this.holder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			// Open the Camera in preview mode
			this.camera = Camera.open();
			Configuration c = getResources().getConfiguration();
			 
			if(c.orientation == Configuration.ORIENTATION_PORTRAIT) {
				this.camera.setDisplayOrientation(90);	
			}

			this.camera.setPreviewDisplay(this.holder);

		} catch (IOException ioe) {
			ioe.printStackTrace(System.out);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		Log.d("Test", "SurfaceChanged");

		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		Camera.Parameters parameters = camera.getParameters();
		List<Camera.Size> csc = parameters.getSupportedPreviewSizes();
		Camera.Size cs = null;
		
		for (Camera.Size s : csc) {
//			Log.i("123", "Height: " + s.height + ", Width: " + s.width);
			if (s.width == 800 && s.height == 480) {
				cs = s;
				break;
			}
		}
		if (cs == null)
			cs = csc.get(csc.size() / 2);

		parameters.setPreviewSize(cs.width, cs.height);
		parameters.set("orientation", "landscape");
        camera.setDisplayOrientation(0);
        parameters.setRotation(0);
		camera.setParameters(parameters);
		camera.startPreview();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when replaced with a new screen
		// Always make sure to release the Camera instance
		camera.setPreviewCallback(null);
		camera.stopPreview();
		camera.release();
		camera = null;
	}

	public Camera getCamera() {
		return this.camera;
	}

	Point screenSize = null;

	@SuppressWarnings("deprecation")
	@TargetApi(13)
	public Point getScreenSize() {
		if (screenSize == null) {
			WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			screenSize = new Point();

			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
				screenSize.x = display.getWidth();
				screenSize.y = display.getHeight();
			} else {
				display.getSize(screenSize);
			}
		}
		return screenSize;
	}
}
