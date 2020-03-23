package mops.module.services;

import java.io.IOException;
import mops.module.database.Modul;
import mops.module.generator.ModulFaker;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;

public class PdfServiceTest {
    @Test
    public void oneModulPdfTest() throws IOException {
//        Modul modul = ModulFaker.generateFakeModul();
//        PDDocument document = PdfService.generatePdf(modul);

        PDDocument document1 = new PDDocument();
        document1.save("test.pdf");
        System.out.println("PDF Created");
        document1.close();
//        try {
//            document.save("~/test.pdf");
//            document.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
