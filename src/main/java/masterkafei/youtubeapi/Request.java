package masterkafei.youtubeapi;


import javax.net.ssl.HttpsURLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private String uri;
    private String method;
    private Map<String, String> parameters = new HashMap<>();

    public Request(String uri, String method) {
        setUri(uri);
        setMethod(method);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    private void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public Request addParameter(String name, String value) {
        this.parameters.put(name, value);

        return this;
    }

    public String execute() {
        try {
            URL url = new URL(this.uri + ParameterStringBuilder.getParamsString(this.parameters));
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            if (con != null) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String input;
                    StringBuilder content = new StringBuilder();

                    while ((input = br.readLine()) != null) {
                        content.append(input);
                    }
                    br.close();
                    return content.toString();

                } catch (IOException e) {

                }

                con.disconnect();
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        return null;
    }
}
