package dismas.com.avocado.service;

import dismas.com.avocado.dto.WordDto;
import dismas.com.avocado.dto.parsingPage.WordEtymologyDto;
import dismas.com.avocado.dto.parsingPage.WordMeanDto;
import dismas.com.avocado.dto.parsingPage.WordSimilarDto;
import dismas.com.avocado.dto.parsingPage.WordTipsDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParsingService {

    public WordMeanDto parsingWordMean(String wordMean,String requestWord) {
        // WordMeanDto 객체 생성
        WordMeanDto dto = new WordMeanDto();

        // 빈 문자열 체크
        if (wordMean == null || wordMean.trim().isEmpty()) {
            dto.setGreetingMsg("검색 결과가 없습니다.");
            dto.setMeanings(new HashMap<>());
            return dto;
        }

        // 기본 greetingMsg 설정
        dto.setGreetingMsg(requestWord+"에 대한 검색 결과입니다");

        // 품사와 정의를 저장할 Map
        Map<String, String> meanings = new LinkedHashMap<>();

        // 각 품사 항목을 위한 정규 표현식 패턴 정의
        String[] parts = {"명사", "동사", "형용사", "부사"};
        for (String part : parts) {
            // 각 품사에 대한 패턴 생성
            String pattern = String.format("(?<=^%s:\\s*)(.*?)(?=\\n\\n|\\n$)", Pattern.quote(part));
            Pattern regexPattern = Pattern.compile(pattern, Pattern.DOTALL | Pattern.MULTILINE);
            Matcher matcher = regexPattern.matcher(wordMean);

            // 정의를 찾고 Map에 저장
            if (matcher.find()) {
                String definition = matcher.group(1).trim();
                meanings.put(part, definition.isEmpty() ? "" : definition);
            } else {
                // 정의가 없는 경우 빈 문자열로 설정
                meanings.put(part, "");
            }
        }

        dto.setMeanings(meanings);

        return dto;
    }

    public WordSimilarDto parsingWordSimilar(String wordSimilar) {
        // WordSimilarDto 객체 생성
        WordSimilarDto dto = new WordSimilarDto();

        // 인사 메시지와 유사 단어 부분을 분리
        String[] parts = wordSimilar.split("\\n\\n", 2); // 첫 번째 분리: 인사 메시지와 유사 단어 부분

        if (parts.length > 1) {
            // 인사 메시지 설정 -> 인사 메시지에 첫번쨰 유사 단어가 들어가는 형식
            dto.setGreetingMsg(parts[0].trim());

            // 유사 단어 부분 설정
            List<String> similarWords = extractSimilarWords(parts[1].trim());
            dto.setContents(similarWords);
        } else if (parts.length == 1) {
            // 인사 메시지만 있는 경우
            dto.setGreetingMsg(parts[0].trim());
            dto.setContents(new ArrayList<>());
        }

        return dto;
    }

    private List<String> extractSimilarWords(String response) {
        List<String> similarWords = new ArrayList<>();

        // 줄바꿈으로 각 항목을 분리
        String[] parts = response.split("\\n\\n");

        // 각 부분을 유사 단어로 처리
        for (String part : parts) {
            String trimmedPart = part.trim();
            if (!trimmedPart.isEmpty()) {
                similarWords.add(trimmedPart);
            }
        }

        return similarWords;
    }

    public WordTipsDto parsingWordTips(String wordTips) {
        // WordTipsDto 객체 생성
        WordTipsDto dto = new WordTipsDto();

        // 방법론들을 추출
        Map<String, String> methods = extractMethods(wordTips);

        // DTO 설정
        dto.setMnemonicMethod(methods.getOrDefault("연상 기억법", ""));
        dto.setWordAnalysis(methods.getOrDefault("단어 분해", ""));
        dto.setSpacedRepetition(methods.getOrDefault("반복 학습", ""));
        dto.setStorytelling(methods.getOrDefault("스토리텔링", ""));

        return dto;
    }

    private Map<String, String> extractMethods(String wordMean) {
        Map<String, String> methods = new HashMap<>();
        // 각 방법론의 시작을 정의
        String[] methodTitles = {"연상 기억법", "단어 분해", "반복 학습", "스토리텔링"};
        String[] parts = wordMean.split("\\n\\n"); // 각 항목을 분리

        String currentMethod = null;
        StringBuilder currentDescription = new StringBuilder();

        for (String part : parts) {
            String trimmedPart = part.trim();

            // 각 항목의 제목을 찾아서 처리
            boolean isTitle = false;
            for (String title : methodTitles) {
                if (trimmedPart.startsWith(title + ":")) {
                    // 현재 방법론이 있다면 저장
                    if (currentMethod != null) {
                        methods.put(currentMethod, currentDescription.toString().trim());
                    }
                    // 새로운 방법론 제목을 설정
                    currentMethod = title;
                    currentDescription = new StringBuilder(trimmedPart.substring(title.length() + 1).trim());
                    isTitle = true;
                    break;
                }
            }

            // 제목이 아닌 경우, 현재 방법론의 설명에 추가
            if (!isTitle && currentMethod != null) {
                currentDescription.append("\n\n").append(trimmedPart);
            }
        }

        // 마지막 방법론의 설명 저장
        if (currentMethod != null) {
            methods.put(currentMethod, currentDescription.toString().trim());
        }

        return methods;
    }


    public WordEtymologyDto parsingWordEtymology(String wordEtymology) {
        // WordEtymologyDto 객체 생성
        WordEtymologyDto dto = new WordEtymologyDto();

        // 빈 문자열 체크
        if (wordEtymology == null || wordEtymology.trim().isEmpty()) {
            dto.setEtymology("");
            dto.setRoot("");
            dto.setPrefix("");
            dto.setSuffix("");
            dto.setEtymologyDescription("");
            dto.setRootDescription("");
            dto.setPrefixDescription("");
            dto.setSuffixDescription("");
            return dto;
        }

        // 항목별 데이터 추출
        String etymology = extractSection(wordEtymology, "어원:");
        String root = extractSection(wordEtymology, "어근:");
        String prefix = extractSection(wordEtymology, "접두사:");
        String suffix = extractSection(wordEtymology, "접미사:");
        String description = extractDescription(wordEtymology);

        // 설명에서 각 세부 항목 추출
        String etymologyDescription = extractSubsection(description, "어원:");
        String rootDescription = extractSubsection(description, "어근:");
        String prefixDescription = extractSubsection(description, "접두사:");
        String suffixDescription = extractSubsection(description, "접미사:");

        // DTO에 설정
        dto.setEtymology(etymology.trim());
        dto.setRoot(root.trim());
        dto.setPrefix(prefix.trim());
        dto.setSuffix(suffix.trim());
        dto.setEtymologyDescription(etymologyDescription.trim());
        dto.setRootDescription(rootDescription.trim());
        // 없음으로 오는 예외 처리
        if (prefixDescription.trim().startsWith("없음")) {
            dto.setPrefixDescription("");
        }
        else {
            dto.setPrefixDescription(prefixDescription.trim());
        }
        if (suffixDescription.trim().startsWith("없음")) {
            dto.setSuffixDescription("");
        }
        else {
            dto.setSuffixDescription(suffixDescription.trim());
        }

        return dto;
    }

    private String extractSection(String text, String section) {
        int startIndex = text.indexOf(section);
        if (startIndex == -1) return "";

        startIndex += section.length();
        int endIndex = findNextSectionStart(text, startIndex);

        String value = text.substring(startIndex, endIndex).trim();
        return normalizeSectionValue(value);
    }
    // 우선적으로 어원: 어근: 접두사: 접미사: 설명에 대한 정보 처리
    private int findNextSectionStart(String text, int startIndex) {
        Pattern pattern = Pattern.compile("(?<=\\n|^)(어원:|어근:|접두사:|접미사:|설명\\n)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find(startIndex)) {
            return matcher.start();
        }
        return text.length();
    }
    // 설명 부분 처리
    private String extractDescription(String text) {
        Pattern pattern = Pattern.compile("(?<=설명\\n)(.*)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }
    //
    private String extractSubsection(String description, String subsection) {
        String patternString = String.format("(?<=%s\\s*)(.*?)(?=(?:\\n(?:어원:|어근:|접두사:|접미사:|$))|$)", Pattern.quote(subsection));
        Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    private String normalizeSectionValue(String value) {
        // 값이 "없음" 또는 비어 있는 경우 빈 문자열 반환
        if ("없음".equalsIgnoreCase(value) || value.isEmpty()) {
            return "";
        }
        // 값의 끝에 불필요한 공백이나 특수문자 제거
        return value.replaceAll("[\\s]*$", "").replaceAll("[:]*$", "");
    }


}
