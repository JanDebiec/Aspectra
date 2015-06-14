package de.jandrotek.android.aspectra.libtouch;

import android.graphics.PointF;

/**
 * Created by jan on 12.04.15.
 */
public class TouchHistory {
    /**
     * Holds data related to a touch pointer, including its current position,
     * pressure and historical positions. Objects are allocated through an
     * object pool using {@link #obtain()} and {@link #recycle()} to reuse
     * existing objects.
     */
    //public static final class TouchHistory {

        // number of historical points to store
        public static final int HISTORY_COUNT = 20;

        public float x;
        public float y;
        public float pressure = 0f;
        public String label = null;
        public int oldIndex;
        public float oldX;
        public float deltaX;
        public float oldY;
        public float deltaY;


    // current position in history array
        public int historyIndex = 0;
        public int historyCount = 0;

        // arrray of pointer position history
        public PointF[] history = new PointF[HISTORY_COUNT];

        private static final int MAX_POOL_SIZE = 10;
        private static final android.support.v4.util.Pools.SimplePool<TouchHistory> sPool =
                new android.support.v4.util.Pools.SimplePool<TouchHistory>(MAX_POOL_SIZE);

        public static TouchHistory obtain(float x, float y, float pressure) {
            TouchHistory data = sPool.acquire();
            if (data == null) {
                data = new TouchHistory();
            }

            data.setTouch(x, y, pressure);

            return data;
        }

        public TouchHistory() {

            // initialise history array
            for (int i = 0; i < HISTORY_COUNT; i++) {
                history[i] = new PointF();
            }
        }

        public void setTouch(float x, float y, float pressure) {
            this.x = x;
            this.y = y;
            this.pressure = pressure;
        }

        public void recycle() {
            this.historyIndex = 0;
            this.historyCount = 0;
            sPool.release(this);
        }

        /**
         * Add a point to its history. Overwrites oldest point if the maximum
         * number of historical points is already stored.
         *
         * @param point
         */
        public void addHistory(float x, float y) {
            PointF p = history[historyIndex];
            p.x = x;
            p.y = y;

            historyIndex = (historyIndex + 1) % history.length;

            if (historyCount < HISTORY_COUNT) {
                historyCount++;
            }
        }

        public float getDeltaX(){
            oldIndex = historyIndex - 1;
            if(oldIndex < 0){
                oldIndex = HISTORY_COUNT - 1;
            }
            PointF oldPoint = history[oldIndex];

            oldX = oldPoint.x;
            deltaX = this.x - oldX;
            return deltaX;
        }

        public float getDeltaY(){
            oldIndex = historyIndex - 1;
            if(oldIndex < 0){
                oldIndex = HISTORY_COUNT - 1;
            }
            PointF oldPoint = history[oldIndex];

            oldY = oldPoint.y;
            deltaY = this.y - oldY;
            return deltaY;
        }
    //}
        public float getLastX(){
            return this.x;
        }
}
