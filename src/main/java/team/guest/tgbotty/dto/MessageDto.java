package team.guest.tgbotty.dto;

public class MessageDto {

    private String sender;
    private String message;
    private Long messageId;
    private String time;

    public MessageDto(String sender, String message, Long messageId, String time) {
        this.sender = sender;
        this.message = message;
        this.messageId = messageId;
        this.time = time;
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

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
