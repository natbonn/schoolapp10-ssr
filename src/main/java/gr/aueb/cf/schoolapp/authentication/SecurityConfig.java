package gr.aueb.cf.schoolapp.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration           // μπορούνε οι μέθοδοι της να δημιουργούν beans (@Bean config)
@EnableWebSecurity       // filter security
@EnableMethodSecurity    // enable @PreAuthorize annotation for services (ορίζουμε ποιος μπορεί να καλεί authorization)
@RequiredArgsConstructor // DI
public class SecurityConfig {

    // auth success handler
    // auth failure handler ως dependencies

    @Bean    // θα ορίσουμε τα φίλτρα για όλους τους controller οριζίντιοι έλεγχοι - από το ειδικό στο γενικό
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/index.html").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/users/register", "/users/success").permitAll()
                        .requestMatchers(HttpMethod.GET, "users/success").permitAll()
                        .requestMatchers("/teachers/insert").hasAuthority("INSERT_TEACHER")
                        .requestMatchers(HttpMethod.GET, "/teachers/edit/{uuid}").hasAuthority("EDIT_TEACHER")
                        .requestMatchers(HttpMethod.POST, "/teachers/edit").hasAuthority("EDIT_TEACHER")
                        .requestMatchers(HttpMethod.GET, "/teachers/update-success").hasAuthority("EDIT_TEACHER")
                        .requestMatchers(HttpMethod.POST, "/teachers/delete/{uuid}").hasAuthority("DELETE_TEACHER")
                        .requestMatchers(HttpMethod.GET, "/teachers/delete-success").hasAuthority("DELETE_TEACHER")
                        .requestMatchers("/teachers/**").hasAnyRole("ADMIN", "EMPLOYEE")  // κανονικά δε το βάζουμε με ρόλο
                        .requestMatchers("/users/**").hasRole("ADMIN")   // μόνο ο ένας hasRole
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/error").permitAll()    // οι φάκελοι που είναι στατικ
                        .anyRequest().authenticated()      // όλα τα άλλα paths που δεν έχω από πάνω ορίσει
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")               // GET /login - ορίζουμε τη δική μας login page - κάνουμε overwrite της SB
//                        .successHandler(auth success handler)
//                        .failureHandler(failure handler)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=")   // δουλεύει με POST
                        .invalidateHttpSession(true)          // για να φύγει το sessionID στο back
                        .deleteCookies("JSESSIONID")          // και στο front να καθαρίσει τo cookie
                );
        return http.build();
    }

    @Bean               // I/Oc container
    public PasswordEncoder passwordEncoder() {    // // αλγόριθμος blow fish / παραλλαγή BCrypt συγκρίνει hash με salt (ισχυρή κρυπτογράφηση)
        return new BCryptPasswordEncoder(12);     // 12 rounds(2 εις τη δωδεκάτη= 4000χλ περίπου)
    }                                             // με iterate για να μη χακαριστεί και να υπάρχει καθυστέρηση (σε μας είναι γρήγορο)


}
