package renderer;

import java.io.*;

import org.lwjgl.util.vector.*;

import input.*;
import terrain.*;

public class Camera implements Serializable {

	private static final long serialVersionUID = 3780637560433800241L;
	private static Vector3f position;
	private static Vector3f rotation;

	private static float movementSpeed;

	public static float getMovementSpeed() {
		return movementSpeed;
	}

	private static int spacebarCount = 0;
	private static boolean flying = false;

	private static boolean inAir = false;
	private static float gravity = 0;

	private static World world;

	private static final float ROTATION_SPEED = 0.2f;
	private static final float SQRT2 = (float) Math.sqrt(2);
	private static final float VERTICAL_OFFSET = 3f;

	public static void create(Vector3f position, Vector3f rotation, float movementSpeed) {
		Camera.position = position;
		Camera.rotation = rotation;
		Camera.movementSpeed = movementSpeed;
	}

	public static void createDefault() {
		Camera.position = new Vector3f(0, 0, 0);
		Camera.rotation = new Vector3f(0, 0, 0);
		Camera.movementSpeed = 0.2f;
	}

	public static void setWorld(World world) {
		Camera.world = world;
	}

	public static void update() {
		calculateUserInputs();
		calculateTerrainCollisions();
	}

	private static void calculateUserInputs() {
		int dx = Mouse.getDX();
		int dy = Mouse.getDY();

		// double scrolly = Mouse.getScrollY();
		// movementSpeed += 0.05 * scrolly;
		// if (movementSpeed < 0) {
		// movementSpeed = 0;
		// }

		if (dx != 0 || dy != 0) {}

		rotation.y += ROTATION_SPEED * dy;
		rotation.x += ROTATION_SPEED * dx;

		if (rotation.y > 90) {
			rotation.y = 90;
		}
		if (rotation.y < -90) {
			rotation.y = -90;
		}
		if (rotation.x >= 360) {
			rotation.x -= 360;
		}
		if (rotation.y <= -360) {
			rotation.x += 360;
		}

		// true if w or s keys are pressed, then further pressing the a or d
		// keys reduces the speed so that the distance moved is constant
		boolean longitudinal = false;

		if (Keyboard.getKeys()[Keyboard.W]) {
			float x = (float) (movementSpeed * -Math.sin(Math.toRadians(rotation.x)));
			float z = (float) (movementSpeed * Math.cos(Math.toRadians(rotation.x)));
			position.x -= x;
			position.z -= z;
			longitudinal = true;
		}
		if (Keyboard.getKeys()[Keyboard.S]) {
			float x = (float) (movementSpeed * -Math.sin(Math.toRadians(rotation.x)));
			float z = (float) (movementSpeed * Math.cos(Math.toRadians(rotation.x)));
			position.x += x;
			position.z += z;
			longitudinal = true;
		}
		if (Keyboard.getKeys()[Keyboard.A]) {
			float x = (float) (movementSpeed * -Math.sin(Math.toRadians(90 + rotation.x)));
			float z = (float) (movementSpeed * Math.cos(Math.toRadians(90 + rotation.x)));
			x /= longitudinal ? SQRT2 : 1;
			z /= longitudinal ? SQRT2 : 1;
			position.x += x;
			position.z += z;
		}
		if (Keyboard.getKeys()[Keyboard.D]) {
			float x = (float) (movementSpeed * -Math.sin(Math.toRadians(90 + rotation.x)));
			float z = (float) (movementSpeed * Math.cos(Math.toRadians(90 + rotation.x)));
			x /= longitudinal ? SQRT2 : 1;
			z /= longitudinal ? SQRT2 : 1;
			position.x -= x;
			position.z -= z;
		}
		if (Keyboard.getKeys()[Keyboard.LSHIFT]) {
			position.y -= movementSpeed;
		}
		if (Keyboard.getKeys()[Keyboard.SPACE]) {
			position.y += movementSpeed;
			inAir = true;
		} else {
			inAir = false;
		}
		if (Keyboard.isSpacebarDoubleTapped()) {
			flying = !flying;
		}

		if (spacebarCount > 0) {
			spacebarCount--;
		}

	}

	private static void calculateTerrainCollisions() {
		float terrainHeight = world.getHeight(position.x, position.z);

		if (position.y > terrainHeight + VERTICAL_OFFSET && !inAir && !flying) {
			gravity += 0.02f;
			position.y -= gravity;
		} else {
			gravity = 0;
		}

		if (position.y < terrainHeight + VERTICAL_OFFSET) {
			position.y = terrainHeight + VERTICAL_OFFSET;
		}

	}

	public static Vector3f getPosition() {
		return position;
	}

	public static Vector3f getRotation() {
		return rotation;
	}

}
