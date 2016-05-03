package terrain;

import org.lwjgl.util.vector.*;

import models.*;
import renderer.*;
import textures.*;
import utils.*;

public class Terrain {

	private static final int SIZE = 800;
	
	private float[][] heights;
	
	private float x, z;
	
	private RawModel rawModel;
	private Texture texture;
	
	private Matrix4f transMx;
	
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
	
	void setX(float x) {
		this.x = x;
	}

	void setZ(float z) {
		this.z = z;
	}
	
	public Matrix4f getTransMx() {
		return transMx;
	}
	

	public Terrain(MasterRenderer masterRenderer, Texture texture) {
		this.texture = texture;

		generateTerrain(texture);
		//renderer.addEntity(new Entity(new TexturedModel(rawModel, texture)));
		masterRenderer.addTerrain(this);
		
		transMx=MathUtils.createTransformationMatrix(new Vector3f(x*SIZE,0,z*SIZE), new Vector3f(0,0,0), 1);
	}

	private void generateTerrain(Texture texture){
		NoiseAlgorithm generator = new NoiseAlgorithm(100, MathUtils.randLong(0, Long.MAX_VALUE-1));
		
		//image must be square
		int vertexCount = 50;
		heights = new float[vertexCount][vertexCount];
		int count = vertexCount * vertexCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) vertexCount - 1) * SIZE;
				float height = getHeight(j, i, generator);
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertexCount - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, generator);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) vertexCount - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertexCount - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < vertexCount - 1; gz++) {
			for (int gx = 0; gx < vertexCount - 1; gx++) {
				int topLeft = (gz * vertexCount) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertexCount) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		rawModel = new RawModel(vertices, textureCoords, normals, indices);
	}
	
	private Vector3f calculateNormal(int x, int y, NoiseAlgorithm algorithm) {
		float heightL = getHeight(x - 1, y, algorithm);
		float heightR = getHeight(x + 1, y, algorithm);
		float heightD = getHeight(x, y - 1, algorithm);
		float heightU = getHeight(x, y + 1, algorithm);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	private float getHeight(int x, int z, NoiseAlgorithm algorithm) {
		return algorithm.generateHeights(x, z);
	}

	public static int getSize() {
		return SIZE;
	}
	
	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - x;
		float terrainZ = worldZ - z;
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0
				|| gridZ < 0) {
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;

		if (xCoord <= (1 - zCoord)) {
			answer = MathUtils.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = MathUtils.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
					new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return answer;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public Texture getTexture() {
		return texture;
	}
	
	
}
