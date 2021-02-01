package com.mthoko.mobile.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImagesData {

	public static void main(String[] args) throws IOException {
		URL resource = ImagesData.class.getClassLoader().getResource("./images/test/questions");
		File dir = new File(resource.getFile());
		String[] filenames = dir.list();
		for (String filename : filenames) {
			if (filename.startsWith("q_")) {
				filename = filename.substring(2, filename.indexOf("."));
				if (!filename.contains("_")) {
					System.out.println(filename);
				}
			}
		}

	}
}