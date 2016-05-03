package renderer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import input.*;

public class Display {

	private static final boolean VSYNC = true;
	private static final boolean FULLSCREEN = true;

	//debug sizes for when fullscreen is disabled
	private static final int WIDTH = 800, HEIGHT = 600;

	private static GLFWErrorCallback errorCallback;
	private static GLFWKeyCallback keyboardCallback = new Keyboard();

	private static long window;

	private static int width, height;

	public static void create() {
		errorCallback = GLFWErrorCallback.createPrint(System.err);
		glfwSetErrorCallback(errorCallback);

		// initializes the GLFW library
		if (glfwInit() == GL_FALSE) {
			System.err.println("Unable to initialize GLFW");
			System.exit(-1);
		}

		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		width = FULLSCREEN ? videoMode.width() : WIDTH;
		height = FULLSCREEN ? videoMode.height() : HEIGHT;
		System.out.printf("Window size: %s by %s%n", width, height);

		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

		// true fullscreen
		// window = glfwCreateWindow(videoMode.width(), videoMode.height(),
		// "LWJGL 3!", FULLSCREEN?glfwGetPrimaryMonitor():NULL, NULL);

		// false fullscreen - using borderless windows - faster
		glfwWindowHint(GLFW_DECORATED, FULLSCREEN ? GLFW_FALSE : GLFW_TRUE);
		window = glfwCreateWindow(width, height, "LWJGL 3!", NULL, NULL);

		// creates the window
		if (window == NULL) {
			System.err.println("Unable to create GLFW window");
			System.exit(-1);
		}

		// gets rid of the normal mouse in the program
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

		// creates key callback - i.e. listeners for key events
		glfwSetKeyCallback(window, keyboardCallback);
		Mouse.create(window);

		glfwMakeContextCurrent(window);

		GL.createCapabilities();

		System.out.println("Using LWJGL " + Version.getVersion() + " with OpenGL " + glGetString(GL_VERSION));

		// if (glfwExtensionSupported())
		// enables v-sync
		glfwSwapInterval(VSYNC ? 1 : 0);

		glfwShowWindow(window);

	}

	public static void update() {

		glfwPollEvents(); // processes events such as keyboard and mouse

		glfwSwapBuffers(window); // vsync

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clears the screen
	}

	public static void close() {
		glfwDestroyWindow(window);
		keyboardCallback.release();
		glfwTerminate();
		errorCallback.release();
	}

	public static long getWindow() {
		return window;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

}
