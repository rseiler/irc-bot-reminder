package at.rseiler.irc.bot.reminder;

import java.io.IOException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

public class Test {

    public static void main(String[] args) throws IOException, ParseException {
//        CronExpression cronExpression = null;
//        String message = "cron \"*/5 * * * * ?\" \"say hello every 5s\"";
//        Pattern pattern = Pattern.compile("cron \"(.*?)\" \"(.*?)\"");
//        Matcher matcher = pattern.matcher(message);
//        if (matcher.find()) {
//            try {
//                System.out.println(matcher.group(1));
//                cronExpression = new CronExpression(matcher.group(1));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("not found");
//        }
//        System.out.println(cronExpression);

//        String input = "cron \"*/5 * * * * ?\" #test foo";
//        Matcher matcher = Pattern.compile("cron \"(.*?)\" (#[\\w\\d]+) (.*)").matcher(input);
//        System.out.println(matcher.find());

//        PersistenceUtil.writeFile(Arrays.asList(
//                new Event[]{
//                        new CronEventChat(new CronExpression("*/8 * * * * ?"), "reinhard", "test", "test"),
//                        new CronEventPrivate(new CronExpression("*/8 * * * * ?"), "reinhard", "test", "test")
//                }
//
//        ));
//
//
//        PersistenceUtil.readFile().stream().forEach(System.out::println);

        TemporalAccessor temporalAccessor = DateTimeFormatter.ofPattern("d.M k:m").parse("21.1 22:22");
        System.out.println(temporalAccessor.isSupported(ChronoField.MONTH_OF_YEAR));

    }

}
