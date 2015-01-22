package at.rseiler.irc.bot.reminder.cronevent;

import com.google.common.base.Optional;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.quartz.CronExpression;

import java.util.List;
import java.util.stream.Collectors;

public class CronEventChat extends CronEvent {

    private static final long serialVersionUID = -8965253939347189201L;

    private String channelName;

    public CronEventChat(CronExpression cronExpression, String user, String message, String channelName) {
        super(user, message, cronExpression);
        this.channelName = channelName;
    }

    @Override
    public void execute(PircBotX pircBotX) {
        while (!getChannel(pircBotX).isPresent()) {
            pircBotX.sendIRC().joinChannel(channelName);
            sleep(1000);
        }

        getChannel(pircBotX).get().send().message(getMessage());
//        getPircBotX().sendRaw().rawLine("PART " + channel);
    }

    @Override
    public String shortDescription(boolean showCreatedBy) {
        String msg = StringUtils.abbreviate(getMessage(), 32);
        String expression = getCronExpression().getCronExpression();
        String creator = showCreatedBy ? String.format("(by %s)", getCreator()) : "";
        return String.format("%s %s %s %s", expression, channelName, msg, creator);
    }

    public String getChannelName() {
        return channelName;
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
        return "CronEventChat{" +
                "channelName='" + channelName + '\'' +
                "} " + super.toString();
    }
}
