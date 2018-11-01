package team.guest.tgbotty.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import team.guest.tgbotty.converter.ChatConverter;
import team.guest.tgbotty.dao.ChatRepository;
import team.guest.tgbotty.dao.RequestRepository;
import team.guest.tgbotty.dto.ChatDto;
import team.guest.tgbotty.dto.ChatViewDto;
import team.guest.tgbotty.dto.RequestDto;
import team.guest.tgbotty.entity.Chat;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier(value = "database")
public class ChatManagerDatabase implements ChatManager {

    private final ChatRepository chatRepository;
    private final ChatConverter chatConverter;
    private final RequestRepository requestRepository;

    public ChatManagerDatabase(ChatRepository chatRepository, ChatConverter chatConverter, RequestRepository requestRepository) {
        this.chatRepository = chatRepository;
        this.chatConverter = chatConverter;
        this.requestRepository = requestRepository;
    }

    @Override
    public List<ChatViewDto> getChatList() {
        List<ChatViewDto> chatViewDtoList = new ArrayList<>();
        chatRepository.findAll().forEach(chat -> chatViewDtoList.add(chatConverter.convert(chat)));
        return chatViewDtoList;
    }

    @Override
    public ChatDto getChatInfo(long id) {
        Chat chat = chatRepository.findByChatId(id).orElseThrow(() -> new NoChatFoundException(id));
        return chatConverter.convertChat(chat);
    }

    @Override
    public List<RequestDto> getRequestList() {
        List<RequestDto> requestDtoList = new ArrayList<>();
        requestRepository.findAll().forEach(request -> requestDtoList.add(chatConverter.convert(request)));
        return requestDtoList;
    }

    @Override
    public Long getChatIdByRequest(Long requestId) {
        return requestRepository
                .findById(requestId)
                .orElseThrow(() -> new NoRequestFoundException(requestId))
                .getChat().getChatId();
    }
}
