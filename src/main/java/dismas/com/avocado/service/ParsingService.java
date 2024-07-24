package dismas.com.avocado.service;

import dismas.com.avocado.dto.WordDto;
import dismas.com.avocado.dto.parsingPage.WordEtymologyDto;
import dismas.com.avocado.dto.parsingPage.WordMeanDto;
import dismas.com.avocado.dto.parsingPage.WordSimilarDto;
import dismas.com.avocado.dto.parsingPage.WordTipsDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParsingService {

    public WordMeanDto parsingWordMean(String wordMean) {
        // WordMeanDto 객체 생성
        WordMeanDto dto = new WordMeanDto();

        // 인사 메시지와 정의 부분을 분리
        String[] parts = wordMean.split("\\n\\n", 2); // 첫 번째 분리: 인사 메시지와 정의 부분

        if (parts.length > 1) {
            // 인사 메시지 설정
            dto.setGreetingMsg(parts[0].trim());

            // 정의 부분 설정
            Map<String, String> meanings = extractMeanings(parts[1].trim());
            dto.setMeanings(meanings);
        } else if (parts.length == 1) {
            // 인사 메시지만 있는 경우
            dto.setGreetingMsg(parts[0].trim());
            dto.setMeanings(new HashMap<>());
        }

        return dto;
    }

    // 품사별 정의 분리
    private Map<String, String> extractMeanings(String response) {
        Map<String, String> meanings = new HashMap<>();

        // 각 품사 항목을 구분하기 위해 줄바꿈으로 분리
        String[] parts = response.split("\\n\\n");

        for (String part : parts) {
            String trimmedPart = part.trim();
            if (trimmedPart.isEmpty()) continue;

            // 품사와 설명을 구분
            String[] keyValue = trimmedPart.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                meanings.put(key, value);
            }
        }

        return meanings;
    }


    public WordSimilarDto parsingWordSimilar(String wordSimilar) {
        // WordSimilarDto 객체 생성
        WordSimilarDto dto = new WordSimilarDto();

        // 인사 메시지와 유사 단어 부분을 분리
        String[] parts = wordSimilar.split("\\n\\n", 2); // 첫 번째 분리: 인사 메시지와 유사 단어 부분

        if (parts.length > 1) {
            // 인사 메시지 설정
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

        // 각 항목의 내용을 저장할 변수
        String etymology = "";
        String root = "";
        String prefix = "";
        String suffix = "";
        String etymologyDescription = "";
        String rootDescription = "";
        String prefixDescription = "";
        String suffixDescription = "";

        // 데이터 항목별로 분리
        String[] parts = wordEtymology.split("\\n\\n");

        // 항목별 내용 추출
        for (String part : parts) {
            if (part.startsWith("어원:")) {
                etymology = part.replace("어원:", "").trim();
            } else if (part.startsWith("어근:")) {
                root = part.replace("어근:", "").trim();
            } else if (part.startsWith("접두사:")) {
                prefix = part.replace("접두사:", "").trim();
            } else if (part.startsWith("접미사:")) {
                suffix = part.replace("접미사:", "").trim();
            } else if (part.startsWith("설명")) {
                String[] descriptionParts = part.split("\\n");
                for (String desc : descriptionParts) {
                    if (desc.startsWith("어원:")) {
                        etymologyDescription = desc.replace("어원:", "").trim();
                    } else if (desc.startsWith("어근:")) {
                        rootDescription = desc.replace("어근:", "").trim();
                    } else if (desc.startsWith("접두사:")) {
                        prefixDescription = desc.replace("접두사:", "").trim();
                    } else if (desc.startsWith("접미사:")) {
                        suffixDescription = desc.replace("접미사:", "").trim();
                    }
                }
            }
        }

        // DTO에 값 설정
        dto.setEtymology(etymology);
        dto.setRoot(root);
        dto.setPrefix(prefix);
        dto.setSuffix(suffix);
        dto.setEtymologyDescription(etymologyDescription);
        dto.setRootDescription(rootDescription);
        dto.setPrefixDescription(prefixDescription);
        dto.setSuffixDescription(suffixDescription);

        return dto;
    }















}
