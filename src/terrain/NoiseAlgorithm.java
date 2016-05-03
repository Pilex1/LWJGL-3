package terrain;

import java.util.*;

import utils.*;

public class NoiseAlgorithm {

	private Random random;
	private float amplitude;

	public NoiseAlgorithm(float amplitude, long seed) {
		random = new Random(seed);
		this.amplitude = amplitude;
	}

	public float generateHeights(int x, int z) {
		float height = getNoise(x, z);
		return height;
	}

	private float getInterpolatedNoise(float x, float z) {
		int intX = (int) x;
		int intZ = (int) z;
		float fracX = x - intX;
		float fracZ = z - intZ;

		float v1 = getSmoothNoise(intX, intZ);
		float v2 = getSmoothNoise(intX + 1, intZ);
		float v3 = getSmoothNoise(intX, intZ + 1);
		float v4 = getSmoothNoise(intX + 1, intZ + 1);
		
		float i1 = interpolateCosine(v1, v2, fracX);
		float i2 = interpolateCosine(v3, v4, fracX);
		return interpolateCosine(i1, i2, fracZ);
	}

	private float interpolateCosine(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float) ((1f - Math.cos(theta)) / 2);
		return a * (1 - f) + b * f;
	}

	private float getSmoothNoise(int x, int z) {
		float corners = (getNoise(x - 1, z - 1) + getNoise(x - 1, z + 1) + getNoise(x + 1, z - 1) + getNoise(x + 1, z + 1)) / 4f / 4f;
		float sides = (getNoise(x - 1, z) + getNoise(x + 1, z) + getNoise(x, z - 1) + getNoise(x, z + 1)) / 4f / 2f;
		float center = getNoise(x, z) / 4f;
		return corners + sides + center;
	}

	private float getNoise(int x, int z) {
		return MathUtils.randFloat(-amplitude, amplitude, random);
	}

}
