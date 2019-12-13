package jbc.timesheet.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationProperties {

    @Value("${default.template:main}")
    private String defaultTemplate;

    public String getDefaultTemplate() {
        return defaultTemplate;
    }


}
