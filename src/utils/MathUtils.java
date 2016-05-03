package utils;

import java.util.*;

import org.lwjgl.util.vector.*;

import renderer.*;

public class MathUtils {

	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		translateMatrix(matrix, translation);
		rotateMatrix(matrix, rotation);
		scaleMatrix(matrix, scale);
		return matrix;
	}

	public static void translateMatrix(Matrix4f matrix, Vector3f translation) {
		Matrix4f.translate(translation, matrix, matrix);
	}

	public static void rotateMatrix(Matrix4f matrix, Vector3f rotation) {
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
	}

	public static void scaleMatrix(Matrix4f matrix, float scale) {
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
	}

	public static Matrix4f createProjectionMatrix(float fov, float farPlane, float nearPlane) {

		float width = Display.getWidth();
		float height = Display.getHeight();

		float aspectRatio = width / height;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = farPlane - nearPlane;

		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((farPlane + nearPlane) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustum_length);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}

	public static Matrix4f createViewMatrix() {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(Camera.getRotation().y), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(Camera.getRotation().x), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Vector3f cameraPos = Camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}

	public static int randInt(int min, int max) {
		return new Random().nextInt((max - min) + 1) + min;
	}

	public static float randFloat(float min, float max) {
		return new Random().nextFloat() * (max - min) + min;
	}

	public static long randLong(long min, long max) {
		return new Random().nextLong() * (max - min) + min;
	}

	public static Vector3f randVector3f(float min, float max) {
		return new Vector3f(randFloat(min, max), randFloat(min, max), randFloat(min, max));
	}

	public static Vector2f randVector2f(float min, float max) {
		return new Vector2f(randFloat(min, max), randFloat(min, max));
	}

	public static int randInt(int min, int max, Random random) {
		return random.nextInt((max - min) + 1) + min;
	}

	public static float randFloat(float min, float max, Random random) {
		return random.nextFloat() * (max - min) + min;
	}

	public static long randLong(long min, long max, Random random) {
		return random.nextLong() + (max - min) + min;
	}

	public static Vector3f randVector3f(float min, float max, Random random) {
		return new Vector3f(randFloat(min, max, random), randFloat(min, max, random), randFloat(min, max, random));
	}

	public static Vector2f randVector2f(float min, float max, Random random) {
		return new Vector2f(randFloat(min, max, random), randFloat(min, max, random));
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

}
