package com.rm.controller;

import com.rm.entity.DocumentType;
import com.rm.entity.EmployeeDocument;
import com.rm.service.EmployeeDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class EmployeeDocumentController {

    private final EmployeeDocumentService service;

    @PostMapping("/upload")
    public EmployeeDocument uploadDocument(

            @RequestParam Long employeeId,

            @RequestParam DocumentType type,

            @RequestParam MultipartFile file
    ) {

        return service.uploadDocument(
                employeeId,
                type,
                file
        );
    }

    @GetMapping("/{employeeId}")
    public List<EmployeeDocument> getDocuments(
            @PathVariable Long employeeId
    ) {

        return service.getDocuments(employeeId);
    }
}