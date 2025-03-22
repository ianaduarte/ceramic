package dev.ianaduarte.ceramic.util;

import org.joml.Vector2f;
import org.joml.Vector3f;

@SuppressWarnings("unused")
public class CMth {
	public static float EPSILON    = 1e-5f;
	public static float HALF_PI    = (float) (Math.PI / 2.0);
	public static float PI         = (float) (Math.PI );
	public static float TAU        = (float) (Math.TAU);
	public static float DEG_TO_RAD = (float) (Math.PI /   180.0);
	public static float RAD_TO_DEG = (float) (  180.0 / Math.PI);
	
	public static boolean approx(float a, float b) {
		return Math.abs(b - a) <= EPSILON;
	}
	
	public static float toDeg(float radians) {
		return radians * RAD_TO_DEG;
	}
	public static float toRad(float degrees) {
		return degrees * DEG_TO_RAD;
	}
	
	public static float wrap(float x, float min, float max) {
		return (x >= min)? x % max : ((x % max) + max) % max;
	}
	public static float wrapRad(float angle) {
		float i = angle % TAU;
		if(i >= PI) i -= TAU;
		if(i < -PI) i += TAU;
		return i;
	}
	public static float wrapDeg(float angle) {
		float i = angle % 360;
		if(i >= 180) i -= 360;
		if(i < -180) i += 360;
		
		return i;
	}
	
	public static float radDiff(float from, float to) {
		float difference = (to - from) % TAU;
		if(difference >  PI) difference -= TAU;
		if(difference < -PI) difference += TAU;
		return difference;
	}
	public static float degDiff(float from, float to) {
		float difference = (to - from) % 360;
		if(difference >  180) difference -= 360;
		if(difference < -180) difference += 360;
		return difference;
	}
	
	public static float lerp(float a, float b, float t) {
		return a + (b - a) * t;
	}
	public static float lerpRad(float a, float b, float t) {
		return a + radDiff(a, b) * t;
	}
	public static float lerpDeg(float a, float b, float t) {
		return a + degDiff(a, b) * t;
	}
	
	public static float gradient(float delta, float... values) {
		if(values.length == 0) throw new IllegalArgumentException("Gradient array cannot be empty.");
		if(delta <= 0) return values[0];
		if(delta >= 1) return values[values.length - 1];
		
		int index = (int) (delta * (values.length - 1));
		float t = delta * (values.length - 1) - index;
		return values[index] * (1 - t) + values[index + 1] * t;
	}
	public static float remapRange(float value, float oldMin, float oldMax, float newMin, float newMax) {
		return (newMin + (value - oldMin) * (newMax - newMin) / (oldMax - oldMin));
	}
	
	public static Vector2f bounceLerp(float current, float target, float velocity, float speed, float bounce) {
		float diff = target - current;
		
		velocity += (bounce * diff) - (speed * velocity);
		return new Vector2f((current + velocity) + diff * speed, velocity);
	}
	public static Vector2f bounceLerpRad(float current, float target, float velocity, float speed, float bounce) {
		float diff = radDiff(current, target);
		
		velocity += (bounce * diff) - (speed * velocity);
		return new Vector2f((current + velocity) + diff * speed, velocity);
	}
	public static Vector2f bounceLerpDeg(float a, float b, float v, float bounce, float speed) {
		float diff = degDiff(a, b);
		v += (diff - v * speed) * bounce;
		
		return new Vector2f((a + v) + (diff * speed), v);
	}
	
	public static Vector3f apply(Vector3f vector, VectorMutator lambda) {
		vector.x = lambda.apply(vector.x, 0);
		vector.y = lambda.apply(vector.y, 1);
		vector.z = lambda.apply(vector.z, 2);
		return vector;
	}
	public static float vecIndex(Vector3f vec, int i) {
		return switch(i) {
			case 0 -> vec.x;
			case 1 -> vec.y;
			case 2 -> vec.z;
			default -> throw new IndexOutOfBoundsException();
		};
	}
	public interface VectorMutator {
		float apply(float component, int index);
	}
}