package com.client.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileProcessor {
	
	public static byte[] fileToBytes(String path) throws IOException
	{
		File file = new File(path);
		byte[] bFile = new byte[(int) file.length()];
        //convert file into array of bytes
	    FileInputStream fileInputStream = new FileInputStream(file);
	    fileInputStream.read(bFile);
	    fileInputStream.close();
	    return bFile;
	}
	
	public static void bytesToFile(byte[] inp, String path) throws IOException
	{
		File file = new File(path);
		FileOutputStream os = new FileOutputStream(file);
		os.write(inp);
		os.close();
	}
}
