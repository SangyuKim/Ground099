package com.android.ground.ground.custom;

import android.graphics.PointF;

/**
 * Created by Tacademy on 2015-11-30.
 */
public class BezierQ {

    private PointF start;
    private PointF middle;
    private PointF end;

    public BezierQ() {
        start = new PointF();
        middle = new PointF();
        end = new PointF();
    }

    public PointF getStart() {
        return start;
    }

    public void setStart(PointF start) {
        this.start = start;
    }

    public PointF getMiddle() {
        return middle;
    }

    public void setMiddle(PointF middle) {
        this.middle = middle;
    }

    public PointF getEnd() {
        return end;
    }

    public void setEnd(PointF end) {
        this.end = end;
    }

    public BezierQ middleOffset(float dx, float dy) {
        this.middle.x += dx;
        this.middle.y += dy;
        return this;
    }

}
