package at.rseiler.irc.bot.reminder.event;

import org.quartz.CronExpression;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public abstract class CronEvent extends Event {

    private static final long serialVersionUID = -2751486160802238002L;

    private CronExpression cronExpression;

    public CronEvent(String creator, String message, CronExpression cronExpression) {
        super(creator, message);
        this.cronExpression = cronExpression;
    }

    @Override
    public boolean executable(LocalDateTime date) {
        return cronExpression != null && cronExpression.isSatisfiedBy(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()));
    }

    @Override
    public boolean removeable(LocalDateTime date) {
        return false;
    }

    CronExpression getCronExpression() {
        return cronExpression;
    }

    @Override
    public String toString() {
        return "CronEvent{" +
                "cronExpression=" + cronExpression +
                "} " + super.toString();
    }

}
