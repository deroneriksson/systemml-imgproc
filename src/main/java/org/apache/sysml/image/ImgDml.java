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

import static org.apache.sysml.api.mlcontext.ScriptFactory.dml;

import java.util.HashMap;
import java.util.Map;

import org.apache.sysml.api.mlcontext.MLResults;
import org.apache.sysml.api.mlcontext.Matrix;
import org.apache.sysml.api.mlcontext.Script;

public class ImgDml {

	public static final String SRC_UTIL = "source('img/img_util.dml') as img_util;";

	public enum Color {
		white, silver, gray, black, red, maroon, yellow, olive, lime, green, aqua, teal, blue, navy, fuchsia, purple
	}

	protected static int[][] imgUtilDml(String function, int[][] i) {
		Matrix o = dml(SRC_UTIL + "o=img_util::" + function + "(i)").in("i", ImgUtil.iToD(i)).out("o").execute()
				.getMatrix("o");
		return ImgUtil.mToI(o);
	}

	protected static int[][] imgUtilDml(String function, int[][] i, int x) {
		Matrix o = dml(SRC_UTIL + "o=img_util::" + function + "(i,x)").in("i", ImgUtil.iToD(i)).in("x", x).out("o")
				.execute().getMatrix("o");
		return ImgUtil.mToI(o);
	}

	public static int[][] splitVertical(int[][] i) {
		return imgUtilDml("splitVertical", i);
	}

	public static int[][] flipHorizontal(int[][] i) {
		return imgUtilDml("flipHorizontal", i);
	}

	public static int[][] flipVertical(int[][] i) {
		return imgUtilDml("flipVertical", i);
	}

	public static int[][] transpose(int[][] i) {
		return imgUtilDml("transpose", i);
	}

	public static int[][] rotate90(int[][] i) {
		return imgUtilDml("rotate90", i);
	}

	public static int[][] rotate180(int[][] i) {
		return imgUtilDml("rotate180", i);
	}

	public static int[][] rotate270(int[][] i) {
		return imgUtilDml("rotate270", i);
	}

	public static Map<String, int[][]> splitChannelsJava(int[][] i) {
		Script s = dml(SRC_UTIL + "[a,r,g,b]=img_util::splitChannelsJava(i);").in("i", ImgUtil.iToD(i)).out("a", "r",
				"g", "b");
		MLResults res = s.execute();

		// try {
		// System.out.print("A:" +
		// DataConverter.toString(DataConverter.convertToMatrixBlock(res.getMatrixAs2DDoubleArray("a"))));
		// System.out.print("R:" +
		// DataConverter.toString(DataConverter.convertToMatrixBlock(res.getMatrixAs2DDoubleArray("r"))));
		// System.out.print("G:" +
		// DataConverter.toString(DataConverter.convertToMatrixBlock(res.getMatrixAs2DDoubleArray("g"))));
		// System.out.print("B:" +
		// DataConverter.toString(DataConverter.convertToMatrixBlock(res.getMatrixAs2DDoubleArray("b"))));
		// } catch (DMLRuntimeException e) {
		// e.printStackTrace();
		// }

		Map<String, int[][]> m = new HashMap<String, int[][]>();
		m.put("a", ImgUtil.mToI(res, "a"));
		m.put("r", ImgUtil.mToI(res, "r"));
		m.put("g", ImgUtil.mToI(res, "g"));
		m.put("b", ImgUtil.mToI(res, "b"));
		return m;
	}

	public static Map<String, int[][]> splitChannels(int[][] i) {
		Script s = dml(SRC_UTIL + "[a,r,g,b]=img_util::splitChannels(i);").in("i", ImgUtil.iToD(i)).out("a", "r", "g",
				"b");
		MLResults res = s.execute();
		Map<String, int[][]> m = new HashMap<String, int[][]>();
		m.put("a", ImgUtil.mToI(res, "a"));
		m.put("r", ImgUtil.mToI(res, "r"));
		m.put("g", ImgUtil.mToI(res, "g"));
		m.put("b", ImgUtil.mToI(res, "b"));
		return m;
	}

	public static ImgChannels splitImgChannelsJava(int[][] i) {
		Map<String, int[][]> m = splitChannelsJava(i);
		ImgChannels ic = new ImgChannels(i[0].length, i.length);
		ic.alpha = m.get("a");
		ic.red = m.get("r");
		ic.green = m.get("g");
		ic.blue = m.get("b");
		return ic;
	}

	public static ImgChannels splitImgChannels(int[][] i) {
		Map<String, int[][]> m = splitChannels(i);
		ImgChannels ic = new ImgChannels(i[0].length, i.length);
		ic.alpha = m.get("a");
		ic.red = m.get("r");
		ic.green = m.get("g");
		ic.blue = m.get("b");
		return ic;
	}

	public static int[][] combineChannelsJava(int[][] a, int[][] r, int[][] g, int[][] b) {
		Script s = dml(SRC_UTIL + "o=img_util::combineChannelsJava(a,r,g,b);").in("a", ImgUtil.iToD(a))
				.in("r", ImgUtil.iToD(r)).in("g", ImgUtil.iToD(g)).in("b", ImgUtil.iToD(b)).out("o");
		MLResults res = s.execute();
		return ImgUtil.dToI(res.getMatrix("o").to2DDoubleArray());
	}

	public static int[][] combineChannels(int[][] a, int[][] r, int[][] g, int[][] b) {
		Script s = dml(SRC_UTIL + "o=img_util::combineChannels(a,r,g,b);").in("a", ImgUtil.iToD(a))
				.in("r", ImgUtil.iToD(r)).in("g", ImgUtil.iToD(g)).in("b", ImgUtil.iToD(b)).out("o");
		MLResults res = s.execute();
		// return ImgUtil.dToI(res.getMatrix("o").to2DDoubleArray());
		return ImgUtil.mToI(res, "o");
	}

	public static int[][] shiftColorChannel(int[][] i, int shift) {
		return imgUtilDml("shiftColorChannel", i, shift);
	}

	public static Map<String, int[][]> color(Color color, int w, int h) {
		String c = color.toString();
		Script s = dml(SRC_UTIL + "[a,r,g,b]=img_util::color(c,w,h);").in("c", c).in("w", w).in("h", h).out("a", "r",
				"g", "b");
		MLResults res = s.execute();
		Map<String, int[][]> m = new HashMap<String, int[][]>();
		m.put("a", ImgUtil.mToI(res, "a"));
		m.put("r", ImgUtil.mToI(res, "r"));
		m.put("g", ImgUtil.mToI(res, "g"));
		m.put("b", ImgUtil.mToI(res, "b"));
		return m;
	}

	public static ImgChannels colorImgChannels(Color color, int w, int h) {
		Map<String, int[][]> m = color(color, w, h);
		ImgChannels ic = new ImgChannels(w, h);
		ic.alpha = m.get("a");
		ic.red = m.get("r");
		ic.green = m.get("g");
		ic.blue = m.get("b");
		return ic;
	}

	public static int[][] layer(int[][] i1, int[][] i2) {
		Script s = dml(SRC_UTIL + "o=img_util::layer(i1,i2);").in("i1", ImgUtil.iToD(i1)).in("i2", ImgUtil.iToD(i2))
				.out("o");
		MLResults res = s.execute();
		return ImgUtil.mToI(res, "o");
	}

	public static int[][] demo(int[][] i) {
		return imgUtilDml("demo", i);
	}
}
