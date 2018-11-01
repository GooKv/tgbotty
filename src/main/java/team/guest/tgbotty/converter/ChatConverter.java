package team.guest.tgbotty.converter;

import org.springframework.stereotype.Service;
import team.guest.tgbotty.dto.ChatViewDto;
import team.guest.tgbotty.dto.MessageDto;
import team.guest.tgbotty.entity.Request;
import team.guest.tgbotty.dto.RequestDto;
import team.guest.tgbotty.entity.Chat;
import team.guest.tgbotty.entity.ChatMessage;

@Service
public class ChatConverter {

    public ChatViewDto convert(Chat chat) {
        ChatViewDto chatViewDto = new ChatViewDto();
        chatViewDto.setDisplayName(chat.getActiveProcessId());
        chatViewDto.setId(chat.getChatId());
        return chatViewDto;
    }

    public MessageDto convert(ChatMessage chatMessage) {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(chatMessage.getText());
        messageDto.setSender(chatMessage.getSender());
        messageDto.setSenderType(chatMessage.getSenderType().name());
        messageDto.setTime(chatMessage.getSendTime().toString());
        messageDto.setMessageId(chatMessage.getId());
        return messageDto;
    }

    public RequestDto convert(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setRequestId(request.getId());
        requestDto.setRequestNumber(request.getNumber());
        requestDto.setText(request.getText());
        return requestDto;
    }

}
