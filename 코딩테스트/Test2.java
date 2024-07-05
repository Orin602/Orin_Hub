import java.util.Arrays;

public class Test2 {
    /*
     * 두 정수 a, b가 주어졌을 때 a와 b 사이에 속한 모든 정수의 합을
     * 리턴하는 함수, solution을 완성하세요.
     * 예를 들어 a = 3, b = 5인 경우, 3 + 4 + 5 = 12이므로 12를 리턴합니다.

     * 조건
     * 1. a,b가 같은 경우 둘 중 아무 수나 리턴.
     * 2. a와 b는 -10000000 이상, 10000000이하인 정수.
     * 3. a와 b의 대소관계는 정해져있지 않음.
     */ 

    // public static long solution(int a, int b) {
    //     if (a == b) {   // 조건1
    //         return a ;
    //     }
    //     // 조건3
    //     int start = Math.min(a, b); // a,b중 작은 값
    //     int end = Math.max(a, b);   // a,b중 큰 값
        
    //     long answer = 0;
    //     for (int x = start; x <= end; x++) {
    //         answer += x;
    //     }
        
    //     return answer;
    // }
    
    // public static void main(String[] args) {
    //     int a = 5;
    //     int b = 3;
    //     long result = solution(a, b);
    //     System.out.println("a : " + a + ", b : " + b + " 일 때 return 값은 : " + result);

    // }


    /*
     * 배열 array의 i번째 숫자부터 j번째 숫자까지 자르고 정렬했을 때,
     * k번째에 있는 수를 구하려 합니다.

     * 예를 들어 array가 [1, 5, 2, 6, 3, 7, 4], i = 2, j = 5, k = 3이라면
     * array의 2번째부터 5번째까지 자르면 [5, 2, 6, 3]입니다.
     * 1에서 나온 배열을 정렬하면 [2, 3, 5, 6]입니다.
     * 2에서 나온 배열의 3번째 숫자는 5입니다.
     */

    public int[] solution(int[] array, int[][] commands) {
        int[] answer = new int[commands.length];
        int[] result = {};
        int i=0, j=0, k=0;

        for (int idx = 0; idx < commands.length; idx++) {
            i = commands[idx][0];
            j = commands[idx][1];
            k = commands[idx][2];
            
            int[] subArray = Arrays.copyOfRange(array, i-1, j); // i-1 부터 j 까지 추출
            Arrays.sort(subArray);  // 추출된 배열 정렬
            answer[idx] = subArray[k-1]; // k번째 요소 추출(index는 0부터 시작이므로 -1)
        }

        return answer;
    }
    
    public static void main(String[] args) {
        Test2 sol = new Test2();
        int[] array = {1, 5, 2, 6, 3, 7, 4};
        int [][] commands = {
            {2, 5, 3}, {4, 4, 1}, {1, 7, 3}
        };
        int[] result = sol.solution(array, commands);
        System.out.println(Arrays.toString(result));
    }
}
