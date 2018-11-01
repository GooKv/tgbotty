package team.guest.tgbotty.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Chat chat;
    @Column(name = "text")
    private String text;
    @Column(name = "send_time")
    private Timestamp sendTime;
    @Column(name = "sender")
    private String sender;

    protected ChatMessage() {
    }

    public ChatMessage(Chat chat, String text) {
        this.chat = chat;
        this.text = text;
    }

    public ChatMessage(Chat chat, String text, Timestamp sendTime, String sender) {
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

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
