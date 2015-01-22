package at.rseiler.irc.bot.reminder;


import at.rseiler.irc.bot.reminder.command.*;
import at.rseiler.irc.bot.reminder.service.EventExecutor;
import at.rseiler.irc.bot.reminder.service.EventFactory;
import at.rseiler.irc.bot.reminder.service.EventListener;
import at.rseiler.irc.bot.reminder.service.EventScheduler;
import at.rseiler.irc.bot.reminder.util.PersistenceUtil;
import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;

/**
 * Program entry point.
 * Connects to the IRC server, creates and starts all services.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 3) {

            EventListener listener = new EventListener();
            Configuration<PircBotX> configuration = new Builder<>()
                    .setServerHostname(args[0])
                    .setLogin(args[1])
                    .setServerPassword(args[2])
                    .addAutoJoinChannel("#bot")
                    .setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates())
                    .setAutoSplitMessage(false)
                    .setAutoReconnect(true)
                    .setMessageDelay(100)
                    .addListener(listener)
                    .buildConfiguration();

            PircBotX bot = new PircBotX(configuration);
            EventExecutor eventExecutor = new EventExecutor(bot);
            EventScheduler eventScheduler = new EventScheduler(eventExecutor);
            eventScheduler.addEvents(PersistenceUtil.readFile());
            listener.addCommand(createCommands(eventScheduler, new EventFactory()));

            eventExecutor.start();
            eventScheduler.start();
            // blocks as long as the bot is running
            bot.startBot();

            eventScheduler.shutdown();
            eventExecutor.shutdown();
        } else {
            System.out.println("Usage: java -jar irc-reminder-bot.jar serverHostname loginName password");
        }
    }

    private static Command[] createCommands(EventScheduler eventScheduler, EventFactory eventFactory) {
        return new Command[]{
                new AddCommand(eventScheduler, eventFactory),
                new ExplainCommand(),
                new ListCommand(eventScheduler),
                new RemoveCommand(eventScheduler)
        };
    }

}
