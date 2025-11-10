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

        // Regex to extract code inside ```java ... ```
        Pattern codeBlock = Pattern.compile("```java([\\s\\S]*?)```");

        // Output directory for generated tests
        String outputDir = "src/test/java/com/example/poc_test_generation/generated";
        Files.createDirectories(Paths.get(outputDir));

        for (JsonNode response : responses) {
            String text = response.at("/output/0/content/0/text").asText();

            Matcher m = codeBlock.matcher(text);
            if (!m.find()) continue;

            String code = m.group(1).trim();

            // Ensure package declaration
            if (!code.startsWith("package")) {
                code = "package com.example.poc_test_generation.generated;\n\n" + code;
            }

            // Optional: ensure imports for Spring Boot @WebMvcTest
            if (!code.contains("import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;")) {
                String imports =
                        "import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;\n" +
                                "import org.springframework.boot.test.mock.mockito.MockBean;\n" +
                                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                                "import org.springframework.test.web.servlet.MockMvc;\n" +
                                "import com.fasterxml.jackson.databind.ObjectMapper;\n";
                code = code.replaceFirst("package .*?;", "$0\n\n" + imports);
            }

            // Get class name
            Matcher classMatcher = Pattern.compile("class\\s+(\\w+)").matcher(code);
            if (!classMatcher.find()) continue;
            String className = classMatcher.group(1);

            // Write to file
            String path = outputDir + "/" + className + ".java";
            Files.writeString(Paths.get(path), code);

            System.out.println("✅ Wrote: " + path);
        }
    }
}
