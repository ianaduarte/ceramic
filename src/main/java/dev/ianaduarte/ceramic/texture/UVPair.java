package dev.ianaduarte.ceramic.texture;

import net.minecraft.util.Mth;

public class UVPair {
	public float u, v;
	public UVPair(float u, float v) {
		this.u = u;
		this.v = v;
	}
	public static UVPair of(float u, float v) {
		return new UVPair(u, v);
	}
	
	public UVPair scaled(float s) {
		this.u *= s;
		this.v *= s;
		return this;
	}
	public UVPair scaled(float x, float y) {
		this.u *= x;
		this.v *= y;
		return this;
	}
	public UVPair offset(float x, float y) {
		this.u += x;
		this.v += y;
		return this;
	}
	public UVPair rotated(float theta) {
		if(theta == 0 || theta == 1) {
			return this;
		} else if(theta == 0.25) {
			float tempU = this.u;
			this.u = -this.v;
			this.v = tempU;
			return this;
		} else if(theta == 0.5) {
			this.u = -this.u;
			this.v = -this.v;
			return this;
		} else if(theta == 0.75) {
			float tempU = this.u;
			this.u = this.v;
			this.v = -tempU;
			return this;
		}
		
		float cosi = Mth.cos(theta * Mth.TWO_PI);
		float sine = Mth.sin(theta * Mth.TWO_PI);
		
		return this.offset(
			 cosi * this.u + sine * this.v,
			-sine * this.u  + cosi * this.v
		);
	}
	
	public float u() {
		return u;
	}
	public float v() {
		return v;
	}
}
