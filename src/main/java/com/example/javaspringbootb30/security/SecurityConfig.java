package com.example.javaspringbootb30.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomFilter customFilter;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        String[] publicRoutes=new String[]{"/css/**","/js/**","/images/**"};
        http.csrf(csrf->csrf.disable());
        //cau hinh filter
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(auth->{
//            auth.requestMatchers(publicRoutes).permitAll();
//            auth.requestMatchers("/").permitAll();
//            auth.requestMatchers("/user").hasRole("USER");
//            auth.requestMatchers("/admin").hasRole("ADMIN");
//            auth.requestMatchers(HttpMethod.POST,"/cc/**").hasRole("AUTHOR");
//            auth.requestMatchers("/abc/**","/xyz/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN");
//            auth.anyRequest().authenticated();
            auth.anyRequest().permitAll();
        });
        //cau hinh form login
//        http.formLogin(form->{
//            form.loginPage("/login");
//            form.loginProcessingUrl("/login-process");
//            form.usernameParameter("email");
//            form.passwordParameter("pass");
//            form.defaultSuccessUrl("/",true);
//            form.permitAll();
//        });
        //cau hinh form logout
        http.logout(logout->{
            logout.logoutSuccessUrl("/");
            logout.invalidateHttpSession(true);
            logout.deleteCookies("JSESSIONID");
            logout.clearAuthentication(true);
            logout.permitAll();
        });
        //cau hinh xu ly loi
        http.exceptionHandling(exceptionHandling->{
            //401
            exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            });
            //403
            exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            });
        });
        //cau hinh xac thuc
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}
