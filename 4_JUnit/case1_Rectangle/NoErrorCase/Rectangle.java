package test;

public class Rectangle {
	int width;
	int height;
	
	public int getW() {
		return width;
	}
	
	public int getH() {
		return height;
	}
	
	public void setW(int widths) {
		this.width = widths;
	}
	
	public void setH(int heights) {
		this.height = heights;
	}
	
	public float getA() {
		return height*width;
	}
	
	public String toString() {
		return "Width : " + getW() + "\nHeight : "
		+ getH() + "\nRectangle Area : " + getA(); 
	}
}


