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
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.spark.sql.SparkSession;
import org.apache.sysml.api.mlcontext.MLContext;
import org.apache.sysml.image.ImgDml.Color;

public class ImageExample {

	public enum Channel {
		ALPHA, RED, GREEN, BLUE
	};

	public static void main(String[] args) throws Exception {

		SparkSession ss = SparkSession.builder().appName("SystemML").master("local").getOrCreate();
		new MLContext(ss);

		File file = new File("src/main/resources/images/apache-systemml-logo.png");
		BufferedImage bi = ImageIO.read(file);
		int width = bi.getWidth();
		int height = bi.getHeight();
		int[][] frontIm = ImgIO.buffToI(bi);

		// change logo text color
		ImgChannels frontCh = ImgDml.splitImgChannels(frontIm);
		frontCh.green = ImgDml.shiftColorChannel(frontCh.green, -255);
		frontCh.blue = ImgDml.shiftColorChannel(frontCh.blue, -255);
		frontCh.red = ImgDml.shiftColorChannel(frontCh.red, 255);
		frontIm = ImgDml.combineChannels(frontCh.alpha, frontCh.red, frontCh.green, frontCh.blue);

		// create background image
		ImgChannels backCh = ImgDml.colorImgChannels(Color.lime, width, height);
		int[][] backIm = ImgDml.combineChannels(backCh.alpha, backCh.red, backCh.green, backCh.blue);

		// layer images
		int[][] comboIm = ImgDml.layer(backIm, frontIm);

		comboIm = ImgDml.splitVertical(comboIm);
		comboIm = ImgDml.rotate90(comboIm);
		comboIm = ImgDml.splitVertical(comboIm);
		comboIm = ImgDml.rotate270(comboIm);

		BufferedImage newBi = ImgIO.iToBuff(comboIm);
		displayImage(newBi);
	}

	public static void displayMatrix(int[][] matrix) {
		System.out.println("Matrix size:" + matrix.length + "x" + matrix[0].length);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (j > 0) {
					System.out.print(", ");
				}
				System.out.print("[" + i + "," + j + "]:" + matrix[i][j]);
			}
			System.out.println();
		}
	}

	public static void displayMatrix(double[][] matrix) {
		System.out.println("Matrix size:" + matrix.length + "x" + matrix[0].length);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (j > 0) {
					System.out.print(", ");
				}
				System.out.print("[" + i + "," + j + "]:" + matrix[i][j]);
			}
			System.out.println();
		}
	}

	public static void displayImage(BufferedImage bi) {
		JLabel jlabel = new JLabel(new ImageIcon(bi));
		JFrame jframe = new JFrame();
		jframe.getContentPane().add(jlabel);
		jframe.setSize(bi.getWidth(), bi.getHeight());
		jframe.setVisible(true);
	}

}
