package dismas.com.avocado.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class DeepLService {
    private static final String API_URL = "https://api-free.deepl.com/v2/translate"; // url 요청 주소
    private String authKey; // DeepL api 키

    @Autowired
    public DeepLService(@Value("${deepl.auth.key}") String authKey) {
        this.authKey = authKey;
    }

    public String translateText(String text, String targetLang) throws Exception {

        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "DeepL-Auth-Key " + authKey);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        JSONObject requestBody = new JSONObject();
        requestBody.put("text", new String[]{text});
        requestBody.put("target_lang", targetLang);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.toString().getBytes("UTF-8"));
            os.flush();
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        JSONObject jsonResponse = new JSONObject(response.toString());
        String translatedText = jsonResponse
                .getJSONArray("translations")
                .getJSONObject(0)
                .getString("text");

        return translatedText;
    }

}
