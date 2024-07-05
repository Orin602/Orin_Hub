import java.util.*;

public class Test4 {
    /* 배열 : 중복 숫자 제거
     * 배열 arr의 각 원소는 숫자 0부터 9까지로 이루어져 있습니다.
     * 이때, 배열 arr에서 연속적으로 나타나는 숫자는 하나만 남기고 전부 제거
     * 남은 수들을 반환할 때는 배열 arr의 원소들의 순서를 유지
     */
    
     public static int[] solution(int[] arr) {
        // 중복 제거한 숫자를 저장할 리스트
        List<Integer> resultList = new ArrayList<>();
        int prev = arr[0];  // 첫 번째 숫자 저장
        resultList.add(prev);

        for(int i = 1; i < arr.length; i++) {   //중복 제거 반복문
            if(arr[i] != prev) {
                resultList.add(arr[i]);
                prev = arr[i];
            }
        }
        // 중복 제거한 숫자의 리스트를 배열로 변환
        int[] result = new int[resultList.size()];
        for(int i = 0; i < resultList.size(); i++) {
            result[i] = resultList.get(i);
        }
        return result;
     }

     public static void main(String[] args) {
        int[] arr = {4,4,4,3,3};
        int[] result = solution(arr);

        System.out.println(Arrays.toString(result));
     }
}
