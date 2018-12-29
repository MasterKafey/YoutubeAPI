package masterkafei.youtubeapi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class API {

    private String key;

    public API(String key) {
        this.key = key;
    }

    public API setKey(String key) {
        this.key = key;

        return this;
    }

    public int getSubscriberCount(String channel) throws ParseException {
        String uri = "https://www.googleapis.com/youtube/v3/channels";
        Request request = new Request(uri, "GET");
        request
                .addParameter("part", "statistics")
                .addParameter("id", channel)
                .addParameter("key", this.key);
        String jsonData = request.execute();

        JSONObject json = (JSONObject) new JSONParser().parse(jsonData);
        JSONArray items = (JSONArray) json.get("items");
        JSONObject firstElement = (JSONObject) items.get(0);
        JSONObject statistics = (JSONObject) firstElement.get("statistics");
        String subscriberCount = (String) statistics.get("subscriberCount");
        return Integer.parseInt(subscriberCount);
    }
}
