package com.phoenixkahlo.physics;

public class Segment {

	/*
	 * Defined by
	 * y=ax+b & x>min & x<max
	 * Unless a is NaN, in which case is defined by
	 * x=b & y>min & y<max
	 */
	private float a;
	private float b;
	private float min;
	private float max;
	
	/**
	 * Creates non-vertical segment.
	 */
	public Segment(float slope, float yIntercept, float xMin, float xMax) {
		this.a = slope;
		this.b = yIntercept;
		this.min = xMin;
		this.max = xMax;
	}
	
	/**
	 * Creates vertical segment.
	 */
	public Segment(float x, float yMin, float yMax) {
		this(Float.NaN, x, yMin, yMax);
	}
	
	/**
	 * Creates segment between 2 points.
	 */
	public Segment(Vector2f p1, Vector2f p2) throws IllegalArgumentException {
		if (p1.x == p2.x) {
			a = Float.NaN;
			b = p1.x;
			if (p1.y > p2.y) {
				max = p1.y;
				min = p2.y;
			} else if (p1.y < p2.y) {
				min = p1.y;
				max = p2.y;
			} else {
				throw new IllegalArgumentException(p1 + " and " + p2 + " are the same");
			}
		} else {
			a = (p2.y - p1.y) / (p2.x - p1.x);
			b = (p2.y * p1.x - p1.y * p2.x) / (p1.x - p2.x);
			if (p1.x > p2.x) {
				max = p1.x;
				min = p2.x;
			} else {
				min = p1.x;
				max = p2.x;
			}
		}
	}
	
	/**
	 * Constructs a segment that has the slope slope and passes through point.
	 */
	public Segment(float slope, Vector2f point, float min, float max) {
		this.a = slope;
		this.min = min;
		this.max = max;
		if (Float.isNaN(slope)){
			b = point.x;
		} else {
			b = -a * point.x + point.y;
		}
	}
	
	public float getMin() {
		return min;
	}
	
	public float getMax() {
		return max;
	}
	
	
	/**
	 * Calculate as if this were a line. Returns NaN if vertical.
	 */
	public float yAt(float x) {
		return x * a + b;
	}
	
	/**
	 * Calculate as if this were a line. Returns NaN if vertical.
	 */
	public float xAt(float y) {
		return (y - b) / a;
	}
	
	public boolean isVertical() {
		return Float.isNaN(a);
	}
	
	public boolean isAbove(Vector2f point) {
		return point.y > yAt(point.x);
	}
	
	public boolean isBelow(Vector2f point) {
		return point.y < yAt(point.x);
	}
	
	/**
	 * @return the point on this segment closest to point. 
	 */
	public Vector2f closestPointTo(Vector2f point) {
		if (isVertical()) {
			if (point.y > max)
				return new Vector2f(b, max);
			else if (point.y < min)
				return new Vector2f(b, min);
			else
				return new Vector2f(b, point.y);
		} else if (a == 0) {
			if (point.x > max)
				return new Vector2f(max, b);
			else if (point.x < min)
				return new Vector2f(min, b);
			else
				return new Vector2f(point.x, b);
		} else {
			Segment line = new Segment(a / -1, point, -Float.MAX_VALUE, Float.MAX_VALUE);
			Vector2f intersection = simpleIntersection(line);
			if (intersection.x > max) {
				return new Vector2f(max, yAt(max));
			} else if (intersection.x < min) {
				return new Vector2f(min, yAt(min));
			} else {
				return intersection;
			}
		}
	}
	
	/**
	 * Ignores limits and assumes neither lines are vertical.
	 */
	private Vector2f simpleIntersection(Segment other) {
		float x = (other.b - b) / (a - other.a);
		return new Vector2f(x, yAt(x));
	}
	
	/**
	 * Return null if segments are parallel or multiply intersecting.
	 */
	public Vector2f intersection(Segment other) {
		if (isVertical() && other.isVertical()) {
			// Both are vertical; they're parallel
			return null;
		} else if (isVertical() ^ other.isVertical()) {
			// One is vertical and the other isn't
			Segment vertical;
			Segment regular;
			if (isVertical()) {
				vertical = this;
				regular = other;
			} else {
				vertical = other;
				regular = this;
			}
			if (regular.max < vertical.b || regular.min > vertical.b) {
				// Regular is out of the x range of vertical
				return null;
			} else {
				Vector2f intersection = new Vector2f(vertical.b, regular.yAt(vertical.b));
				if (intersection.y > vertical.max || intersection.y < vertical.min) {
					// Regular passes above or below vertical
					return null;
				} else {
					// They intersect
					return intersection;
				}
			}
		} else {
			// Neither are vertical
			if (a == other.a) {
				// They're parallel
				return null;
			} else {
				Vector2f intersection = simpleIntersection(other);
				if (intersection.x < min || intersection.x < other.min || 
						intersection.x > max || intersection.x > other.max) {
					// They don't intersect
					return null;
				} else {
					// They intersect
					return intersection;
				}
			}
		}
	}

	public boolean intersects(Segment segment) {
		return intersection(segment) != null;
	}
	
}
