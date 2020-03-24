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
import java.util.List;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfService {
    private static final PDFMergerUtility pdfMerger = new PDFMergerUtility();

    private static final DataHolder OPTIONS = PegdownOptionsAdapter.flexmarkOptions(
            Extensions.ALL & ~(Extensions.ANCHORLINKS | Extensions.EXTANCHORLINKS_WRAP)
    ).toImmutable();

    private static final String CSS = "body {\n"
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

        MutableDataHolder markdownOptions = new MutableDataSet();
        markdownOptions.setFrom(ParserEmulationProfile.MARKDOWN);
        Parser parser = Parser.builder(markdownOptions).build();
        Node document = parser.parse(str);

        final HtmlRenderer htmlRenderer = HtmlRenderer.builder(OPTIONS).build();
        String html = htmlRenderer.render(document);
        html = PdfConverterExtension.embedCss(html, CSS);

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
