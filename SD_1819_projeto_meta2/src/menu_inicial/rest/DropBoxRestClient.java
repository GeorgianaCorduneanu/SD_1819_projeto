package menu_inicial.rest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;

import uc.sd.apis.DropBoxApi2;



// Step 1: Create Dropbox Account

// Step 2: Create Application (https://www.dropbox.com/developers)

public class DropBoxRestClient {


	// Access codes #1: per application used to get access codes #2
	private static final String API_APP_KEY = "ygfmiw6wfe2psg8";
	private static final String API_APP_SECRET = "f25vajhg3irpnw3";

	// Access codes #2: per user per application
	private static final String API_USER_TOKEN = "";


    public static String getAuthorizationUrl(OAuthService service) {
        return service.getAuthorizationUrl(null);
    }
	public static OAuthService buildService() {
		return new ServiceBuilder()
				.provider(DropBoxApi2.class)
				.apiKey(API_APP_KEY)
				.apiSecret(API_APP_SECRET)
				.callback("http://localhost:8080/dbxRedirect.action")
				.build();
	}
	public static Token getAToken(OAuthService service, Verifier verifier) {
        return service.getAccessToken(null, verifier);
	}


	public static void listFiles(OAuthService service, Token accessToken) {
		OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.dropboxapi.com/2/files/list_folder", service);
		request.addHeader("authorization", "Bearer " + accessToken.getToken());
		request.addHeader("Content-Type",  "application/json");
		request.addPayload("{\n" +
				"    \"path\": \"\",\n" +
				"    \"recursive\": false,\n" +
				"    \"include_media_info\": false,\n" +
				"    \"include_deleted\": false,\n" +
				"    \"include_has_explicit_shared_members\": false,\n" +
				"    \"include_mounted_folders\": true\n" +
				"}");

		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println("HTTP RESPONSE: =============");
		System.out.println(response.getCode());
		System.out.println(response.getBody());
		System.out.println("END RESPONSE ===============");


		JSONObject rj = (JSONObject) JSONValue.parse(response.getBody());
		JSONArray contents = (JSONArray) rj.get("entries");
		for (int i=0; i<contents.size(); i++) {
			JSONObject item = (JSONObject) contents.get(i);
			String path = (String) item.get("name");
			System.out.println(" - " + path);
		}

	}
    public static String getUserMail(OAuthService service, Token accessToken){
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.dropboxapi.com/2/users/get_current_account", service);
        request.addHeader("authorization", "Bearer " + accessToken.getToken());
        request.addHeader("Content-Type",  "application/json");
        request.addPayload("null ");

        Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println("HTTP RESPONSE: =============");
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        System.out.println("END RESPONSE ===============");


        JSONObject rj = (JSONObject) JSONValue.parse(response.getBody());
        String mail = (String) rj.get("email");

        return mail;
    }
    public static String getFileId(OAuthService service, Token accessToken, String path){
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.dropboxapi.com/2/files/get_metadata", service);
        request.addHeader("authorization", "Bearer " + accessToken.getToken());
        request.addHeader("Content-Type",  "application/json");
        request.addPayload("{\"path\":\""+path+"\"}");

        Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println("HTTP RESPONSE: =============");
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        System.out.println("END RESPONSE ===============");

        JSONObject rj = (JSONObject) JSONValue.parse(response.getBody());
        String id = (String) rj.get("id");

        return id;
    }
    public static void shareWithEmail(OAuthService service, Token accessToken,String mail, String musica_id){
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.dropboxapi.com/2/sharing/add_file_member", service);
        request.addHeader("authorization", "Bearer " + accessToken.getToken());
        request.addHeader("Content-Type",  "application/json");
        request.addPayload("{\"file\":\""+musica_id+"\",\"members\":[{\".tag\":\"email\",\"email\":\""+mail+"\"}]}");

        Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println("HTTP RESPONSE: =============");
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        System.out.println("END RESPONSE ===============");
    }
    public static String getFilePathUsingID(OAuthService service, Token accessToken, String id){
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.dropboxapi.com/2/files/get_metadata", service);
        request.addHeader("authorization", "Bearer " + accessToken.getToken());
        request.addHeader("Content-Type",  "application/json");
        request.addPayload("{\"path\":\""+id+"\"}");

        Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println("HTTP RESPONSE: =============");
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        System.out.println("END RESPONSE ===============");

        JSONObject rj = (JSONObject) JSONValue.parse(response.getBody());
        String path = (String) rj.get("path_display");

        return path;
    }
	private static void addFile(String path, OAuthService service, Token accessToken) {
      // TODO
	}

	private static void deleteFile(String path, OAuthService service, Token accessToken) {
      // TODO
	}


}
