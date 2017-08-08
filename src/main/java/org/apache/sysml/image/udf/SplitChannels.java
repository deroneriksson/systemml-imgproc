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

package org.apache.sysml.image.udf;

import org.apache.sysml.image.ImageExample.Channel;
import org.apache.sysml.image.ImgUtil;
import org.apache.sysml.udf.FunctionParameter;
import org.apache.sysml.udf.Matrix;
import org.apache.sysml.udf.Matrix.ValueType;
import org.apache.sysml.udf.PackageFunction;

public class SplitChannels extends PackageFunction {
	private static final long serialVersionUID = -2948732287947937128L;

	private Matrix alpha;
	private Matrix red;
	private Matrix green;
	private Matrix blue;

	@Override
	public int getNumFunctionOutputs() {
		return 4;
	}

	@Override
	public FunctionParameter getFunctionOutput(int pos) {
		switch (pos) {
		case 0:
			return alpha;
		case 1:
			return red;
		case 2:
			return green;
		case 3:
			return blue;
		}
		throw new RuntimeException("Invalid output");
	}

	@Override
	public void execute() {
		try {
			Matrix im = (Matrix) getFunctionInput(0);
			double[][] d = im.getMatrixAsDoubleArray();

			int width = d[0].length;
			int height = d.length;

			double[][] a = new double[height][width];
			double[][] r = new double[height][width];
			double[][] g = new double[height][width];
			double[][] b = new double[height][width];

			int[][] i = ImgUtil.dToI(d);

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					a[y][x] = ImgUtil.obtainPixelByChannel(i[y][x], Channel.ALPHA);
					r[y][x] = ImgUtil.obtainPixelByChannel(i[y][x], Channel.RED);
					g[y][x] = ImgUtil.obtainPixelByChannel(i[y][x], Channel.GREEN);
					b[y][x] = ImgUtil.obtainPixelByChannel(i[y][x], Channel.BLUE);
				}
			}
			alpha = new Matrix(height, width, ValueType.Double);
			red = new Matrix(height, width, ValueType.Double);
			green = new Matrix(height, width, ValueType.Double);
			blue = new Matrix(height, width, ValueType.Double);

			alpha.setMatrixDoubleArray(a);
			red.setMatrixDoubleArray(r);
			green.setMatrixDoubleArray(g);
			blue.setMatrixDoubleArray(b);

		} catch (Exception e) {
			throw new RuntimeException("Error splitting channels", e);
		}
	}
}
