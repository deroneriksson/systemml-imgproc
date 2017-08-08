/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.sysml.image;

import org.apache.sysml.api.mlcontext.MLResults;
import org.apache.sysml.api.mlcontext.Matrix;
import org.apache.sysml.image.ImageExample.Channel;

public class ImgUtil {

	public static int[][] dToI(double[][] d) {
		int[][] i = new int[d.length][d[0].length];
		for (int r = 0; r < d.length; r++) {
			for (int c = 0; c < d[0].length; c++) {
				i[r][c] = (int) d[r][c];
			}
		}
		return i;
	}

	public static double[][] iToD(int[][] i) {
		double[][] d = new double[i.length][i[0].length];
		for (int r = 0; r < i.length; r++) {
			for (int c = 0; c < i[0].length; c++) {
				d[r][c] = (double) i[r][c];
			}
		}
		return d;
	}

	public static int[][] mToI(Matrix m) {
		return dToI(m.to2DDoubleArray());
	}

	public static int[][] mToI(MLResults res, String mName) {
		return dToI(res.getMatrix(mName).to2DDoubleArray());
	}

	public static int obtainPixelByChannel(int pixel, Channel channel) {
		switch (channel) {
		case ALPHA:
			return (pixel & 0xFF000000) >> 24;
		case RED:
			return (pixel & 0x00FF0000) >> 16;
		case GREEN:
			return (pixel & 0x0000FF00) >> 8;
		case BLUE:
			return pixel & 0x000000FF;
		}
		throw new RuntimeException("Couldn't mask pixel by channel");
	}

	public static int combineChannels(int alpha, int red, int green, int blue) {
		int argb = (alpha << 24) | (red << 16) | (green << 8) | (blue);
		return argb;
	}

	public static int[][] combineChannels(int[][] alpha, int[][] red, int[][] green, int[][] blue) {
		int width = alpha[0].length;
		int height = alpha.length;
		int[][] result = new int[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				result[y][x] = ImgUtil.combineChannels(alpha[y][x], red[y][x], green[y][x], blue[y][x]);
			}
		}
		return result;
	}

	public static void shiftChannel(int[][] channel, int shift) {
		int width = channel[0].length;
		int height = channel.length;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int val = channel[y][x] + shift;
				if (val > 255) {
					channel[y][x] = 255;
				} else if (val < 0) {
					channel[y][x] = 0;
				} else {
					channel[y][x] = val;
				}
			}
		}
	}
}
