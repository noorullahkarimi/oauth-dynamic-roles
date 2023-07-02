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

    @Autowired
    public SecurityConfig(DataSource dataSource, UserService userService){
        this.dataSource = dataSource;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.csrf().disable().
               authorizeRequests()
               .antMatchers("/", "/login","/logout").permitAll()
               .antMatchers("/admin/").hasAnyAuthority("ADMIN")
               .antMatchers("/user/").hasAnyAuthority("ADMIN", "USER")
               .anyRequest().authenticated()
               .and().formLogin().loginPage("/login").usernameParameter("email");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
