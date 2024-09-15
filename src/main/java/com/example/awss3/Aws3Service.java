package com.example.awss3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
@Service
@Slf4j
public class Aws3Service {
    private static final Logger log = LogManager.getLogger(Aws3Service.class);
    @Value("${cloud.application.bucket.name}")
    private String bucketName;
    @Autowired
    private AmazonS3 amazonS3;

    public String uploadFile(MultipartFile file)
    {
        File file1=convertMultipartFile(file);
        String cle=UUID.randomUUID().toString()+file1;
        amazonS3.putObject(bucketName,cle,file1);
        file1.delete();
        return "File uploaded:"+cle+file1;

    }
    public byte[] downloadFile(String fileName) throws IOException {
        S3Object s3Object=amazonS3.getObject(bucketName,fileName);
        S3ObjectInputStream inputStream=s3Object.getObjectContent();
       return IOUtils.toByteArray(inputStream);
    }
    public String deleteFile(String fileName)
    {
        amazonS3.deleteObject(bucketName,fileName);
        return "filename remove:"+fileName;
    }
    private File convertMultipartFile(MultipartFile file)
    {
        File convertedFile=new File(file.getOriginalFilename());
        try(FileOutputStream fos =new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }catch (IOException e)
        {
            log.error("Error converting multipartFile to file",e);
        }
        return convertedFile;
    }

}
