package masterkafei.youtubeapi;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class Configuration {

    private FileConfiguration config;

    public static final String YOUTUBE_API_KEY_CONFIGURATION_KEY = "youtube_api_key";
    public static final String TIME_UPDATE_CONFIGURATION_KEY = "time_update";
    public static final String TIMER_LAUNCHED_CONFIGURATION_KEY = "timer_launched";
    public static final String YOUTUBE_CHANNEL_ID_CONFIGURATION_KEY = "youtube_channel_id";

    private static final HashMap<String, Object> defaultValues;
    static {
        defaultValues = new HashMap<>();
        defaultValues.put(YOUTUBE_API_KEY_CONFIGURATION_KEY, "ThisIsNotAValidYoutubeApiKeyChangeIt");
        defaultValues.put(TIME_UPDATE_CONFIGURATION_KEY, 1000);
        defaultValues.put(TIMER_LAUNCHED_CONFIGURATION_KEY, false);
        defaultValues.put(YOUTUBE_CHANNEL_ID_CONFIGURATION_KEY, "");
    }

    public Configuration(FileConfiguration config) {
        this.setConfig(config);
        initConfiguration();
        getConfig().options().copyDefaults(true);
    }

    private void initConfiguration() {
        for (HashMap.Entry<String, Object> entry : defaultValues.entrySet()) {
            if(!getConfig().isSet(entry.getKey()))
            {
                setConfiguration(entry.getKey(), entry.getValue());
            }
        }
    }

    private Configuration setConfig(FileConfiguration config) {
        this.config = config;

        return this;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void setConfiguration(String key, Object value) {
        getConfig().set(key, value);
        getConfig().options().copyDefaults(true);
    }

    public Object getConfiguration(String key) {
        return getConfig().get(key);
    }
}
