package com.fys.alluxio.fs;

import alluxio.AlluxioURI;
import alluxio.client.file.FileInStream;
import alluxio.client.file.FileSystem;
import alluxio.exception.AlluxioException;

import java.io.IOException;

public class FileSystemRead {

  public static void main(String[] args) {
    FileSystem fs = FileSystem.Factory.get();
    AlluxioURI path = new AlluxioURI("/data/alloxio");
    try {
      FileInStream input = fs.openFile(path);
      byte[] buf = new byte[512];
      int read = input.read(buf);
      while (read != -1) {
        System.out.write(buf, 0 , read);
        read = input.read(buf);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (AlluxioException e) {
      e.printStackTrace();
    }
  }
}
