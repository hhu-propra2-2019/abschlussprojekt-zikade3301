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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

@Service
public class HtmlService {
    private static final DataHolder OPTIONS = PegdownOptionsAdapter.flexmarkOptions(
            Extensions.ALL & ~(Extensions.ANCHORLINKS | Extensions.EXTANCHORLINKS_WRAP)
    ).toImmutable();

    public static String markdownToHtml(String markdown) {
        MutableDataHolder markdownOptions = new MutableDataSet();
        markdownOptions.setFrom(ParserEmulationProfile.MARKDOWN);
        Parser parser = Parser.builder(markdownOptions).build();
        Node document = parser.parse(markdown);

        final HtmlRenderer htmlRenderer = HtmlRenderer.builder(OPTIONS).build();

        return htmlRenderer.render(document);
    }

    public static PDDocument htmlToPdf(String html) {
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
}
