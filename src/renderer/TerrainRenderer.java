package renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.*;

import org.lwjgl.util.vector.*;

import models.*;
import shaders.*;
import terrain.*;
import textures.*;

public class TerrainRenderer extends AbstractRenderer {

	private TerrainShader terrainShader = new TerrainShader();

	private List<Terrain> terrains = new ArrayList<>();

	TerrainRenderer(Matrix4f projMx) {
		terrainShader.start();
		terrainShader.loadProjMx(projMx);
		terrainShader.loadVwMx();
		terrainShader.stop();
	}

	@Override void loadLight(Light light) {
		terrainShader.loadLight(light);
	}

	public List<Terrain> getTerrains() {
		return terrains;
	}

	@Override void start() {
		terrainShader.start();
	}

	@Override void render() {
		terrainShader.loadVwMx();

		for (Terrain terrain : terrains) {
			RawModel rawModel = terrain.getRawModel();
			Texture texture = terrain.getTexture();

			glBindVertexArray(rawModel.getVaoID());

			glEnableVertexAttribArray(AbstractShader.ATTRIB_VERTEX);
			glEnableVertexAttribArray(AbstractShader.ATTRIB_NORMALS);

			if (texture != null) {
				glEnableVertexAttribArray(AbstractShader.ATTRIB_TEXTURE_COORDS);

				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, terrain.getTexture().getTextureID());
			}

			terrainShader.loadTransMx(terrain.getTransMx());

			glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);

			glDisableVertexAttribArray(AbstractShader.ATTRIB_TEXTURE_COORDS);
			glDisableVertexAttribArray(AbstractShader.ATTRIB_NORMALS);
			glDisableVertexAttribArray(AbstractShader.ATTRIB_VERTEX);

			glBindVertexArray(0);
		}
	}

	@Override void stop() {
		terrainShader.stop();
	}
}
