package mops.module.services;

import com.qkyrie.markdown2pdf.Markdown2PdfConverter;
import com.qkyrie.markdown2pdf.internal.exceptions.ConversionException;
import com.qkyrie.markdown2pdf.internal.exceptions.Markdown2PdfLogicException;
import java.io.IOException;
import java.util.List;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PdfService {
    private static final PDFMergerUtility pdfMerger = new PDFMergerUtility();
    private static final Markdown2PdfConverter markdown2PdfConverter = Markdown2PdfConverter.newConverter();

    /**
     * @param module
     * @return
     */
    public static PDDocument generatePdf(List<Modul> module) {
        PDDocument document = new PDDocument();
        for (Modul modul : module) {
            PDDocument toAppend = generatePdf(modul);
            appendPdf(document, toAppend);
            try {
                toAppend.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return document;
    }


    /**
     * @param modul
     * @return
     */
    public static PDDocument generatePdf(Modul modul) {
        String str = buildString(modul);
        final PDDocument[] documents = new PDDocument[1];
        try {
            markdown2PdfConverter.readFrom(() -> str).writeTo(bytes -> {
                try {
                    documents[0] = PDDocument.load(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).doIt();
        } catch (ConversionException | Markdown2PdfLogicException e) {
            e.printStackTrace();
        }

        replaceTimesWithHelvetica(documents[0]);
        return documents[0];
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

    private static void replaceTimesWithHelvetica(PDDocument document) {
        for (PDPage page : document.getPages()) {
            PDResources resources = page.getResources();
            for (COSName key : resources.getFontNames()) {
                PDFont font = null;
                try {
                    font = resources.getFont(key);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (font != null) {
                    String fontName = font.getFontDescriptor().getFontName();

                    switch (fontName) {
                        case "Times-Roman":
                            resources.put(key, PDType1Font.HELVETICA);
                            break;
                        case "Times-Bold":
                            resources.put(key, PDType1Font.HELVETICA_BOLD);
                            break;
                        case "Times-Italic":
                            resources.put(key, PDType1Font.HELVETICA_OBLIQUE);
                            break;
                        case "Times-BoldItalic":
                            resources.put(key, PDType1Font.HELVETICA_BOLD_OBLIQUE);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
}
