package team.guest.tgbotty.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long chatId;
    @ElementCollection
    private List<String> processList = new ArrayList<>();
    private String activeProcessId;

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

    @Override
    public String toString() {
        return String.format(
                "Chat[id=%d, activeProcessId='%s']",
                id, activeProcessId);
    }


}
