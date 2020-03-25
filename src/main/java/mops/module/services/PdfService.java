package mops.module.services;

import com.vladsch.flexmark.pdf.converter.PdfConverterExtension;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfService {
    private static final PDFMergerUtility pdfMerger = new PDFMergerUtility();

    /**
     * @param modul
     * @return
     */
    public static PDDocument generatePdf(Modul modul) {
        String str = buildString(modul);
        String html = PdfConverterExtension.embedCss(HtmlService.markdownToHtml(str), HtmlService.CSS_MODULE);
        return HtmlService.htmlToPdf(html);
    }

    /**
     * @param module
     * @return
     */
    public static PDDocument generatePdf(List<Modul> module) {
        StringBuilder str = new StringBuilder();
        // Frontpage
        PDDocument document = new PDDocument();
        appendPdf(document, generateFrontPage());
        for (Modulkategorie mk : Modulkategorie.values()) {
            appendPdf(document, generateAfterCategory(mk, module));
        }
        //Inhaltsverzeichnis
//        int pageCount = 1;
//        for (Modul modul : sortedModule) {
//            pageForModul.put(modul, pageCount);
//            PDDocument toAppend = generatePdf(modul);
//            pageCount += toAppend.getNumberOfPages();
//            appendPdf(document, toAppend);
//            closeDocument(toAppend);
//        }
//
//        PDDocument tableOfContents = getTableOfContents(pageForModul, sortedModule);
//        appendPdf(tableOfContents, document);
//        closeDocument(document);
//        document = tableOfContents;
//        return document;
        return document;
    }

    private static PDDocument generateAfterCategory(Modulkategorie mk, List<Modul> module) {
        PDDocument document = new PDDocument();
        StringBuilder str = new StringBuilder();
        List<Modul> sortedModule = module.stream()
                .filter(m -> m.getModulkategorie() == mk)
                .sorted(Comparator.comparing(Modul::getTitelDeutsch))
                .collect(Collectors.toList());
        int subIndex = 1;
        str.append("# Kapitel " + (mk.ordinal() + 1) + "\n"
                + "<h1 id=\"test" + mk.ordinal() + "\">" + (mk.ordinal() + 1) + ". " + mk.toReadable() + "</h1>\n"
                + "# " + getSpace(3) + mk.toReadable() + "\n");
        for (Modul m : sortedModule) {
            str.append("## " + (mk.ordinal() + 1) + "." + subIndex + " " + buildString(m));
            subIndex++;
        }

        String html = PdfConverterExtension.embedCss(HtmlService.markdownToHtml(str.toString()), HtmlService.CSS_MODULE);
        return HtmlService.htmlToPdf(html);

    }

    private static PDDocument generateFrontPage() {
        StringBuilder str = new StringBuilder();
        str.append("# Modulhandbuch\n"
                + "## für den\n"
                + "## Bachelor- und Master-Studiengang Informatik\n"
                + getLinebreak(22)
                + "## Institut für Informatik\n"
                + "## der Mathematisch-Naturwissenschaftliche Fakultät\n"
                + "## der Heinrich-Heine-Universität\n"
                + getLinebreak(1)
                + "## Herausgegeben vom\n"
                + "## Ausschuss für die Bachelor- und Master-Prüfung\n"
                + "## im Fach Informatik\n");

        String html = PdfConverterExtension.embedCss(HtmlService.markdownToHtml(str.toString()), HtmlService.CSS_FRONTPAGE);
        return HtmlService.htmlToPdf(html);
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
        String html = PdfConverterExtension.embedCss(str.toString(), HtmlService.CSS_INHALTSVERZEICHNIS);
        return HtmlService.htmlToPdf(html);
    }

    private static String buildString(Modul modul) {
        StringBuilder str = new StringBuilder();
        str.append(modul.getTitelDeutsch() + "\n");
        str.append("## " + getSpace(5) + modul.getTitelEnglisch() + "\n");
        str.append("### Studiengang\n");
        str.append(modul.getStudiengang() + "\n");
        str.append("### Leistungspunkte\n");
        str.append(modul.getGesamtLeistungspunkte() + "\n");
        str.append("### Lehrveranstaltungen\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getTitel)
                .map(x -> x + "\n")
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
                .map(x -> x + "\n")
                .forEach(str::append);
        str.append("### Verwendbarkeit\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getBeschreibung)
                .map(Veranstaltungsbeschreibung::getVerwendbarkeit)
                .map(x -> x + "\n")
                .forEach(str::append);
        str.append("### Teilnahmevoraussetzungen\n");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getVoraussetzungenTeilnahme)
                .map(x -> x + "\n")
                .forEach(str::append);
        str.append("### Studiengang\n");
        modul.getModulbeauftragte().stream()
                .map(x -> x + "\n")
                .forEach(str::append);

        return str.toString();
    }

    private static String getLinebreak(int n) {
        String str = "";
        for (int i = 0; i < n; i++) {
            str += "<br/>";
        }
        return str += "\n";
    }

    private static String getSpace(int n) {
        String str = "";
        for (int i = 0; i < n; i++) {
            str += "&nbsp;";
        }
        return str += " ";
    }

    private static void appendPdf(PDDocument dist, PDDocument source) {
        try {
            pdfMerger.appendDocument(dist, source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeDocument(PDDocument document) {
        try {
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
