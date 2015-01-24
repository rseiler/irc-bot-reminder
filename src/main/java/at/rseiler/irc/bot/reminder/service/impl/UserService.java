package at.rseiler.irc.bot.reminder.service.impl;

import at.rseiler.irc.bot.reminder.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private final Map<String, UserData> userDataList = new HashMap<>();

    public void putLastSeenEvents(String user, List<Event> events) {
        getUserData(user).setLastSeenEvents(events);
    }

    public List<Event> getLastSeenEvents(String user) {
        return getUserData(user).getLastSeenEvents();
    }

    private UserData getUserData(String user) {
        if(!userDataList.containsKey(user)) {
            userDataList.put(user, new UserData());
        }

        return userDataList.get(user);
    }

    private static class UserData {

        private List<Event> lastSeenEvents = new ArrayList<>();

        public List<Event> getLastSeenEvents() {
            return lastSeenEvents;
        }

        public void setLastSeenEvents(List<Event> lastSeenEvents) {
            this.lastSeenEvents = lastSeenEvents;
        }

    }

}
