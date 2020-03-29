package mops.module.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mops.module.database.Modul;
import mops.module.services.ModulService;
import mops.module.services.PdfService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/module")
public class PdfDownloadController {

    private final ModulService modulService;
    private final PdfService pdfService;

    /**
     * Stellt das aktuelle PDF-Modulhandbuch zum Download bereit.
     *
     * @param response HTTP Response
     */
    @GetMapping("/pdf")
    public void getPdf(HttpServletResponse response) {

        List<Modul> module = modulService.getAllSichtbareModule();

        ByteArrayOutputStream pdfDocument = pdfService.generatePdf(module);

        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=Modulhandbuch.pdf");

        try {
            pdfDocument.writeTo(response.getOutputStream());
            response.flushBuffer();
            pdfDocument.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}