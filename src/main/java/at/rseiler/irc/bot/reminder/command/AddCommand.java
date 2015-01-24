package at.rseiler.irc.bot.reminder.command;

import at.rseiler.irc.bot.reminder.event.Event;
import at.rseiler.irc.bot.reminder.service.impl.EventFactory;
import at.rseiler.irc.bot.reminder.service.impl.EventScheduler;
import at.rseiler.irc.bot.reminder.service.PersistenceService;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.Optional;

/**
 * Processes commands which will add a new event.
 *
 * @author reinhard.seiler@gmail.com
 */
public class AddCommand extends CommandAdapter {

    private static final String ADD = "add";

    private final EventFactory eventFactory;
    private final PersistenceService persistenceService;

    public AddCommand(EventScheduler eventScheduler, EventFactory eventFactory, PersistenceService persistenceService) {
        super(eventScheduler);
        this.eventFactory = eventFactory;
        this.persistenceService = persistenceService;
    }

    @Override
    public void execute(GenericMessageEvent event) {
        String command = event.getMessage();

        if (command.startsWith(ADD)) {
            Optional<Event> cronEvent = eventFactory.create(event.getUser().getNick(), command);

            if (cronEvent.isPresent()) {
                getEventScheduler().addEvent(cronEvent.get());
                persistenceService.persist(event, getEventScheduler().getCronEvents());
            } else {
                event.respond("Failed to create event.");
            }
        }
    }

}
