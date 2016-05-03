package terrain;

import java.util.*;

import org.lwjgl.util.vector.*;

public class World {

	private Terrain[][] world;

	public Terrain[][] getWorld() {
		return world;
	}
	
	/**
	 * size of the entire world
	 */
	private Vector2f size;

	/**
	 * number of terrains in the world
	 */
	private Vector2f terrains;

	/**
	 * @param sizeX
	 *            the number of terrains in the X direction
	 * @param sizeZ
	 *            the number of terrains in the Z direction
	 */
	public World(int terrainsX, int terrainsZ) {
		world = new Terrain[terrainsX][terrainsZ];
		size = new Vector2f(terrainsX * Terrain.getSize(),terrainsZ * Terrain.getSize());
		terrains = new Vector2f(terrainsX, terrainsZ);
	}

	/**
	 * 
	 * @param terrain
	 *            the terrain to be added
	 * @param x
	 *            the X chunk that the terrain is to be added to
	 * @param z
	 *            the Z chunk that the terrain is to be added to
	 * @return true if the terrain was added successfully, false if the chunk
	 *         location was out of bounds
	 */
	public boolean add(Terrain terrain, int x, int z) {
		try {
			if (x >= size.x || z >= size.y) {
				throw new WorldOutOfBoundsException();
			}
			world[x][z] = terrain;
			terrain.setX(x * Terrain.getSize());
			terrain.setZ(z * Terrain.getSize());
		} catch (WorldOutOfBoundsException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public float getHeight(float x, float z) {
		int terrainX = (int) (x / Terrain.getSize());
		int terrainZ = (int) (z / Terrain.getSize());
		try {
			Terrain terrain = world[terrainX][terrainZ];
			return terrain.getHeightOfTerrain(x, z);
		} catch (Exception e) {
			return 0;
		}
	}

	public Vector3f getCoords(float x, float z) {
		return new Vector3f(x, getHeight(x, z), z);
	}

	public Vector3f getCoords(float x, float z, float offsetY) {
		return new Vector3f(x, getHeight(x, z) + offsetY, z);
	}

	public Iterator<Terrain> getIterator() {
		List<Terrain> list = new ArrayList<Terrain>();
		for (Terrain[] t1 : world) {
			for (Terrain t2 : t1) {
				list.add(t2);
			}
		}
		return list.iterator();
	}
	
	public Vector2f getSize() {
		return size;
	}

	public Vector2f getTerrains() {
		return terrains;
	}
	
	public Vector3f getCenter(int yoffset) {
		return getCoords(terrains.x*Terrain.getSize()/2,terrains.y*Terrain.getSize()/2, yoffset);
	}

	private static class WorldOutOfBoundsException extends Exception {
		public WorldOutOfBoundsException() {
			super("Cannot access outside world bounds");
		}
	}

}
