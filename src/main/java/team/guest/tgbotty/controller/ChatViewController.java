package team.guest.tgbotty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import team.guest.tgbotty.dto.ChatDto;
import team.guest.tgbotty.dto.ChatViewDto;

import java.util.List;

@RestController
public class ChatViewController {

    private final ChatManager chatManager;
    private final CustomTgRestController customTgRestController;

    @Autowired
    public ChatViewController(CustomTgRestController customTgRestController,
                              @Qualifier("database") ChatManager chatManager) {
        this.customTgRestController = customTgRestController;
        this.chatManager = chatManager;
    }

    @RequestMapping("view")
    @ResponseBody
    public List<ChatViewDto> getChatList() {
        return chatManager.getChatList();
    }

    @RequestMapping("view/{id}")
    @ResponseBody
    public ChatDto getChatInfo(@PathVariable("id") long id) {
        return chatManager.getChatInfo(id);
    }

    @RequestMapping("view/{id}/startDialog")
    @ResponseBody
    public void startDialog(@PathVariable("id") long id) {
        customTgRestController.startDialogWithHuman();
    }

    @RequestMapping("view/{id}/sendMessage")
    @ResponseBody
    public void sendMessage(@PathVariable("id") long id, String message) {
        customTgRestController.sendMessageFromSupporter(message);
    }

}
