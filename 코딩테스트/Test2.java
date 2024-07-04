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

    public static long solution(int a, int b) {
        if (a == b) {   // 조건1
            return a ;
        }
        // 조건3
        int start = Math.min(a, b); // a,b중 작은 값
        int end = Math.max(a, b);   // a,b중 큰 값
        
        long answer = 0;
        for (int x = start; x <= end; x++) {
            answer += x;
        }
        
        return answer;
    }
    
    public static void main(String[] args) {
        int a = 5;
        int b = 3;
        long result = solution(a, b);
        System.out.println("a : " + a + ", b : " + b + " 일 때 return 값은 : " + result);

    }
}
