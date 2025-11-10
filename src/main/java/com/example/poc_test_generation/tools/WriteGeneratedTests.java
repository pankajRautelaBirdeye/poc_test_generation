package com.example.poc_test_generation.tools;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.*;

public class WriteGeneratedTests {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode responses = mapper.readTree(new File("combined_output.json"));

        Pattern codeBlock = Pattern.compile("```java([\\s\\S]*?)```");

        for (JsonNode response : responses) {
            String text = response.at("/output/0/content/0/text").asText();

            Matcher m = codeBlock.matcher(text);
            if (!m.find()) continue;

            String code = m.group(1).trim();

            // get class name
            Matcher classMatcher = Pattern.compile("class\\s+(\\w+)").matcher(code);
            if (!classMatcher.find()) continue;

            String className = classMatcher.group(1);

            String path = "src/test/java/generated/" + className + ".java";

            Files.createDirectories(Paths.get("src/test/java/generated"));
            Files.writeString(Paths.get(path), code);

            System.out.println("✅ Wrote: " + path);
        }
    }
}

