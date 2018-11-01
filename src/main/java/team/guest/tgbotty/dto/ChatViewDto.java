package team.guest.tgbotty.dto;

public class ChatViewDto {

    private Integer id;
    private String displayName;

    public ChatViewDto(Integer id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public ChatViewDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
