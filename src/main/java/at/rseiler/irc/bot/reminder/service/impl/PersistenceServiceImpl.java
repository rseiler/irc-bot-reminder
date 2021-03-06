package at.rseiler.irc.bot.reminder.service.impl;

import at.rseiler.irc.bot.reminder.event.Event;
import at.rseiler.irc.bot.reminder.service.PersistenceService;
import org.apache.log4j.Logger;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Util class to write/read the events from/to the disk.
 */
public class PersistenceServiceImpl implements PersistenceService {

    private static final Logger LOG = Logger.getLogger(PersistenceServiceImpl.class);
    private static final String FILE_NAME = "reminder-data.json";

    /**
     * Writes the events to the disk.
     *
     * @param events the events which should be persisted
     * @throws IOException
     */
    @Override
    public void writeFile(List<Event> events) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(FILE_NAME); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(events);
        }
    }

    /**
     * Reads the persisted events.
     *
     * @return the read events
     * @throws IOException
     */
    @Override
    public List<Event> readFile() throws IOException {
        if (new File(FILE_NAME).exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                return (List<Event>) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                LOG.error("Failed to ");
            }
        }

        return new ArrayList<>();
    }

    /**
     * Writes the events to the disk. If an exception occurs than the initiator will be notified.
     *
     * @param event  the event which triggered the persisting of the events
     * @param events the events which should be persisted
     */
    @Override
    public void persist(GenericMessageEvent event, List<Event> events) {
        try {
            writeFile(events);
        } catch (IOException e) {
            LOG.error("Failed to persist data.", e);
            event.respond("Failed to persist data.");
        }
    }

}
