package at.rseiler.irc.bot.reminder.event;

import org.pircbotx.PircBotX;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Event implements Serializable {

    private static final long serialVersionUID = 871743480304938444L;

    private String creator;
    private String message;

    public Event(String creator, String message) {
        this.creator = creator;
        this.message = message;
    }

    public abstract boolean executable(LocalDateTime date);

    public abstract boolean removeable(LocalDateTime date);

    public abstract void execute(PircBotX pircBotX);

    public abstract String shortDescription(boolean showCreatedBy);

    public String getMessage() {
        return message;
    }

    public String getCreator() {
        return creator;
    }
}
