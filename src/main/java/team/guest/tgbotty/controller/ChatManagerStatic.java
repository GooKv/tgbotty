package team.guest.tgbotty.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import team.guest.tgbotty.dto.ChatDto;
import team.guest.tgbotty.dto.ChatViewDto;
import team.guest.tgbotty.dto.MessageDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Qualifier(value = "static")
public class ChatManagerStatic implements ChatManager {

    private static final Map<Long, ChatDto> chats = ImmutableMap.of(
            1L, new ChatDto(1L,
                            "chat with babushka",
                            Arrays.asList(new MessageDto("babushka",
                                                         "CUSTOMER", "ololo",
                                                         15L,
                                                         "12" +
                                                                 ".10" +
                                                                 ".2018 " +
                                                                 "12:00" +
                                                                 ":09"),
                                          new MessageDto("bot",
                                                         "BOT", "mes from bot",
                                                         23L,
                                                         "12" +
                                                                 ".10" +
                                                                 ".2018 " +
                                                                 "12:00" +
                                                                 ":06"))),
            2L, new ChatDto(1L,
                            "chat with dedushka",
                            Arrays.asList(new MessageDto("dedushka",
                                                         "CUSTOMER", "alala",
                                                         155L,
                                                         "12" +
                                                                 ".10" +
                                                                 ".2018 " +
                                                                 "12:00" +
                                                                 ":49"),
                                          new MessageDto("bot",
                                                         "BOT", "mes from bot",
                                                         123L,
                                                         "12" +
                                                                 ".10" +
                                                                 ".2018 " +
                                                                 "12:00" +
                                                                 ":54"))));


    @Override
    public List<ChatViewDto> getChatList() {
        return chats.entrySet()
                .stream()
                .map(entry -> new ChatViewDto(entry.getKey(), entry.getValue().getId().toString()))
                .collect(Collectors.toList());
    }

    @Override
    public ChatDto getChatInfo(long id) {
        return chats.get(id);
    }
}
