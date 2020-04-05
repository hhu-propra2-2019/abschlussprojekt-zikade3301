package mops.module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import mops.module.database.Veranstaltungsform;
import mops.module.database.Zusatzfeld;
import mops.module.services.AntragService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@SpringBootTest
@Disabled
public class FillDatabase {

    @Autowired
    AntragService antragService;

    @Disabled
    @Test
    public void fill() {
        Path path = Paths.get("./dump/Module");
        List<Path> folders = getFolders(path);
        List<List<Path>> files = folders.stream().map(this::getFiles).collect(Collectors.toList());
        files.stream().flatMap(x -> x.stream()).forEach(this::addToDatabase);
    }

    private void addToDatabase(Path path) {
        String template = loadModulTemplate(path);
        String localTemplate = splitTag(template, "modul");
        Modul modul = buildModul(localTemplate);
        Antrag antrag = antragService.addModulCreationAntrag(modul, "Initiale Erstellung");
        antragService.approveModulCreationAntrag(antrag);
    }

    private Modul buildModul(String template) {
        Modul modul = new Modul();
        modul.setStudiengang(splitTag(template, "studiengang"));
        modul.setTitelDeutsch(splitTag(template, "titelDeutsch"));
        modul.setTitelEnglisch(splitTag(template, "titelEnglisch"));
        modul.setModulkategorie(Modulkategorie.values()[Integer.parseInt(splitTag(template, "modulkategorie"))-1]);
        modul.setGesamtLeistungspunkte(splitTag(template, "gesamtLeistungspunkte"));
        modul.setModulbeauftragte(splitTag(template, "modulbeauftragte"));
        modul.setVeranstaltungen(buildVeranstaltungen(splitTag(template, "veranstaltungen")));

        return modul;
    }

    private Set<Veranstaltung> buildVeranstaltungen(String template) {
        Set<Veranstaltung> veranstaltungen= new HashSet<>();
        List<String> templates = splitTags(template, "veranstaltung");
        templates.stream().map(this::buildVeranstaltung).forEach(veranstaltungen::add);

        return veranstaltungen;
    }

    private Veranstaltung buildVeranstaltung(String template) {
        Veranstaltung veranstaltung= new Veranstaltung();
        veranstaltung.setTitel(splitTag(template, "titel"));
        veranstaltung.setLeistungspunkte(splitTag(template, "leistungspunkte"));
        veranstaltung.setVeranstaltungsformen(buildVeranstaltungsformen(splitTag(template, "veranstaltungsformen")));
        veranstaltung.setBeschreibung(buildVeranstaltungsbeschreibung(splitTag(template, "beschreibung")));
        veranstaltung.setVoraussetzungenTeilnahme(splitTag(template, "voraussetzungenTeilnahme"));
        veranstaltung.setZusatzfelder(buildZusatzfelder(splitTag(template, "zusatzfelder")));

        return veranstaltung;
    }

    private Set<Veranstaltungsform> buildVeranstaltungsformen(String template) {
        Set<Veranstaltungsform> veranstaltungsformen= new HashSet<>();
        List<String> templates = splitTags(template, "veranstaltungsform");
        templates.stream().map(this::buildVeranstaltungsform).forEach(veranstaltungsformen::add);
        return veranstaltungsformen;
    }

    private Veranstaltungsform buildVeranstaltungsform(String template) {
        Veranstaltungsform veranstaltungsform=new Veranstaltungsform();
        veranstaltungsform.setForm(splitTag(template, "form"));
        veranstaltungsform.setSemesterWochenStunden(Integer.parseInt(splitTag(template, "semesterWochenStunden")));
        return veranstaltungsform;
    }

    private Veranstaltungsbeschreibung buildVeranstaltungsbeschreibung(String template) {
        Veranstaltungsbeschreibung veranstaltungsbeschreibung = new Veranstaltungsbeschreibung();
        veranstaltungsbeschreibung.setInhalte(splitTag(template, "inhalte"));
        veranstaltungsbeschreibung.setLernergebnisse(splitTag(template, "lernergebnisse"));
        veranstaltungsbeschreibung.setLiteratur(splitTag(template, "literatur"));
        veranstaltungsbeschreibung.setVerwendbarkeit(splitTag(template, "verwendbarkeit"));
        veranstaltungsbeschreibung.setVoraussetzungenBestehen(splitTag(template, "voraussetzungenBestehen"));
        veranstaltungsbeschreibung.setHaeufigkeit(splitTag(template, "haeufigkeit"));
        veranstaltungsbeschreibung.setSprache(splitTag(template, "sprache"));
        return veranstaltungsbeschreibung;
    }

    private Set<Zusatzfeld> buildZusatzfelder(String template) {
        Set<Zusatzfeld> zusatzfelder= new HashSet<>();
        List<String> templates = splitTags(template, "zusatzfelder");
        templates.stream().map(this::buildZusatzfeld).forEach(zusatzfelder::add);
        return zusatzfelder;

    }

    private Zusatzfeld buildZusatzfeld(String template) {
        Zusatzfeld zusatzfeld=new Zusatzfeld();
        zusatzfeld.setTitel(splitTag(template, "titel"));
        zusatzfeld.setInhalt(splitTag(template, "inhalt"));
        return zusatzfeld;
    }

    private List<String> splitTags(String template, String tag) {
        template="\n"+ template;
        String[] templates = template.split("\n<" + tag + ">\n");
        return Arrays.stream(templates)
                .skip(1)
                .map(t -> "<" + tag + ">\n"+t)
                .map(t -> splitTag(t, tag))
                .collect(Collectors.toList());
    }

    private String splitTag(String template, String tag) {
        int openingTag = template.indexOf("<" + tag + ">") + ("<" + tag + ">").length();
        int closingTag = template.indexOf("</" + tag + ">");
        if(openingTag==-1 || closingTag == -1){
            return "";
        }
        String subTemplate = template.substring(openingTag, closingTag);
        return removeEmptyLines(subTemplate);
    }

    private String removeEmptyLines(String template) {
        template = template.substring(template.indexOf("\n") + "\n".length(), template.length());  // Remove Begin & Ending "\n"
        return removeIndent(template);
    }

    private String removeIndent(String template) {
        String p1 = "[^ \n]";
        Pattern pattern = Pattern.compile(p1);
        Matcher matcher = pattern.matcher(template);
        int indent = 0;
        if (matcher.find()) {
            indent = matcher.start();
        } else {
            return "";
        }
        int finalIndent = indent;
        template = Arrays.stream(template.split("\\r?\\n"))
                .map(line -> {
                    if (line.length() > finalIndent) {
                        return line.substring(finalIndent);
                    }
                    return line;
                })
                .map(line -> line + "\n")
                .collect(Collectors.joining());
        return template.substring(0, template.length() - 1);
    }

    private String loadModulTemplate(Path path) {
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private List<Path> getFolders(Path path) {
        try {
            List<Path> folders;
            Stream<Path> walk;
            walk = Files.walk(path);
            folders = walk
                    .filter(Files::isDirectory)
                    .map(Path::toString)
                    .map(Paths::get)
                    .sorted()
                    .skip(1)
                    .collect(Collectors.toList());
            return folders;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    private List<Path> getFiles(Path path) {
        try {
            List<Path> files;
            Stream<Path> walk;
            walk = Files.walk(path);
            files = walk
                    .filter(Objects::nonNull)
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(f -> f.endsWith(".html"))
                    .map(Paths::get)
                    .sorted()
                    .collect(Collectors.toList());
            return files;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }
}
