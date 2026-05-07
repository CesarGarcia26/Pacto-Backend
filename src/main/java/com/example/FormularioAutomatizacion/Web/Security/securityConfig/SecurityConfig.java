package com.example.FormularioAutomatizacion.Web.Security.securityConfig;

import com.example.FormularioAutomatizacion.Web.Security.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private CorsConfig corsConfig;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(this.corsConfig.corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ Endpoints públicos
                        .requestMatchers(HttpMethod.POST, "/api/ubicacion/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/ubicacion/guardar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/ubicacion/guardar-colectiva").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ubicacion/departamentos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ubicacion/ciudades/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ubicacion/departamento/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ubicacion/enfermedades").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/ubicacion/user/**").permitAll()

                        // ✅ Resto protegido
                        .anyRequest().authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner generateCompanyHashes(PasswordEncoder passwordEncoder) {
        return args -> {
            String[] companies = {
                    "ceiba", "pragma", "nuvant", "susanita",
                    "PIRANI", "ROLPARTS S.A.S", "INVERSIONES GONDWANA",
                    "INVERSIONES AYT UMBRIA S.A.S", "GONDWLANA SERVICIOS",
                    "MAQUEL S.A.S", "C+PLUS", "PERCEPTIO",
                    "COMERCIALIZADORA GAPAL", "SUS INSUMOS", "ACRRIN MADERA CON DISEÑO"
            };

            System.out.println("🔹 Hashes de contraseñas para empresas:");
            for (String company : companies) {
                String rawPassword = company.replaceAll("\\s|\\+", "").toLowerCase() + "form";
                String hash = passwordEncoder.encode(rawPassword);
                System.out.println("Empresa: " + company);
                System.out.println("Contraseña: " + rawPassword);
                System.out.println("Hash BCrypt: " + hash);
                System.out.println("-------------------------------------------------");
            }
        };
    }
}