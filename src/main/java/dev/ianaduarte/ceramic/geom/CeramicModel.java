package dev.ianaduarte.ceramic.geom;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Map;

@SuppressWarnings("unchecked")
public class CeramicModel {
	private final Map<String, Part> parts;
	public Transform transform;
	
	public CeramicModel(Map<String, Part> parts) {
		this.parts = parts;
		this.transform = Transform.empty();
	}
	
	public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
		poseStack.pushPose();
		transform.apply(poseStack);
		for(Part part : parts.values()) part.render(poseStack, buffer, packedLight, packedOverlay, color);
		poseStack.popPose();
	}
	public Part getPart(String name) {
		return this.parts.get(name);
	}
	public Part getHierachyPart(String hierarchy) {
		String[] order = hierarchy.split("\\.");
		if(order.length == 0) return null;
		
		Part part = parts.get(order[0]);
		if(order.length == 1) return part;
		
		for(int i = 1; i < order.length; i++) part.getChild(order[i]);
		return part;
	}
	
	public Pair<String, Part>[] getParts() {
		return parts.entrySet().stream().map(e -> new Pair<>(e.getKey(), e.getValue())).toList().toArray(new Pair[0]);
	}
	public String getRandomHierarchy(RandomSource randomSource) {
		Pair<String, Part> currentPart = getParts()[randomSource.nextInt(parts.size())];
		if(currentPart.getSecond().children.isEmpty() || randomSource.nextBoolean()) return currentPart.getFirst();
		
		StringBuilder hierarchy = new StringBuilder(currentPart.getFirst());
		while(randomSource.nextBoolean()) {
			currentPart = currentPart.getSecond().getChildren()[randomSource.nextInt(currentPart.getSecond().children.size())];
			hierarchy.append(".").append(currentPart.getFirst());
			if(currentPart.getSecond().children.isEmpty()) break;
		}
		
		return hierarchy.toString();
	}
	public void applyHierarchyTransforms(String hierarchy, PoseStack poseStack) {
		transform.apply(poseStack);
		String[] order = hierarchy.split("\\.");
		if(order.length == 0) return;
		
		Part part = parts.get(order[0]);
		part.transform.apply(poseStack);
		if(order.length == 1) return;
		
		for(int i = 1; i < order.length; i++) {
			part = part.getChild(order[i]);
			part.transform.apply(poseStack);
		}
	}
	
	public static class Part {
		private final CeramicBakedQuad[] quads;
		private final Map<String, Part> children;
		public Transform initialTransform;
		public Transform transform;
		public boolean visible;
		
		public Part(CeramicBakedQuad[] quads, Map<String, Part> childrenMap) {
			this.quads = quads;
			this.children = childrenMap;
			this.transform = Transform.empty();
			this.initialTransform = Transform.empty();
			this.visible = true;
		}
		
		private void compileToBuffer(PoseStack.Pose pose, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
			Matrix4f matrix4f = pose.pose();
			Vector3f tempNormal = new Vector3f();
			Vector3f tempPosition = new Vector3f();
			
			for(var quad : this.quads) {
				pose.transformNormal(quad.normal, tempNormal);
				
				for(var vertex : quad.vertices) {
					matrix4f.transformPosition(vertex.x() / 16.0f, vertex.y() / 16.0f, vertex.z() / 16.0f, tempPosition);
					
					buffer.addVertex(
						tempPosition.x, tempPosition.y, tempPosition.z,
						color, vertex.u(), vertex.v(), packedOverlay, packedLight,
						tempNormal.x, tempNormal.y, tempNormal.z
					);
				}
			}
		}
		public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
			if(!visible) return;
			poseStack.pushPose();
			this.transform.apply(poseStack);
			
			this.compileToBuffer(poseStack.last(), buffer, packedLight, packedOverlay, color);
			for(Part         child  : this.children.values()) child.render(poseStack, buffer, packedLight, packedOverlay, color);
			poseStack.popPose();
		}
		
		public void setInitialTransform(Transform pose) {
			this.initialTransform = pose;
		}
		public void loadInitialTransform() {
			this.transform.set(initialTransform);
		}
		
		public Part getChild(String name) {
			return this.children.get(name);
		}
		public Pair<String, Part>[] getChildren() {
			return children.entrySet().stream().map(e -> new Pair<>(e.getKey(), e.getValue())).toList().toArray(new Pair[0]);
		}
		
		public CeramicBakedQuad getRandomQuad(RandomSource random) {
			return this.quads[random.nextInt(this.quads.length)];
		}
	}
}
