package mops.module.services;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.pdf.converter.PdfConverterExtension;
import com.vladsch.flexmark.profile.pegdown.Extensions;
import com.vladsch.flexmark.profile.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
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
        Context context = new Context();
        List<PdfModulWrapper> pdfModulWrapperList=module.stream().map(PdfModulWrapper::new).collect(Collectors.toList());
        context.setVariable("moduls", module);
        context.setVariable("module", pdfModulWrapperList);
        context.setVariable("categories", getUsedKategorien(pdfModulWrapperList));
        context.setVariable("pdfService", this);

        StringWriter writer = new StringWriter();
        templateEngine.process("modulhandbuch", context, writer);

        PDDocument document = HtmlService.htmlToPdf(writer.toString());
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
        return module.stream()
                .filter(m -> m.getModulkategorie() == modulkategorie)
                .sorted(Comparator.comparing(PdfModulWrapper::getTitelDeutsch))
                .collect(Collectors.toList());
    }

    public static List<Modulkategorie> getUsedKategorien(List<PdfModulWrapper> module) {
        List<Modulkategorie> modulkategorien = new ArrayList<>();
        for (Modulkategorie modulkategorie : Modulkategorie.values()) {
            if (module.stream()
                    .filter(modul -> modul.getModulkategorie() == modulkategorie)
                    .count() > 0) {
                modulkategorien.add(modulkategorie);
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
        context.setVariable("moduls", module);

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

    public List<Modul> filterModule(List<Modul> module, Modulkategorie modulkategorie){
        return module.stream()
                .filter(m -> m.getModulkategorie().equals(modulkategorie))
                .sorted(Comparator.comparing(Modul::getTitelDeutsch))
                .collect(Collectors.toList());
    }
}
