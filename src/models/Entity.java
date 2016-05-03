package models;

import org.lwjgl.util.vector.*;

import utils.*;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	
	private Matrix4f transMx;

	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		
		transMx = MathUtils.createTransformationMatrix(position, rotation, scale);
	}
	
	public Entity(TexturedModel model) {
		this(model, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 1);
	}
	
	public void increasePosition(Vector3f position) {
		Vector3f.add(this.position, position, this.position);
		MathUtils.translateMatrix(transMx, position);
	}
	
	public void increaseRotation(Vector3f rotation) {
		Vector3f.add(this.rotation, rotation, this.rotation);
		MathUtils.rotateMatrix(transMx, rotation);
	}
	
	public void increaseScale(float scale) {
		this.scale *= scale;
		MathUtils.scaleMatrix(transMx, scale);
	}

	public TexturedModel getTexturedModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		Vector3f diff = null;
		Vector3f.sub(position, this.position, diff);
		MathUtils.translateMatrix(transMx, diff);
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		Vector3f diff = null;
		Vector3f.sub(rotation, this.rotation, diff);
		MathUtils.rotateMatrix(transMx, diff);
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		float diff = scale - this.scale;
		MathUtils.scaleMatrix(transMx, diff);
		this.scale = scale;
	}
	
	public Matrix4f getTransMx() {
		return transMx;
	}
	
}
