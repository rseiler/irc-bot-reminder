package at.rseiler.irc.bot.reminder.event;

import org.apache.commons.lang3.StringUtils;
import org.pircbotx.PircBotX;
import org.quartz.CronExpression;

public class CronEventPrivate extends CronEvent {

    private static final long serialVersionUID = -8978701567702497643L;

    private String user;

    public CronEventPrivate(CronExpression cronExpression, String creator, String message, String user) {
        super(creator, message, cronExpression);
        this.user = user;
    }

    @Override
    public void execute(PircBotX pircBotX) {
        pircBotX.sendIRC().message(user, getMessage());
    }

    @Override
    public String shortDescription(boolean showCreatedBy) {
        String msg = StringUtils.abbreviate(getMessage(), 32);
        String expression = getCronExpression().getCronExpression();
        String creator = showCreatedBy ? String.format("(by %s)", getCreator()) : "";
        return String.format("%s %s %s %s", expression, user, msg, creator);
    }

    @Override
    public String toString() {
        return "CronEventPrivate{" +
                "user='" + user + '\'' +
                "} " + super.toString();
    }
}
