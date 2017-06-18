package com.baselet.control.basics.geom;

public class Point {

	private int x;
	private int y;

	public Point() {}

	public Point(int x, int y) {
		super();
		this.setX(x);
		this.setY(y);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Point move(int diffX, int diffY) {
		setX(getX() + diffX);
		setY(getY() + diffY);
		return this;
	}

	public double distance(Point o) {
		double distX = (double)o.getX() - getX();
		double distY = (double)o.getY() - getY();
		return Math.sqrt(distX * distX + distY * distY);
	}

	public Point copy() {
		return new Point(getX(), getY());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getX();
		result = prime * result + getY();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Point other = (Point) obj;
		if (getX() != other.getX()) {
			return false;
		}
		if (getY() != other.getY()) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "p(x=" + getX() + ", y=" + getY() + ")";
	}

	public PointDouble toPointDouble() {
		return new PointDouble(getX(), getY());
	}

}
