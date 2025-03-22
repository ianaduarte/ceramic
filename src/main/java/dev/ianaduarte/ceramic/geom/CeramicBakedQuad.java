package dev.ianaduarte.ceramic.geom;

import dev.ianaduarte.ceramic.texture.UVPair;
import org.joml.Vector3f;

public class CeramicBakedQuad {
	public final CeramicVertex[] vertices;
	public final Vector3f normal;
	
	public CeramicBakedQuad(CeramicVertex v1, CeramicVertex v2, CeramicVertex v3, CeramicVertex v4, float textureWidth, float textureHeight, boolean mirrorX, boolean mirrorY, boolean invert) {
		float invWidth  = 1 / textureWidth;
		float invHeight = 1 / textureHeight;
		this.vertices = new CeramicVertex[]{
			CeramicVertex.of(v1).remap(v1.uv().scaled(invWidth, invHeight)),
			CeramicVertex.of(v2).remap(v2.uv().scaled(invWidth, invHeight)),
			CeramicVertex.of(v3).remap(v3.uv().scaled(invWidth, invHeight)),
			CeramicVertex.of(v4).remap(v4.uv().scaled(invWidth, invHeight))
		};
		
		if(invert) {
			CeramicVertex vf = vertices[0];
			CeramicVertex vs = vertices[2];
			vertices[0] = vertices[1];
			vertices[1] = vf;
			vertices[2] = vertices[3];
			vertices[3] = vs;
		}
		
		if(mirrorX) {
			UVPair uvf = vertices[0].uv();
			UVPair uvs = vertices[2].uv();
			
			vertices[0].remap(vertices[1].uv());
			vertices[1].remap(uvf);
			vertices[2].remap(vertices[3].uv());
			vertices[3].remap(uvs);
		}
		if(mirrorY) {
			UVPair vf = vertices[0].uv();
			UVPair vs = vertices[1].uv();
			
			vertices[0] = vertices[0].remap(vertices[2].uv());
			vertices[1] = vertices[1].remap(vertices[3].uv());
			vertices[2] = vertices[2].remap(vf);
			vertices[3] = vertices[3].remap(vs);
		}
		
		this.normal = new Vector3f(
			(v2.y() - v1.y()) * (v4.z() - v1.z()) - (v2.z() - v1.z()) * (v4.y() - v1.y()),
			(v2.z() - v1.z()) * (v4.x() - v1.x()) - (v2.x() - v1.x()) * (v4.z() - v1.z()),
			(v2.x() - v1.x()) * (v4.y() - v1.y()) - (v2.y() - v1.y()) * (v4.x() - v1.x())
		).normalize();
		if(invert) this.normal.mul(-1);
	}
}
