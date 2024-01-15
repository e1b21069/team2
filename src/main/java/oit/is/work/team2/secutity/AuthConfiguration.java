package oit.is.work.team2.secutity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {
  /**
   * 認可処理に関する設定（認証されたユーザがどこにアクセスできるか）
   *
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login
        .permitAll())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")) // ログアウト後に / にリダイレクト
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(AntPathRequestMatcher.antMatcher("/numeron/**")) // "/admin/**"
            .authenticated() // /admin以下は認証済みであること
            .requestMatchers(AntPathRequestMatcher.antMatcher("/**"))
            .permitAll())// 上記以外は全員アクセス可能
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/*")))// h2-console用にCSRF対策を無効化
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions
                .sameOrigin()));
    return http.build();
  }

  /**
   * 認証処理に関する設定（誰がどのようなロールでログインできるか）
   *
   * @return
   */
  @Bean
  public InMemoryUserDetailsManager userDetailsService() {

    // ユーザ名，パスワード，ロールを指定してbuildする
    // このときパスワードはBCryptでハッシュ化されているため，{bcrypt}とつける
    // ハッシュ化せずに平文でパスワードを指定する場合は{noop}をつける
    // ハッシュ化されたパスワードを得るには，この授業のbashターミナルで下記のように末尾にユーザ名とパスワードを指定すると良い(要VPN)
    // $ sshrun htpasswd -nbBC 10 player1 nmnm
    // $ sshrun htpasswd -nbBC 10 player2 nmnm
    // $ sshrun htpasswd -nbBC 10 player3 nmnm
    // $ sshrun htpasswd -nbBC 10 player4 nmnm
    // $ sshrun htpasswd -nbBC 10 admin numenume

    UserDetails user1 = User.withUsername("player1")
        .password("{bcrypt}$2y$10$POP.G1k/x1yJ2hcgS028ee6JcteYspfDhpCrZCC9OSSi8NshB1TLO").roles("USER").build();
    UserDetails user2 = User.withUsername("player2")
        .password("{bcrypt}$2y$10$POP.G1k/x1yJ2hcgS028ee6JcteYspfDhpCrZCC9OSSi8NshB1TLO").roles("USER").build();
    UserDetails user3 = User.withUsername("player3")
        .password("{bcrypt}$2y$10$POP.G1k/x1yJ2hcgS028ee6JcteYspfDhpCrZCC9OSSi8NshB1TLO").roles("USER").build();
    UserDetails user4 = User.withUsername("player4")
        .password("{bcrypt}$2y$10$POP.G1k/x1yJ2hcgS028ee6JcteYspfDhpCrZCC9OSSi8NshB1TLO").roles("USER").build();
    UserDetails user5 = User.withUsername("player5")
        .password("{bcrypt}$2y$10$.bqmJ3ArSbdnowKakfENsOmh0wfNRdIa/J6IpOE03eBYvFTzkKMza").roles("USER").build();
    UserDetails user6 = User.withUsername("player6")
        .password("{bcrypt}$2y$10$FWrs/wg1vncRfycc.ORhq.lel0DEfg92Mv.MKACdrBdk8ERRtSQ.K").roles("USER").build();
    UserDetails user7 = User.withUsername("player7")
        .password("{bcrypt}$2y$10$7owep7mSwQtQOLRB9WZ3VudXUB/yzSKo/Ywmi0z7HLUNjiWe7O6fq").roles("USER").build();
    UserDetails user8 = User.withUsername("player8")
        .password("{bcrypt}$2y$10$RxG3C5tB6c.A9zmLS162gexKsrOhFmG09feh6LxWEvh8K9M4/.k7W").roles("USER").build();
    UserDetails admin = User.withUsername("admin")
        .password("{bcrypt}$2y$10$qd8CFFNBiMBHLtElL.94/enxdfWGJ60XlgiYcQ7OMJEsDjFZQyl5C").roles("ADMIN").build();

    // 生成したユーザをImMemoryUserDetailsManagerに渡す（いくつでも良い）
    return new InMemoryUserDetailsManager(user1, user2, user3, user4, user5, user6, user7, user8, admin);
  }

}
