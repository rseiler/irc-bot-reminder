package at.rseiler.irc.bot.reminder.service.impl;

import at.rseiler.irc.bot.reminder.command.Command;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Listens to incoming messages and processes its with the given {@link Command}s.
 *
 * @author reinhard.seiler@gmail.com
 */
public class EventListener extends ListenerAdapter {

    private final List<Command> commands = new ArrayList<>();

    public EventListener() {
    }

    public EventListener(Command... cmds) {
        Collections.addAll(commands, cmds);
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
        commands.stream().forEach(command -> command.execute(event));
    }

    public void addCommand(Command... cmds) {
        Collections.addAll(commands, cmds);
    }

}
