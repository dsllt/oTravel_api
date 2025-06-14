package com.dsllt.oTravel_api.infra.web.security;


import com.dsllt.oTravel_api.infra.web.filter.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {


    @Autowired
    private SecurityFilter securityFilter;
    @Autowired
    private CorsConfiguration corsConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/auth").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/reset-password/send").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/reset-password/reset").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/review").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/review/{id}").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/review").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/place").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/place/{id}").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/place/filter").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/schedule/{id}").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/menu/{id}").permitAll()
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/users/{id}").hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.cors(cors -> cors.configurationSource(corsConfiguration.getCorsConfigurationSource()));
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
