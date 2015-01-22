# IRC Bot Reminder

Connects to a IRC server and listens to private messages. Send your messages to the bot and tell him when the message should be sent and to whom.

## Usage

```
java -jar irc-reminder-bot.jar hostname username password
```

## Commands

* ```add ["cron-expression"|datetime|time] [#chat|@user] the message```: adds a reminder message
* ```list```: lists the reminder messages which the user created
* ```list-all```: lists all reminder message
* ```remove [number]```: removes the reminder message with the selected number. the number match the number of the list command.
* ```all-remove [number]```: removes the reminder message with the selected number. the number match the number of the list-all command.   
* ```explain ["cron-expression"]```: explains a cron expression

## Examples

```
add "0 * * * *" @rseiler my hourly reminder
add "0 * * * *" #mychat my hourly reminder for the chat
add 11:11 #mychat this message will be sent at 11:11 on the same day if it is before 11:11 or at 11:11 at the next day
add 2016-12-24 00:00 #mychat reminder for xmas for 2016
add 24.12.2016 00:00 #mychat reminder for xmas for 2016
add 12-24 00:00 #mychat reminder for xmas for the current year
add 24. send your messages to the bot and tell him when the message should be sent and to whom.12 00:00 #mychat reminder for xmas for the current year
add 24 0:0 #mychat reminder for the 24th of the current month
list
remove 1
list-all
all-remove 1
explain "0 * * * *"
```
