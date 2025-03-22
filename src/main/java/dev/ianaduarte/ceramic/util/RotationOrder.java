package dev.ianaduarte.ceramic.util;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public enum RotationOrder {
	XYZ(new int[]{ 0, 1, 2 }),
	ZYX(new int[]{ 2, 1, 0 }),
	XZY(new int[]{ 0, 2, 1 }),
	YZX(new int[]{ 1, 2, 0 }),
	ZXY(new int[]{ 2, 0, 1 }),
	YXZ(new int[]{ 1, 0, 2 });
	
	final int[] orderIndices;
	RotationOrder(int[] orderIndices) {
		this.orderIndices = orderIndices;
	}
	
	public Quaternionf getQuaternionDeg(float xRot, float yRot, float zRot) {
		return this.getQuaternion(
			CMth.toRad(xRot),
			CMth.toRad(yRot),
			CMth.toRad(zRot)
		);
	}
	public Quaternionf getQuaternion(float xRot, float yRot, float zRot) {
		Quaternionf quaternion = new Quaternionf();
		
		float[] angles = new float[]{ xRot, yRot, zRot };
		for(int i : this.orderIndices) {
			switch(i) {
				case 0 -> { if(xRot != 0) quaternion.rotateX(xRot); }
				case 1 -> { if(yRot != 0) quaternion.rotateY(yRot); }
				case 2 -> { if(zRot != 0) quaternion.rotateZ(zRot); }
			}
		}
		return quaternion;
	}
	
	public Vector3f getEuler(float xRot, float yRot, float zRot) {
		Quaternionf quaternion = this.getQuaternion(xRot, yRot, zRot);
		return quaternion.getEulerAnglesXYZ(new Vector3f());
	}
}