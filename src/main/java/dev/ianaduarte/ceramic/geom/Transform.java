package dev.ianaduarte.ceramic.geom;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ianaduarte.ceramic.util.CMth;
import dev.ianaduarte.ceramic.util.RotationOrder;

public class Transform {
	public float xPos  , yPos  , zPos;
	public float xRot  , yRot  , zRot;
	public float xScale, yScale, zScale;
	
	public static Transform empty() {
		return new Transform();
	}
	public static Transform translation(float x, float y, float z) {
		return new Transform(x, y, z, 0, 0, 0, 1, 1, 1);
	}
	public static Transform rotation(float x, float y, float z) {
		return new Transform(0, 0, 0, x, y, z, 1, 1, 1);
	}
	public static Transform rotationDeg(float x, float y, float z) {
		return Transform.rotation(
			CMth.toRad(x),
			CMth.toRad(y),
			CMth.toRad(z)
		);
	}
	public static Transform scaling(float s) {
		return new Transform(0, 0, 0, 0, 0, 0, s, s, s);
	}
	public static Transform scaling(float x, float y, float z) {
		return new Transform(0, 0, 0, 0, 0, 0, x, y, z);
	}
	
	private Transform() {
		this.xPos   = 0;
		this.yPos   = 0;
		this.zPos   = 0;
		this.xRot   = 0;
		this.yRot   = 0;
		this.zRot   = 0;
		this.xScale = 1;
		this.yScale = 1;
		this.zScale = 1;
	}
	private Transform(float xPos, float yPos, float zPos, float xRot, float yRot, float zRot, float xScale, float yScale, float zScale) {
		this.xPos   = xPos;
		this.yPos   = yPos;
		this.zPos   = zPos;
		this.xRot   = xRot;
		this.yRot   = yRot;
		this.zRot   = zRot;
		this.xScale = xScale;
		this.yScale = yScale;
		this.zScale = zScale;
	}
	
	public void set(Transform other) {
		this.xPos = other.xPos;
		this.yPos = other.yPos;
		this.zPos = other.zPos;
		
		this.xRot = other.xRot;
		this.yRot = other.yRot;
		this.zRot = other.zRot;
		
		this.xScale = other.xScale;
		this.yScale = other.yScale;
		this.zScale = other.zScale;
	}
	
	public void apply(PoseStack poseStack) {
		if(this.xPos   != 0 || this.yPos   != 0 || this.zPos   != 0) poseStack.translate(xPos / 16, yPos / 16, zPos / 16);
		if(this.xRot   != 0 || this.yRot   != 0 || this.zRot   != 0) poseStack.mulPose(RotationOrder.XZY.getQuaternion(xRot, yRot, zRot));
		if(this.xScale != 1 || this.yScale != 1 || this.zScale != 1) poseStack.scale(this.xScale, this.yScale, this.zScale);
	}
	
	public Transform offset(float x, float y, float z) {
		this.xPos += x;
		this.yPos += y;
		this.zPos += z;
		return this;
	}
	public Transform rotate(float x, float y, float z) {
		this.xRot += x;
		this.yRot += y;
		this.zRot += z;
		return this;
	}
	public Transform rotateDeg(float x, float y, float z) {
		this.xRot += CMth.toRad(x);
		this.yRot += CMth.toRad(y);
		this.zRot += CMth.toRad(z);
		return this;
	}
	public Transform scale(float s) {
		this.xScale += s;
		this.yScale += s;
		this.zScale += s;
		return this;
	}
	public Transform scale(float x, float y, float z) {
		this.xScale += x;
		this.yScale += y;
		this.zScale += z;
		return this;
	}
	
	public Transform setRotation(float x, float y, float z) {
		this.xRot = x;
		this.yRot = y;
		this.zRot = z;
		return this;
	}
	public Transform setRotationDeg(float x, float y, float z) {
		this.xRot = CMth.toRad(x);
		this.yRot = CMth.toRad(y);
		this.zRot = CMth.toRad(z);
		return this;
	}
	public Transform setPosition(float x, float y, float z) {
		this.xPos = x;
		this.yPos = y;
		this.zPos = z;
		return this;
	}
	public Transform setScale(float s) {
		this.xScale = s;
		this.yScale = s;
		this.zScale = s;
		return this;
	}
	public Transform setScale(float x, float y, float z) {
		this.xScale = x;
		this.yScale = y;
		this.zScale = z;
		return this;
	}
}
