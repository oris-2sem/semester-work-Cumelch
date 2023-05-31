package ru.itis.zooshop.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.itis.zooshop.repository.UserRepository;
import ru.itis.zooshop.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().ignoringAntMatchers("/api/**");

        http.
                authorizeRequests().
                requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().
                antMatchers("/").permitAll().
                antMatchers("/offers/all", "/offers/search", "/offers/{id}/details").permitAll().
                antMatchers("/offers/**").authenticated().
                antMatchers("/admin/**").hasRole("ADMIN").
                antMatchers("/api/**").hasRole("ADMIN").
                antMatchers("users/**").authenticated().
                antMatchers("/users/login", "/users/register").anonymous().
                antMatchers("/users/logout").authenticated().
                        anyRequest().
                authenticated().
                and().
                        formLogin().
                        loginPage("/users/login").
                        usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY).
                        passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY).
                        defaultSuccessUrl("/").
                        failureForwardUrl("/users/login-error").
                and().
                        logout().
                        logoutUrl("/users/logout").
                logoutSuccessUrl("/").
                        invalidateHttpSession(true).
                deleteCookies("JSESSIONID");


        return http.build();
    }
}
