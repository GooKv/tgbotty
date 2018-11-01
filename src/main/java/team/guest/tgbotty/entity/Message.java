package team.guest.tgbotty.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "text")
    private String text;
    @Column(name = "send_time")
    private Timestamp sendTime;
    @Column
    private Person sender;

    protected Message() {
    }

    public Message(Long chatId, String text) {
        this.chatId = chatId;
        this.text = text;
    }

    public Message(Long chatId, String text, Timestamp sendTime, Person sender) {
        this.chatId = chatId;
        this.text = text;
        this.sendTime = sendTime;
        this.sender = sender;
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

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }
}
