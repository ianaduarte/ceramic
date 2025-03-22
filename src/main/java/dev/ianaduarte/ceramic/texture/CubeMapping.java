package dev.ianaduarte.ceramic.texture;

public class CubeMapping {
	public final UVPlane north, south, east, west, top, bottom;
	
	private CubeMapping(UVPlane north, UVPlane south, UVPlane east, UVPlane west, UVPlane top, UVPlane bottom) {
		this.north = north;
		this.south = south;
		this.east = east;
		this.west = west;
		this.top = top;
		this.bottom = bottom;
	}
	public static CubeMapping of(UVPlane north, UVPlane south, UVPlane east, UVPlane west, UVPlane top, UVPlane bottom) {
		return new CubeMapping(north, south, east, west, top, bottom);
	}
	public static CubeMapping vanilla(float offsetU, float offsetV, float width, float height, float depth) {
		float hWidth  = width  * 0.5f;
		float hDepth  = depth  * 0.5f;
		float hHeight = height * 0.5f;
		
		return new CubeMapping(
			UVPlane.of(depth            , depth, 0, width, height).offset(offsetU + hWidth, offsetV + hHeight),
			UVPlane.of(2 * depth + width, depth, 0, width, height).offset(offsetU + hWidth, offsetV + hHeight),
			UVPlane.of(0                , depth, 0, depth, height).offset(offsetU + hDepth, offsetV + hHeight),
			UVPlane.of(depth + width    , depth, 0, depth, height).offset(offsetU + hDepth, offsetV + hHeight),
			UVPlane.of(depth            , 0    , 0, width, depth ).offset(offsetU + hWidth, offsetV + hDepth),
			UVPlane.of(depth + width    , 0    , 0, width, depth ).offset(offsetU + hWidth, offsetV + hDepth)
		);
	}
	//story mode-like
	public static CubeMapping cylindrical(float offsetU, float offsetV, float width, float height, float depth) {
		float hWidth  = width  * 0.5f;
		float hDepth  = depth  * 0.5f;
		float hHeight = height * 0.5f;
		
		return new CubeMapping(
			UVPlane.of(depth            , depth         , 0.0f, width, height).offset(offsetU + hWidth, offsetV + hHeight),
			UVPlane.of(2 * depth + width, depth         , 0.0f, width, height).offset(offsetU + hWidth, offsetV + hHeight),
			UVPlane.of(0                , depth         , 0.0f, depth, height).offset(offsetU + hDepth, offsetV + hHeight),
			UVPlane.of(depth + width    , depth         , 0.0f, depth, height).offset(offsetU + hDepth, offsetV + hHeight),
			UVPlane.of(depth            , 0             , 0.0f, width, depth ).offset(offsetU + hWidth, offsetV + hDepth),
			UVPlane.of(depth            , depth + height, 0.5f, width, depth ).offset(offsetU + hWidth, offsetV + hDepth)
		);
	}
	
	//used in some dungeons textures such as the redstone monstrosity
	public static CubeMapping topWrap(float offsetU, float offsetV, float width, float height, float depth) {
		throw new RuntimeException("unimplemented");
	}
	
	public static CubeMapping hStack(float offsetU, float offsetV, float width, float height, float depth) {
		float hWidth  = width  * 0.5f;
		float hDepth  = depth  * 0.5f;
		float hHeight = height * 0.5f;
		
		return new CubeMapping(
			UVPlane.of(        depth,      0, 0, width, height).offset(offsetU + hWidth, offsetV + hHeight),
			UVPlane.of(        depth, height, 0, width, height).offset(offsetU + hWidth, offsetV + hHeight),
			UVPlane.of(            0,      0, 0, depth, height).offset(offsetU + hDepth, offsetV + hHeight),
			UVPlane.of(            0, height, 0, depth, height).offset(offsetU + hDepth, offsetV + hHeight),
			UVPlane.of(depth + width,      0, 0, width, depth ).offset(offsetU + hWidth, offsetV +  hDepth),
			UVPlane.of(depth + width,  depth, 0, width, depth ).offset(offsetU + hWidth, offsetV +  hDepth)
		);
	}
}
