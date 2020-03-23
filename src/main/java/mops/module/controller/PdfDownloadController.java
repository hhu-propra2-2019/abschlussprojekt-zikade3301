package mops.module.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import mops.module.database.Modul;
import mops.module.generator.ModulFaker;
import mops.module.services.PdfService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/module")
public class PdfDownloadController {

    /**
     * @param response
     */
    @GetMapping("/pdf")
    public void getPdf(HttpServletResponse response) {

        List<Modul> module = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            module.add(ModulFaker.generateFakeModul());
        }
        PDDocument document1 = PdfService.generatePdf(module);

        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=Modulhandbuch.pdf");

        try {
            document1.save(response.getOutputStream());
            response.getOutputStream().flush();
            document1.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}