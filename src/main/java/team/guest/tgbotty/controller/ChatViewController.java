package team.guest.tgbotty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(value = "view/{id}/sendMessage", method = RequestMethod.POST )
    @ResponseBody
    public void sendMessage(@PathVariable("id") long id, String message) {
        customTgRestController.sendMessageFromSupporter(message);
    }

    @RequestMapping("view/request/")
    @ResponseBody
    public void getRequestList() {
        chatManager.getRequestList();
    }

    @RequestMapping("view/request/{id}")
    @ResponseBody
    public void getChatIdByRequest(@PathVariable("id") long id) {
        chatManager.getChatIdByRequest(id);
    }

}
