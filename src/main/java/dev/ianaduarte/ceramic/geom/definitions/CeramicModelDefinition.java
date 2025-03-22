package dev.ianaduarte.ceramic.geom.definitions;

import com.google.common.collect.ImmutableMap;
import dev.ianaduarte.ceramic.geom.CeramicModel;
import dev.ianaduarte.ceramic.geom.Transform;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import java.util.Map;

public class CeramicModelDefinition {
	int texWidth, texHeight;
	Map<String, CeramicModelPartDefinition> partDefinitions;
	
	public CeramicModelDefinition(int texWidth, int texHeight) {
		this.texWidth = texWidth;
		this.texHeight = texHeight;
		this.partDefinitions = new Object2ObjectArrayMap<>();
	}
	
	public CeramicModelPartDefinition addPart(String name, CeramicMeshDefinition mesh, Transform initialPose) {
		CeramicModelPartDefinition partDefinition = new CeramicModelPartDefinition(mesh, initialPose);
		this.partDefinitions.put(name, partDefinition);
		return partDefinition;
	}
	
	public CeramicModel bake() {
		ImmutableMap.Builder<String, CeramicModel.Part> bakedParts = ImmutableMap.builder();
		
		for(var pDef : this.partDefinitions.entrySet()) {
			bakedParts.put(pDef.getKey(), pDef.getValue().bake(texWidth, texHeight));
		}
		return new CeramicModel(bakedParts.build());
	}
}
