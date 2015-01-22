package at.rseiler.irc.bot.reminder.command;

import org.pircbotx.hooks.types.GenericMessageEvent;

/**
 * Handles a GenericMessageEvent.
 *
 * @author reinhard.seiler@gmail.com
 */
public interface Command {

    void execute(GenericMessageEvent event);

}
