package dev.ianaduarte.ceramic.texture;

public class UVPlane {
	public float u, v, r;
	public float width, height;
	
	private UVPlane(float u, float v, float r, float width, float height) {
		this.u = u;
		this.v = v;
		this.r = r;
		this.width = width;
		this.height = height;
	}
	public static UVPlane of(float u, float v, float r, float width, float height) {
		return new UVPlane(u, v, r, width, height);
	}
	
	public UVPlane scaled(float x, float y) {
		this.u *= x;
		this.v *= y;
		this.width *= x;
		this.height *= y;
		return this;
	}
	public UVPlane offset(float x, float y) {
		this.u += x;
		this.v += y;
		return this;
	}
	public UVPlane rotated(float r) {
		this.r += r;
		return this;
	}
	
	public float u() {
		return u;
	}
	public float v() {
		return v;
	}
	public float r() {
		return r;
	}
	public float width() {
		return width;
	}
	public float height() {
		return height;
	}
}
