package main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.*;

import input.*;
import models.*;
import renderer.*;
import serialization.*;
import terrain.*;
import textures.*;
import utils.*;

public class MainGameLoop {

	private void run() {
		Display.create();

		FilesManager.load();

		MasterRenderer masterRenderer = new MasterRenderer();

		World world = new World(1, 1);
		for (int i = 0; i < world.getTerrains().x; i++) {
			for (int j = 0; j < world.getTerrains().y; j++) {
				world.add(new Terrain(masterRenderer, new Texture("purpleTile")), i, j);
			}
		}
		Camera.setWorld(world);

		Light light = new Light(new Vector3f(400, 200, 400), new Vector3f(5, 5, 5));
		masterRenderer.addLight(light);
		Entity cube = new Entity(TexturedModel.fromFile("Cube", "cubeUVExportTex1"), new Vector3f(400, 100, 400), new Vector3f(0, 0, 0), 10f);
		masterRenderer.addEntity(cube);

		float rotationSpeed = 0.2f;

		while (glfwWindowShouldClose(Display.getWindow()) == GL_FALSE) {
			Mouse.update();
			Keyboard.update();
			Camera.update();

			cube.increaseRotation(new Vector3f(rotationSpeed, rotationSpeed, rotationSpeed));

			masterRenderer.render();

			Display.update();

		}

		Display.close();
		FilesManager.save();
		ResourceManager.cleanUp();

	}

	public static void main(String[] args) {
		new MainGameLoop().run();
	}

}
