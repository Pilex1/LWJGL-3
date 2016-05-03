package serialization;

import java.io.*;

import org.lwjgl.util.vector.*;

import renderer.*;

public class FilesManager {

	private static final String DIR = "E:/Plexico/LWJGL 3/";

	private static final String CAMERA = "camera.ser";

	private FilesManager() {}

	public static void load() {
		try (InputStream fileIn = new FileInputStream(DIR + CAMERA); ObjectInputStream in = new ObjectInputStream(fileIn);) {
			Vector3f position = (Vector3f) in.readObject();
			Vector3f rotation = (Vector3f) in.readObject();
			float movementSpeed = in.readFloat();
			Camera.create(position, rotation, 0.2f);
			//throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
			Camera.create(new Vector3f(0,0,0), new Vector3f(0,0,0), 0.2f);
		}
	}

	public static void save() {
		new File(DIR).mkdirs();
		try (OutputStream fileOut = new FileOutputStream(DIR + CAMERA); ObjectOutputStream out = new ObjectOutputStream(fileOut);) {
			out.writeObject(Camera.getPosition());
			out.writeObject(Camera.getRotation());
			out.writeFloat(Camera.getMovementSpeed());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
