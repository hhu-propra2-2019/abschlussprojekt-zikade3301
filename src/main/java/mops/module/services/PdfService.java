package mops.module.services;

import com.qkyrie.markdown2pdf.Markdown2PdfConverter;
import io.micrometer.core.instrument.MockClock;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfparser.PDFXRefStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.aspectj.util.FileUtil;

public class PdfService {
    private final static PDFMergerUtility pdfMerger = new PDFMergerUtility();
    private final static Markdown2PdfConverter markdown2PdfConverter = Markdown2PdfConverter.newConverter();

    public static PDDocument generatePdf(List<Modul> module) {
        PDDocument document = new PDDocument();
        for (Modul modul : module) {
            appendPdf(document, generatePdf(modul));
        }
        try {
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    public static PDDocument generatePdf(Modul modul) {
        final PDDocument[] document = new PDDocument[1];
        String str = buildString(modul);
        markdown2PdfConverter.readFrom(() -> str).writeTo(out -> {
            try {
                document[0] = PDDocument.load(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return document[0];

    }

    private static String buildString(Modul modul) {
        StringBuilder str = new StringBuilder();
        str.append(modul.getTitelDeutsch() + "\\");
        str.append(modul.getTitelEnglisch() + "\\");
        str.append("## Studiengang\\");
        str.append(modul.getStudiengang() + "\\");
        str.append("## Leistungspunkte\\");
        str.append(modul.getGesamtLeistungspunkte() + "\\");
        str.append("## Lehrveranstaltungen\\");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getTitel)
                .map(x -> x + "\\")
                .forEach(str::append);
        str.append("## Inhalte\\");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getBeschreibung)
                .map(Veranstaltungsbeschreibung::getInhalte)
                .map(x -> x + "\\")
                .forEach(str::append);
        str.append("## Literatur\\");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getBeschreibung)
                .map(Veranstaltungsbeschreibung::getLiteratur)
                .map(x -> x + "\\")
                .forEach(str::append);
        str.append("## Verwendbarkeit\\");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getBeschreibung)
                .map(Veranstaltungsbeschreibung::getVerwendbarkeit)
                .map(x -> x + "\\")
                .forEach(str::append);
        str.append("## Teilnahmevoraussetzungen\\");
        modul.getVeranstaltungen().stream()
                .map(Veranstaltung::getVoraussetzungenTeilnahme)
                .map(x -> x + "\\")
                .forEach(str::append);
        str.append("## Studiengang\\");
        modul.getModulbeauftragte().stream()
                .map(x -> x + "\\")
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
