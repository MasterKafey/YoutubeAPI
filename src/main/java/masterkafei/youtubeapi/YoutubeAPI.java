package masterkafei.youtubeapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public final class YoutubeAPI extends JavaPlugin {

    private Configuration configuration;
    private Timer timer;
    private API api;

    private static List<String> helpMessage = new ArrayList<String>() {{
        add(ChatColor.GREEN + "/youtube-api start [channel-id] : Start the timer");
        add(ChatColor.GREEN + "/youtube-api stop : Stop the timer");
        add(ChatColor.GREEN + "/youtube-api time <milliseconds> : Configure the time interval");
        add(ChatColor.GREEN + "/youtube-api key <api-key> : Configure the youtube api key");
    }};

    @Override
    public void onEnable() {
        this.configuration = new Configuration(this.getConfig());
        saveConfig();
        api = new API((String) configuration.getConfiguration(Configuration.YOUTUBE_API_KEY_CONFIGURATION_KEY));
        timer = new Timer((Integer) configuration.getConfiguration(Configuration.TIME_UPDATE_CONFIGURATION_KEY));
        if ((boolean) configuration.getConfiguration(Configuration.TIMER_LAUNCHED_CONFIGURATION_KEY)) {
            timer.start(getTimerTask());
        }
    }

    @Override
    public void onDisable() {
        timer.stop();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("youtube-api")) {
            if (args.length == 0) {

                sender.sendMessage(helpMessage.toArray(new String[0]));
                return true;
            }

            switch (args[0]) {
                case ("start"):
                    if (args.length != 2 && args.length != 1) {
                        sender.sendMessage(helpMessage.get(0));
                        return true;
                    }
                    if (args.length == 2) {
                        configuration.setConfiguration(Configuration.YOUTUBE_CHANNEL_ID_CONFIGURATION_KEY, args[1]);
                        saveConfig();
                    }
                    if (configuration.getConfiguration(Configuration.YOUTUBE_CHANNEL_ID_CONFIGURATION_KEY) == null) {
                        sender.sendMessage("You need to pass an id channel for the first time");
                    }
                    if (timer.start(getTimerTask())) {
                        configuration.setConfiguration(Configuration.TIMER_LAUNCHED_CONFIGURATION_KEY, true);
                        saveConfig();
                        sender.sendMessage(ChatColor.GREEN + "Timer successfully started !");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Timer not stopped yet !");
                    }
                    return true;

                case ("stop"):
                    if (args.length != 1) {
                        sender.sendMessage(helpMessage.get(1));
                        return true;
                    }
                    if (timer.stop()) {
                        configuration.setConfiguration(Configuration.TIMER_LAUNCHED_CONFIGURATION_KEY, false);
                        saveConfig();
                        sender.sendMessage(ChatColor.GREEN + "Timer successfully stopped !");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Timer is not started yet !");
                    }
                    return true;
                case ("time"):
                    try {
                        int value = Integer.parseInt(args[1]);
                        if (value < 100) {
                            value = 100;
                            sender.sendMessage(ChatColor.RED + "Youtube may block some request under 100 milliseconds");
                        }
                        configuration.setConfiguration(Configuration.TIME_UPDATE_CONFIGURATION_KEY, value);
                        saveConfig();
                        timer.setMilliseconds(value);
                        sender.sendMessage(ChatColor.GREEN + "Timer : " + value + " milliseconds");
                    } catch (NumberFormatException exception) {
                        sender.sendMessage(ChatColor.RED + args[1] + " is not a valid number");
                    }
                    return true;
                case ("key"):
                    configuration.setConfiguration(Configuration.YOUTUBE_API_KEY_CONFIGURATION_KEY, args[1]);
                    saveConfig();
                    this.api.setKey(args[1]);
                    sender.sendMessage(ChatColor.GREEN + "Key successfully updated !");
                    return true;
            }

            return true;
        }
        return false;
    }

    public TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                int subs = 0;
                try {
                    subs = api.getSubscriberCount((String) configuration.getConfiguration(Configuration.YOUTUBE_CHANNEL_ID_CONFIGURATION_KEY));
                } catch (Exception exception) {
                    System.out.println("Error : Check your youtube api key");
                    timer.stop();
                }

                System.out.println(subs);
                try {
                    Bukkit.getScoreboardManager().getMainScoreboard().getObjective("CompteurAbos").getScore("#AbonnesCurrent").setScore(subs);
                } catch (Exception exception) {
                    System.out.println("Error did you correctly configure the datapack ?");
                    timer.stop();
                }
            }
        };
    }
}
