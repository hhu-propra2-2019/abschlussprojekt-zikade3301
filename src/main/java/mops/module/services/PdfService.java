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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfService {
    private static final PDFMergerUtility pdfMerger = new PDFMergerUtility();

    private static final DataHolder OPTIONS = PegdownOptionsAdapter.flexmarkOptions(
            Extensions.ALL & ~(Extensions.ANCHORLINKS | Extensions.EXTANCHORLINKS_WRAP)
    ).toImmutable();

    private static final String CSS_INHALTSVERZEICHNIS = "body {\n"
            + "    font-family: 'Helvetica', sans-serif;\n"
            + "    overflow: hidden;\n"
            + "    word-wrap: break-word;\n"
            + "    font-size: 14px;\n"
            + "}";

    private static final String CSS_MODULE = "body {\n"
            + "    font-family: 'Helvetica', sans-serif;\n"
            + "    overflow: hidden;\n"
            + "    word-wrap: break-word;\n"
            + "    font-size: 12px;\n"
            + "}";

    /**
     * @param module
     * @return
     */
    public static PDDocument generatePdf(List<Modul> module) {
        int pageCount = 1;
        PDDocument document = new PDDocument();

        Map<Modul, Integer> pageForModul = new HashMap<>();
        List<Modul> sortedModule = new ArrayList<>();

        int anzahlModulkategorien = Modulkategorie.values().length;
        for (int i = 0; i < anzahlModulkategorien; i++) {
            Modulkategorie currentModulkategorie = Modulkategorie.values()[i];
            module.stream()
                    .filter(m -> m.getModulkategorie() == currentModulkategorie)
                    .forEach(sortedModule::add);
        }

        for (Modul modul : sortedModule) {
            pageForModul.put(modul, pageCount);
            PDDocument toAppend = generatePdf(modul);
            pageCount += toAppend.getNumberOfPages();
            appendPdf(document, toAppend);
            try {
                toAppend.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        PDDocument tableOfContents = getTableOfContents(pageForModul, sortedModule);
        appendPdf(tableOfContents, document);
        try {
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableOfContents;
    }

    private static PDDocument getTableOfContents(Map<Modul, Integer> pageForModul,
                                                 List<Modul> sortedModule) {
        StringBuilder str = new StringBuilder("<h1>Inhaltsverzeichnis</h1>");
        str.append("<table style=\"width:100%\">");
        for (Modul modul : sortedModule) {
            str.append("<tr><td align=\"left\">" + modul.getTitelDeutsch()
                    + "</td><td align=\"right\">" + pageForModul.get(modul) + "</td></tr>");
        }
        str.append("</table>");
        String html = PdfConverterExtension.embedCss(str.toString(), CSS_INHALTSVERZEICHNIS);
        return htmlToPdf(html);
    }


    /**
     * @param modul
     * @return
     */
    public static PDDocument generatePdf(Modul modul) {
        String str = buildString(modul);

        String html = PdfConverterExtension.embedCss(markdownToHtml(str), CSS_MODULE);
        return htmlToPdf(html);
    }

    private static String markdownToHtml(String markdown) {
        MutableDataHolder markdownOptions = new MutableDataSet();
        markdownOptions.setFrom(ParserEmulationProfile.MARKDOWN);
        Parser parser = Parser.builder(markdownOptions).build();
        Node document = parser.parse(markdown);

        final HtmlRenderer htmlRenderer = HtmlRenderer.builder(OPTIONS).build();

        return htmlRenderer.render(document);
    }

    private static PDDocument htmlToPdf(String html) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfConverterExtension.exportToPdf(outputStream, html, "", OPTIONS);

        PDDocument pdDocument = null;
        try {
            pdDocument = PDDocument.load(outputStream.toByteArray());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pdDocument;
    }

    private static String buildString(Modul modul) {
        StringBuilder str = new StringBuilder();
        str.append("# " + modul.getTitelDeutsch() + "\n");
        str.append("# " + modul.getTitelEnglisch() + "\n");
        str.append("## Studiengang\n");
        str.append(modul.getStudiengang() + "\n");
        str.append("## Leistungspunkte\n");
        str.append(modul.getGesamtLeistungspunkte() + "\n");
        str.append("## Lehrveranstaltungen\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getTitel)
                .map(x -> x + "\n")
                .forEach(str::append);
        str.append("## Inhalte\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getBeschreibung)
                .map(Veranstaltungsbeschreibung::getInhalte)
                .map(x -> x + "\n")
                .forEach(str::append);
        str.append("## Literatur\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getBeschreibung)
                .map(Veranstaltungsbeschreibung::getLiteratur)
                .map(x -> x + "\n")
                .forEach(str::append);
        str.append("## Verwendbarkeit\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getBeschreibung)
                .map(Veranstaltungsbeschreibung::getVerwendbarkeit)
                .map(x -> x + "\n")
                .forEach(str::append);
        str.append("## Teilnahmevoraussetzungen\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getVoraussetzungenTeilnahme)
                .map(x -> x + "\n")
                .forEach(str::append);
        str.append("## Studiengang\n");
        modul.getModulbeauftragte().stream()
                .map(x -> x + "\n")
                .forEach(str::append);

        return str.toString();
    }

    private static void appendPdf(PDDocument dist, PDDocument source) {
        try {
            pdfMerger.appendDocument(dist, source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
