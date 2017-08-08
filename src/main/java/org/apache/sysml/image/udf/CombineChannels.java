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

import org.apache.sysml.image.ImgUtil;
import org.apache.sysml.udf.FunctionParameter;
import org.apache.sysml.udf.Matrix;
import org.apache.sysml.udf.Matrix.ValueType;
import org.apache.sysml.udf.PackageFunction;

public class CombineChannels extends PackageFunction {
	private static final long serialVersionUID = -5518339331025304092L;

	private Matrix m;

	@Override
	public int getNumFunctionOutputs() {
		return 1;
	}

	@Override
	public FunctionParameter getFunctionOutput(int pos) {
		return m;
	}

	@Override
	public void execute() {
		try {
			Matrix alpha = (Matrix) getFunctionInput(0);
			Matrix red = (Matrix) getFunctionInput(1);
			Matrix green = (Matrix) getFunctionInput(2);
			Matrix blue = (Matrix) getFunctionInput(3);

			int[][] a = ImgUtil.dToI(alpha.getMatrixAsDoubleArray());
			int[][] r = ImgUtil.dToI(red.getMatrixAsDoubleArray());
			int[][] g = ImgUtil.dToI(green.getMatrixAsDoubleArray());
			int[][] b = ImgUtil.dToI(blue.getMatrixAsDoubleArray());

			int[][] i = ImgUtil.combineChannels(a, r, g, b);
			double[][] d = ImgUtil.iToD(i);

			int width = d[0].length;
			int height = d.length;

			m = new Matrix(height, width, ValueType.Double);
			m.setMatrixDoubleArray(d);
		} catch (Exception e) {
			throw new RuntimeException("Error combining channels", e);
		}
	}
}
