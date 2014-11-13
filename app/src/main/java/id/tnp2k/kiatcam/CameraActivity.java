package id.tnp2k.kiatcam;

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.media.ExifInterface;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mCameraPreview;

    private byte[] mCameraData;
    private Bitmap mCameraBitmap;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private String curTeacher;
    private boolean mIsCapturing;
    public static final String EXTRA_CAMERA_DATA = "camera_data";
    static final int DONE_CAMERA = 99;


    private OnClickListener mCaptureButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // get an image from the camera
            mCamera.takePicture(null, null, mPicture);

        }
    };

    public void DonePhoto() {
        if (mCameraData != null) {
            Intent intent = new Intent(this,PreviewActivity.class);
            intent.putExtra(EXTRA_CAMERA_DATA, mCameraData);
            setResult(RESULT_OK, intent);
            startActivityForResult(intent,DONE_CAMERA);
            finish();
        } else {
            setResult(RESULT_CANCELED);
        }

    }

    private OnClickListener mBackButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // kill activity
            CameraActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        TextView text=(TextView)findViewById(R.id.TeacherCaption);
        Bundle b = getIntent().getExtras();
        curTeacher = b.getString("current");
        text.setText(curTeacher); //set teacher name
        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mCameraPreview);
        mIsCapturing = true;
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(mCaptureButtonClickListener);
        Button backButton = (Button) findViewById(R.id.button_back);
        backButton.setOnClickListener(mBackButtonClickListener);
    }

    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
            } catch (Exception e) {
        // cannot get camera or does not exist
        }
        return camera;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            mCameraData = data;
            //Bitmap bitmap = BitmapFactory.decodeByteArray(mCameraData,0,mCameraData.length);
            DonePhoto();
            //mCamera.stopPreview();
            /*
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: " +
                        e.getMessage());
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                // Reset the stream of 'output' for output writing.
                // Compress current 'Bitmap' to 'output' as JPEG format
                //bmp.compress(Bitmap.CompressFormat.JPEG, 95, fos);
                //mCameraBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.write(data);
                //fos.flush();
                fos.close();
                ExifInterface exifInt = new ExifInterface(pictureFile.getAbsolutePath()); //try exif
                exifInt.setAttribute("UserComment",curTeacher);
                exifInt.saveAttributes();
                mCamera.startPreview();
            } catch (FileNotFoundException e) {
                //Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                //Log.d(TAG, "Error accessing file: " + e.getMessage());
            }*/

        }
    };



    /** Create a file Uri for saving an image or video */


    /** Create a File for saving an image or video */


}
