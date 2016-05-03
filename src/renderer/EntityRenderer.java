package renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.*;

import org.lwjgl.util.vector.*;

import models.*;
import shaders.*;
import textures.*;

public class EntityRenderer extends AbstractRenderer {
	
	private EntityShader entityShader = new EntityShader();
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	
	EntityRenderer(Matrix4f projMx) {
		entityShader.start();
		entityShader.loadProjMx(projMx);
		entityShader.loadVwMx();
		entityShader.stop();
	}
	
	public Map<TexturedModel, List<Entity>> getEntities() {
		return entities;
	}

	@Override void start() {
		entityShader.start();
	}

	@Override void render() {
		entityShader.loadVwMx();
		
		for (TexturedModel texturedModel : entities.keySet()) {
			RawModel rawModel = texturedModel.getRawModel();
			Texture texture = texturedModel.getTexture();

			glBindVertexArray(rawModel.getVaoID());

			glEnableVertexAttribArray(AbstractShader.ATTRIB_VERTEX);
			glEnableVertexAttribArray(AbstractShader.ATTRIB_NORMALS);
			if (texture != null) {
				glEnableVertexAttribArray(AbstractShader.ATTRIB_TEXTURE_COORDS);
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());
			}

			List<Entity> currentEntities = entities.get(texturedModel);
			for (Entity entity : currentEntities) {
				entityShader.loadTransMx(entity.getTransMx());
				glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
			}

			glDisableVertexAttribArray(AbstractShader.ATTRIB_TEXTURE_COORDS);
			glDisableVertexAttribArray(AbstractShader.ATTRIB_NORMALS);
			glDisableVertexAttribArray(AbstractShader.ATTRIB_VERTEX);

			glBindVertexArray(0);
		}
		
	}

	@Override void stop() {
		entityShader.stop();
		
	}

	@Override void loadLight(Light light) {
		entityShader.loadLight(light);
	}
}
