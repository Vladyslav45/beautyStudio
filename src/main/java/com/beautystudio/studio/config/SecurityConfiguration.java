package com.beautystudio.studio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select email, password, active from users where email = ?")
                .authoritiesByUsernameQuery("select users.email, role_type from role inner join users on users.email = ?")
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                    .antMatchers("/", "/login").permitAll()
                    .antMatchers("/register").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/home", true)
                    .failureUrl("/login?error=true")
                    .usernameParameter("email")
                    .passwordParameter("password")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/?logout")
                .permitAll()
                .and()
                    .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/images/**");
    }


}
