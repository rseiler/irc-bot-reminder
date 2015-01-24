package at.rseiler.irc.bot.reminder.service;

import at.rseiler.irc.bot.reminder.event.Event;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.IOException;
import java.util.List;

public interface PersistenceService {
    void writeFile(List<Event> events) throws IOException;

    List<Event> readFile() throws IOException;

    void persist(GenericMessageEvent event, List<Event> events);
}
