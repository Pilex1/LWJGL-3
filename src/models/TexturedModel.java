package models;

import textures.*;

public class TexturedModel {

	private RawModel model;
	private Texture texture;

	public TexturedModel(float[] vertices, float[] textureCoords, float[] normals, int[] indices, Texture texture) {
		model = new RawModel(vertices, textureCoords, normals, indices);
		this.texture = texture;
	}

	public TexturedModel(RawModel model, Texture texture) {
		this.model = model;
		this.texture = texture;
	}
	
	public static TexturedModel fromFile(String modelFile) {
		TexturedModel texturedModel = new TexturedModel(RawModel.fromOBJ(modelFile),null);
		System.err.printf("Warning! Model \"%s\" has no texture!",texturedModel.getRawModel().getName());
		return new TexturedModel(RawModel.fromOBJ(modelFile),null);
		
	}

	public static TexturedModel fromFile(String modelFile, String textureFile) {
		return new TexturedModel(RawModel.fromOBJ(modelFile), new Texture(textureFile));
	}

	public RawModel getModel() {
		return model;
	}

	public void setModel(RawModel model) {
		this.model = model;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return model;
	}

	public Texture getTexture() {
		return texture;
	}

}
