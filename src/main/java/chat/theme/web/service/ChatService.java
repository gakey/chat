package chat.theme.web.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * チャット処理のサービスクラス
 */

@Service
public class ChatService {
	private Set<WebSocketSession> conns = java.util.Collections
			.synchronizedSet(new HashSet<WebSocketSession>());
	private Map<WebSocketSession, String> nickNames = new ConcurrentHashMap<WebSocketSession, String>();

	/**
	 * コネクションを登録
	 *
	 * @param session セッション
	 */
	public void registerOpenConnection(WebSocketSession session) {
		// コネクションを登録
		conns.add(session);
	}

	/**
	 * コネクションを登録
	 *
	 * @param session セッション
	 */
	public void registerCloseConnection(WebSocketSession session) {
		// ニックネームを取得
		String nick = nickNames.get(session);
		// セッションを捨てる
		conns.remove(session);
		// ニックネームを捨てる
		nickNames.remove(session);
		// ニックネーム有無確認
		if (nick != null) {
			// ニックネームがある場合、取得したニックネームでメッセージを送信
			String messageToSend = "{\"removeUser\":\"" + nick + "\"}";
			for (WebSocketSession sock : conns) {
				try {
					sock.sendMessage(new TextMessage(messageToSend));
				} catch (IOException e) {
					System.out.println("IO exception when sending remove user message");
				}
			}
		}
	}

	/**
	 * メッセージ送信処理
	 *
	 * @param session セッション
	 * @param message メッセージ
	 */
	public void processMessage(WebSocketSession session, String message) {
		// セッション内にニックネームが格納されていた場合
		if (!nickNames.containsKey(session)) {
			// メッセージのエスケープ処理
			message = message.replace("\"", "\\\"");

			// ニックネーム数分処理を行う
			for (String nick : nickNames.values()) {
				try {
					// セッションに対してメッセージを格納する
					session.sendMessage(new TextMessage("{\"addUser\":\""
							+ nick + "\"}"));
				} catch (IOException e) {
					System.out.println("Error when sending addUser message");
				}
			}

			// ニックネームと共にメッセージをセッションに格納する
			nickNames.put(session, message);

			// websocket接続されているセッションにメッセージを送信
			String messageToSend = "{\"addUser\":\"" + message + "\"}";
			for (WebSocketSession sock : conns) {
				try {
					sock.sendMessage(new TextMessage(messageToSend));
				} catch (IOException e) {
					System.out.println("Error when sending broadcast addUser message");
				}
			}
		} else {
			// セッション内にニックネームが格納されていない場合、
			// websocket接続されているセッションにメッセージを送信
			String messageToSend = "{\"nickname\":\"" + nickNames.get(session)
					+ "\", \"message\":\"" + message.replace("\"", "\\\"")
					+ "\"}";
			for (WebSocketSession sock : conns) {
				try {
					sock.sendMessage(new TextMessage(messageToSend));
				} catch (IOException e) {
					System.out.println("Error when sending message message");
				}
			}
		}
	}

}
