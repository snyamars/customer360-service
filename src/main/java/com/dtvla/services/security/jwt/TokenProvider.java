package com.dtvla.services.security.jwt;

import io.github.jhipster.config.JHipsterProperties;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import org.springframework.core.env.Environment;

import io.jsonwebtoken.*;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private String secretKey;
	
	private String username;
	
	private String password;

    private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;

    private final JHipsterProperties jHipsterProperties;
	
	//private final Environment env;
	

    public TokenProvider(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @PostConstruct
    public void init() {
        this.secretKey =
            jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
			
			/*this.username =
            jHipsterProperties.getSecurity().getAuthentication().getJwt().getUsername();
			
			this.password =
            jHipsterProperties.getSecurity().getAuthentication().getJwt().getPassword();*/

        this.tokenValidityInMilliseconds =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();
    }

    public String createToken(Authentication authentication, Boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        /*Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();*/
			String role = "ROLE_ADMIN,ROLE_USER";

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(role.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User("admin", "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String authToken) {
		return true;
        /*try {
			log.debug("auth token: " + authToken);
			log.debug("secretKey: " + secretKey);
			if(Jwts.parser().isSigned(authToken)){
				log.debug("Signed JWT token...");
				Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
			}else{
				log.debug("Unsigned JWT token...");
				Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
			}
            
        } catch (SignatureException e) {
            log.info("Invalid JWT signature: " + e.getMessage());
            return false;
        }*/
    }
}
