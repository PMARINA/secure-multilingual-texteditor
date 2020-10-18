package gutil;

/**
 * @author Dov Kruger
 * Does layout like the original Motif from 30 years ago.
 * Works much better than the default Java layouts.
 * This object contains the constraints
 * each coordinate is %of parent rectangle + delta in pixels
 */
public class PercentInfo {
    public float px1, py1, dx1, dy1, px2, py2, dx2, dy2;
    public PercentInfo(float px1, float py1, float dx1, float dy1, float px2,
                       float py2, float dx2, float dy2) {
        this.px1 = px1;
        this.py1 = py1;
        this.dx1 = dx1;
        this.dy1 = dy1;
        this.px2 = px2;
        this.py2 = py2;
        this.dx2 = dx2;
        this.dy2 = dy2;
    }
   
    public PercentInfo(double px1, double py1, double dx1, double dy1, double px2,
                       double py2, double dx2, double dy2) {
        this((float)px1, (float)py1, (float)dx1, (float)dy1,(float)px2, (float)py2, (float)dx2, (float)dy2);
    }
    
    public String toString() {
        return px1 + " " + py1 + " " + dx1 + " " + dy1 + " " + px2 + " " + py2 + " " + dx2 + " " + dy2;
    }
 }
