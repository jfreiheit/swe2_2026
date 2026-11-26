package htw.freiheit.studi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class StudiApplicationTests {

    @Test
    @SuppressWarnings("PMD.UnitTestShouldIncludeAssert") // Test schlaegt fehl, wenn der Context nicht startet
    void contextLoads() {
        // bewusst leer
    }

}
