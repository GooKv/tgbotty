package team.guest.tgbotty.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import team.guest.tgbotty.converter.ChatConverter;
import team.guest.tgbotty.dao.ChatRepository;
import team.guest.tgbotty.dto.ChatDto;
import team.guest.tgbotty.dto.ChatViewDto;
import team.guest.tgbotty.entity.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier(value = "database")
public class ChatManagerDatabase implements ChatManager {

    private final ChatRepository chatRepository;
    private final ChatConverter chatConverter;

    public ChatManagerDatabase(ChatRepository chatRepository, ChatConverter chatConverter) {
        this.chatRepository = chatRepository;
        this.chatConverter = chatConverter;
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
        return new ChatDto(chat.getChatId(),
                           chat.getActiveProcessId(),
                           chat.getChatMessages().stream().map(chatConverter::convert).collect(Collectors.toList()));
    }
}
