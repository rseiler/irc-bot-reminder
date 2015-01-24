package at.rseiler.irc.bot.reminder.event;

import com.google.common.base.Optional;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OnceEventChat extends OnceEvent {

    private static final long serialVersionUID = -8978701567702497643L;

    private String channelName;

    public OnceEventChat(String creator, String message, LocalDateTime localDateTime, String channelName) {
        super(creator, message, localDateTime);
        this.channelName = channelName;
    }

    @Override
    public String shortDescription(boolean showCreatedBy) {
        String msg = StringUtils.abbreviate(getMessage(), 32);
        String expression = getLocalDateTime().format(DATE_TIME_FORMATTER);
        String creator = showCreatedBy ? String.format("(by %s)", getCreator()) : "";
        return String.format("%s %s %s %s", expression, channelName, msg, creator);
    }

    @Override
    public void execute(PircBotX pircBotX) {
        while (!getChannel(pircBotX).isPresent()) {
            pircBotX.sendIRC().joinChannel(channelName);
            sleep(1000);
        }

        getChannel(pircBotX).get().send().message(getMessage());
    }

    private Optional<Channel> getChannel(PircBotX pircBotX) {
        List<Channel> channels = pircBotX.getUserBot().getChannels().stream().filter(channel -> channel.getName().equals(channelName)).collect(Collectors.toList());
        return channels.size() == 1 ? Optional.of(channels.get(0)) : Optional.absent();
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignore) {
        }
    }

    @Override
    public String toString() {
        return "OnceEventChat{" +
                "channelName='" + channelName + '\'' +
                "} " + super.toString();
    }

}
