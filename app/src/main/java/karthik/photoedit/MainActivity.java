package karthik.photoedit;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.net.URI;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button start;
    String filename;
    Uri imageUri;
    private static final int REQ_CODE_TAKE_PICTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button)findViewById(R.id.startButton);
        start.setOnClickListener(this);
        isStoragePermissionGranted();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_TAKE_PICTURE &&
                resultCode == RESULT_OK) {
//            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            Uri bmp = imageUri;
            Intent editIntent = new Intent(this,PictureEdit.class);
            editIntent.putExtra("PICTURE", bmp);
//            editIntent.putExtra("PICTURE", bmp.toString());
            startActivity(editIntent);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == start.getId()) {
            filename = Environment.getExternalStorageDirectory().getPath() + "/newtest/testfile.jpg";
            imageUri = Uri.fromFile(new File(filename));
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    imageUri);
            startActivityForResult (cameraIntent, REQ_CODE_TAKE_PICTURE);



//            Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(picIntent, REQ_CODE_TAKE_PICTURE);



//            String filename = Environment.getExternalStorageDirectory().getPath() + "/folder/testfile.jpg";
//            Uri imageUri = Uri.fromFile(new File(filename));
//
//            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
//                    imageUri);
//            startActivityForResult(cameraIntent, REQ_CODE_TAKE_PICTURE);

//            ContentValues values = new ContentValues();
//            values.put(MediaStore.Images.Media.TITLE, "New Picture");
//            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
//            Uri imageUri = getContentResolver().insert(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            startActivityForResult(intent, REQ_CODE_TAKE_PICTURE);
        }


    }
}
