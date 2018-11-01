package team.guest.tgbotty.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import team.guest.tgbotty.dao.ChatRepository;
import team.guest.tgbotty.dto.ChatDto;
import team.guest.tgbotty.dto.ChatViewDto;
import team.guest.tgbotty.dto.MessageDto;
import team.guest.tgbotty.entity.Chat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ChatViewController {

    private static final Map<Long, ChatDto> chats = ImmutableMap.of(
            1L, new ChatDto(1L,
                            "chat with babushka",
                            Arrays.asList(new MessageDto("babushka",
                                                         "ololo",
                                                         15L,
                                                         "12" +
                                                                 ".10" +
                                                                 ".2018 " +
                                                                 "12:00" +
                                                                 ":09"),
                                          new MessageDto("bot",
                                                         "mes from bot",
                                                         23L,
                                                         "12" +
                                                                 ".10" +
                                                                 ".2018 " +
                                                                 "12:00" +
                                                                 ":06"))),
            2L, new ChatDto(1L,
                            "chat with dedushka",
                            Arrays.asList(new MessageDto("dedushka",
                                                         "alala",
                                                         155L,
                                                         "12" +
                                                                 ".10" +
                                                                 ".2018 " +
                                                                 "12:00" +
                                                                 ":49"),
                                          new MessageDto("bot",
                                                         "mes from bot",
                                                         123L,
                                                         "12" +
                                                                 ".10" +
                                                                 ".2018 " +
                                                                 "12:00" +
                                                                 ":54"))));

    private final ChatRepository chatRepository;

    @Autowired
    public ChatViewController(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @RequestMapping("view")
    @ResponseBody
    public List<ChatViewDto> getChatList() {
        return chats.entrySet()
                .stream()
                .map(entry -> new ChatViewDto(entry.getKey(),
                                              entry.getValue().getDisplayName()))
                .collect(Collectors.toList());
    }

    @RequestMapping("view/{id}")
    @ResponseBody
    public ChatDto getChatInfo(@PathVariable("id") long id) {
        Chat chat = chatRepository.findByChatId(id).orElseThrow(() -> new NoChatFoundException(id));
        return new ChatDto(chat.getChatId(),
                           chat.getActiveProcessId(),
                           Arrays.asList(new MessageDto("dedushka",
                                                        "alala",
                                                        12L,
                                                        "12" +
                                                                ".10" +
                                                                ".2018 " +
                                                                "12:00" +
                                                                ":00"),
                                         new MessageDto("bot",
                                                        "mes from bot",
                                                        13L,
                                                        "12" +
                                                                ".10" +
                                                                ".2018 " +
                                                                "12:00" +
                                                                ":05")));
    }


}
