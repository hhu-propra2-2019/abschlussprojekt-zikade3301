package mops.module.services;

import com.qkyrie.markdown2pdf.Markdown2PdfConverter;
import com.qkyrie.markdown2pdf.internal.exceptions.ConversionException;
import com.qkyrie.markdown2pdf.internal.exceptions.Markdown2PdfLogicException;
import com.qkyrie.markdown2pdf.internal.writing.SimpleFileMarkdown2PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfService {
    private final static PDFMergerUtility pdfMerger = new PDFMergerUtility();
    private final static Markdown2PdfConverter markdown2PdfConverter = Markdown2PdfConverter.newConverter();

    public static PDDocument generatePdf(List<Modul> module) {
        PDDocument document = new PDDocument();
        for (Modul modul : module) {
            appendPdf(document, generatePdf(modul));
        }
        return document;
    }

    public static PDDocument generatePdf(Modul modul) {
        String str = buildString(modul);
        final PDDocument[] document = new PDDocument[1];
        try {
            markdown2PdfConverter.readFrom(() -> str).writeTo(bytes -> {
                try {
                    document[0] = PDDocument.load(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).doIt();
        } catch (ConversionException | Markdown2PdfLogicException e) {
            e.printStackTrace();
        }

        return document[0];
    }

    private static String buildString(Modul modul) {
        StringBuilder str = new StringBuilder();
        str.append("#" + modul.getTitelDeutsch() + "\n");
        str.append("#" + modul.getTitelEnglisch() + "\n");
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
