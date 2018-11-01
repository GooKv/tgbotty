package team.guest.tgbotty.dto;

public class ChatViewDto {

    private Long id;
    private String displayName;

    public ChatViewDto(Long id, String displayName) {
        this.id = id;
        this.displayName = displayName;
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
}
