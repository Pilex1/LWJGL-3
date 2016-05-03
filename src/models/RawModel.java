package models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.*;
import java.nio.*;
import java.util.*;

import org.lwjgl.*;
import org.lwjgl.util.vector.*;

import res.*;
import shaders.*;
import utils.*;

public class RawModel {

	private final int vaoID;
	private final int vboVerticesID, vboTextureCoordsID, vboNormalsID, vboIndicesID;
	private final int vertexCount;

	private String name = "Unnamed model";

	public RawModel(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
		vertexCount = indices.length;

		vaoID = glGenVertexArrays();
		ResourceManager.addVAO(getVaoID());
		glBindVertexArray(getVaoID());

		vboVerticesID = bindVBO(AbstractShader.ATTRIB_VERTEX, vertices, 3);

		vboTextureCoordsID = bindVBO(AbstractShader.ATTRIB_TEXTURE_COORDS, textureCoords,2);
		
		vboNormalsID = bindVBO(AbstractShader.ATTRIB_NORMALS, normals, 3);

		vboIndicesID = glGenBuffers();
		ResourceManager.addVBO(vboIndicesID);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndicesID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, toIntBuffer(indices), GL_STATIC_DRAW);

		glBindVertexArray(0);
	}
	
	private int bindVBO(int bufferIndex, float[] data, int dimensions) {
		int vboID = glGenBuffers();
		ResourceManager.addVBO(vboID);
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, toFloatBuffer(data), GL_STATIC_DRAW);
		glVertexAttribPointer(bufferIndex, dimensions, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER,0);
		return vboID;
	}

	private FloatBuffer toFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	private IntBuffer toIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVboIndicesID() {
		return vboIndicesID;
	}
	
	public int getVboVerticesID() {
		return vboVerticesID;
	}

	public int getVboTextureCoordsID() {
		return vboTextureCoordsID;
	}

	public int getVboNormalsID() {
		return vboNormalsID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public static RawModel fromOBJ(String file) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream("/res/models/" + file + ".obj")))) {

			String line;
			List<Vertex> vertices = new ArrayList<Vertex>();
			List<Vector2f> textures = new ArrayList<Vector2f>();
			List<Vector3f> normals = new ArrayList<Vector3f>();
			List<Integer> indices = new ArrayList<Integer>();

			while (true) {
				line = reader.readLine();
				if (line.startsWith("v ")) {
					String[] currentLine = line.split(" ");
					Vector3f vertex = new Vector3f((float) Float.valueOf(currentLine[1]), (float) Float.valueOf(currentLine[2]), (float) Float.valueOf(currentLine[3]));
					Vertex newVertex = new Vertex(vertices.size(), vertex);
					vertices.add(newVertex);

				} else if (line.startsWith("vt ")) {
					String[] currentLine = line.split(" ");
					Vector2f texture = new Vector2f((float) Float.valueOf(currentLine[1]), (float) Float.valueOf(currentLine[2]));
					textures.add(texture);
				} else if (line.startsWith("vn ")) {
					String[] currentLine = line.split(" ");
					Vector3f normal = new Vector3f((float) Float.valueOf(currentLine[1]), (float) Float.valueOf(currentLine[2]), (float) Float.valueOf(currentLine[3]));
					normals.add(normal);
				} else if (line.startsWith("f ")) {
					break;
				}
			}
			while (line != null && line.startsWith("f ")) {
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				processVertex(vertex1, vertices, indices);
				processVertex(vertex2, vertices, indices);
				processVertex(vertex3, vertices, indices);
				line = reader.readLine();
			}
			reader.close();

			removeUnusedVertices(vertices);
			float[] verticesArray = new float[vertices.size() * 3];
			float[] texturesArray = new float[vertices.size() * 2];
			float[] normalsArray = new float[vertices.size() * 3];
			float furthest = convertDataToArrays(vertices, textures, normals, verticesArray, texturesArray, normalsArray);
			int[] indicesArray = convertIndicesListToArray(indices);

			RawModel rawModel = new RawModel(verticesArray, texturesArray, normalsArray, indicesArray);

			rawModel.name = file;

			// ModelData data = new ModelData(verticesArray, texturesArray,
			// normalsArray, indicesArray,
			// furthest);
			// return data;
			return rawModel;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getName() {
		return name;
	}

	private static class Vertex {
		private static final int NO_INDEX = -1;

		private Vector3f position;
		private int textureIndex = NO_INDEX;
		private int normalIndex = NO_INDEX;
		private Vertex duplicateVertex = null;
		private int index;
		private float length;

		private Vertex(int index, Vector3f position) {
			this.index = index;
			this.position = position;
			this.length = position.length();
		}

		private boolean isSet() {
			return textureIndex != NO_INDEX && normalIndex != NO_INDEX;
		}

		private boolean hasSameTextureAndNormal(int textureIndexOther, int normalIndexOther) {
			return textureIndexOther == textureIndex && normalIndexOther == normalIndex;
		}
	}

	private static void processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {
		int index = Integer.parseInt(vertex[0]) - 1;
		Vertex currentVertex = vertices.get(index);
		int textureIndex = Integer.parseInt(vertex[1]) - 1;
		int normalIndex = Integer.parseInt(vertex[2]) - 1;
		if (!currentVertex.isSet()) {
			currentVertex.textureIndex = textureIndex;
			currentVertex.normalIndex = normalIndex;
			indices.add(index);
		} else {
			dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices, vertices);
		}
	}

	private static int[] convertIndicesListToArray(List<Integer> indices) {
		int[] indicesArray = new int[indices.size()];
		for (int i = 0; i < indicesArray.length; i++) {
			indicesArray[i] = indices.get(i);
		}
		return indicesArray;
	}

	private static float convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures, List<Vector3f> normals, float[] verticesArray, float[] texturesArray, float[] normalsArray) {
		float furthestPoint = 0;
		for (int i = 0; i < vertices.size(); i++) {
			Vertex currentVertex = vertices.get(i);
			if (currentVertex.length > furthestPoint) {
				furthestPoint = currentVertex.length;
			}
			Vector3f position = currentVertex.position;
			Vector2f textureCoord = textures.get(currentVertex.textureIndex);
			Vector3f normalVector = normals.get(currentVertex.normalIndex);
			verticesArray[i * 3] = position.x;
			verticesArray[i * 3 + 1] = position.y;
			verticesArray[i * 3 + 2] = position.z;
			texturesArray[i * 2] = textureCoord.x;
			texturesArray[i * 2 + 1] = 1 - textureCoord.y;
			normalsArray[i * 3] = normalVector.x;
			normalsArray[i * 3 + 1] = normalVector.y;
			normalsArray[i * 3 + 2] = normalVector.z;
		}
		return furthestPoint;
	}

	private static void dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex, int newNormalIndex, List<Integer> indices, List<Vertex> vertices) {
		if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			indices.add(previousVertex.index);
		} else {
			Vertex anotherVertex = previousVertex.duplicateVertex;
			if (anotherVertex != null) {
				dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex, indices, vertices);
			} else {
				Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.position);
				duplicateVertex.textureIndex = newTextureIndex;
				duplicateVertex.normalIndex = newNormalIndex;
				previousVertex.duplicateVertex = duplicateVertex;
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.index);
			}

		}
	}

	private static void removeUnusedVertices(List<Vertex> vertices) {
		for (Vertex vertex : vertices) {
			if (!vertex.isSet()) {
				vertex.textureIndex = 0;
				vertex.normalIndex = 0;
			}
		}
	}
}
