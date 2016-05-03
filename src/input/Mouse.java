package input;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;

public class Mouse {

	private static GLFWMouseButtonCallback mouseButtonCallback = new MouseButton();
	private static GLFWScrollCallback mouseScrollCallback = new MouseScroll();

	public static void create(long window) {
		MouseMovement.create(window);
		glfwSetScrollCallback(window, mouseScrollCallback);
		glfwSetMouseButtonCallback(window, mouseButtonCallback);
	}

	public static void update() {
		MouseMovement.update();
	}

	public static int getDX() {
		return MouseMovement.dx;
	}

	public static int getDY() {
		return MouseMovement.dy;
	}

	public static double getScrollX() {
		return MouseScroll.x;
	}

	public static double getScrollY() {
		return MouseScroll.y;
	}

	private static class MouseButton extends GLFWMouseButtonCallback {
		@Override public void invoke(long window, int button, int action, int mods) {

		}
	}

	private static class MouseScroll extends GLFWScrollCallback {
		private static double x = 0;
		private static double y = 0;

		@Override public void invoke(long window, double xoffset, double yoffset) {
			MouseScroll.x = xoffset;
			MouseScroll.y = yoffset;
		}
	}

	private static class MouseMovement {

		private static Integer x, y;
		private static Integer dx, dy;

		private static long window;

		private static void create(long window) {
			MouseMovement.window = window;
		}

		private static void update() {
			DoubleBuffer bufferx = BufferUtils.createDoubleBuffer(1), buffery = BufferUtils.createDoubleBuffer(1);
			glfwGetCursorPos(window, bufferx, buffery);
			int curx = (int) bufferx.get();
			int cury = (int) buffery.get();
			dx = curx - (x == null ? curx : x);
			dy = cury - (y == null ? cury : y);
			x = curx;
			y = cury;
		}
	}

}
