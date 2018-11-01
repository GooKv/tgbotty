package team.guest.tgbotty.dto;

import java.util.List;

public class ChatDto {

    private Long id;
    private String displayName;
    private List<MessageDto> messagesDtoList;

    public ChatDto(Long id, String displayName, List<MessageDto> messagesDtoList) {
        this.id = id;
        this.displayName = displayName;
        this.messagesDtoList = messagesDtoList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<MessageDto> getMessagesDtoList() {
        return messagesDtoList;
    }

    public void setMessagesDtoList(List<MessageDto> messagesDtoList) {
        this.messagesDtoList = messagesDtoList;
    }
}
