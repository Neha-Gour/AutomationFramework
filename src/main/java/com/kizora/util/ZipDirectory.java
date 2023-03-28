package com.kizora.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipDirectory {
	// Zip the existing ExtentReports folder to

//	public static void main(String[] args) throws IOException {
	public String getZipDirectory() throws IOException {
		
		
		String fname = ZipDirectory.createFolder();
		String sourceFile = String.valueOf(System.getProperty("user.dir"))+"\\ExtentReports";
		System.out.println(sourceFile);
		FileOutputStream fos = new FileOutputStream(fname + ".zip");
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		File fileToZip = new File(sourceFile);

		zipFile(fileToZip, fileToZip.getName(), zipOut);
		zipOut.close();
		fos.close();
		return fname;
	}

	// Create a folder with current date
	public static String createFolder() {
		// Set the path creating new folder with currentdate name
		String folderPath = String.valueOf(System.getProperty("user.dir"));
		// Enter the name for folder

		Date date = new Date();
		SimpleDateFormat  formatter = new SimpleDateFormat("ddMMMyy-hh-mm");
		String todayDate = formatter.format(date);
		System.out.println("Date Format with ddMMMyy-hh-mm : " + todayDate);

		System.out.println("FileName:" + todayDate + folderPath);

		// Complete path
		String path = folderPath + "\\" + "RCSReport"+todayDate;
		System.out.println(path);

		// Create a folder with latest date/time by instantiate the file class
		File f1 = new File(path);

		// Create a folder using mkdir()method
		boolean resFolder = f1.mkdir();

		if (resFolder) {
			System.out.println("Folder created successfully");
			return path;
		} else
			System.out.println("Error Found!");

		return null;

	}

	private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
		if (fileToZip.isHidden()) {
			return;
		}
		if (fileToZip.isDirectory()) {
			if (fileName.endsWith("/")) {
				zipOut.putNextEntry(new ZipEntry(fileName));
				zipOut.closeEntry();
			} else {
				zipOut.putNextEntry(new ZipEntry(fileName + "/"));
				zipOut.closeEntry();
			}
			File[] children = fileToZip.listFiles();
			for (File childFile : children) {
				zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
			}
			return;
		}
		FileInputStream fis = new FileInputStream(fileToZip);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zipOut.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		fis.close();
	}

}
