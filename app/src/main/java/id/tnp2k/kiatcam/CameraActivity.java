package id.tnp2k.kiatcam;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private Bitmap mCameraBitmap;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static String curTeacher;
    private static final int FINISH_PHOTO_REQ = 99;


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
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK,info);
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int rotate = (info.orientation - degrees + 360) % 360;
        Camera.Parameters params = mCamera.getParameters();
        params.setRotation(rotate);
        mCamera.setParameters(params);
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera

                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );
        Button backButton = (Button) findViewById(R.id.button_back);
        backButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // kill activity
                        CameraActivity.this.finish();
                    }
                }
        );
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

    private void toFinish(){
        Intent intent = new Intent(this, FinishActivity.class);
        Bundle b  = new Bundle();
        b.putString("current",curTeacher);
        if (intent.getExtras() == null) {
            intent.putExtras(b);
        } else {
            intent.replaceExtras(b);
        }

        startActivityForResult(intent,FINISH_PHOTO_REQ);
    }

    private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){/*
                Log.d(TAG, "Error creating media file, check storage permissions: " +
                        e.getMessage());*/
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                //mCameraBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.write(data);
                fos.close();
                ExifInterface exifInt = new ExifInterface(pictureFile.getAbsolutePath()); //try exif
                exifInt.setAttribute("UserComment",curTeacher);
                exifInt.saveAttributes();
                toFinish();
                //mCamera.startPreview();
            } catch (FileNotFoundException e) {
                /*Log.d(TAG, "File not found: " + e.getMessage());*/
            } catch (IOException e) {
                /*Log.d(TAG, "Error accessing file: " + e.getMessage());*/
            }

        }
    };



    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "KIATcam");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("KIATcam", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        //String  = new SimpleDateFormat("DD").format(new Date());
        Calendar c = Calendar.getInstance();
        int tanggal = c.get(Calendar.DATE);
        int bulan = c.get(Calendar.MONTH);
        String path = File.separator + "var" + File.separator + "temp";
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            String photopath = mediaStorageDir.getPath();
            mediaFile = new File(photopath + File.separator + "KIAT_"+ timeStamp + ".jpg");

        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}
