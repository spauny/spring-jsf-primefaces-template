package gamesys.studiocd.smarthound.app.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"gamesys.studiocd.smarthound"})
@Import({AsyncConfig.class})
public class AppConfig {

    @Bean
    public CustomScopeConfigurer customScope() {
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        Map<String, Object> workflowScope = new HashMap<>();
        workflowScope.put("view", new ViewScope());
        configurer.setScopes(workflowScope);
        return configurer;
    }
}
