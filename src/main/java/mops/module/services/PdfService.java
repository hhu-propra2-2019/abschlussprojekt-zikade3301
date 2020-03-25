package mops.module.services;

import com.vladsch.flexmark.pdf.converter.PdfConverterExtension;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Service
public class PdfService {

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/pdfgeneration/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");

        templateEngine.setTemplateResolver(templateResolver);
    }

    private TemplateEngine templateEngine;

    private static final int COUNT_OF_PRE_PAGES = 2;

    private static String CSS_MODULE = getCss();

    private static final String pathToPdfResources = "./src/main/resources/templates/pdfgeneration";

    /**
     * @param module
     * @return
     */
    public PDDocument generatePdf(List<Modul> module) {
        int pageCount = 1 + COUNT_OF_PRE_PAGES;

        Map<Modul, Integer> pageForModul = new HashMap<>();
        List<Modul> sortedModule = new ArrayList<>();

        int anzahlModulkategorien = Modulkategorie.values().length;
        for (int i = 0; i < anzahlModulkategorien; i++) {
            Modulkategorie currentModulkategorie = Modulkategorie.values()[i];
            module.stream()
                    .filter(m -> m.getModulkategorie() == currentModulkategorie)
                    .forEach(sortedModule::add);
        }

        String alleModule = "";
        List<Modul> sortedModuleSubList = new ArrayList<>();
        for (Modul modul : sortedModule) {
            sortedModuleSubList.add(modul);
            String html = buildString(sortedModuleSubList);
            alleModule = html;

            String cssHtml = PdfConverterExtension.embedCss(html, CSS_MODULE);

            pageForModul.put(modul, pageCount);
            PDDocument toAppend = HtmlService.htmlToPdf(cssHtml);
            pageCount = toAppend.getNumberOfPages() + COUNT_OF_PRE_PAGES;
            try {
                toAppend.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String tableOfContents = getTableOfContents(pageForModul, sortedModule);
        String frontPage = generateFrontPage();

        String complete = frontPage + tableOfContents + alleModule;
        complete = PdfConverterExtension.embedCss(complete, CSS_MODULE);

        PDDocument document = HtmlService.htmlToPdf(complete);
        return document;
    }

    private static String generateFrontPage() {
        StringBuilder str = new StringBuilder();
        try {
            Path path = Paths.get(pathToPdfResources + "/frontpage.html");
            List<String> lines = Files.readAllLines(path);
            lines.stream().map(x -> x + "\n").forEach(str::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    private static String getTableOfContents(Map<Modul, Integer> pageForModul,
                                             List<Modul> sortedModule) {
        StringBuilder str = new StringBuilder();
        str.append("<inhaltsverzeichnis>");
        str.append("<h2>Inhaltsverzeichnis</h2>");
        str.append("<table style=\"width:100%\">");
        for (Modul modul : sortedModule) {
            str.append("<tr><td align=\"left\"><a href=\"#modul" + modul.getId() + "\">" + modul.getTitelDeutsch()
                    + "</a></td><td align=\"right\"><a href=\"#modul" + modul.getId() + "\">" + pageForModul.get(modul) + "</a></td></tr>");
        }
        str.append("</table>");
        str.append("</inhaltsverzeichnis>");
        return str.toString();
    }

    public static List<PdfModulWrapper> filterModuleAfterKategorie(List<PdfModulWrapper> module, Modulkategorie modulkategorie) {
        return module.stream().filter(m -> m.getModulkategorie() == modulkategorie).collect(Collectors.toList());
    }

    public static List<Modulkategorie> getUsedKategorien(List<PdfModulWrapper> module) {
        List<Modulkategorie> modulkategorien = new ArrayList<>();
        for(PdfModulWrapper modul : module) {
            if (!modulkategorien.contains(modul.getModulkategorie())) {
                modulkategorien.add(modul.getModulkategorie());
            }
        }
        return modulkategorien;
    }

    public String buildString(List<Modul> module) {
        Context context = new Context();
        context.setVariable("pdfService", this);

        List<PdfModulWrapper> modulWrapperList = module.stream()
                .map(PdfModulWrapper::new).collect(Collectors.toList());
        context.setVariable("module", modulWrapperList);
        context.setVariable("categories", getUsedKategorien(modulWrapperList));

        StringWriter writer = new StringWriter();
        templateEngine.process("modulhandbuch", context, writer);
        return writer.toString();
    }

    private static String getCss() {
        StringBuilder str = new StringBuilder();
        try {
            Path path = Paths.get(pathToPdfResources + "/handbuch.css");
            List<String> lines = Files.readAllLines(path);
            lines.stream().map(x -> x + "\n").forEach(str::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
}
