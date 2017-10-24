package com.myshop.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by chs on 2017-10-24.
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
