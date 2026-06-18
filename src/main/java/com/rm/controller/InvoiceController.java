package com.rm.controller;

import com.rm.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invoices")
public class InvoiceController {

    private final PdfService pdfService;

    @GetMapping("/{billId}")
    public ResponseEntity<ByteArrayResource>
    downloadInvoice(
            @PathVariable Long billId
    ) {

        byte[] pdf =
                pdfService
                        .generateInvoicePdf(
                                billId
                        );

        ByteArrayResource resource =
                new ByteArrayResource(
                        pdf
                );

        return ResponseEntity.ok()
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoice.pdf"
                )
                .contentLength(
                        pdf.length
                )
                .body(resource);
    }
}