package com.rm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final String UPLOAD_DIR =
            "uploads/products/";

    public String uploadProductImage(
            MultipartFile file
    ) {

        try {

            if(file.isEmpty()) {

                throw new RuntimeException(
                        "File is empty"
                );
            }

            String contentType =
                    file.getContentType();

            if(contentType == null ||
                    !contentType.startsWith(
                            "image/"
                    )) {

                throw new RuntimeException(
                        "Only images allowed"
                );
            }
            if(file.getSize() >
                    5 * 1024 * 1024) {

                throw new RuntimeException(
                        "Max size 5MB"
                );
            }
            String fileName =
                    System.currentTimeMillis()
                            + "_"
                            + file.getOriginalFilename();

            Path path =
                    Paths.get(
                            UPLOAD_DIR,
                            fileName
                    );

            Files.createDirectories(
                    path.getParent()
            );

            Files.write(
                    path,
                    file.getBytes()
            );

            return fileName;

        } catch (IOException e) {

            throw new RuntimeException(
                    "File upload failed"
            );
        }
    }


    public String uploadEmployeeDocument(
            MultipartFile file,
            String employeeCode
    ) {

        try {

            String folder =
                    "uploads/employees/"
                            + employeeCode
                            + "/";

            String fileName =
                    System.currentTimeMillis()
                            + "_"
                            + file.getOriginalFilename();

            Path path =
                    Paths.get(
                            folder,
                            fileName
                    );

            Files.createDirectories(
                    path.getParent()
            );

            Files.write(
                    path,
                    file.getBytes()
            );

            return folder + fileName;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Document upload failed"
            );
        }
    }
}