package com.StudyCafe_R.infra.config;

import org.apache.catalina.Context;
import org.apache.catalina.session.StandardManager;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {
    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer() {
        return (Context context) -> {
            StandardManager manager = new StandardManager();
            manager.setPathname(null); // Disable session persistence
            context.setManager(manager);
        };
    }
}
