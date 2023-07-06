package com.example.oauth_jdbc_authentication.Config;

import com.example.oauth_jdbc_authentication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;

    private DataSource dataSource;

    private final Oauth2UserService oauth2UserService;

    @Autowired
    public SecurityConfig(DataSource dataSource, UserService userService, Oauth2UserService oauth2UserService) {
        this.dataSource = dataSource;
        this.userService = userService;
        this.oauth2UserService = oauth2UserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().
                authorizeRequests()
                .antMatchers("/", "/login", "/logout", "/getCookie", "/info").permitAll()
                .anyRequest().authenticated()
                .and().oauth2Login().loginPage("/logingoogle")
                .authorizationEndpoint().baseUri("/login/oauth2/").and()
                .redirectionEndpoint().baseUri("/login/callback").and()
                .userInfoEndpoint().userService(oauth2UserService).and()
                .successHandler(new LoginSuccessHandler())
                .and().formLogin().loginPage("/login").usernameParameter("email")
                .successHandler(new LoginSuccessHandler())
                .and().rememberMe().rememberMeCookieName("remember")
                .tokenValiditySeconds(60).rememberMeParameter("remember")
                .and().exceptionHandling().accessDeniedPage("/error")
                .and().logout().logoutUrl("/mylogout").deleteCookies("remember");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
