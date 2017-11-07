package karthik.photoedit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Karthik Admin on 4/23/2017.
 */

public class MyCanvas extends View {

    HashMap<Integer, Path> activePaths;
    HashMap<Integer, Integer> pathColors;
    Stack<Integer>undoStack;
    Stack<Integer>lineUndoStack;
    int[] fingerTrack;

    ArrayList xVals;
    ArrayList yVals;
    ArrayList imagesList;


    Paint pathPaint;
    int idx;
    int colorIndex;
    int lastID;


    //change to a stack
    public MyCanvas(Context context, AttributeSet attr){
        super(context, attr);
        activePaths = new HashMap<>();
        pathColors = new HashMap<>();

        xVals = new ArrayList();
        yVals = new ArrayList();
        imagesList = new ArrayList();
        undoStack = new Stack<>();
        lineUndoStack = new Stack<>();


        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setColor(Color.RED);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(70);
        fingerTrack = new int[] {0,100,200,300,400};
//        fingerTrack = {0,0,0,0,0};
        idx = 0;


    }

    public void changeColor(int color){
            colorIndex = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(Path path : activePaths.values()){
            int colorToSet = pathColors.get(path.hashCode());
            if(colorToSet == 0){
                pathPaint.setColor(Color.RED);
            }
            if(colorToSet == 1){
                pathPaint.setColor(Color.BLUE);
            }
            if(colorToSet == 2){
                pathPaint.setColor(Color.GREEN);
            }
            canvas.drawPath(path, pathPaint);
        }
        int i = 0;
        for (i = 0; i< xVals.size(); i++){
            if(imagesList.get(i).equals(0)){
                Bitmap okImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.ok);
                canvas.drawBitmap(okImage,(float) xVals.get(i), (float)yVals.get(i), null);
            }
            else if(imagesList.get(i).equals(1)){

                Bitmap okImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.star);
                canvas.drawBitmap(okImage,(float) xVals.get(i), (float)yVals.get(i), null);

            }

        }


        super.onDraw(canvas);
    }


    protected void drawImageHold(float X, float Y){

        xVals.add(X);
        yVals.add(Y);
        imagesList.add(0);
        invalidate();
        undoStack.push(1);

    }

    protected void drawImageDouble(float X, float Y){

        xVals.add(X);
        yVals.add(Y);
        imagesList.add(1);
        invalidate();
        undoStack.push(1);

    }

    public void addPath(int id, float x, float y){
        Path path = new Path();
        System.out.println("id in add " + id);
        path.moveTo(x,y);
        activePaths.put(fingerTrack[id], path);
        pathColors.put(path.hashCode(), colorIndex);
        fingerTrack[id]++;
        lineUndoStack.push(fingerTrack[id]);
        undoStack.push(0);
        invalidate();


    }

    public void updatePath(int id, float x, float y){
//        Path path = activePaths.get(id);
        System.out.println("id in update " + id);
        Path path = activePaths.get(fingerTrack[id] - 1);

        if(path != null){

            path.lineTo(x,y);
        }
        invalidate();
    }

    public void clearPaths(){
        activePaths.clear();
        xVals.clear();
        yVals.clear();
        imagesList.clear();
        invalidate();
    }

    public void undoHandler (){
//        if(activePaths.containsKey(id)){
//            activePaths.remove(id);
//        }
        if(!undoStack.empty()) {
            if (undoStack.peek() == 0) {
                if(!lineUndoStack.empty()) {
                    lastID = lineUndoStack.peek();
                    lineUndoStack.pop();
                    activePaths.remove(lastID - 1);
                    undoStack.pop();
                }
            }
            else if (undoStack.peek() == 1) {

                if(!xVals.isEmpty() && !yVals.isEmpty() && !imagesList.isEmpty()) {
                    xVals.remove(xVals.get(xVals.size() - 1));
                    yVals.remove(yVals.get(yVals.size() - 1));
                    imagesList.remove(imagesList.get(imagesList.size() - 1));
                    undoStack.pop();
                }
            }

            invalidate();
        }
    }

}