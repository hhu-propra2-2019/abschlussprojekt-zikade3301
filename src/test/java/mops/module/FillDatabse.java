package mops.module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class FillDatabse {
    @Test
    public void fill() {
        Path path = Paths.get("./dump/Module");
        List<Path> folders = getFolders(path);
        List<List<Path>> files = folders.stream().map(this::getFiles).collect(Collectors.toList());
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
            files.forEach(System.out::println);
            return files;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }
}
