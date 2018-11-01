package team.guest.tgbotty.controller;

import team.guest.tgbotty.dto.ChatDto;
import team.guest.tgbotty.dto.ChatViewDto;

import java.util.List;

public interface ChatManager {

    List<ChatViewDto> getChatList();

    ChatDto getChatInfo(long id);

}
