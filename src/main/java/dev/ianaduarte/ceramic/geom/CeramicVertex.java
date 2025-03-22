package dev.ianaduarte.ceramic.geom;

import dev.ianaduarte.ceramic.texture.UVPair;

public class CeramicVertex {
	float x, y, z;
	float u, v;
	
	public CeramicVertex(float x, float y, float z, float u, float v) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.u = u;
		this.v = v;
	}
	
	public static CeramicVertex of(CeramicVertex v) {
		return new CeramicVertex(v.x, v.y, v.z, v.u, v.v);
	}
	public static CeramicVertex of(float x, float y, float z, UVPair uv) {
		return new CeramicVertex(x, y, z, uv.u(), uv.v());
	}
	
	
	public CeramicVertex remap(float u, float v) {
		this.u = u;
		this.v = v;
		return this;
	}
	public CeramicVertex remap(UVPair uv) {
		this.u = uv.u();
		this.v = uv.v();
		return this;
	}
	public CeramicVertex rotate(float uvRotation) {
		return this;
	}
	
	public float x() {
		return this.x;
	}
	public float y() {
		return this.y;
	}
	public float z() {
		return this.z;
	}
	public float u() {
		return this.u;
	}
	public float v() {
		return this.v;
	}
	public UVPair uv() {
		return new UVPair(this.u, this.v);
	}
}
