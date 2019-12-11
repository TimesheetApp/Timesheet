package jbc.timesheet.configuration;

import org.springframework.beans.factory.annotation.Value;

public class ApplicationProperties {

    @Value("${default.template:main}")
    private String defaultTemplate;

    public String getDefaultTemplate() {
        return defaultTemplate;
    }
}
