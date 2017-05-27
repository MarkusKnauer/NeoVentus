package de.neoventus.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * web socket configuration class
 *
 * @author Dennis Thanner
 * @version 0.0.1
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

//	@Override
//	public void configureMessageBroker(MessageBrokerRegistry registry) {
//		registry.enableSimpleBroker("/order/");
//		registry.setApplicationDestinationPrefixes("/app");
//	}

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//		messages.simpDestMatchers("/**").authenticated();
	}

	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/socket-api").setAllowedOrigins("*").withSockJS();
	}
}
