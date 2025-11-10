package com.example.poc_test_generation.tools;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ScanAnnotations {
    public static void main(String[] args) throws Exception {
        Path root = Paths.get("src/main/java");
        List<Map<String, String>> methods = new ArrayList<>();

        Pattern methodPattern = Pattern.compile("public\\s+[\\w<>\\[\\]]+\\s+(\\w+)\\s*\\(");

        Files.walk(root).forEach(path -> {
            if (path.toString().endsWith(".java")) {
                try {
                    String content = Files.readString(path);
                    if (content.contains("@AutoGenerateTest")) {
                        Matcher m = methodPattern.matcher(content);
                        while (m.find()) {
                            Map<String, String> entry = new HashMap<>();
                            entry.put("file", path.toString());
                            entry.put("method", m.group(1));
                            entry.put("path", "src/test/java/" + path.getFileName().toString().replace(".java", "Test.java"));
                            methods.add(entry);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        System.out.println(new ObjectMapper().writeValueAsString(methods));
    }
}
