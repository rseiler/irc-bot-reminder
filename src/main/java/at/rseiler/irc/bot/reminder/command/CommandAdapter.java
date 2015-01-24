package at.rseiler.irc.bot.reminder.command;

import at.rseiler.irc.bot.reminder.service.impl.EventScheduler;

public abstract class CommandAdapter implements Command {

    private final EventScheduler eventScheduler;

    public CommandAdapter(EventScheduler eventScheduler) {
        this.eventScheduler = eventScheduler;
    }

    EventScheduler getEventScheduler() {
        return eventScheduler;
    }
}
