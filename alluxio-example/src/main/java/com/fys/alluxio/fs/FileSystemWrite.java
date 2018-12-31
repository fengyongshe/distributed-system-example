package com.fys.alluxio.fs;

import alluxio.AlluxioURI;
import alluxio.client.file.FileOutStream;
import alluxio.client.file.FileSystem;
import alluxio.exception.AlluxioException;

import java.io.IOException;

public class FileSystemWrite {

  public static void main(String[] args) {
    FileSystem fs = FileSystem.Factory.get();
    AlluxioURI path = new AlluxioURI("/data/alloxio");
    try {
      FileOutStream out = fs.createFile(path);
      out.write("Hello Alluxio".getBytes());
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (AlluxioException e) {
      e.printStackTrace();
    }

  }
}
