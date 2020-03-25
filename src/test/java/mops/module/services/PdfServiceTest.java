package mops.module.services;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.generator.ModulFaker;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@SpringBootTest
@ActiveProfiles("dev")
public class PdfServiceTest {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private PdfService pdfService;

    @Test
    public void oneModulPdfTest() throws IOException {
        List<Modul> module = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Modul modul =ModulFaker.generateFakeModul();
            modul.setId((long)i);
            module.add(modul);
        }
//        PDDocument document1 = PdfService.generatePdf(module);
//        document1.save("test.pdf");
//        System.out.println("PDF Created");
//        document1.close();

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("moduls", module);
        context.setVariable("categories", Modulkategorie.values());
        context.setVariable("pdfService", pdfService);

        StringWriter writer = new StringWriter();
        templateEngine.process("modulhandbuch", context, writer);

        PDDocument document = PdfService.htmlToPdf(writer.toString());
        document.save("test.pdf");
        System.out.println("PDF Created");
        document.close();
    }
}
