package shaders;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.nio.*;
import java.util.*;

import org.lwjgl.*;
import org.lwjgl.util.vector.*;

import utils.*;

public abstract class AbstractShader {

	// positions for vbos
	public static final int ATTRIB_VERTEX = 0;
	public static final int ATTRIB_TEXTURE_COORDS = 1;
	public static final int ATTRIB_NORMALS = 2;

	private final int programID;
	private final int vertexID;
	private final int fragmentID;

	protected Map<String, Integer> uniformLocations = new HashMap<>();

	private Map<String, Integer> attributeLocations = new HashMap<>();
	
	private String currentFile;

	public AbstractShader(String vertexShader, String fragmentShader) {
		programID = glCreateProgram();

		vertexID = glCreateShader(GL_VERTEX_SHADER);
		glAttachShader(programID, vertexID);
		glShaderSource(this.vertexID, loadToString(vertexShader));
		glCompileShader(vertexID);
		if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Could not compile "+currentFile);
			System.err.println(glGetShaderInfoLog(vertexID));
			System.exit(-1);
		}

		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		glAttachShader(programID, fragmentID);
		glShaderSource(fragmentID, loadToString(fragmentShader));
		glCompileShader(fragmentID);
		if (glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Could not compile "+currentFile);
			System.err.println(glGetShaderInfoLog(fragmentID));
			System.exit(-1);
		}

		attributeLocations = setAttributeNames();
		for (String attributeName : attributeLocations.keySet()) {
			int vaoIndex = attributeLocations.get(attributeName);
			glBindAttribLocation(programID, vaoIndex, attributeName);
		}

		glCompileShader(programID);

		glLinkProgram(programID);
		if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println("Could not link shader!");
			System.err.println(glGetProgramInfoLog(programID));
			System.exit(-1);
		}

		glValidateProgram(programID);

		List<String> uniformNames = setUniformNames();
		for (String uniformName : uniformNames) {
			generateUniformLocation(uniformName);
		}
		
		ResourceManager.addShader(this);
	}

	/**
	 * 
	 * @return string of the variable name in the shader code, int of the
	 *         attribute list in the vao to be bound
	 */
	protected abstract Map<String, Integer> setAttributeNames();

	protected abstract List<String> setUniformNames();

	private void generateUniformLocation(String uniformName) {
		uniformLocations.put(uniformName, glGetUniformLocation(programID, uniformName));
	}

	protected void loadFloat(String location, float value) {
		glUniform1f(uniformLocations.get(location), value);
	}

	protected void loadInt(String location, int value) {
		glUniform1i(uniformLocations.get(location), value);
	}

	protected void loadVector2f(String location, Vector2f vector) {
		glUniform2f(uniformLocations.get(location), vector.x, vector.y);
	}

	protected void loadVector3f(String location, Vector3f vector) {
		glUniform3f(uniformLocations.get(location), vector.x, vector.y, vector.z);
	}

	protected void loadVector4f(String location, Vector4f vector) {
		glUniform4f(uniformLocations.get(location), vector.x, vector.y, vector.z, vector.w);
	}

	protected void loadMatrix4f(String location, Matrix4f matrix) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		matrix.store(buffer);
		buffer.flip();
		glUniformMatrix4fv(uniformLocations.get(location), false, buffer);
	}

	protected void loadBoolean(String location, boolean value) {
		glUniform1f(uniformLocations.get(location), value ? 1 : 0);
	}

	public void start() {
		glUseProgram(programID);
	}

	public void stop() {
		glUseProgram(0);
	}

	public void cleanUp() {
		stop();
		glDetachShader(programID, vertexID);
		glDetachShader(programID, fragmentID);
		glDeleteShader(vertexID);
		glDeleteShader(fragmentID);
		glDeleteProgram(programID);
	}

	private String loadToString(String file) {
		StringBuilder sb = new StringBuilder();
		currentFile = "/shaders/"+file+".glsl";
		try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(currentFile)))) {
			String string;
			while ((string = br.readLine()) != null) {
				sb.append(string + "\n");
			}
		} catch (IOException e) {
			System.err.println("Failed to read shader file!");
			e.printStackTrace();
			System.exit(-1);
		}

		return sb.toString();
	}

}
