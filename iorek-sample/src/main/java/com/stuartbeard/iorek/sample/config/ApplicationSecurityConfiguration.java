package com.stuartbeard.iorek.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user1").password(passwordEncoder().encode("password")).roles("USER")
            .and()
            .withUser("user2").password(passwordEncoder().encode("oreoCooki3")).roles("USER")
            .and()
            .withUser("user3").password(passwordEncoder().encode("thisIsAnAwesomelySecurePasswordThatHasNotBeenCompromisedKnowinglyYet")).roles("USER")
            .and().passwordEncoder(passwordEncoder())
            .and().eraseCredentials(false);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();

        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/account", "/account/password/reset").permitAll()
            .antMatchers("/").permitAll()
            .anyRequest().fullyAuthenticated();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
