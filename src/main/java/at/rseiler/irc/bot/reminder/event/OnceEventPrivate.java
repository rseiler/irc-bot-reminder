package at.rseiler.irc.bot.reminder.event;

import org.apache.commons.lang3.StringUtils;
import org.pircbotx.PircBotX;

import java.time.LocalDateTime;

public class OnceEventPrivate extends OnceEvent {

    private static final long serialVersionUID = -8978701567702497643L;

    private String user;

    public OnceEventPrivate(String creator, String message, LocalDateTime localDateTime, String user) {
        super(creator, message, localDateTime);
        this.user = user;
    }

    @Override
    public String shortDescription(boolean showCreatedBy) {
        String msg = StringUtils.abbreviate(getMessage(), 32);
        String expression = getLocalDateTime().format(DATE_TIME_FORMATTER);
        String creator = showCreatedBy ? String.format("(by %s)", getCreator()) : "";
        return String.format("%s %s %s %s", expression, user, msg, creator);
    }

    @Override
    public void execute(PircBotX pircBotX) {
        pircBotX.sendIRC().message(user, getMessage());
    }

}
