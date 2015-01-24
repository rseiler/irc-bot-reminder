package at.rseiler.irc.bot.reminder.command;

import org.apache.log4j.Logger;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.quartz.CronExpression;

import java.text.ParseException;

/**
 * Processes commands which explain a cron expression.
 *
 * @author reinhard.seiler@gmail.com
 */
public class ExplainCommand implements Command {

    private static final Logger LOG = Logger.getLogger(ExplainCommand.class);
    private static final String EXPLAIN = "explain";

    @Override
    public void execute(GenericMessageEvent event) {
        String command = event.getMessage();

        if (command.startsWith(EXPLAIN)) {
            try {
                String expression = command.substring(EXPLAIN.length()).trim();
                CronExpression cronExpression = new CronExpression(expression);
                for (String line : cronExpression.getExpressionSummary().split("\n")) {
                    event.respond(line);
                }
            } catch (ParseException e) {
                LOG.info(e);
                event.respond("Failed to parse expression: " + e.getMessage());
            }
        }
    }
}
