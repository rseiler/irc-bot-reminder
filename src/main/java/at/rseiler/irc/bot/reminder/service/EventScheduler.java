package at.rseiler.irc.bot.reminder.service;

import at.rseiler.irc.bot.reminder.cronevent.Event;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds all active events and checks if the event should be executed. If the event fires than it will be passed to the
 * {@link EventExecutor} which will execute the event. It will automatically remove events which aren't active anymore.
 *
 * @author reinhard.seiler@gmail.com
 */
public class EventScheduler extends Thread {

    private static final int INIT_WAIT_DURATION = 15000;

    private final EventExecutor eventExecutor;
    private final List<Event> events = Collections.synchronizedList(new ArrayList<>());

    private boolean running = true;

    public EventScheduler(EventExecutor eventExecutor) {
        this.eventExecutor = eventExecutor;
    }

    @Override
    public void run() {
        initWait();

        while (running) {
            LocalDateTime now = LocalDateTime.now();
            eventExecutor.execute(getExecutableEvents(now));
            removeEvents(getRemoveableEvents(now));
            sleep(msToNextSecond());
        }
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void addEvents(List<Event> eventList) {
        events.addAll(eventList);
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }

    public void removeEvents(List<Event> eventList) {
        events.removeAll(eventList);
    }

    public List<Event> getCronEvents() {
        return events;
    }

    public List<Event> getUserEvents(String user) {
        return getCronEvents().stream()
                .filter(e -> e.getCreator().equals(user))
                .collect(Collectors.toList());
    }

    public void shutdown() {
        running = false;
    }

    /**
     * Waits so that the bot can fully connect to the irc server.
     */
    private void initWait() {
        sleep(INIT_WAIT_DURATION + msToNextSecond());
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignore) {
        }
    }

    private List<Event> getExecutableEvents(LocalDateTime date) {
        return this.events.stream().filter(event -> event.executable(date)).collect(Collectors.toList());
    }

    private List<Event> getRemoveableEvents(LocalDateTime date) {
        return this.events.stream().filter(event -> event.removeable(date)).collect(Collectors.toList());
    }

    private int msToNextSecond() {
        return 1000 - LocalTime.now().getNano() / 1_000_000;
    }

}
