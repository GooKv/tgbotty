package team.guest.tgbotty.dto;

public class MessageDto {

    private String sender;
    private String message;

    public MessageDto(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public MessageDto() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
