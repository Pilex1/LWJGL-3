package input;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.*;

public class Keyboard extends GLFWKeyCallback {

	public static final int W = 0, A = 1, S = 2, D = 3, ESCAPE = 4, LSHIFT = 5, SPACE = 6;

	private static boolean[] keys = new boolean[7];

	private static int spacebarCount = 0;
	private static boolean spacebarDoubleTapped;

	@Override public void invoke(long window, int key, int scancode, int action, int mods) {
		boolean press = action != GLFW_RELEASE;

		switch (key) {
		case GLFW_KEY_W:
			keys[W] = press;
			break;
		case GLFW_KEY_A:
			keys[A] = press;
			break;
		case GLFW_KEY_S:
			keys[S] = press;
			break;
		case GLFW_KEY_D:
			keys[D] = press;
			break;
		case GLFW_KEY_ESCAPE:
			keys[ESCAPE] = press;
			glfwSetWindowShouldClose(window, GL_TRUE);
			break;
		case GLFW_KEY_LEFT_SHIFT:
			keys[LSHIFT] = press;
			break;
		case GLFW_KEY_SPACE:
			if (action == GLFW_PRESS) {
				if (spacebarCount > 0) {
					spacebarDoubleTapped = true;
					spacebarCount=0;
				} else {
					spacebarCount = 10;
				}
			}
			keys[SPACE] = press;
			break;
		}
	}
	
	public static void update() {
		if (spacebarCount > 0) {
			spacebarCount--;
		}
	}

	public static boolean[] getKeys() {
		return keys;
	}

	/**
	 * 
	 * @return true if the spacebar has been double tapped; also resets the
	 *         counter, so this needs to be called every frame from the Camera
	 *         class
	 */
	public static boolean isSpacebarDoubleTapped() {
		if (spacebarDoubleTapped) {
			spacebarDoubleTapped = false;
			spacebarCount=0;
			return true;
		}
		return false;

	}

}
