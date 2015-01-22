package at.rseiler.irc.bot.reminder.command;

import at.rseiler.irc.bot.reminder.cronevent.Event;
import at.rseiler.irc.bot.reminder.service.EventScheduler;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.List;

/**
 * Processes commands which list active events.
 *
 * @author reinhard.seiler@gmail.com
 */
public class ListCommand extends CommandAdapter {


    private static final String LIST = "list";
    private static final String LIST_ALL = "list-all";

    public ListCommand(EventScheduler eventScheduler) {
        super(eventScheduler);
    }

    @Override
    public void execute(GenericMessageEvent event) {
        String command = event.getMessage();

        if (LIST.equals(command)) {
            List<Event> events = getEventScheduler().getUserEvents(event.getUser().getNick());
            printEventList(event, events, false);
        } else if (LIST_ALL.equals(command)) {
            List<Event> events = getEventScheduler().getCronEvents();
            printEventList(event, events, true);
        }
    }

    private void printEventList(GenericMessageEvent genericMessageEvent, List<Event> events, boolean showCreatedBy) {
        if (events.isEmpty()) {
            genericMessageEvent.respond("Event list is empty.");
        }

        for (int i = 0; i < events.size(); i++) {
            genericMessageEvent.respond((i + 1) + ". " + events.get(i).shortDescription(showCreatedBy));
        }
    }


}
