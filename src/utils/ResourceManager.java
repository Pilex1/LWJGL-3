package utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.*;

import shaders.*;

public class ResourceManager {

	private static Set<Integer> vaos = new HashSet<>();
	private static Set<Integer> vbos = new HashSet<>();
	private static Set<Integer> textures = new HashSet<>();
	private static Set<AbstractShader> shaders = new HashSet<>();

	public static void addVAO(int vaoID) {
		vaos.add(vaoID);
	}

	public static void addVBO(int vboID) {
		vbos.add(vboID);
	}

	public static void addTexture(int textureID) {
		textures.add(textureID);
	}
	
	public static void addShader(AbstractShader shader) {
		shaders.add(shader);
	}

	public static void cleanUp() {
		for (int vao : vaos) {
			glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			glDeleteBuffers(vbo);
		}
		for (int texture : textures) {
			glDeleteTextures(texture);
		}
		for (AbstractShader shader : shaders) {
			shader.stop();
			shader.cleanUp();
		}

	}

	
}
