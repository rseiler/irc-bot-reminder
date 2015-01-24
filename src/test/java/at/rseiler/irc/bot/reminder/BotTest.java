package at.rseiler.irc.bot.reminder;

import at.rseiler.irc.bot.reminder.service.PersistenceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pircbotx.User;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

public class BotTest {

    private Context context;

    @Before
    public void setUp() throws Exception {
        PersistenceService persistenceService = mock(PersistenceService.class);
        context = new Context(persistenceService).init("hostName", "login", "password");
    }

    @Test
    public void testAddAndRemove() throws Exception {
        context.getListener().onPrivateMessage(createPrivateMessageEvent("add \"*/5 * * * * ?\" @rseiler test3", "rseiler"));
        context.getListener().onPrivateMessage(createPrivateMessageEvent("add \"*/5 * * * * ?\" #rseiler test4", "rseiler"));
        context.getListener().onPrivateMessage(createPrivateMessageEvent("add 12:00 @rseiler test1", "rseiler"));
        context.getListener().onPrivateMessage(createPrivateMessageEvent("add 13:00 #rseiler test2", "rseiler"));
        context.getListener().onPrivateMessage(createPrivateMessageEvent("add invalid", "rseiler"));
        context.getListener().onPrivateMessage(createPrivateMessageEvent("list", "rseiler"));
        context.getListener().onPrivateMessage(createPrivateMessageEvent("remove 2", "rseiler"));

        Assert.assertThat(context.getEventScheduler().getCronEvents().size(), is(3));

        PrivateMessageEvent messageEvent = createPrivateMessageEvent("list", "rseiler");
        context.getListener().onPrivateMessage(messageEvent);

        verify(messageEvent, times(3)).respond(anyString());
    }

    @Test
    public void testExplain() throws Exception {
        PrivateMessageEvent messageEvent = createPrivateMessageEvent("explain */5 * * * * ?", "rseiler");
        context.getListener().onPrivateMessage(messageEvent);

        verify(messageEvent, times(11)).respond(anyString());
    }

    private PrivateMessageEvent createPrivateMessageEvent(String message, String nick) {
        PrivateMessageEvent event = mock(PrivateMessageEvent.class);
        when(event.getMessage()).thenReturn(message);
        User user = mock(User.class);
        when(user.getNick()).thenReturn(nick);
        when(event.getUser()).thenReturn(user);
        return event;
    }

}