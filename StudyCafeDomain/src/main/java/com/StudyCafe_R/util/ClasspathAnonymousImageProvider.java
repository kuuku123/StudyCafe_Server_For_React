package com.StudyCafe_R.util;

import java.io.IOException;
import java.io.InputStream;

public class ClasspathAnonymousImageProvider implements ImageProvider {
    private final String resourcePath;

    public ClasspathAnonymousImageProvider(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public byte[] load() throws IOException {
        try (InputStream in =
                     Thread.currentThread()
                             .getContextClassLoader()
                             .getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return in.readAllBytes();
        }
    }
}