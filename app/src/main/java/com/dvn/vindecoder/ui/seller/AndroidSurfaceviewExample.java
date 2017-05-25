
package com.dvn.vindecoder.ui.seller;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dvn.vindecoder.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AndroidSurfaceviewExample extends Activity implements SurfaceHolder.Callback {
	TextView testView;

	Camera camera;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;

	PictureCallback rawCallback;
	ShutterCallback shutterCallback;
	PictureCallback jpegCallback;
	protected static final int CAMERA_REQUEST = 0;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_surface);

		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceHolder = surfaceView.getHolder();

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		surfaceHolder.addCallback(this);

		// deprecated setting, but required on Android versions prior to 3.0
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		jpegCallback = new PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) {
				FileOutputStream outStream = null;
				try {
					outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
					outStream.write(data);
					outStream.close();
					Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
				}
				Toast.makeText(getApplicationContext(), "Picture Saved", 2000).show();
				refreshCamera();
			}
		};
	}

	private void openCameraApplication() {
		Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (picIntent.resolveActivity(getPackageManager())!= null){
			startActivityForResult(picIntent, CAMERA_REQUEST);
		}
	}

	public void captureImage(View v) throws IOException {
		//take the picture
		camera.takePicture(null, null, jpegCallback);
	}

	public void refreshCamera() {
		if (surfaceHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			camera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here
		// start preview with new settings
		try {
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();
		} catch (Exception e) {

		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		refreshCamera();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			// open the camera
			camera = Camera.open();
		} catch (RuntimeException e) {
			// check for exceptions
			System.err.println(e);
			return;
		}
		Camera.Parameters param;
		param = camera.getParameters();

		// modify parameter
		param.setPreviewSize(352, 288);
		camera.setParameters(param);
		try {
			// The Surface has been created, now tell the camera where to draw
			// the preview.
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();
		} catch (Exception e) {
			// check for exceptions
			System.err.println(e);
			return;
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// stop preview and release camera
		camera.stopPreview();
		camera.release();
		camera = null;
	}

}