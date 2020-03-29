package mops.module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
        Path path = Paths.get("./dump/Module");
        List<Path> folders = getFolders(path);
        List<List<Path>> files = folders.stream().map(this::getFiles).collect(Collectors.toList());
        files.stream().flatMap(x -> x.stream()).forEach(this::addToDatabase);
    }

    private void addToDatabase(Path path) {
        String template = loadModulTemplate(path);
        Modul modul = buildModul(template);
    }

    private Modul buildModul(String template) {
        Modul modul = new Modul();
        // TODO: Find Veranstaltungen tag and call buildVeranstaltungen(template).
        //       Fill Modul single fields.

        return null;
    }

    private Set<Veranstaltung> buildVeranstaltungen(String template) {
        Set<Veranstaltung> veranstaltungen;
        // TODO: Find Veranstaltung tag and for each single Veranstaltung.
        //       Call buildVeranstaltung(template).

        return null;
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
        System.out.println(path);
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
