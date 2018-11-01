package team.guest.tgbotty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import team.guest.tgbotty.bot.BotProcessScriptsFacade;
import team.guest.tgbotty.dto.ChatDto;
import team.guest.tgbotty.dto.ChatViewDto;
import team.guest.tgbotty.dto.RequestDto;

import java.util.HashMap;
import java.util.List;

@RestController
public class ChatViewController {

    private final ChatManager chatManager;
    private final CustomTgRestController customTgRestController;
    
    private final BotProcessScriptsFacade botProcessScriptsFacade;
    private HashMap<Long, String> avatars = new HashMap<>();
    
    @Autowired
    public ChatViewController(CustomTgRestController customTgRestController,
                              @Qualifier("database") ChatManager chatManager, 
                              BotProcessScriptsFacade botProcessScriptsFacade) {
        this.customTgRestController = customTgRestController;
        this.chatManager = chatManager;
        this.botProcessScriptsFacade = botProcessScriptsFacade;
    }

    @RequestMapping("view")
    @ResponseBody
    public List<ChatViewDto> getChatList() {
        return chatManager.getChatList();
    }

    @RequestMapping("view/{id}")
    @ResponseBody
    public ChatDto getChatInfo(@PathVariable("id") long id) {
        ChatDto dto = chatManager.getChatInfo(id);
        dto.setAvatarUrl(avatars.get(id));
        return dto;
    }
    
    public void setAvatar(long id, String value) {
        avatars.put(id, value);
    }

    @RequestMapping("view/{id}/startDialog")
    @ResponseBody
    public void startDialog(@PathVariable("id") long id) {
        customTgRestController.startDialogWithHuman(id);
    }

    @RequestMapping(value = "view/{id}/sendMessage", method = RequestMethod.POST )
    @ResponseBody
    public void sendMessage(@PathVariable("id") long id, String message) throws TelegramApiException {
        customTgRestController.sendMessageFromSupporter(id, message);
    }

    @RequestMapping("view/request/")
    @ResponseBody
    public List<RequestDto> getRequestList() {
        return chatManager.getRequestList();
    }

    @RequestMapping("view/request/{id}")
    @ResponseBody
    public Long getChatIdByRequest(@PathVariable("id") long id) {
        return chatManager.getChatIdByRequest(id);
    }

}
