package com.myshop.service.impl;

import com.google.common.collect.Lists;
import com.myshop.service.IFileService;
import com.myshop.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by chs on 2017-10-24.
 */
@Service("iFileService")
public class IFileServiceImpl implements IFileService{
    Logger logger = LoggerFactory.getLogger(IFileServiceImpl.class);

    public String upload(MultipartFile file,String path){
      String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadName = UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("上传文件名{}，上传的路径{}，新文件名{}",fileName,path,uploadName);

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadName);

        try {
            file.transferTo(targetFile);
            //长传到ftp服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //删除upload下的文件
//            targetFile.delete();

        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }

}
