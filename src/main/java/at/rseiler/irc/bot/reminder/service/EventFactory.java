package at.rseiler.irc.bot.reminder.service;

import at.rseiler.irc.bot.reminder.cronevent.CronEventChat;
import at.rseiler.irc.bot.reminder.cronevent.CronEventPrivate;
import at.rseiler.irc.bot.reminder.cronevent.Event;
import at.rseiler.irc.bot.reminder.cronevent.OnceEventPrivate;
import org.apache.log4j.Logger;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Factory to create Event objects.
 *
 * @author reinhard.seiler@gmail.com
 */
public class EventFactory {

    private static final Logger LOG = Logger.getLogger(EventFactory.class);
    private static final Pattern CRON_CHAT_PATTERN = Pattern.compile("add \"(.*?)\" (#[_\\w\\d]+) (.*)");
    private static final Pattern CRON_USER_PATTERN = Pattern.compile("add \"(.*?)\" @([_\\.\\w\\d]+) (.*)");
    private static final Pattern ONCE_USER_PATTERN_TIME = Pattern.compile("add (\\d{1,2}:\\d{1,2}) @([_\\.\\w\\d]+) (.*)");
    private static final Pattern ONCE_USER_PATTERN_DATE_TIME = Pattern.compile("add ([-\\.\\d]+ \\d{1,2}:\\d{1,2}) @([_\\.\\w\\d]+) (.*)");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("k:m");
    private static final DateTimeFormatter[] DATE_TIME_FORMATTERS = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("y-M-d k:m"),
            DateTimeFormatter.ofPattern("M-d k:m"),
            DateTimeFormatter.ofPattern("d k:m"),
            DateTimeFormatter.ofPattern("d.M.y k:m"),
            DateTimeFormatter.ofPattern("d.M k:m"),
    };

    /**
     * Creates an event.
     *
     * @param user    the creator of the event
     * @param command the event command
     * @return an {@code Optional} with the event
     */
    public Optional<Event> create(String user, String command) {
        try {
            Matcher chatMatcher = CRON_CHAT_PATTERN.matcher(command);
            if (chatMatcher.find()) {
                return Optional.of(new CronEventChat(new CronExpression(chatMatcher.group(1)), user, chatMatcher.group(3), chatMatcher.group(2)));
            }

            Matcher userMatcher = CRON_USER_PATTERN.matcher(command);
            if (userMatcher.find()) {
                return Optional.of(new CronEventPrivate(new CronExpression(userMatcher.group(1)), user, userMatcher.group(3), userMatcher.group(2)));
            }
        } catch (ParseException e) {
            LOG.info("Failed to parse cron expression", e);
        }

        Matcher onceUserTimeMatcher = ONCE_USER_PATTERN_TIME.matcher(command);
        if (onceUserTimeMatcher.find()) {
            LocalTime time = LocalTime.parse(onceUserTimeMatcher.group(1), TIME_FORMATTER);
            LocalDateTime localDateTime = time.atDate(LocalDate.now()).plusDays(time.isBefore(LocalTime.now()) ? 1 : 0);
            return Optional.of(new OnceEventPrivate(user, onceUserTimeMatcher.group(3), localDateTime, onceUserTimeMatcher.group(2)));
        }

        Matcher onceUserDateTimeMatcher = ONCE_USER_PATTERN_DATE_TIME.matcher(command);
        if (onceUserDateTimeMatcher.find()) {
            Optional<LocalDateTime> localDateTime = parseDateTime(onceUserDateTimeMatcher.group(1));
            if (localDateTime.isPresent()) {
                return Optional.of(new OnceEventPrivate(user, onceUserDateTimeMatcher.group(3), localDateTime.get(), onceUserDateTimeMatcher.group(2)));
            }
        }

        return Optional.empty();
    }

    /**
     * Tries to parse the dateTime with different formatters.
     *
     * @param dateTime the string representing a date time
     * @return an {@code Optional} with a LocalDateTime representing the dateTime
     */
    private Optional<LocalDateTime> parseDateTime(String dateTime) {
        TemporalAccessor temporalAccessor = null;
        for (DateTimeFormatter dateTimeFormatter : DATE_TIME_FORMATTERS) {
            try {
                temporalAccessor = dateTimeFormatter.parse(dateTime);
            } catch (DateTimeParseException ignore) {
            }
        }

        if (temporalAccessor != null) {
            return Optional.of(createLocalDateTime(temporalAccessor));
        }

        return Optional.empty();
    }

    /**
     * Creates a LocalDateTime object. If the temporalAccessor doesn't provide all necessary TemporalFields than the
     * missing fields will be set to the current date time (now).
     *
     * @param temporalAccessor the object from which the LocalDateTime is buld
     * @return the LocalDateTime
     */
    private LocalDateTime createLocalDateTime(TemporalAccessor temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.now().with(ChronoField.SECOND_OF_MINUTE, 0).with(ChronoField.MILLI_OF_SECOND, 0);
        TemporalField[] temporalFields = {ChronoField.YEAR, ChronoField.MONTH_OF_YEAR, ChronoField.DAY_OF_MONTH, ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR};

        for (TemporalField temporalField : temporalFields) {
            if (temporalAccessor.isSupported(temporalField)) {
                localDateTime = localDateTime.with(temporalField, temporalAccessor.get(temporalField));
            }
        }
        return localDateTime;
    }

}
