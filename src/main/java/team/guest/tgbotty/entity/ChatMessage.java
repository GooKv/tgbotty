package team.guest.tgbotty.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @ManyToOne
    //@JoinColumn(name="id", nullable=false)
    private Chat chat;
    @Column(name = "text")
    private String text;
    @Column(name = "send_time")
    private Date sendTime;
    @Column(name = "sender")
    private String sender;

    protected ChatMessage() {
    }

    public ChatMessage(Chat chat, String text) {
        this.chat = chat;
        this.text = text;
    }

    public ChatMessage(Chat chat, String text, Date sendTime, String sender) {
        this.chat = chat;
        this.text = text;
        this.sendTime = sendTime;
        this.sender = sender;
    }

    public Chat getChatId() {
        return chat;
    }

    public void setChatId(Chat chat) {
        this.chat = chat;
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
