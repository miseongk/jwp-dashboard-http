package org.apache.coyote.http11;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ViewResolver {

    private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    private ViewResolver() {
    }

    public static String read(final String fileResource) {
        final URL resource = classLoader.getResource("static" + fileResource);
        final String resourceFile = getFile(resource);
        final Path path = Paths.get(resourceFile);
        final List<String> fileLines = readFileLines(path);
        return String.join(System.lineSeparator(), fileLines);
    }

    private static String getFile(final URL resource) {
        if (resource == null) {
            throw new IllegalArgumentException("경로가 올바르지 않습니다.");
        }
        return resource.getFile();
    }

    private static List<String> readFileLines(final Path path) {
        try {
            return Files.readAllLines(path);
        } catch (final IOException e) {
            throw new UncheckedIOException("요청을 읽어오는데 실패했습니다.", e);
        }
    }
}
