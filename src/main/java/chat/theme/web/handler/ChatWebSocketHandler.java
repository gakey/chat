package chat.theme.web.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import chat.theme.web.service.ChatService;

/**
 * ソケットハンドラ
 */

public class ChatWebSocketHandler extends TextWebSocketHandler {

	  @Autowired
	  private ChatService chatService;

	  /**
	   * 接続が確立したセッションをプールします。
	   * @param session セッション
	   * @throws Exception 例外が発生した場合
	   */
	  @Override
	  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	    System.out.println("New connection");
	    chatService.registerOpenConnection(session);
	  }

	  /**
	   * 切断された接続をプールから削除します。
	   * @param session セッション
	   * @param status ステータス
	   * @throws Exception 例外が発生した場合
	   */
	  @Override
	  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
	    chatService.registerCloseConnection(session);

	  }

	  /**
	   * ハンドリングした例外を処理します。
	   * @param session セッション
	   * @param exception 例外
	   */
	  @Override
	  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	    chatService.registerCloseConnection(session);

	  }

	  /**
	   * ハンドリングしたテキストメッセージを処理します。
	   * @param session セッション
	   * @param message メッセージ
	   */
	  @Override
	  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//	    System.out.println("New message: " + message.getPayload());
	    chatService.processMessage(session, message.getPayload());
	  }
}
