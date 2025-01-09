package com.doer.mraims.loanprocess.core.logger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api.logger")
public class ApiLoggerProperties {

    private boolean enabled = false; // Default value is false

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

