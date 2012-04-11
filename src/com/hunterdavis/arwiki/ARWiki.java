package com.hunterdavis.arwiki;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class ARWiki extends Activity implements
CameraCallback {
	
	private FrameLayout cameraholder = null;
	private CameraSurface camerasurface = null;
	WebView engine;
	
	private class HelloWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// Hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// hide the notification bar. That way, we can use the full screen.
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		cameraholder = (FrameLayout) findViewById(R.id.camera_preview);

		setupPictureMode();
		
		navigateToWiki();

    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && engine.canGoBack()) {
            engine.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private void navigateToWiki() {
    	engine = (WebView) findViewById(R.id.web_engine);  
    	engine.setWebViewClient(new HelloWebViewClient());
    	engine.setBackgroundColor(0);
    	engine.getSettings().setJavaScriptEnabled(true);
    	engine.loadUrl("http://www.wikipedia.org"); 
    }
    
	private void setupPictureMode() {
		camerasurface = new CameraSurface(this);

		cameraholder.addView(camerasurface, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		camerasurface.setCallback(this);
	}
	
	@Override
	public void onJpegPictureTaken(byte[] data, Camera camera) {
		try {
			FileOutputStream outStream = new FileOutputStream(String.format(
					"/sdcard/%d.jpg", System.currentTimeMillis()));

			outStream.write(data);
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		camerasurface.startPreview();
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// Log.d("preview frame called","preview frame!");
		/*
		int width = camera.getParameters().getPreviewSize().width;
		int height = camera.getParameters().getPreviewSize().height;
		YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, width, height,
				null);
		Rect rect = new Rect(0, 0, width, height);
		ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
		yuvimage.compressToJpeg(rect, 100, output_stream);
		Bitmap imagePreviewBitmap = BitmapFactory.decodeByteArray(
				output_stream.toByteArray(), 0, output_stream.size());
	    */
	}
	
	@Override
	public void onRawPictureTaken(byte[] data, Camera camera) {
	}

	@Override
	public void onShutter() {
	}
	
	@Override
	public String onGetVideoFilename() {
		String filename = String.format("/sdcard/%d.3gp",
				System.currentTimeMillis());

		return filename;
	}
	
	
    
}