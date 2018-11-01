package team.guest.tgbotty.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import team.guest.tgbotty.converter.ChatConverter;
import team.guest.tgbotty.dao.ChatRepository;
import team.guest.tgbotty.dto.ChatDto;
import team.guest.tgbotty.dto.ChatViewDto;
import team.guest.tgbotty.dto.MessageDto;

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

    private final ChatRepository chatRepository;
    private final ChatConverter chatConverter;

    @Autowired
    public ChatViewController(ChatRepository chatRepository, ChatConverter chatConverter) {
        this.chatRepository = chatRepository;
        this.chatConverter = chatConverter;
    }

    @RequestMapping("view")
    @ResponseBody
    public List<ChatViewDto> getChatList() {
        /*List<ChatViewDto> chatViewDtoList = new ArrayList<>();
        chatRepository.findAll().forEach(chat -> chatViewDtoList.add(chatConverter.convert(chat)));
        return chatViewDtoList;*/
       return chats.entrySet().stream().map(entry -> new ChatViewDto(entry.getKey(), entry.getValue().getId().toString())).collect(Collectors.toList());
    }

    @RequestMapping("view/{id}")
    @ResponseBody
    public ChatDto getChatInfo(@PathVariable("id") long id) {
        /*Chat chat = chatRepository.findByChatId(id).orElseThrow(() -> new NoChatFoundException(id));
        return new ChatDto(chat.getChatId(),
                           chat.getActiveProcessId(),
                           chat.getChatMessages().stream().map(chatConverter::convert).collect(Collectors.toList()));*/
        return chats.get(id);
    }


}
