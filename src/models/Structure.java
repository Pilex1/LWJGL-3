package models;

import org.lwjgl.util.vector.*;

import utils.*;

/**
 * 
 * An object that will not change its position, rotation, scale
 * E.g. terrain
 *
 */
public class Structure {
	
	private final TexturedModel model;
	private final Vector3f position;
	private final Vector3f rotation;
	private final float scale;
	
	private final Matrix4f transMx;

	public Structure(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		
		transMx = MathUtils.createTransformationMatrix(position, rotation, scale);
	}

	public TexturedModel getTexturedModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public Matrix4f getTransMx() {
		return transMx;
	}
}
