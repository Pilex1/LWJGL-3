package textures;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.*;
import java.nio.*;
import java.util.*;

import de.matthiasmann.twl.utils.*;
import de.matthiasmann.twl.utils.PNGDecoder.*;


public class Texture {

	private int width, height;
	private int textureID;

	private static List<Integer> textures = new ArrayList<>();
	private static final float MIPMAPPING_FACTOR = 0.1f;

	public Texture(String file) {
		try (InputStream in = getClass().getResourceAsStream("/res/textures/" + file + ".png")) {
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();

			ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
			buffer.flip();

			textureID = glGenTextures();
			textures.add(textureID);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, textureID);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			
			glGenerateMipmap(GL_TEXTURE_2D);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, MIPMAPPING_FACTOR);
			
			

			glBindTexture(GL_TEXTURE_2D, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getTextureID() {
		return textureID;
	}

}
