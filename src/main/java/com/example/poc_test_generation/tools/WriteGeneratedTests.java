package com.example.poc_test_generation.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WriteGeneratedTests {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode files = mapper.readTree(new File("generated.json"));

        for (JsonNode f : files) {
            String path = f.get("path").asText();
            String content = f.get("content").asText();

            File file = new File(path);
            file.getParentFile().mkdirs();

            Files.writeString(Paths.get(path), content);
        }

        System.out.println("Java: Test files written");
    }
}
