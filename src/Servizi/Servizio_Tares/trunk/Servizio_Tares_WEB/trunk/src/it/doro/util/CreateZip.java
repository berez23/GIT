package it.doro.util;

import java.io.*;
import java.util.zip.*;

public class CreateZip {
	
  public static int buffer = 10240;
  
  public void createZip(File zipFile, File[] listFiles) {
    try {
      byte b[] = new byte[buffer];
      FileOutputStream fout = new FileOutputStream(zipFile);
      ZipOutputStream out = new ZipOutputStream(fout);
      for (int i = 0; i < listFiles.length; i++) {
        if (listFiles[i] == null || !listFiles[i].exists()|| listFiles[i].isDirectory())
      System.out.println();
        ZipEntry addFiles = new ZipEntry(listFiles[i].getName());
        addFiles.setTime(listFiles[i].lastModified());
        out.putNextEntry(addFiles);

        FileInputStream fin = new FileInputStream(listFiles[i]);
        while (true) {
          int len = fin.read(b, 0, b.length);
          if (len <= 0)
            break;
          out.write(b, 0, len);
        }
        fin.close();
      }
      out.close();
      fout.close();
      System.out.println("Zip File is created successfully.");
    } catch (Exception ex){
    	ex.printStackTrace();
    }
  }
  
//  public static void main(String[]args){
//    CreateJar jar=new CreateJar();
//    File folder = new File("C://Answers//Examples");
//      File[] files = folder.listFiles();
//    File file=new File("C://Answers//Examples//Examples.zip");
//    jar.createZip(file, files);
//  }
  
}