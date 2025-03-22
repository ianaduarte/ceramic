package dev.ianaduarte.ceramic.geom.definitions;

import dev.ianaduarte.ceramic.geom.CeramicBakedQuad;
import dev.ianaduarte.ceramic.geom.CeramicVertex;

public class CeramicMeshQuadDefinition {
	CeramicVertex v1, v2, v3, v4;
	boolean mirrorX, mirrorY, invert;
	
	public CeramicMeshQuadDefinition(CeramicVertex[] vertices, boolean mirrorX, boolean mirrorY, boolean invert) {
		if(vertices.length != 4) throw new IllegalArgumentException();
		this.v1 = vertices[0];
		this.v2 = vertices[1];
		this.v3 = vertices[2];
		this.v4 = vertices[3];
		this.mirrorX = mirrorX;
		this.mirrorY = mirrorY;
		this.invert = invert;
	}
	
	public CeramicBakedQuad bake(int textureWidth, int textureHeight) {
		return new CeramicBakedQuad(v1, v2, v3, v4, textureWidth, textureHeight, this.mirrorX, this.mirrorY, invert);
	}
}
