package team.guest.tgbotty.converter;

import com.google.common.collect.Iterables;
import org.springframework.stereotype.Service;
import team.guest.tgbotty.dto.ChatDto;
import team.guest.tgbotty.dto.ChatViewDto;
import team.guest.tgbotty.dto.MessageDto;
import team.guest.tgbotty.dto.RequestDto;
import team.guest.tgbotty.entity.Chat;
import team.guest.tgbotty.entity.ChatMessage;
import team.guest.tgbotty.entity.Request;
import team.guest.tgbotty.entity.SenderType;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatConverter {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public ChatViewDto convert(Chat chat) {
        ChatViewDto chatViewDto = new ChatViewDto();
        chatViewDto.setDisplayName(constructDisplayName(chat));
        chatViewDto.setId(chat.getChatId());

        ChatMessage lastMessage =
                Iterables.getLast(chat.getChatMessages()
                                          .stream()
                                          .filter(chatMessage -> chatMessage.getSenderType().equals(SenderType.CUSTOMER))
                                          .collect(Collectors.toList()),
                                  null);

        chatViewDto.setLastMessage(convert(lastMessage));

        return chatViewDto;
    }

    public ChatDto convertChat(Chat chat) {
        ChatDto chatDto = new ChatDto();
        chatDto.setDisplayName(constructDisplayName(chat));
        chatDto.setCanTalk(chat.getActiveProcessId() == null);
        chatDto.setId(chat.getChatId());
        chatDto.setMessagesDtoList(chat.getChatMessages().stream().map(this::convert).collect(Collectors.toList()));
        chatDto.setRequestDtoList(chat.getChatRequests().stream().map(this::convert).collect(Collectors.toList()));
        return chatDto;
    }

    private String constructDisplayName(Chat chat) {
        List<ChatMessage> chatMessages =
                chat.getChatMessages()
                        .stream()
                        .filter(chatMessage -> chatMessage.getSenderType().equals(SenderType.CUSTOMER))
                        .collect(Collectors.toList());
        if (chatMessages.isEmpty()) {
            return "";
        }
        return chatMessages.get(0).getSender() + " " + formatDate(chatMessages.get(0).getSendTime());
    }
    
    private String formatDate(Timestamp timestamp) {
        return dateFormat.format(timestamp);
    }
    
    public MessageDto convert(ChatMessage chatMessage) {
        if (chatMessage == null) {
            return null;
        }
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(chatMessage.getText());
        messageDto.setSender(chatMessage.getSender());
        messageDto.setSenderType(chatMessage.getSenderType().name());
        messageDto.setTime(formatDate(chatMessage.getSendTime()));
        messageDto.setMessageId(chatMessage.getId());
        return messageDto;
    }

    public RequestDto convert(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setRequestId(request.getId());
        requestDto.setRequestNumber(request.getNumber());
        requestDto.setText(request.getText());
        requestDto.setChatId(request.getChat().getId());
        return requestDto;
    }

}
