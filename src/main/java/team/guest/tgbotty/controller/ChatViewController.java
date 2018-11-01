package team.guest.tgbotty.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import team.guest.tgbotty.dto.ChatDto;
import team.guest.tgbotty.dto.ChatViewDto;
import team.guest.tgbotty.dto.MessageDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ChatViewController {

    private static final Map<Integer, ChatDto> chats = ImmutableMap.of(
            1, new ChatDto(1,
                    "chat with babushka",
                    Arrays.asList(new MessageDto("babushka", "ololo"),
                            new MessageDto("bot", "mes from bot"))),
            2, new ChatDto(1,
                    "chat with dedushka",
                    Arrays.asList(new MessageDto("dedushka", "alala"),
                            new MessageDto("bot", "mes from bot"))));

    @RequestMapping("view")
    @ResponseBody
    public List<ChatViewDto> getChatList() {
        return chats.entrySet()
                .stream()
                .map(integerChatDtoEntry -> new ChatViewDto(integerChatDtoEntry.getKey(),
                        integerChatDtoEntry.getValue().getDisplayName()))
                .collect(Collectors.toList());
    }

    @RequestMapping("view/{id}")
    @ResponseBody
    public ChatDto getChatInfo(@PathVariable("id") int id) {
        return chats.get(id);
    }


}
