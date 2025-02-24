package com.app.waki.user.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    RequestMatcher publicUrls = new OrRequestMatcher(
            // USER
            new AntPathRequestMatcher("/user/create"),
            new AntPathRequestMatcher("/user/login"),
            // SWAGGER
            new AntPathRequestMatcher("/swagger-ui.html"),
            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/swagger-ui/**")
    );

    RequestMatcher UserUrls = new OrRequestMatcher(
            //PROFILE
            new AntPathRequestMatcher("/profile/{profileId}"),
            new AntPathRequestMatcher("/profile/validatePrediction/{profileId}"),
            new AntPathRequestMatcher("/profile/predictionByDate/{profileId}"),
            new AntPathRequestMatcher("/profile/validatePrediction/{profileId}"),
            new AntPathRequestMatcher("/profile/process"),
            new AntPathRequestMatcher("/profile/update"),
            //DIVISION
            new AntPathRequestMatcher("/division/update"),
            new AntPathRequestMatcher("/division/userRanking/{userRankingId}"),
            new AntPathRequestMatcher("/division/userRankingList/{userRankingId}"),
            //PREDICTION
            new AntPathRequestMatcher("/prediction/{profileId}"),
            new AntPathRequestMatcher("/prediction/byDate/{profileId}"),
            new AntPathRequestMatcher("/prediction/byCompetition/{profileId}"),
            new AntPathRequestMatcher("/prediction/byMatchId/{profileId}"),
            new AntPathRequestMatcher("/prediction/existence/{profileId}"),
            //NOTIFICATION
            new AntPathRequestMatcher("/notification/{profileId}"),
            //MATCH-TEST
            new AntPathRequestMatcher("/test/match/finalize"),
            //MATCH
            new AntPathRequestMatcher("/league/fetch-league", "GET"),
            new AntPathRequestMatcher("/fixture/fetch-fixture", "Get"),
            new AntPathRequestMatcher("/odd/fetch-odds", "Get"),
            new AntPathRequestMatcher("/standing/fetch-standings", "Get"),
            new AntPathRequestMatcher("/league/allLeagues", "Get"),
            new AntPathRequestMatcher("/fixture/getFixtureDate", "Get"),
            new AntPathRequestMatcher("/fixture/getFixtureCodeDate", "Get"),
            new AntPathRequestMatcher("/fixture/getAllTeams", "Get"),
            new AntPathRequestMatcher("/odd/allOdds", "Get"),
            new AntPathRequestMatcher("/odd/{id}", "Get"),
            new AntPathRequestMatcher("/standing/{leagueId}", "Get"),
            //PLAYER
            new AntPathRequestMatcher("/player/fetch-player", "Get"),
            new AntPathRequestMatcher("/player/allPlayersProfiles", "Get"),
            new AntPathRequestMatcher("/player/allPlayerProfilesWithStats", "Get"),
            new AntPathRequestMatcher("/player/playerProfileWithStat/{id}", "Get"),
            new AntPathRequestMatcher("/player/playerProfileWithStat/{id}/{season}", "Get"),
            new AntPathRequestMatcher("/trophie/fetch-trophie", "Get"),
            new AntPathRequestMatcher("/trophie/playerTrophie/{id}", "Get"),
            new AntPathRequestMatcher("/trophie/playerTrophie/{id}/{season}", "Get")
        );
    RequestMatcher adminUrls = new OrRequestMatcher(
                new AntPathRequestMatcher("/user/admin")
    );

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers(publicUrls)
                                .permitAll()
                                .requestMatchers(UserUrls).hasRole("USER")
                                .requestMatchers(adminUrls).hasRole("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}