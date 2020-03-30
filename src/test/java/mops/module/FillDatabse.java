package mops.module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import mops.module.database.Veranstaltungsform;
import mops.module.database.Zusatzfeld;
import org.junit.jupiter.api.Test;

public class FillDatabse {
    @Test
    public void fill() {
        Path path = Paths.get("./dump/Module/bspmodul.html");
//        List<Path> folders = getFolders(path);
//        List<List<Path>> files = folders.stream().map(this::getFiles).collect(Collectors.toList());
//        files.stream().flatMap(x -> x.stream()).forEach(this::addToDatabase);
        addToDatabase(path);
    }

    private void addToDatabase(Path path) {
        String template = loadModulTemplate(path);
        String localTemplate = splitTag(template, "modul");
        Modul modul = buildModul(localTemplate);
    }

    private Modul buildModul(String template) {
        Modul modul = new Modul();
        buildVeranstaltungen(splitTag(template, "veranstaltungen"));
        // TODO: Find Veranstaltungen tag and call buildVeranstaltungen(template).
        //       Fill Modul single fields.

        return null;
    }

    private String splitTag(String template, String tag) {
        int openingTag = template.indexOf("<" + tag + ">") + ("<" + tag + ">").length();
        int closingTag = template.indexOf("</" + tag + ">");
        String subTemplate = template.substring(openingTag, closingTag);
        return removeEmptyLines(subTemplate);
    }

    private String removeEmptyLines(String template) {
        template = template.substring(template.indexOf("\n") + "\n".length(), template.length() - 1);  // Remove Begin & Ending "\n"
        String p1 = "[^ ]";
        Pattern pattern = Pattern.compile(p1);
        Matcher matcher = pattern.matcher(template);
        int indent = 0;
        if (matcher.find()) {
            indent = matcher.start();
            template = removeIndent(template, indent);
            template = template.substring(0, template.length() - 1);
        }

        return template;
    }

    private String removeIndent(String template, int indent) {
        return template.lines()
                .map(line -> {
                    if (line.length() > indent) {
                        return line.substring(indent);
                    }
                    return line;
                })
                .map(line -> line + "\n")
                .collect(Collectors.joining());
    }

    private Set<Veranstaltung> buildVeranstaltungen(String template) {
        Set<Veranstaltung> veranstaltungen;
        //String[] templates = splitTags(template, "veranstaltung");
        // TODO: Find Veranstaltung tag and for each single Veranstaltung.
        //       Call buildVeranstaltung(template).

        return null;
    }

    private String[] splitTags(String template, String tag) {
        String[] templates = template.split("</" + tag + ">");
//        Arrays.stream(templates).map(x-> x.replaceAll("<" + tag + ">", "")).forEach(x -> System.out.println(x+"*end*"));
        return templates;
    }

    private Veranstaltung buildVeranstaltung(String template) {
        Veranstaltung veranstaltung;
        // TODO: Find <veranstaltungsformen> Tag and call buildVeranstaltungsformen(template)
        //       Find <beschreibung> and call buildVeranstaltungsbeschreibung(template)
        //       Find <zusatzfelder> and call buildZusatzfelder(template)
        //       Fill out single Fields
        return null;
    }

    private Set<Veranstaltungsform> buildVeranstaltungsformen(String template) {
        Set<Veranstaltungsform> veranstaltungsformen;
        // TODO  Find <veranstaltungsform> Tag and call buildVeranstaltungsform(template)
        return null;
    }

    private Veranstaltungsform buildVeranstaltungsform(String template) {
        Veranstaltungsform veranstaltungsform;
        // TODO: Fill out fields
        return null;
    }

    private Veranstaltungsbeschreibung buildVeranstaltungsbeschreibung(String template) {
        // TODO: Fill out beschreibung
        return null;
    }

    private Set<Zusatzfeld> buildZusatzfelder(String template) {
        Set<Zusatzfeld> zusatzfelder;
        // TODO  Find <zusatzfeld> Tag and call buildZusatzfeld(template)
        return null;
    }

    private Zusatzfeld buildZusatzfeld(String template) {
        Zusatzfeld zusatzfeld;
        // TODO: Fill out fields
        return null;
    }

    private String loadModulTemplate(Path path) {
        try {
            return Files.readString(path);
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
