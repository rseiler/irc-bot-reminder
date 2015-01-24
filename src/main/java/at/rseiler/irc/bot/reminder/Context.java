package at.rseiler.irc.bot.reminder;

import at.rseiler.irc.bot.reminder.command.*;
import at.rseiler.irc.bot.reminder.service.*;
import at.rseiler.irc.bot.reminder.service.impl.*;
import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;

import java.io.IOException;

public class Context {

    private final PersistenceService persistenceService;
    private EventExecutor eventExecutor;
    private EventScheduler eventScheduler;
    private PircBotX bot;
    private EventListener listener;

    public Context(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public Context init(String serverHostName, String login, String ServerPassword) throws IOException {
        listener = new EventListener();
        Configuration<PircBotX> configuration = new Builder<>()
                .setServerHostname(serverHostName)
                .setLogin(login)
                .setServerPassword(ServerPassword)
                .addAutoJoinChannel("#bot")
                .setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates())
                .setAutoSplitMessage(false)
                .setAutoReconnect(true)
                .setMessageDelay(50)
                .addListener(listener)
                .buildConfiguration();

        bot = new PircBotX(configuration);
        eventExecutor = new EventExecutor(bot);
        eventScheduler = new EventScheduler(eventExecutor);
        eventScheduler.addEvents(persistenceService.readFile());
        listener.addCommand(createCommands(eventScheduler, persistenceService));

        return this;
    }

    public EventExecutor getEventExecutor() {
        return eventExecutor;
    }

    public EventScheduler getEventScheduler() {
        return eventScheduler;
    }

    public PircBotX getBot() {
        return bot;
    }

    EventListener getListener() {
        return listener;
    }

    private Command[] createCommands(EventScheduler eventScheduler, PersistenceService persistenceService) {
        UserService userService = new UserService();
        return new Command[]{
                new AddCommand(eventScheduler, new EventFactory(), persistenceService),
                new ExplainCommand(),
                new ListCommand(eventScheduler, userService),
                new RemoveCommand(eventScheduler, userService, persistenceService)
        };
    }

}
