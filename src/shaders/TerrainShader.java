package shaders;

import java.util.*;

import org.lwjgl.util.vector.*;

import models.*;
import utils.*;

public class TerrainShader extends AbstractShader {
	
	private static final String vertexShader = "terrainVertexShader";
	private static final String fragmentShader = "terrainFragmentShader";

	public TerrainShader() {
		super(vertexShader, fragmentShader);
	}

	@Override protected Map<String, Integer> setAttributeNames() {
		Map<String, Integer> attributeNames = new HashMap<>();
		
		attributeNames.put("position", AbstractShader.ATTRIB_VERTEX);
		attributeNames.put("textureCoords", AbstractShader.ATTRIB_TEXTURE_COORDS);
		attributeNames.put("normal", AbstractShader.ATTRIB_NORMALS);
		
		return attributeNames;
	}

	@Override protected List<String> setUniformNames() {
		ArrayList<String> uniformNames = new ArrayList<>();
		
		Collections.addAll(uniformNames, "transMx","projMx", "vwMx", "lightPos", "lightColour");
		
		return uniformNames;
	}
	
	public void loadTransMx(Matrix4f matrix) {
		super.loadMatrix4f("transMx", matrix);
	}
	
	public void loadProjMx(Matrix4f matrix) {
		super.loadMatrix4f("projMx", matrix);
	}
	
	public void loadVwMx() {
		super.loadMatrix4f("vwMx", MathUtils.createViewMatrix());
	}
	
	public void loadLight(Light light) {
		super.loadVector3f("lightPos", light.getPosition());
		super.loadVector3f("lightColour", light.getColour());
	}

}
