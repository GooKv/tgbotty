package team.guest.tgbotty.dto;

public class ChatViewDto {

    private Long id;
    private String displayName;
    private MessageDto lastMessage;

    private String avatarUrl;
    
    public ChatViewDto(Long id, String displayName, MessageDto lastMessage) {
        this.id = id;
        this.displayName = displayName;
        this.lastMessage = lastMessage;
    }

    public ChatViewDto(Long id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }
    
    public void setAvatarUrl(String url) {
        this.avatarUrl = url;
    }
    
    public String getAvatarUrl() {
        return this.avatarUrl;
    }
    
    public ChatViewDto() {
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

    public void setLastMessage(MessageDto lastMessage) {
        this.lastMessage = lastMessage;
    }

    public MessageDto getLastMessage() {
        return lastMessage;
    }
}
