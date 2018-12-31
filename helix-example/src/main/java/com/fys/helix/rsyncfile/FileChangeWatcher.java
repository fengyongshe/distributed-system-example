package com.fys.helix.rsyncfile;

public interface FileChangeWatcher {

  void onEntryModified(String path);
  void onEntryAdded(String path);
  void onEntryDeleted(String path);

}
