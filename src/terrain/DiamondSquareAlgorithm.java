package terrain;

import java.util.*;

import utils.*;

public class DiamondSquareAlgorithm {

	private float[][] heightMap;
	private Random random;

	private int max;
	private float factor;

	public DiamondSquareAlgorithm(int size, int factor, long seed) {
		max = (int) Math.pow(2, size);
		heightMap = new float[max + 1][max + 1];
		random = new Random(seed);

		heightMap[0][0] = MathUtils.randFloat(-factor, factor, random);
		heightMap[0][max] = MathUtils.randFloat(-factor, factor, random);
		heightMap[max][0] = MathUtils.randFloat(-factor, factor, random);
		heightMap[max][max] = MathUtils.randFloat(-factor, factor, random);

		divide(max);

	}

	public float[][] getHeightMap() {
		return heightMap;
	}

	private void divide(int size) {
		int half = size / 2;

		if (half <= 1)
			return;
		
		//for (int y = )
		
		for (int y = 0; y <= max; y += half) {
			for (int x = (y + half) % size; x <= max; x += size) {
				diamond(x, y, half, MathUtils.randFloat(-factor, factor, random) * size);
			}
		}
		
		
		for (int y = half; y < max; y += size) {
			for (int x = half; x < max; x += size) {
				square(x, y, half, MathUtils.randFloat(-factor, factor, random) * size);
			}
		}
		
		
		
		divide(half);

	}

	private void square(int x, int y, int size, float randOffset) {
		float total = 0;
		int count = 0;
		try {
			total += heightMap[x + size][y + size];
			count++;
		} catch (ArrayIndexOutOfBoundsException e) {}
		try {
			total += heightMap[x - size][y + size];
			count++;
		} catch (ArrayIndexOutOfBoundsException e) {}
		try {
			total += heightMap[x + size][y - size];
			count++;
		} catch (ArrayIndexOutOfBoundsException e) {}
		try {
			total += heightMap[x - size][y - size];
			count++;
		} catch (ArrayIndexOutOfBoundsException e) {}
		float avg = total / count;
		heightMap[x][y] = avg + randOffset;
	}

	private void diamond(int x, int y, int size, float randOffset) {

		float total = 0;
		total += heightMap[x][y - size];
		total += heightMap[x + size][y];
		total += heightMap[x][y + size];
		total += heightMap[x - size][y];
		float avg = total / 4;
		heightMap[x][y] = avg + randOffset;
	}
}
