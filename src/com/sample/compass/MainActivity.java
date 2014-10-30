package com.sample.compass;

import java.text.DecimalFormat;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener, LocationListener {

    private float currentOrientation = 0f;
    private static final float ALPHA = 0.25f;
    
    private LocationManager locationManager;
    private String locationProvider;
    private double latitude;
    private double longitude;
    
    private SensorManager sensorManager;
    private PackageManager packageManager; 
//    private ImageView image;
//    private TextView degree;
    private TextView locationBox;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] prevAccelerometer = new float[3];
    private float[] prevMagnetometer = new float[3];
    private boolean prevAccelerometerSet = false;
    private boolean prevMagnetometerSet = false;
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];
    
    private Sensor lAccelerometer;
    private float[] prevAccVal = new float[]{-1,-1,-1};
    private float[] accVal = new float[3];
    private long timestamp;
    private long prevTimestamp = -1;
    double distX = 0;
	double distY = 0;
	double distZ = 0; 
	Context context;
	
    private DrawSurfaceView drawSurfaceView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();

//        image = (ImageView) findViewById(R.id.compassImage);
//        degree = (TextView) findViewById(R.id.orientation);
        locationBox = (TextView) findViewById(R.id.location);
        drawSurfaceView = (DrawSurfaceView) findViewById(R.id.drawSurfaceView);
        locationBox.setText("Current Location: \n");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);        
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);	
        
        packageManager = this.getPackageManager();
        FeatureInfo[] features = packageManager.getSystemAvailableFeatures();
        //check log for list of available features
        for(int i = 0; i < features.length; i++)
        	Log.i("Features", "Feature: " + features[i].name);
        
        drawSurfaceView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				getLocationProvider();
				getLocation();
		        Log.i("Location", "Latitude: " + latitude + "\nLongitude: " + longitude );
		    	locationBox.setText("Latitude: " + latitude + "\nLongitude: " + longitude );
			}
        	
        });
        
        drawSurfaceView = (DrawSurfaceView) findViewById(R.id.drawSurfaceView);
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean hasCompass = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
        boolean hasAccelerometer = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        
        getLocationProvider();
		getLocation();
        Log.i("Location", "Latitude: " + latitude + "\nLongitude: " + longitude );
    	locationBox.setText("Latitude: " + latitude + "\nLongitude: " + longitude );
    	drawSurfaceView.setMyLocation(latitude, longitude);
		AlertDialog ad = new AlertDialog.Builder(this).create();

        if(!hasCompass || !hasAccelerometer){
    			ad.setTitle("Error");
    			ad.setMessage("Device doesn't support Compass.\n The application will exit now.");
    			ad.setCancelable(false);
    			ad.setButton(-3, "OK", new DialogInterface.OnClickListener() {
    				@Override
    				public void onClick(DialogInterface arg0, int arg1) {
    					finish();
    				}				  					  
    			});
    			ad.show();
    		}
        else{        	
        	magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);   
            lAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        	sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
        	sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        	sensorManager.registerListener(this, lAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    	if(event.sensor == lAccelerometer){
			if(prevTimestamp == -1){
				prevAccVal = lowPass(event.values.clone(), prevAccVal);
				prevTimestamp = event.timestamp;
			}
			else{
				accVal = lowPass(event.values.clone(), accVal);		
				timestamp = event.timestamp;
				double accX = accVal[0] - prevAccVal[0];
				double accY = accVal[1] - prevAccVal[1];
				double accZ = accVal[2] - prevAccVal[2];
				if(accX < 0)
					accX = 0;
				if(accY < 0)
					accY = 0;
				if(accZ < 0)
					accZ = 0;
				double t = (timestamp - prevTimestamp)/1000000000.0;
				double t2 = t * t;
				distX += ((0.5) * (accX) * (t2) * 100.0);
				distY += ((0.5) * (accY) * (t2) * 100.0);
				distZ += ((0.5) * (accZ) * (t2) * 100.0);
				prevAccVal[0] = accVal[0];
				prevAccVal[1] = accVal[1];
				prevAccVal[2] = accVal[2];		
				prevTimestamp = timestamp;
				double totalDist = distX + distY + distZ;
				String val = Double.toString(distZ).substring(0, Double.toString(distZ).indexOf("."));
//				Log.i("123", "Dist" + val);
				drawSurfaceView.setDistance(val);
			}
		}		
    	else if (event.sensor == accelerometer) {            
    		prevAccelerometer = lowPass(event.values.clone(), prevAccelerometer);
            prevAccelerometerSet = true;
        } 
    	else if (event.sensor == magnetometer) {
    		prevMagnetometer = lowPass(event.values.clone(), prevMagnetometer);
        	prevMagnetometerSet = true;
        }
        if (prevAccelerometerSet && prevMagnetometerSet) {
            SensorManager.getRotationMatrix(rotationMatrix, null, prevAccelerometer, prevMagnetometer);
            SensorManager.getOrientation(rotationMatrix, orientation);
         
            float azimuthInRadians = orientation[0];
            float pitchInRadians = orientation[1];
            float rollInRadians = orientation[2];
            float azimuthInDegrees = Math.round((Math.toDegrees(azimuthInRadians)));
            float pitchInDegrees = Math.round((Math.toDegrees(pitchInRadians)));
            float rollInDegrees = Math.round((Math.toDegrees(rollInRadians)));
            
//            Log.i("Rotation", "Azimuth: " + azimuthInDegrees + "Pitch: " + pitchInDegrees + "Roll: " + rollInDegrees);
            
            if(drawSurfaceView != null){
            	drawSurfaceView.setRotationValues(azimuthInDegrees, pitchInDegrees, rollInDegrees);
            	drawSurfaceView.invalidate();
            }

            RotateAnimation ra = new RotateAnimation(currentOrientation, -azimuthInDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
     
            ra.setDuration(250);
            ra.setFillAfter(true);
//            image.startAnimation(ra);
            currentOrientation = -azimuthInDegrees;
        }
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    
    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;     
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }
    
    private void getLocationProvider(){
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);        	        	        
        				
        Criteria criteria = new Criteria();        
        locationProvider = locationManager.getBestProvider(criteria, true);
//        Log.i("LocationProvider", locationProvider);
        boolean providerEnabled = locationManager.isProviderEnabled(locationProvider);
        if(!providerEnabled){
        	Toast.makeText(getBaseContext(), locationProvider + " disabled", Toast.LENGTH_LONG).show();
        }                                
	}
    
    private void getLocation(){
		locationManager.removeUpdates(this);					
		locationManager.requestLocationUpdates(locationProvider, 0, 0, this);
		Location location = locationManager.getLastKnownLocation(locationProvider);		
		
		if(location != null){
			DecimalFormat f = new DecimalFormat("##.00");
			latitude = Double.parseDouble(f.format(location.getLatitude()));
			longitude = Double.parseDouble(f.format(location.getLongitude()));
//			latitude = location.getLatitude();
//			longitude = location.getLongitude();
        }
        else{
        	Toast.makeText(getBaseContext(), "Cannot fetch Current Location", Toast.LENGTH_LONG).show();
        	return;
        }
}

	@Override
	public void onLocationChanged(Location location) {
		getLocation();
//        Log.i("LocationChanged", "Latitude: " + latitude + "\nLongitude: " + longitude );
        drawSurfaceView.setMyLocation(latitude, longitude);
    	locationBox.setText("Latitude: " + latitude + "\nLongitude: " + longitude );
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}
	
	public void onNavigateButtonClick(View view) throws InterruptedException
	{
			//boolean db_Status = dbUtil.checkDatabaseRunning();
		
		
		new DatabaseUtil().execute(context);
	}
}