package org.sz.action.monitor.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * This class provides WebSocket broker configuration for the {@link org.sz.action.monitor.publish.ActionMessageSender interface}.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

    @Value("${broker.destinationPrefix}")
    private String destinationPrefix;

    @Value("${broker.applicationDestinationPrefix}")
    private String applicationDestinationPrefix;

    @Value("${stompEndpoint.url}")
    private String stompEndpoint;

    @Value("${stompEndpoint.allowedOrigins}")
    private String allowedOrigins;

    /**
     * Configures a simple in-memory message broker and sets prefixes for the application.
     *
     * @param registry message broker registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(destinationPrefix);
        registry.setApplicationDestinationPrefixes(applicationDestinationPrefix);
    }

    /**
     * Register STOMP endpoints for the application and sets allowed origins based on the application.properties file.
     *
     * @param registry message broker registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(stompEndpoint).setAllowedOrigins(allowedOrigins).withSockJS();
    }

}