package nl.bioinf.jp_kcd_wr.image_library.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Security config file for securing the webapp.
 * @author Wietse Reitsma
 */



@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    private static final Logger logger = Logger.getLogger(WebSecurityConfig.class.getName());
    private final Environment environment;

    @Autowired
    private DataSource dataSource;

    @Value("${remember.me}")
    private static int rememberMe;

    @Autowired
    public WebSecurityConfig(Environment environment) {
        this.environment = environment;
    }

    /**
     * secures pages from being accessed without a login, except for the home and login page
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
//                .antMatchers("/upload")      /* this is to test roles */
//                .access("hasRole('ADMIN')")
                .antMatchers("/", "/home", "/login*","/js/**", "/css/**","/static/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/imageview")
                .and()
                .logout().permitAll()
                .and()
                .rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86400);


    }


    /**
     * the passwordencoder, uses a BCryptpasswordencoder to encrypt passwords for security
     * @return the password encoder
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     *
     * Database security configuration for the login system.
     * @param auth            authentication manager
     * @param passwordEncoder the password encoder
     * @throws Exception exception
     */
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery(
                        "select name, password, enabled from users where name = ? and enabled = true")
                .authoritiesByUsernameQuery(
                        "select name, concat('ROLE_',role) from users where name = ?");
    }
}
