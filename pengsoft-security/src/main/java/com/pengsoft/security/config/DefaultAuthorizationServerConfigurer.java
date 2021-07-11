package com.pengsoft.security.config;

import javax.inject.Inject;

import com.pengsoft.security.config.properties.OAuthClientConfigurationProperties;
import com.pengsoft.security.config.properties.OAuthConfigurationProperties;
import com.pengsoft.security.config.properties.WebSecurityConfigurationProperties;
import com.pengsoft.security.domain.DefaultUserDetails;
import com.pengsoft.security.json.OAuth2AccessTokenMixIn;
import com.pengsoft.security.service.UserService;
import com.pengsoft.support.json.ObjectMapper;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty("pengsoft.security.oauth.authorization-server-enabled")
@EnableConfigurationProperties({ WebSecurityConfigurationProperties.class, OAuthConfigurationProperties.class,
        OAuthClientConfigurationProperties.class })
@EnableAuthorizationServer
public class DefaultAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private WebSecurityConfigurationProperties webSecurityConfigurationProperties;

    @Inject
    private OAuthConfigurationProperties oauthConfigurationProperties;

    @Inject
    private TokenStore tokenStore;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private UserService userService;

    public DefaultAuthorizationServerConfigurer(final ObjectMapper objectMapper) {
        objectMapper.addMixIn(OAuth2AccessToken.class, OAuth2AccessTokenMixIn.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenStore tokenStore(final RedisConnectionFactory connectionFactory) {
        return new RedisTokenStore(connectionFactory);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        var builder = clients.inMemory();
        oauthConfigurationProperties.getClients()
                .forEach(client -> builder.withClient(client.getId()).secret(passwordEncoder.encode(client.getSecret()))
                        .authorizedGrantTypes("password").scopes(client.getScope())
                        .accessTokenValiditySeconds(client.getAccessTokenValiditySeconds()));
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager).allowedTokenEndpointRequestMethods(HttpMethod.POST)
                .tokenStore(tokenStore).userDetailsService(userDetailsService);
    }

    @EventListener
    public void authenticationSuccessEventListener(final AuthenticationSuccessEvent event) {
        final var authentication = event.getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken
                && authentication.getPrincipal() instanceof DefaultUserDetails) {
            userService.signInSuccess(authentication.getName());
        }
    }

    @EventListener
    public void authenticationFailureEventListener(final AbstractAuthenticationFailureEvent event) {
        final var exception = event.getException();
        final var authentication = event.getAuthentication();
        if (exception instanceof BadCredentialsException) {
            userService.signInFailure(authentication.getName(),
                    webSecurityConfigurationProperties.getAllowSignInFailure());
        }
    }

}
