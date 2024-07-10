package dismas.com.avocado.dto;

public interface OAuth2Response {
    //소셜 로그인 -> 구글,카카오,네이버 등
    String getProvider();
    //제공자에서 발급해주는 아이디(번호)
    String getProviderId();
    //이메일 -> 카카오는 email정보 획득을 위한 추가 작업필요
    String getEmail();
    //사용자 실명 -> 카카오는 우선 nickName으로 구현
    String getName();
}
