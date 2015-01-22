package at.rseiler.irc.bot.reminder.command;

import at.rseiler.irc.bot.reminder.cronevent.Event;
import at.rseiler.irc.bot.reminder.service.EventScheduler;
import at.rseiler.irc.bot.reminder.util.PersistenceUtil;
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
    private static final String ALL_REMOVE = "all-remove";

    public RemoveCommand(EventScheduler eventScheduler) {
        super(eventScheduler);
    }

    @Override
    public void execute(GenericMessageEvent event) {
        String command = event.getMessage();

        if (command.startsWith(REMOVE)) {
            List<Event> events = getEventScheduler().getUserEvents(event.getUser().getNick());
            String index = command.substring(REMOVE.length()).trim();
            removeEvent(event, events, index);
        } else if (command.startsWith(ALL_REMOVE)) {
            List<Event> events = getEventScheduler().getCronEvents();
            String index = command.substring(ALL_REMOVE.length()).trim();
            removeEvent(event, events, index);
        }
    }

    private void removeEvent(GenericMessageEvent event, List<Event> events, String index) {
        if (StringUtils.isNumeric(index) && Integer.parseInt(index) > 0 && Integer.parseInt(index) <= events.size()) {
            getEventScheduler().removeEvent(events.get(Integer.parseInt(index) - 1));
            PersistenceUtil.persist(event, getEventScheduler().getCronEvents());
        } else {
            event.respond("Invalid parameter: " + index);
        }
    }

}
