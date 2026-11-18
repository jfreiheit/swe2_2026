package htw.freiheit.studi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI studiOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Studi-Backend API")
                        .version("1.0.0")
                        .description("REST-API zur Verwaltung von Nutzern und Rollen (Lehrbeispiel SWE2, HTW Berlin).")
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")))
                .servers(List.of(new Server().url("/").description("Aktueller Server")));
    }
}
