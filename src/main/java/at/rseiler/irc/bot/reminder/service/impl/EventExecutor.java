package at.rseiler.irc.bot.reminder.service.impl;

import at.rseiler.irc.bot.reminder.event.Event;
import org.apache.log4j.Logger;
import org.pircbotx.PircBotX;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Executes the events.
 *
 * @author reinhard.seiler@gmail.com
 */
public class EventExecutor extends Thread {

    private static final Logger LOG = Logger.getLogger(EventExecutor.class);

    private final BlockingQueue<Event> queue = new LinkedBlockingQueue<>();
    private final PircBotX pircBotX;
    private boolean running = true;

    public EventExecutor(PircBotX pircBotX) {
        this.pircBotX = pircBotX;
    }

    public void execute(List<Event> events) {
        for (Event event : events) {
            try {
                queue.put(event);
            } catch (InterruptedException e) {
                LOG.error("Failed to put event on the queue", e);
            }
        }

    }

    @Override
    public void run() {
        while (running) {
            try {
                queue.take().execute(pircBotX);
            } catch (InterruptedException ignore) {
            }
        }
    }

    public void shutdown() {
        running = false;
    }

}
