package karthik.photoedit;

import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Karthik Admin on 4/23/2017.
 */

public class TouchListener implements View.OnTouchListener {
    PictureEdit pictureEdit;
    GestureDetectorCompat gestureDetectorCompat;
    public TouchListener(PictureEdit pe){
        this.pictureEdit = pe;
        gestureDetectorCompat = new GestureDetectorCompat(this.pictureEdit, new MyGestureListener());
    }

    //change this guy
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        gestureDetectorCompat.onTouchEvent(motionEvent);
        int maskedAction=motionEvent.getActionMasked();
        switch (maskedAction){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                for(int size=motionEvent.getPointerCount(), i = 0; i<size; i++){
                    if(i == (size - 1)) {
                        int id = motionEvent.getPointerId(i);
                        pictureEdit.addPath(id, motionEvent.getX(i), motionEvent.getY(i));
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                for(int size=motionEvent.getPointerCount(), i = 0; i<size; i++){
                    int id = motionEvent.getPointerId(i);
                    pictureEdit.updatePath(id, motionEvent.getX(i), motionEvent.getY(i));
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
//                for(int size=motionEvent.getPointerCount(), i = 0; i<size; i++){
//                    int id = motionEvent.getPointerId(i);
//                    pictureEdit.removePath(id);
//                }
                break;

        }
        return true;
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public void onLongPress(MotionEvent e) {

            pictureEdit.onLongPress(e.getX(), e.getY());
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            pictureEdit.onDoubleTap(e.getX(), e.getY());
            return super.onDoubleTap(e);
        }
    }

}