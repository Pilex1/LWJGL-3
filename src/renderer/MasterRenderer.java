package renderer;

import static org.lwjgl.opengl.GL11.*;

import java.util.*;

import org.lwjgl.util.vector.*;

import models.*;
import terrain.*;
import utils.*;

public class MasterRenderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 20000;

	private static final Vector4f bgColour = new Vector4f(0, 0, 0, 1);

	private List<AbstractRenderer> renderers = new ArrayList<>();

	public MasterRenderer() {
		// sets the clear colour
		glClearColor(bgColour.x / 255, bgColour.y / 255, bgColour.z / 255, bgColour.w / 255);

		// depth testing
		glEnable(GL_DEPTH_TEST);
		// culling back faces
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		Matrix4f projMx = MathUtils.createProjectionMatrix(FOV, FAR_PLANE, NEAR_PLANE);
		renderers.add(new EntityRenderer(projMx));
		renderers.add(new TerrainRenderer(projMx));
	}

	public void addEntity(Entity entity) {
		for (AbstractRenderer renderer : renderers) {
			if (renderer instanceof EntityRenderer) {
				Map<TexturedModel, List<Entity>> entities = ((EntityRenderer) renderer).getEntities();
				TexturedModel model = entity.getTexturedModel();
				List<Entity> storedEntities = entities.get(model);
				if (storedEntities == null) {
					storedEntities = new ArrayList<>();
					storedEntities.add(entity);
					entities.put(model, storedEntities);
				} else {
					storedEntities.add(entity);
				}
			}
		}
	}

	public void addTerrain(Terrain terrain) {
		for (AbstractRenderer renderer : renderers) {
			if (renderer instanceof TerrainRenderer) {
				List<Terrain> terrains = ((TerrainRenderer) renderer).getTerrains();
				terrains.add(terrain);
			}
		}
	}

	private Light light = null;

	public void addLight(Light light) {
		this.light = light;
	}

	public void render() {
		for (AbstractRenderer renderer : renderers) {
			renderer.start();
			if (light != null) {
				renderer.loadLight(light);
			}
			renderer.render();
			renderer.stop();
		}
	}

}
