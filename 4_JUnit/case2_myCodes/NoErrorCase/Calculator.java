package test;

public class Calculator {
	double x,x1,y1;
	double y,x2,y2;
	double distance;
	
	public double getX() {
		x= Math.pow(x1-x2, 2);
		return x;
	}
	
	public double getY() {
		y = Math.pow(y1-y2, 2);
		return y;
	}
	
	public void setX(double X1, double X2) {
		this.x1 = X1;
		this.x2 = X2;
	}
	
	public void setY(double Y1, double Y2) {
		this.y1 = Y1;
		this.y2 = Y2;
	}
	
	public double getDistance() {
		return Math.sqrt(getX()+getY());
	}
	
	public String toString() {
		return "(x1,y1) : (" +this.x1+","+this.y1+")"+
		"   (x2,y2) : (" +this.x2+","+this.y2+")"
		+ "\nDistance between two points : " + getDistance();
	}
}














