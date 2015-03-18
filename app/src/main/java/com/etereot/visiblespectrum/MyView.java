package com.etereot.visiblespectrum;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;


class MyView extends SurfaceView implements SurfaceHolder.Callback {
    class MyThread extends Thread {




        private Handler mHandler;

        private SurfaceHolder mSurfaceHolder;

        /*
         * State-tracking constants
         */

        public static final int STATE_PAUSE = 1;
        public static final int STATE_READY =2;
        public static final int STATE_RUNNING = 3;


        private int mMode;

        private boolean mRun = false;

        private final Object mRunLock = new Object();



        private Paint mPaint;
        private Paint lPaint;

        private Triangle mTriangle;

        private PointF vTriangle1;
        private PointF vTriangle2;
        private PointF vTriangle3;



        private Luz mLuz;

        private ArrayList Objetos;

        //coordenadas iniciales de la luz

        private PointF iLuz;

        private double iLuzx;
        private double iLuzy;







        public MyThread(SurfaceHolder surfaceHolder, Context context,
                        Handler handler) {

            mSurfaceHolder = surfaceHolder;
            mHandler = handler;
            mContext = context;


            iLuz = new PointF();

            mLuz = new Luz();


            //Orden importante, de izquierda a derecha y de abajo a arriva
            vTriangle3 = new PointF(100,200);
            vTriangle1 = new PointF(50 ,75);
            vTriangle2 = new PointF(150,75);


            mTriangle = new Triangle();

            mPaint = new Paint();
            mPaint.setARGB(250, 0, 0, 254);
            mPaint.setStrokeWidth(2);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setAntiAlias(true);

            lPaint = new Paint();
            mPaint.setARGB(200, 255, 255, 255);
            mPaint.setStrokeWidth(1);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setAntiAlias(true);





            /**Array para guardar todo objeto creado para ver las colisiones,
             * mas tarde debiera de ser un arbol que colocara cada objeto
             * segun el cuadrante que le toque
             */
            Objetos = new ArrayList(3);
            Objetos.add(mTriangle);









         }



        public void doStart(){
            synchronized (mSurfaceHolder) {

                setState(STATE_RUNNING);

            }

        }

        public void pause() {
            synchronized (mSurfaceHolder) {
                if (mMode == STATE_RUNNING) setState(STATE_PAUSE);
            }
        }

        /**
         * Restores game state from the indicated Bundle. Typically called when
         * the Activity is being restored after having been previously
         * destroyed.
         *
         * @param savedState Bundle containing the game state
         */
        public synchronized void restoreState(Bundle savedState) {


        }

        public Bundle saveState(Bundle map){
            synchronized (mSurfaceHolder) {

            }

            return map;

        }

        @Override
        public void run() {

            this.setGeometry();

            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        if (mMode == STATE_RUNNING) updatePhysics();
                        //  Do not allow mRun to be set false until
                        // we are sure all canvas draw operations are complete.
                        //
                        // If mRun has been toggled false, inhibit canvas operations.
                        synchronized (mRunLock) {
                            if (mRun) doDraw(c);
                        }
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        private void setGeometry(){

            mTriangle.setTriangle(vTriangle1,vTriangle2,vTriangle3);

            iLuzx = mCanvasWidth/2;
            iLuzy = mCanvasHeight - 10;
            iLuz.set((float)iLuzx,(float)iLuzy);

            mLuz.setLuz(iLuz);


        }



        /**
         * Sets the game mode.
         */
        public void setState(int mode) {
            synchronized (mSurfaceHolder) {
                setState(mode, null);
            }
        }

        public void setState(int mode, CharSequence message) {
            /*
             *  We cant touch the view, so
             * Instead we use a Message + Handler to relay commands to the main
             * thread, which updates the user-text View.
             */
            synchronized (mSurfaceHolder) {
                mMode = mode;

                if (mMode == STATE_RUNNING) {
                    Message msg = mHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("text", "");
                    b.putInt("viz", View.INVISIBLE);
                    msg.setData(b);
                    mHandler.sendMessage(msg);
                } else {


                    Resources res = mContext.getResources();
                    CharSequence str = "";
                    if (mMode == STATE_READY)
                        str = res.getText(R.string.mode_ready);
                    else if (mMode == STATE_PAUSE)
                        str = res.getText(R.string.mode_pause);
                    if (message != null) {
                        str = message + "\n" + str;
                    }



                    Message msg = mHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("text", str.toString());
                    b.putInt("viz", View.VISIBLE);
                    msg.setData(b);
                    mHandler.sendMessage(msg);
                }
            }
        }



        public void setRunning(boolean b) {
            // Do not allow mRun to be modified while any canvas operations
            // are potentially in-flight..
            synchronized (mRunLock) {
                mRun = b;
            }
        }

        /* Callback invoked when the surface dimensions change. */
        public void setSurfaceSize(int width, int height) {
            // synchronized to make sure these all change atomically
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;



            }
        }



        public void unpause(){

        }


        public void doDraw(Canvas canvas){
            canvas.drawColor(0xFF000000);

            mTriangle.drawTriangle(canvas,mPaint);
            mLuz.drawLuz(canvas,lPaint);
            //canvas.drawCircle(mCanvasWidth/2,mCanvasHeight/2,123,mPaint);

        }

        public void updatePhysics(){
            mLuz.UpdateLuz(Objetos);


        }





    }

    public static int mCanvasHeight = 1;

    public static int mCanvasWidth = 1;

    private Context mContext;

    /** Pointer to the text view to display "Paused.." etc. */
    private TextView mStatusText;

    /** The thread that actually draws the animation */
    private MyThread thread;

    public MyView(Context context, AttributeSet attrs){

        super(context,attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        // create thread only; it's started in surfaceCreated()
        thread = new MyThread(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {
                mStatusText.setVisibility(m.getData().getInt("viz"));
                mStatusText.setText(m.getData().getString("text"));
            }
        });

        setFocusable(true);

    }

    public MyThread getThread() {
        return thread;
    }


    /**
     * Standard override to get key-press events.
     */
   // @Override
   // public boolean onKeyDown(int keyCode, KeyEvent msg) {
   //     return thread.doKeyDown(keyCode, msg);
   // }


    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) thread.pause();
    }

    /**
     * Installs a pointer to the text view used for messages.
     */
    public void setTextView(TextView textView) {
        mStatusText = textView;
    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        thread.setSurfaceSize(width, height);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        thread.setRunning(true);
        thread.start();
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}




