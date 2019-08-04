package ru.agrmv.twitter.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import ru.agrmv.twitter.service.UserService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    public WebSecurityConfigurer(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    /*Для этих путей разрешен полный доступ*/
                    /*Для всех остальных нужна авторизация*/
                .antMatchers("/", "/registration").permitAll()
                    .anyRequest().authenticated()
                .and()
                    /*Включаем форму логина*/
                    .formLogin()
                    /*Устанавливаем страницу для логина*/
                    .loginPage("/login")
                    /*Стрницей логина разрешено пользоваться всем*/
                    .permitAll()
                .and()
                    /*Включаем форму логаута*/
                    .logout()
                    .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*Менеджер учетных записей*/
        auth.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}