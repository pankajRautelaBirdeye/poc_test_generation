package com.example.poc_test_generation.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WriteGeneratedTests {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode responses = mapper.readTree(new File("combined_output.json"));

        // Pattern to extract java code block
        Pattern codeBlock = Pattern.compile("```java([\\s\\S]*?)```");

        String outputDir = "src/test/java/generated";
        Files.createDirectories(Paths.get(outputDir));

        for (JsonNode response : responses) {
            String text = response.at("/output/0/content/0/text").asText();

            Matcher m = codeBlock.matcher(text);
            if (!m.find()) continue;

            String code = m.group(1).trim();

            // Force package declaration
            if (!code.startsWith("package")) {
                code = "com.example.poc_test_generation;\n\n" + code;
            }

            // Replace @Mock + @InjectMocks with @MockBean
            code = code.replaceAll("@Mock\\s+private", "@MockBean private");
            code = code.replaceAll("@InjectMocks\\s+private", "");

            // Get class name
            Matcher classMatcher = Pattern.compile("class\\s+(\\w+)").matcher(code);
            if (!classMatcher.find()) continue;
            String className = classMatcher.group(1);

            String path = outputDir + "/" + className + ".java";
            Files.writeString(Paths.get(path), code);

            System.out.println("✅ Wrote: " + path);
        }
    }
}
