package at.rseiler.irc.bot.reminder.command;

import at.rseiler.irc.bot.reminder.event.Event;
import at.rseiler.irc.bot.reminder.service.impl.EventScheduler;
import at.rseiler.irc.bot.reminder.service.impl.UserService;
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

    private final UserService userService;

    public ListCommand(EventScheduler eventScheduler, UserService userService) {
        super(eventScheduler);
        this.userService = userService;
    }

    @Override
    public void execute(GenericMessageEvent event) {
        String command = event.getMessage();

        if (LIST.equals(command)) {
            List<Event> events = getEventScheduler().getUserEvents(event.getUser().getNick());
            userService.putLastSeenEvents(event.getUser().getNick(), events);
            printEventList(event, events, false);
        } else if (LIST_ALL.equals(command)) {
            List<Event> events = getEventScheduler().getCronEvents();
            userService.putLastSeenEvents(event.getUser().getNick(), events);
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
