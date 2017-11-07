package karthik.photoedit;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.Touch;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class PictureEdit extends AppCompatActivity implements View.OnClickListener{

    MyCanvas myCanvas;
    TouchListener touchListener;
    static final int REQUEST_IMAGE = 1;
    Button redbt;
    Button bluebt;
    Button greenbt;
    Button undobt;
    Button clearbt;
    Button donebt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_edit);
//        ImageView backgroundImage = (ImageView)findViewById(R.id.myCanvas);
        Intent myIntent = getIntent();

        redbt = (Button) findViewById(R.id.redButton);
        bluebt = (Button) findViewById(R.id.blueButton);
        greenbt = (Button) findViewById(R.id.greenButton);
        undobt = (Button) findViewById(R.id.undoButton);
        clearbt = (Button) findViewById(R.id.clearButton);
        donebt = (Button) findViewById(R.id.doneButton);

        redbt.setOnClickListener(this);
        bluebt.setOnClickListener(this);
        greenbt.setOnClickListener(this);
        undobt.setOnClickListener(this);
        clearbt.setOnClickListener(this);
        donebt.setOnClickListener(this);



        myCanvas = (MyCanvas) findViewById(R.id.myCanvas);
        touchListener=new TouchListener(this);
        myCanvas.setOnTouchListener(touchListener);


//        Bitmap myPic = myIntent.getParcelableExtra("PICTURE");
//        myCanvas.setBackground(new BitmapDrawable(getResources(), myPic));

//        Uri myPic = Uri.parse(myIntent.getStringExtra("PICTURE"));
        Uri myPic = myIntent.getParcelableExtra("PICTURE");
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), myPic);
            ExifInterface exif = new ExifInterface(myPic.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            if(orientation == 6)
                matrix.postRotate(90);
            else if(orientation == 3)
                matrix.postRotate(180);
            else if(orientation == 8)
                matrix.postRotate(270);

           bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//            myCanvas.setBackground(new BitmapDrawable(getResources(), bitmap));
            myCanvas.setBackground(new BitmapDrawable(getResources(), bitmap));
            //myCanvas.setRotation(90);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        InputStream image_stream = null;
//        try {
//            image_stream = getContentResolver().openInputStream(myPic);
//            Bitmap bitmap= BitmapFactory.decodeStream(image_stream );
//            myCanvas.setBackground(new BitmapDrawable(bitmap));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

//        backgroundImage.setImageBitmap(myPic);
//
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inSampleSize = 4;
//        Bitmap bitmap = BitmapFactory.decodeResource(myPic, 1, opts);
//        backgroundImage.setImageBitmap (bitmap);
    }

    public void addPath(int id, float x, float y){
        myCanvas.addPath(id, x, y);
    }

    public void undoHandler(int id){
        myCanvas.undoHandler();
    }
    public void updatePath(int id, float x, float y){
        myCanvas.updatePath(id, x, y);
    }
    public void changeColor(int color){
        myCanvas.changeColor(color);
    }
    Random random = new Random();
    public void onLongPress(float x, float y) {
        myCanvas.drawImageHold(x, y);



//        Intent takePicIntenet = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(takePicIntenet.resolveActivity(getPackageManager()) != null){
//            startActivityForResult(takePicIntenet, REQUEST_IMAGE);
//        }
    }

    public  void onDoubleTap(float x, float y) {
        myCanvas.drawImageDouble(x, y);
//        myCanvas.setBackgroundColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));}
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == redbt.getId()){
            changeColor(0);
        }
        if(v.getId() == bluebt.getId()){
            changeColor(1);
        }
        if(v.getId() == greenbt.getId()){
            changeColor(2);
        }
        if(v.getId() == clearbt.getId()){
            myCanvas.clearPaths();
        }
        if(v.getId() == undobt.getId()){
            myCanvas.undoHandler();
        }
        if(v.getId() == donebt.getId()){
            finish();
        }
    }
}
