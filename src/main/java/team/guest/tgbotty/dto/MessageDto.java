package team.guest.tgbotty.dto;

public class MessageDto {

    private String sender;
    private String senderType;
    private String message;
    private Long messageId;
    private String time;
    private boolean isLocation;

    public MessageDto(String sender, String senderType, String message, Long messageId, String time) {
        this.sender = sender;
        this.senderType = senderType;
        this.message = message;
        this.messageId = messageId;
        this.time = time;
    }

    public MessageDto() {
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
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

    public boolean isLocation() {
        return isLocation;
    }

    public void setLocation(boolean location) {
        isLocation = location;
    }
}
