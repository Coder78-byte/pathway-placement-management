package com.placement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

interface ResumeStorage {
    String store(Long studentId, byte[] bytes) throws IOException;
    byte[] read(String key) throws IOException;
    void delete(String key) throws IOException;
}

@Component
class LocalResumeStorage implements ResumeStorage {
    private final Path root;

    LocalResumeStorage(@Value("${app.storage-path}") String path) {
        root = Path.of(path).toAbsolutePath().normalize();
    }

    @Override public String store(Long studentId, byte[] bytes) throws IOException {
        Files.createDirectories(root);
        String key = studentId + "-" + UUID.randomUUID() + ".pdf";
        Files.write(root.resolve(key), bytes, StandardOpenOption.CREATE_NEW);
        return key;
    }

    @Override public byte[] read(String key) throws IOException {
        return Files.readAllBytes(resolve(key));
    }

    @Override public void delete(String key) throws IOException {
        Files.deleteIfExists(resolve(key));
    }

    private Path resolve(String key) {
        Path file = root.resolve(key).normalize();
        if (!file.startsWith(root)) throw new SecurityException("Invalid resume key");
        return file;
    }
}
