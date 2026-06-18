package com.rm.service;

import com.rm.entity.DocumentType;
import com.rm.entity.Employee;
import com.rm.entity.EmployeeDocument;
import com.rm.repository.EmployeeDocumentRepository;
import com.rm.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmployeeDocumentService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeDocumentRepository documentRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public EmployeeDocument uploadDocument(
            Long employeeId,
            DocumentType type,
            MultipartFile file
    ) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"
                                ));

        String filePath =
                fileStorageService
                        .uploadEmployeeDocument(
                                file,
                                employee.getEmployeeCode()
                        );

        EmployeeDocument document =
                EmployeeDocument.builder()
                        .employee(employee)
                        .documentType(type)
                        .fileName(
                                file.getOriginalFilename()
                        )
                        .filePath(filePath)
                        .uploadedAt(
                                LocalDateTime.now()
                        )
                        .build();

        return documentRepository.save(document);
    }

    public List<EmployeeDocument>
    getDocuments(Long employeeId) {

        return documentRepository
                .findByEmployeeId(employeeId);
    }

}
