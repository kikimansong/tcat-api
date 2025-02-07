package isol.tcat_api.global.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CommonUtils {

    /**
     * JSON 문자열을 Map<String, List<Integer>> 형태로 변환
     * @param json JSON 문자열
     */
    public Map<String, List<Integer>> jsonToMap(String json) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, List<Integer>>>() {});
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    /**
     * 두 맵에서 겹치는 값 찾기
     */
    public Map<String, Set<Integer>> findSameKeyValue(Map<String, List<Integer>> map1, Map<String, List<Integer>> map2) {
        Map<String, Set<Integer>> commonValues = new HashMap<>();

        for (String key : map1.keySet()) {
            if (map2.containsKey(key)) {    // 같은 key인 경우에 비교
                Set<Integer> set1 = new HashSet<>(map1.get(key));
                Set<Integer> set2 = new HashSet<>(map2.get(key));

                set1.retainAll(set2);   // 교집합

                if (!set1.isEmpty()) {
                    commonValues.put(key, set1);
                }
            }
        }

        return commonValues;
    }

    /**
     * 이메일 마스킹 처리
     * EX. testmail@test.com -> test****
     */
    public String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }

        String[] splitEmail = email.split("@");
        String splitString = splitEmail[0];

        if (splitString.length() < 4) {
            splitString = splitString + "****";
        } else {
            splitString = splitString.substring(0, 4) + "****";
        }

        return splitString;
    }

}
