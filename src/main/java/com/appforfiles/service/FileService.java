package com.appforfiles.service;

import java.util.List;
import java.util.Optional;
import com.appforfiles.model.File;

public interface FileService {

  public List<File> getAll();

  public Optional<File> getById(Integer id);

  public void saveOrUpdate(File file);

  public void deleteById(Integer id);

  void delete(File files);
}
