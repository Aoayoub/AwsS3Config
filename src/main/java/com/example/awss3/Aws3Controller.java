package com.example.awss3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping
public class Aws3Controller {
    @Autowired
    private Aws3Service aws3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file)
    {
        return new ResponseEntity<>(aws3Service.uploadFile(file), HttpStatus.OK);

    }
    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource>downloadFile(@PathVariable String fileName) throws IOException {
        byte[] data =aws3Service.downloadFile(fileName);
        ByteArrayResource resource =new ByteArrayResource(data);
        return  ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type","application/octet-stream")
                .header("Content-disposition","attachment; filename=\""+fileName+ "\"").body(resource);
    }
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String>deleteFile(@PathVariable String fileName)
    {
        return  new ResponseEntity<>(aws3Service.deleteFile(fileName),HttpStatus.OK);
     }
}
