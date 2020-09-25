package main.java.datafusion.util;

import java.util.*;

public class Functions {

	public static double dot(double[] x, double[] y) {
		assert x.length == y.length;
		double res = 0.0;
		for (int i = 0; i < x.length; i++)
			res += x[i] * y[i];
		return res;
	}

	public static double L1Norm(double[] x) {
		double res = 0.0;
		for (int i = 0; i < x.length; i++)
			res += Math.abs(x[i]);
		return res;
	}

	public static double L2Norm(double[] x) {
		double res = 0.0;
		for (int i = 0; i < x.length; i++)
			res += Math.pow(x[i], 2);
		return Math.sqrt(res);
	}

	public static double cosine(double[] x, double[] y) {
		assert x.length == y.length;
		return dot(x, y) / (L2Norm(x) * L2Norm(y));
	}

	public static double L1dist(double[] x, double[] y) {
		assert x.length == y.length;
		double dist = 0.0;
		for (int i = 0; i < x.length; i++) {
			dist += Math.abs(x[i] - y[i]);
		}
		return dist;
	}

	public static double L2dist(double[] x, double[] y) {
		assert x.length == y.length;
		double dist = 0.0;
		for (int i = 0; i < x.length; i++) {
			dist += Math.pow(x[i] - y[i], 2);
		}
		dist = Math.sqrt(dist);
		return dist;
	}

	public static float minFloats(List<Float> floats) {
		float min = Float.MAX_VALUE;
		for (Float f : floats) {
			if (f < min)
				min = f;
		}
		return min;
	}

	public static float maxFloats(List<Float> floats) {
		float max = Float.MIN_VALUE;
		for (Float f : floats) {
			if (f > max)
				max = f;
		}
		return max;
	}
}
