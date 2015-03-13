package ch.born.wte.ui.auth.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "ch.born.wte.ui.auth.server" }, scopedProxy = ScopedProxyMode.INTERFACES)
public class AuthApplicationConfig {

}
