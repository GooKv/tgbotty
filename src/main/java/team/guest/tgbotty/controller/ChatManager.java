package team.guest.tgbotty.controller;

import team.guest.tgbotty.dto.ChatDto;
import team.guest.tgbotty.dto.ChatViewDto;
import team.guest.tgbotty.dto.RequestDto;

import java.util.List;

public interface ChatManager {

    List<ChatViewDto> getChatList();

    ChatDto getChatInfo(long id);

    List<RequestDto> getRequestList();

    Long getChatIdByRequest(Long requestId);

}
