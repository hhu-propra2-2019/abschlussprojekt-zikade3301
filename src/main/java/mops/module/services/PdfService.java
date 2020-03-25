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
import mops.module.database.Zusatzfeld;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PdfService {
    private static final PDFMergerUtility pdfMerger = new PDFMergerUtility();

    private static final DataHolder OPTIONS = PegdownOptionsAdapter.flexmarkOptions(
            Extensions.ALL & ~(Extensions.ANCHORLINKS | Extensions.EXTANCHORLINKS_WRAP)
    ).toImmutable();

    private static final int COUNT_OF_PRE_PAGES = 1;

    private static final String CSS_MODULE =
              "body {\n"
            + "    font-family: 'Helvetica', sans-serif;\n"
            + "    overflow: hidden;\n"
            + "    word-wrap: break-word;\n"
            + "    font-size: 12px;\n"
            + "}\n\n"
            + "inhaltsverzeichnis {\n"
            + "    display: block;\n"
            + "    page-break-after: always;\n"
            + "    font-size: 14px;\n"
            + "}\n\n"
            + "a:link {\n"
            + "    text-decoration: none;\n"
            + "    color: black;\n"
            + "}\n\n";

    /**
     * @param module
     * @return
     */
    public static PDDocument generatePdf(List<Modul> module) {
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
        for (Modul modul : sortedModule) {
            alleModule = alleModule.replaceFirst("<h2>Platzhalter</h2>", "");

            String html = markdownToHtml(buildString(modul));
            html = "<div id=\"modul" + modul.getId() + "\">" + html;
            html = html + "</div><br /><br />";

            alleModule += html;

            // Platzhalter, um Seite auf der das neue Modul beginnt ermitteln zu können
            alleModule += "<h2>Platzhalter</h2>";

            String cssHtml = PdfConverterExtension.embedCss(alleModule.toString(), CSS_MODULE);
            pageForModul.put(modul, pageCount);
            PDDocument toAppend = htmlToPdf(cssHtml);
            pageCount = toAppend.getNumberOfPages() + COUNT_OF_PRE_PAGES;
            try {
                toAppend.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        alleModule = alleModule.replaceFirst("<h2>Platzhalter</h2>", "");

        String tableOfContents = getTableOfContents(pageForModul, sortedModule);

        String complete = tableOfContents + alleModule.toString();
        complete = PdfConverterExtension.embedCss(complete, CSS_MODULE);

        PDDocument document = htmlToPdf(complete);
        addPageNumbers(document);
        return document;
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
        //String html = PdfConverterExtension.embedCss(str.toString(), CSS_INHALTSVERZEICHNIS);
        return str.toString();
    }


    /**
     * @param modul
     * @return
     */
    public static PDDocument generatePdf(Modul modul) {
        String html = markdownToHtml(buildString(modul));

        html = PdfConverterExtension.embedCss(html, CSS_MODULE);
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
        str.append("## " + modul.getTitelDeutsch() + "\n");
        str.append("## " + modul.getTitelEnglisch() + "\n");
        str.append("### Studiengang\n");
        str.append(modul.getStudiengang() + "\n");
        str.append("### Leistungspunkte\n");
        str.append(modul.getGesamtLeistungspunkte() + "\n");
        str.append("### Lehrveranstaltungen\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getTitel)
                .map(x -> "- " + x + "\n")
                .forEach(str::append);
        str.append("### Inhalte\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getBeschreibung)
                .map(Veranstaltungsbeschreibung::getInhalte)
                .map(x -> x + "\n")
                .forEach(str::append);
        str.append("### Literatur\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getBeschreibung)
                .map(Veranstaltungsbeschreibung::getLiteratur)
                .map(x -> "- " + x + "\n")
                .forEach(str::append);
        str.append("### Verwendbarkeit\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getBeschreibung)
                .map(Veranstaltungsbeschreibung::getVerwendbarkeit)
                .map(x -> "- " + x + "\n")
                .forEach(str::append);
        str.append("### Teilnahmevoraussetzungen\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getVoraussetzungenTeilnahme)
                .map(x -> "- " + x + "\n")
                .forEach(str::append);
        str.append("### Voraussetzungen für die Vergabe von Leistungspunkten\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getBeschreibung)
                .map(Veranstaltungsbeschreibung::getVoraussetzungenBestehen)
                .map(x -> "- " + x + "\n")
                .forEach(str::append);
        str.append("### Studiengang\n");
        modul.getModulbeauftragte().stream()
                .map(x -> x + "\n")
                .forEach(str::append);
        str.append("### Häufigkeit des Angebots, modulare Schiene\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getBeschreibung)
                .map(Veranstaltungsbeschreibung::getHaeufigkeit)
                .map(x -> x + "\n")
                .forEach(str::append);
        str.append("### Modulbeauftragte und hauptamtliche Lehrende\n");
        modul.getModulbeauftragte().stream()
                .map(x -> x + ", ")
                .forEach(str::append);
        str.append("\n");
        for (Veranstaltung veranstaltung : modul.getVeranstaltungen()) {
            for (Zusatzfeld zusatzfeld : veranstaltung.getZusatzfelder()) {
                str.append("### " + zusatzfeld.getTitel() + "\n");
                str.append(zusatzfeld.getInhalt() + "\n");
            }
        }

        return str.toString();
    }

    private static void appendPdf(PDDocument dist, PDDocument source) {
        try {
            pdfMerger.appendDocument(dist, source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addPageNumbers(PDDocument document) {
        PDPageTree allPages = document.getPages();
        PDFont font = PDType1Font.HELVETICA;
        float fontSize = 8.0f;
        for (int i = 1; i < allPages.getCount(); i++) {
            PDPage page = allPages.get(i);
            String pageNumber = "- " + (i + 1) + " -";

            try {
                float pageNumberWidth = font.getStringWidth(pageNumber) / 1000 * fontSize;
                float pageNumberHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
                float bottomMargin = 30.0f;

                PDPageContentStream footerContentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                footerContentStream.beginText();
                footerContentStream.setFont(font, fontSize);
                footerContentStream.newLineAtOffset((PDRectangle.A4.getUpperRightX() - pageNumberWidth) / 2, PDRectangle.A4.getLowerLeftY() + bottomMargin + pageNumberHeight);
                footerContentStream.showText(pageNumber);
                footerContentStream.endText();
                footerContentStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
