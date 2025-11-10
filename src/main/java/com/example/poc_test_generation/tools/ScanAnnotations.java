package com.example.poc_test_generation.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.File;
import java.nio.file.*;
import java.util.*;

public class ScanAnnotations {
    public static void main(String[] args) throws Exception {

        List<Map<String, Object>> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        Path root = Paths.get("src/main/java");

        Files.walk(root)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        var cu = new JavaParser().parse(path).getResult().orElse(null);
                        if (cu == null) return;

                        cu.findAll(MethodDeclaration.class).forEach(method -> {
                            if (method.getAnnotationByName("AutoGenerateTest").isPresent()) {

                                Map<String, Object> entry = new HashMap<>();
                                entry.put("className", cu.getPrimaryTypeName().orElse("Unknown"));
                                entry.put("methodName", method.getNameAsString());
                                entry.put("sourceCode", method.toString());

                                // Extract mapping annotation
                                method.getAnnotations().forEach(a -> {
                                    if (a.getNameAsString().endsWith("Mapping")) {
                                        entry.put("mapping", a.toString());
                                    }
                                });

                                result.add(entry);
                            }
                        });

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("methods.json"), result);
        System.out.println("✅ Extracted metadata written to methods.json");
    }
}
