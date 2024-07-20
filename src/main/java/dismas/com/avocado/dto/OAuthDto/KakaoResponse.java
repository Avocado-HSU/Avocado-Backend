package dismas.com.avocado.dto.OAuthDto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response{
    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {

        return "kakao";
    }

    @Override
    public String getProviderId() {

        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {

        return "카카오는 별개의 앱 인증을 거친 후 가능합니다.";
    }

    @Override
    public String getName() {
        return (String) getProfile().get("nickname");
    }
    public String getImageUrl() {
        return (String)getProfile().get("profile_image_url");
    }

    public Map<String, Object> getKakaoAccount(){
        return(Map<String, Object>) attribute.get("kakao_account");
    }

    public Map<String, Object> getProfile(){
        return (Map<String, Object>) getKakaoAccount().get("profile");
    }
}
