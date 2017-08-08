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

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.apache.sysml.image.ImageExample.Channel;

public class ImgIO {

	public static int[][] buffToI(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		int[][] i = new int[height][width];
		for (int y = 0; y < height; y++) {
			int[] row = new int[width];
			bi.getRGB(0, y, width, 1, row, 0, 1);
			i[y] = row;
		}
		return i;
	}

	public static BufferedImage iToBuff(int[][] i) {
		int width = i[0].length;
		int height = i.length;
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < height; y++) {
			int[] row = i[y];
			bi.setRGB(0, y, width, 1, row, 0, 1);
		}
		return bi;
	}

	public static ImgChannels buffToImgChannels(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		ImgChannels ic = new ImgChannels(width, height);
		for (int y = 0; y < height; y++) {
			int[] row = new int[width];
			bi.getRGB(0, y, width, 1, row, 0, 1);
			for (int x = 0; x < width; x++) {
				ic.alpha[y][x] = ImgUtil.obtainPixelByChannel(row[x], Channel.ALPHA);
				ic.red[y][x] = ImgUtil.obtainPixelByChannel(row[x], Channel.RED);
				ic.green[y][x] = ImgUtil.obtainPixelByChannel(row[x], Channel.GREEN);
				ic.blue[y][x] = ImgUtil.obtainPixelByChannel(row[x], Channel.BLUE);
			}
		}
		return ic;
	}

	public static Map<String, int[][]> buffToChannels(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		int[][] alpha = new int[height][width];
		int[][] red = new int[height][width];
		int[][] green = new int[height][width];
		int[][] blue = new int[height][width];
		for (int y = 0; y < height; y++) {
			int[] row = new int[width];
			bi.getRGB(0, y, width, 1, row, 0, 1);
			for (int x = 0; x < width; x++) {
				alpha[y][x] = ImgUtil.obtainPixelByChannel(row[x], Channel.ALPHA);
				red[y][x] = ImgUtil.obtainPixelByChannel(row[x], Channel.RED);
				green[y][x] = ImgUtil.obtainPixelByChannel(row[x], Channel.GREEN);
				blue[y][x] = ImgUtil.obtainPixelByChannel(row[x], Channel.BLUE);
			}
		}
		Map<String, int[][]> m = new HashMap<String, int[][]>();
		m.put("a", alpha);
		m.put("r", red);
		m.put("g", green);
		m.put("b", blue);
		return m;
	}
}
