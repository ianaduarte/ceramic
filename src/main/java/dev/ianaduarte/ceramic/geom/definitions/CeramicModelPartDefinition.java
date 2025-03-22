package dev.ianaduarte.ceramic.geom.definitions;

import com.google.common.collect.ImmutableMap;
import dev.ianaduarte.ceramic.geom.CeramicModel;
import dev.ianaduarte.ceramic.geom.CeramicBakedQuad;
import dev.ianaduarte.ceramic.geom.Transform;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import java.util.Map;

public class CeramicModelPartDefinition {
	private final CeramicMeshDefinition mesh;
	private final Transform partPose;
	private final Map<String, CeramicModelPartDefinition> children;
	
	public CeramicModelPartDefinition(CeramicMeshDefinition mesh) {
		this.mesh = mesh;
		this.partPose = Transform.empty();
		this.children = new Object2ObjectArrayMap<>();
	}
	public CeramicModelPartDefinition(CeramicMeshDefinition mesh, Transform partPose) {
		this.mesh = mesh;
		this.partPose = partPose;
		this.children = new Object2ObjectArrayMap<>();
	}
	
	public CeramicModelPartDefinition addChild(String name, CeramicMeshDefinition mesh, Transform initialPose) {
		CeramicModelPartDefinition partDefinition = new CeramicModelPartDefinition(mesh, initialPose);
		this.children.put(name, partDefinition);
		return partDefinition;
	}
	public CeramicModelPartDefinition getChild(String name) {
		return this.children.get(name);
	}
	
	public CeramicModel.Part bake(int texWidth, int texHeight) {
		//Map<String, Model.Part> childrenMap = new Object2ObjectArrayMap<>();
		ImmutableMap.Builder<String, CeramicModel.Part> childrenMap = ImmutableMap.builder();
		for(var childDefinition : this.children.entrySet()) {
			childrenMap.put(childDefinition.getKey(), childDefinition.getValue().bake(texWidth, texHeight));
		}
		
		CeramicBakedQuad[] bakedMesh = this.mesh.bake(texWidth, texHeight);
		CeramicModel.Part part = new CeramicModel.Part(bakedMesh, childrenMap.build());
		part.setInitialTransform(this.partPose);
		part.loadInitialTransform();
		return part;
	}
}
