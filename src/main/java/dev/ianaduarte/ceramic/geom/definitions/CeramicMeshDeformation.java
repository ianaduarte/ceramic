package dev.ianaduarte.ceramic.geom.definitions;

public class CeramicMeshDeformation {
	public static final CeramicMeshDeformation NONE = CeramicMeshDeformation.uniform(0);
	public static final CeramicMeshDeformation EXPAND_HALF = CeramicMeshDeformation.uniform(0.5f);
	public static final CeramicMeshDeformation EXPAND_QUARTER = CeramicMeshDeformation.uniform(0.25f);
	public final float xScale;
	public final float yScale;
	public final float zScale;
	
	public static CeramicMeshDeformation of(float x, float y, float z) {
		return new CeramicMeshDeformation(x, y, z);
	}
	public static CeramicMeshDeformation uniform(float s) {
		return new CeramicMeshDeformation(s, s, s);
	}
	
	private CeramicMeshDeformation(float xScale, float yScale, float zScale) {
		this.xScale = xScale;
		this.yScale = yScale;
		this.zScale = zScale;
	}
	public CeramicMeshDeformation extend(float grow) {
		return new CeramicMeshDeformation(this.xScale + grow, this.yScale + grow, this.zScale + grow);
	}
	public CeramicMeshDeformation extend(float growX, float growY, float growZ) {
		return new CeramicMeshDeformation(this.xScale + growX, this.yScale + growY, this.zScale + growZ);
	}
}
