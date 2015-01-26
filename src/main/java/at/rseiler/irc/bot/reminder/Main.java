package at.rseiler.irc.bot.reminder;


import at.rseiler.irc.bot.reminder.service.impl.PersistenceServiceImpl;

/**
 * Program entry point.
 * Connects to the IRC server, creates and starts all services.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 4) {
            Context context = new Context(new PersistenceServiceImpl()).init(args[0], Integer.parseInt(args[1]), args[2], args[3]);

            context.getEventExecutor().start();
            context.getEventScheduler().start();
            // blocks as long as the bot is running
            context.getBot().startBot();

            context.getEventScheduler().shutdown();
            context.getEventExecutor().shutdown();
        } else {
            System.out.println("Usage: java -jar irc-reminder-bot.jar hostname port username password");
        }
    }

}
