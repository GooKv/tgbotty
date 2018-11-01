package team.guest.tgbotty.bot.callbacks;

public abstract class BotKeyboardCallback implements IBotCallback {
    
    private int originalMessageId;
    
    public void setOriginalMessageId(int id) {
        this.originalMessageId = id;
    }
    
    public int getOriginalMessageId() {
        return this.originalMessageId;
    }
    
}
