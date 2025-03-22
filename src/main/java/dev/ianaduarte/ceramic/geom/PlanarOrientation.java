package dev.ianaduarte.ceramic.geom;

import dev.ianaduarte.ceramic.texture.UVPair;
import dev.ianaduarte.ceramic.texture.UVPlane;

public enum PlanarOrientation {
	XY,
	XZ,
	ZX,
	ZY,
	YX,
	YZ;
	
	public CeramicVertex[] makePlane(float width, float height, float originX, float originY, float originZ, UVPlane uvPlane) {
		UVPair uv4 = UVPair.of(-0.5f, -0.5f).scaled(uvPlane.width(), uvPlane.height()).rotated(uvPlane.r()).offset(uvPlane.u(), uvPlane.v());
		UVPair uv3 = UVPair.of( 0.5f, -0.5f).scaled(uvPlane.width(), uvPlane.height()).rotated(uvPlane.r()).offset(uvPlane.u(), uvPlane.v());
		UVPair uv2 = UVPair.of( 0.5f,  0.5f).scaled(uvPlane.width(), uvPlane.height()).rotated(uvPlane.r()).offset(uvPlane.u(), uvPlane.v());
		UVPair uv1 = UVPair.of(-0.5f,  0.5f).scaled(uvPlane.width(), uvPlane.height()).rotated(uvPlane.r()).offset(uvPlane.u(), uvPlane.v());
		
		return switch(this) {
			case XY -> new CeramicVertex[] {
				CeramicVertex.of(originX + width, originY         , originZ, uv1),
				CeramicVertex.of(originX        , originY         , originZ, uv2),
				CeramicVertex.of(originX        , originY + height, originZ, uv3),
				CeramicVertex.of(originX + width, originY + height, originZ, uv4)
			};
			case XZ -> new CeramicVertex[] {
				CeramicVertex.of(originX + width, originY, originZ - height, uv1),
				CeramicVertex.of(originX        , originY, originZ - height, uv2),
				CeramicVertex.of(originX        , originY, originZ         , uv3),
				CeramicVertex.of(originX + width, originY, originZ         , uv4)
			};
			case ZX -> new CeramicVertex[] {
				CeramicVertex.of(originX + height, originY, originZ        , uv1),
				CeramicVertex.of(originX + height, originY, originZ + width, uv2),
				CeramicVertex.of(originX         , originY, originZ + width, uv3),
				CeramicVertex.of(originX         , originY, originZ        , uv4)
			};
			case ZY -> new CeramicVertex[] {
				CeramicVertex.of(originX, originY         , originZ        , uv1),
				CeramicVertex.of(originX, originY         , originZ - width, uv2),
				CeramicVertex.of(originX, originY + height, originZ - width, uv3),
				CeramicVertex.of(originX, originY + height, originZ        , uv4)
			};
			case YX -> new CeramicVertex[] {
				CeramicVertex.of(originX + height, originY        , originZ, uv1),
				CeramicVertex.of(originX + height, originY + width, originZ, uv2),
				CeramicVertex.of(originX         , originY + width, originZ, uv3),
				CeramicVertex.of(originX         , originY        , originZ, uv4)
			};
			case YZ -> new CeramicVertex[] {
				CeramicVertex.of(originX, originY        , originZ + height, uv1),
				CeramicVertex.of(originX, originY + width, originZ + height, uv2),
				CeramicVertex.of(originX, originY + width, originZ         , uv3),
				CeramicVertex.of(originX, originY        , originZ         , uv4)
			};
		};
	}
}