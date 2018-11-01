package team.guest.tgbotty.entity;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column
    private Long chatId;
    @ElementCollection
    private List<String> processList = new ArrayList<>();
    @Column
    private String activeProcessId;
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @ElementCollection
    @OneToMany(mappedBy ="chat")
    private List<ChatMessage> chatMessages = new ArrayList<>();
    @ElementCollection
    @OneToMany(mappedBy ="chat")
    private List<Request> chatRequests = new ArrayList<>();
    @Column
    private Boolean dialogMode = false;
    
    protected Chat() {
    }

    public Chat(Long chatId) {
        this.chatId = chatId;
    }

    public Chat(String activeProcessId) {
        this.activeProcessId = activeProcessId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getProcessList() {
        return processList;
    }

    public void setProcessList(List<String> processList) {
        this.processList = processList;
    }

    public String getActiveProcessId() {
        return activeProcessId;
    }

    public void setActiveProcessId(String activeProcessId) {
        this.activeProcessId = activeProcessId;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public List<Request> getChatRequests() {
        return chatRequests;
    }

    public void setChatRequests(List<Request> chatRequests) {
        this.chatRequests = chatRequests;
    }

    public void setDialogMode(boolean value) {
        this.dialogMode = value;
    }
    
    public Boolean getDialogMode() {
        return this.dialogMode;
    }
    
    @Override
    public String toString() {
        return String.format(
                "Chat[id=%d, activeProcessId='%s']",
                id, activeProcessId);
    }
    
}
