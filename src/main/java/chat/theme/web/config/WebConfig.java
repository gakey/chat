package chat.theme.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

import chat.theme.web.handler.ChatWebSocketHandler;

/**
 * コンフィグ設定
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
@ComponentScan(basePackages={"chat.theme.web.service"})
public class WebConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer{

	/**
	 * 実際のWebSocket通信を処理するハンドラを登録
	 */
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(chatWebSocketHandler(), "/chat").withSockJS();
	}

	/**
	 * コネクションごとにハンドラオブジェクトを分ける
	 * @return PerConnectionWebSocketHandler WebSocket通信を処理するハンドラ
	 */
	@Bean
	public WebSocketHandler chatWebSocketHandler() {
		return new PerConnectionWebSocketHandler(
				(Class<? extends WebSocketHandler>) ChatWebSocketHandler.class);
	}

	/**
	 * デフォルトサーブレットのハンドラ設定
	 */
	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
}
