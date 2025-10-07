package com.example.campusjobs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.campusjobs.services.DbUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authProvider(DbUserDetailsService uds, BCryptPasswordEncoder enc) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(enc);
        return p;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(reg -> reg
                // ✅ อนุญาตเพจสาธารณะ + error + สตาติก
                .requestMatchers("/", "/login", "/register", "/error",
                                 "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                // ✅ STAFF เท่านั้นที่โพสต์งานได้
                .requestMatchers("/jobs/new", "/jobs").hasRole("STAFF")
                // นอกนั้นต้องล็อกอิน
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(log -> log
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );
        // ❌ ไม่เปิด httpBasic เพื่อไม่ให้ตอบ 401 กับ HEAD/health-check
        return http.build();
    }
}
