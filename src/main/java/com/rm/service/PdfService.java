package com.rm.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.rm.entity.Bill;
import com.rm.entity.BillItem;
import com.rm.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.lowagie.text.pdf.PdfPTable;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final BillRepository billRepository;

    public byte[] generateInvoicePdf(
            Long billId
    ) {

        Bill bill =
                billRepository.findById(billId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Bill not found"
                                ));

        try {

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            Document document =
                    new Document();

            PdfWriter.getInstance(
                    document,
                    out
            );

            document.open();

            addHeader(
                    document,
                    bill
            );

            addItems(
                    document,
                    bill
            );

            addFooter(
                    document,
                    bill
            );

            document.close();

            return out.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "PDF generation failed",e
            );
        }
    }
    private void addHeader(
            Document document,
            Bill bill
    ) throws Exception {

        Font titleFont =
                new Font(
                        Font.HELVETICA,
                        18,
                        Font.BOLD
                );

        Paragraph title =
                new Paragraph(
                        "RETAIL MANAGEMENT",
                        titleFont
                );

        title.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(title);

        document.add(
                new Paragraph(" ")
        );

        document.add(
                new Paragraph(
                        "Invoice : "
                                + bill.getInvoiceNumber()
                )
        );

        document.add(
                new Paragraph(
                        "Date : "
                                + bill.getCreatedAt()
                )
        );

        document.add(
                new Paragraph(
                        "Customer : "
                                + bill.getCustomer().getName()
                )
        );

        document.add(
                new Paragraph(" ")
        );
    }

    private void addItems(
            Document document,
            Bill bill
    ) throws Exception {

        PdfPTable table =
                new PdfPTable(4);

        table.addCell("Product");
        table.addCell("Qty");
        table.addCell("Price");
        table.addCell("Total");

        for(BillItem item :
                bill.getItems()) {

            table.addCell(
                    item.getProductName()
            );

            table.addCell(
                    String.valueOf(
                            item.getQuantity()
                    )
            );

            table.addCell(
                    item.getPrice()
                            .toString()
            );

            table.addCell(
                    item.getTotalPrice()
                            .toString()
            );
        }

        document.add(table);

        document.add(
                new Paragraph(" ")
        );
    }
    private void addFooter(
            Document document,
            Bill bill
    ) throws Exception {

        document.add(
                new Paragraph(
                        "Total Amount : ₹"
                                + bill.getTotalAmount()
                )
        );

        document.add(
                new Paragraph(
                        "Loyalty Points Earned : "
                                + bill.getEarnedPoints()
                )
        );

        document.add(
                new Paragraph(" ")
        );

        document.add(
                new Paragraph(
                        "Thank You For Shopping"
                )
        );
    }
}