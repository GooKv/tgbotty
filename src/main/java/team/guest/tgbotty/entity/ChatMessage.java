package team.guest.tgbotty.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "text")
    private String text;
    @Column(name = "send_time")
    private Date sendTime;
    @Column(name = "sender")
    private String sender;

    protected ChatMessage() {
    }

    public ChatMessage(Long chatId, String text) {
        this.chatId = chatId;
        this.text = text;
    }

    public ChatMessage(Long chatId, String text, Date sendTime, String sender) {
        this.chatId = chatId;
        this.text = text;
        this.sendTime = sendTime;
        this.sender = sender;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return text;
    }

    public void setMessage(String message) {
        this.text = message;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
