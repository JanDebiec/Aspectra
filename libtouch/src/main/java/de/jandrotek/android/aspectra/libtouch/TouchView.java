package de.jandrotek.android.aspectra.libtouch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jan on 09.04.15.
 */
public class TouchView extends View {

    OnTouchViewInteractionListener mCallback;

    public static final int ePlotAction_Idle = 0;
    public static final int ePlotAction_Move = 1;
    public static final int ePlotAction_Stretch = 2;

    private int plotAction = ePlotAction_Idle;
    // Hold data for active touch pointer IDs
    private SparseArray<TouchHistory> mTouchesHistory;

    private int mMeasureX;
    private int mMeasureY;

    // Is there an active touch?
    private boolean mHasTouch = false;
    private float deltaXStart = 0f;


    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // SparseArray for touch events, indexed by touch id
        mTouchesHistory = new SparseArray<TouchHistory>(10);
        mCallback = (OnTouchViewInteractionListener)context;

       // initialisePaint();
    }

//    @Override
//    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mMeasureX = getMeasuredWidth();
//        mMeasureY = getMeasuredHeight();
//
//        setMeasuredDimension (mMeasureX, mMeasureY);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int action = event.getAction();
        float deltaX = 0f;
        float deltaXAbs = 0f;
        float deltaXAbsStart = 0f;
        //float deltaMove = 0f;
        float x0 = 0;
        float x1 = 0;
        int viewWidthX = getWidth();

        /*
         * Switch on the action. The action is extracted from the event by
         * applying the MotionEvent.ACTION_MASK. Alternatively a call to
         * event.getActionMasked() would yield in the action as well.
         */
        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN: {
                // first pressed gesture has started

                /*
                 * Only one touch event is stored in the MotionEvent. Extract
                 * the pointer identifier of this touch from the first index
                 * within the MotionEvent object.
                 */
                int id = event.getPointerId(0);

                TouchHistory data = TouchHistory.obtain(event.getX(0), event.getY(0),
                        event.getPressure(0));
                data.label = "id: " + 0;

                /*
                 * Store the data under its pointer identifier. The pointer
                 * number stays consistent for the duration of a gesture,
                 * accounting for other pointers going up or down.
                 */
                mTouchesHistory.put(id, data);

                mHasTouch = true;
                deltaXStart = 0f;

                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                /*
                 * A non-primary pointer has gone down, after an event for the
                 * primary pointer (ACTION_DOWN) has already been received.
                 */

                /*
                 * The MotionEvent object contains multiple pointers. Need to
                 * extract the index at which the data for this particular event
                 * is stored.
                 */

                //int pointersCount = event.getPointerCount();

                int index = event.getActionIndex();
                int id = event.getPointerId(index);

                TouchHistory data = TouchHistory.obtain(event.getX(index), event.getY(index),
                        event.getPressure(index));
                data.label = "id: " + id;

                // add previous position to history and add new values
                data.addHistory(data.x, data.y);
                data.setTouch(event.getX(index), event.getY(index),
                        event.getPressure(index));
               /*
                 * Store the data under its pointer identifier. The index of
                 * this pointer can change over multiple events, but this
                 * pointer is always identified by the same identifier for this
                 * active gesture.
                 */

                if (index == 1) { // second touch

                    // defining x0 and deltaStart
                    // get the data stored externally about this pointer.
                    TouchHistory data0 = mTouchesHistory.get(0);
                    x0 = data0.getLastX();

                    x1 = event.getX(index);

                    deltaXStart = x1 - x0;

                }
                mTouchesHistory.put(id, data);
                plotAction = ePlotAction_Idle;
                break;
            }

            case MotionEvent.ACTION_UP: {
                /*
                 * Final pointer has gone up and has ended the last pressed
                 * gesture.
                 */

                /*
                 * Extract the pointer identifier for the only event stored in
                 * the MotionEvent object and remove it from the list of active
                 * touches.
                 */
                int id = event.getPointerId(0);
                TouchHistory data = mTouchesHistory.get(id);
                mTouchesHistory.remove(id);
                data.recycle();

                mHasTouch = false;
                plotAction = ePlotAction_Idle;

                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                /*
                 * A non-primary pointer has gone up and other pointers are
                 * still active.
                 */

                /*
                 * The MotionEvent object contains multiple pointers. Need to
                 * extract the index at which the data for this particular event
                 * is stored.
                 */
                int pointersCount = event.getPointerCount();

                int index = event.getActionIndex();
                int id = event.getPointerId(index);

                TouchHistory data = mTouchesHistory.get(id);
                mTouchesHistory.remove(id);
                data.recycle();

                if(pointersCount == 2) {
                    plotAction = ePlotAction_Stretch;
                } else if(pointersCount == 1) {
                    plotAction = ePlotAction_Move;
                }

                break;
            }

            case MotionEvent.ACTION_MOVE: {
                /*
                 * A change event happened during a pressed gesture. (Between
                 * ACTION_DOWN and ACTION_UP or ACTION_POINTER_DOWN and
                 * ACTION_POINTER_UP)
                 */

                /*
                 * Loop through all active pointers contained within this event.
                 * Data for each pointer is stored in a MotionEvent at an index
                 * (starting from 0 up to the number of active pointers). This
                 * loop goes through each of these active pointers, extracts its
                 * data (position and pressure) and updates its stored data. A
                 * pointer is identified by its pointer number which stays
                 * constant across touch events as long as it remains active.
                 * This identifier is used to keep track of a pointer across
                 * events.
                 */

                int pointersCount = event.getPointerCount();
                if(pointersCount == 2) {
                    plotAction = ePlotAction_Stretch;
                } else if(pointersCount == 1) {
                    plotAction = ePlotAction_Move;
                }

                for (int index = 0; index < pointersCount; index++) {
                    // get pointer id for data stored at this index
                    int id = event.getPointerId(index);

                    // get the data stored externally about this pointer.
                    TouchHistory data = mTouchesHistory.get(id);

                    // add previous position to history and add new values
                    data.addHistory(data.x, data.y);
                    data.setTouch(event.getX(index), event.getY(index),
                            event.getPressure(index));
                    if(id == 0){
                        x0 = event.getX(index);
                        deltaX = data.getDeltaX();
                        deltaXAbs = deltaX;
                    }else if(id == 1){
                        x1 = event.getX(index);
                        deltaX = x1 - x0;

                        deltaXAbs = deltaX - deltaXStart;
                    }
                }
                break;
            }
        }

        //TODO: calculate callback paramterers
        //viewWidthX
        mCallback.onTouchViewInteraction(plotAction, deltaXAbs);
        return true;
    }

    public interface OnTouchViewInteractionListener {
        // TODO: Update argument type and name
        void onTouchViewInteraction(int _toolId, float _value);
    }


}
