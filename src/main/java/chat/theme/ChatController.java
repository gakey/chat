package chat.theme;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * トップページ用コントローラーです。
 */
@Controller
public class ChatController {
    /**
     * トップページを表示します。
     * @return テンプレートのパス
     */
    @RequestMapping("/")
    public String showTopPage() {
        return "chat";
    }
}