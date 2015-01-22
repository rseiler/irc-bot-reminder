package at.rseiler.irc.bot.reminder.cronevent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class OnceEvent extends Event {

    private static final long serialVersionUID = -6114155326872305049L;
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private LocalDateTime localDateTime;

    public OnceEvent(String creator, String message, LocalDateTime localDateTime) {
        super(creator, message);
        this.localDateTime = localDateTime;
    }

    @Override
    public boolean executable(LocalDateTime date) {
        return localDateTime.isEqual(date);
    }

    @Override
    public boolean removeable(LocalDateTime date) {
        return localDateTime.isBefore(date);
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

}
