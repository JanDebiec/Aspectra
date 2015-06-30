//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jjoe64.graphview.series;

import com.jjoe64.graphview.series.DataPointInterface;
import java.io.Serializable;
import java.util.Date;

public class DataPointJan implements DataPointInterface, Serializable {
    private static final long serialVersionUID = 1428263322645L;

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    private double x;
    private double y;

    public DataPointJan(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public DataPointJan() {
        this.x = 0;
        this.y = 0;
    }

    public DataPointJan(Date x, double y) {
        this.x = (double)x.getTime();
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public String toString() {
        return "[" + this.x + "/" + this.y + "]";
    }
}
