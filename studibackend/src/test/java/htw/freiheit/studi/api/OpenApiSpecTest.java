package htw.freiheit.studi.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Vergleicht die aus dem Code erzeugte OpenAPI-Beschreibung mit der eingecheckten Datei openapi.yaml.
 * Jede Aenderung an der API faellt so im Build auf und wird im Pull Request als Diff sichtbar.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OpenApiSpecTest {

    private static final Path COMMITTED = Path.of("openapi.yaml");
    private static final Path GENERATED = Path.of("target", "openapi.yaml");

    private final MockMvc mockMvc;

    @Autowired
    OpenApiSpecTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void generatedSpec_ShouldMatchCommittedSpec() throws Exception {
        String generated = mockMvc.perform(get("/v3/api-docs.yaml"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Files.createDirectories(GENERATED.getParent());
        Files.writeString(GENERATED, generated, StandardCharsets.UTF_8);

        assertThat(COMMITTED).as("openapi.yaml fehlt - erzeugte Datei liegt unter %s", GENERATED).exists();
        assertThat(generated)
                .as("Die API hat sich geaendert. Pruefen Sie den Unterschied und uebernehmen Sie ihn mit: "
                        + "cp %s %s", GENERATED, COMMITTED)
                .isEqualTo(Files.readString(COMMITTED, StandardCharsets.UTF_8));
    }
}
