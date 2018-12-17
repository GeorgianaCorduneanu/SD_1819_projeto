package menu_inicial.action;


import com.github.scribejava.core.oauth.OAuthService;
import menu_inicial.rest.DropBoxApi2;
import menu_inicial.rest.DropBoxRestClient;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuthService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class DropboxAuth_action extends DropBoxRestClient {
    private String url;
    public String getUrl() {
        return url;
    }

    public String execute() throws Exception {
        OAuthService service = buildService();
        url = service.getAuthorizationUrl(null);
        return "redirect";
    }

    public void setUrl(String url) {
        this.url = url;
    }
}