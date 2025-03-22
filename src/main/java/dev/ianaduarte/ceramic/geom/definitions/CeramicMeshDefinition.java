package dev.ianaduarte.ceramic.geom.definitions;

import dev.ianaduarte.ceramic.geom.CeramicBakedQuad;
import dev.ianaduarte.ceramic.geom.PlanarOrientation;
import dev.ianaduarte.ceramic.texture.CubeMapping;
import dev.ianaduarte.ceramic.texture.UVPlane;

import java.util.ArrayList;
import java.util.List;

public class CeramicMeshDefinition {
	private final List<CeramicMeshQuadDefinition> unbakedQuads;
	
	public CeramicMeshDefinition() {
		this.unbakedQuads = new ArrayList<>();
	}
	
	public CeramicMeshDefinition addPlane(PlanarOrientation orientation, float width, float height, float originX, float originY, float originZ, UVPlane plane, boolean mirror, boolean invert) {
		this.unbakedQuads.add(new CeramicMeshQuadDefinition(
			orientation.makePlane(width, height, originX, originY, originZ, plane),
			mirror, false, invert
		));
		return this;
	}
	public CeramicMeshDefinition addCuboid(float width, float height, float depth, float originX, float originY, float originZ, CeramicMeshDeformation meshDeformation, CubeMapping cubeMapping, boolean mirror, boolean invert){
		//NSEWTB
		width  += meshDeformation.xScale * 2;
		height += meshDeformation.yScale * 2;
		depth  += meshDeformation.zScale * 2;
		originX -= meshDeformation.xScale;
		originY -= meshDeformation.yScale;
		originZ += meshDeformation.zScale;
		
		
		if(cubeMapping.north != null) this.unbakedQuads.add(new CeramicMeshQuadDefinition(PlanarOrientation.XY.makePlane(width, height, originX, originY, originZ - depth, cubeMapping.north),  mirror, false,  invert));
		if(cubeMapping.south != null) this.unbakedQuads.add(new CeramicMeshQuadDefinition(PlanarOrientation.XY.makePlane(width, height, originX, originY, originZ        , cubeMapping.south), !mirror, false, !invert));
		
		if(mirror) {
			if(cubeMapping.west != null) this.unbakedQuads.add(new CeramicMeshQuadDefinition(PlanarOrientation.ZY.makePlane(depth, height, originX + width, originY, originZ, cubeMapping.west), true, false,  invert));
			if(cubeMapping.east != null) this.unbakedQuads.add(new CeramicMeshQuadDefinition(PlanarOrientation.ZY.makePlane(depth, height, originX        , originY, originZ, cubeMapping.east), false, false, !invert));
		} else {
			if(cubeMapping.east != null) this.unbakedQuads.add(new CeramicMeshQuadDefinition(PlanarOrientation.ZY.makePlane(depth, height, originX + width, originY, originZ, cubeMapping.east), false, false,  invert));
			if(cubeMapping.west != null) this.unbakedQuads.add(new CeramicMeshQuadDefinition(PlanarOrientation.ZY.makePlane(depth, height, originX        , originY, originZ, cubeMapping.west), true, false, !invert));
		}
		
		if(cubeMapping.top    != null) this.unbakedQuads.add(new CeramicMeshQuadDefinition(PlanarOrientation.XZ.makePlane(width, depth, originX, originY + height, originZ,    cubeMapping.top), mirror, false, invert));
		if(cubeMapping.bottom != null) this.unbakedQuads.add(new CeramicMeshQuadDefinition(PlanarOrientation.XZ.makePlane(width, depth, originX, originY         , originZ, cubeMapping.bottom), !mirror, false, !invert));
		return this;
	}
	
	public CeramicBakedQuad[] bake(int textureWidth, int textureHeight) {
		return unbakedQuads
			.stream()
			.map((unbakedQuad) -> unbakedQuad.bake(textureWidth, textureHeight))
			.toArray(CeramicBakedQuad[]::new);
	}
}
