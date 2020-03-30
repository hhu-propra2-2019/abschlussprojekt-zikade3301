package mops.module.services;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.wrapper.PdfModulWrapper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class PdfService {

    private TemplateEngine templateEngine;

    /**
     * Initialisiert die Thymeleaf TemplateEngine.
     *
     * @param templateEngine TemplateEngine
     */
    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Geniert das PDF-Modulhandbuch.
     *
     * @param module List der Module
     * @return PDFBox Dokument
     */
    public ByteArrayOutputStream generatePdf(List<Modul> module) {
        Context context = new Context();
        List<PdfModulWrapper> pdfModulWrapperList = module
                .stream()
                .map(PdfModulWrapper::new)
                .collect(Collectors.toList());
        List<Modulkategorie> usedKategorien = new LinkedList<>(Arrays.asList(
                Modulkategorie.values()));
        removeKategorieIfUnused(pdfModulWrapperList, usedKategorien, Modulkategorie.SONSTIGE);

        context.setVariable("module", pdfModulWrapperList);
        context.setVariable("categories", usedKategorien);
        context.setVariable("pdfService", this);
        context.setVariable("currentSemester",
                ModulService.getSemesterFromDate(LocalDateTime.now(), true));

        StringWriter writer = new StringWriter();
        templateEngine.process("pdfgeneration/modulhandbuch", context, writer);

        return HtmlService.htmlToPdf(writer.toString());
    }

    /**
     * Filtert Module aus einer Liste, die zu einer bestimmten Kategorie gehören.
     *
     * @param module         Liste von Modulwrappern
     * @param modulkategorie Modulkategorie
     * @return Gefilterte Liste
     */
    public static List<PdfModulWrapper> filterModuleAfterKategorie(List<PdfModulWrapper> module,
                                                                   Modulkategorie modulkategorie) {
        return module.stream()
                .filter(m -> m.getModulkategorie() == modulkategorie)
                .sorted(Comparator.comparing(PdfModulWrapper::getTitelDeutsch))
                .collect(Collectors.toList());
    }

    /**
     * Entfernt bestimmte Kategorie aus Liste von Modulkategorien,
     * falls keines der angegebenen Module aus dieser Kategorie stammt.
     * @param module Liste von Modulen, die auf die Modulkategorie geprüft werden
     * @param modulkategorien Liste von Modulkategorien,
     *                        aus der die Kategorie eventuell entfernt wird
     * @param modulkategorie Modulkategorie, die unter Umständen entfernt wird
     */
    private static void removeKategorieIfUnused(List<PdfModulWrapper> module,
                                                List<Modulkategorie> modulkategorien,
                                                Modulkategorie modulkategorie) {
        if (module.stream().noneMatch(modul -> modul.getModulkategorie() == modulkategorie)) {
            modulkategorien.remove(modulkategorie);
        }
    }
}
