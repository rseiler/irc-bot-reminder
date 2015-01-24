package at.rseiler.irc.bot.reminder.command;

import at.rseiler.irc.bot.reminder.event.Event;
import at.rseiler.irc.bot.reminder.service.impl.EventScheduler;
import at.rseiler.irc.bot.reminder.service.PersistenceService;
import at.rseiler.irc.bot.reminder.service.impl.UserService;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.List;

/**
 * Processes commands which removes events.
 *
 * @author reinhard.seiler@gmail.com
 */
public class RemoveCommand extends CommandAdapter {

    private static final String REMOVE = "remove";
    private final UserService userService;
    private final PersistenceService persistenceService;

    public RemoveCommand(EventScheduler eventScheduler, UserService userService, PersistenceService persistenceService) {
        super(eventScheduler);
        this.userService = userService;
        this.persistenceService = persistenceService;
    }

    @Override
    public void execute(GenericMessageEvent event) {
        String command = event.getMessage();

        if (command.startsWith(REMOVE)) {
            List<Event> events = userService.getLastSeenEvents(event.getUser().getNick());
            String index = command.substring(REMOVE.length()).trim();
            removeEvent(event, events, index);
        }
    }

    private void removeEvent(GenericMessageEvent event, List<Event> events, String index) {
        boolean success = false;

        if (StringUtils.isNumeric(index)) {
            int indexInt = Integer.parseInt(index);
            if (indexInt > 0 && indexInt <= events.size()) {
                getEventScheduler().removeEvent(events.get(indexInt - 1));
                events.remove(indexInt - 1);
                persistenceService.persist(event, getEventScheduler().getCronEvents());
                success = true;
            }
        }

        if (!success) {
            event.respond("Invalid parameter: " + index);
        }
    }

}
