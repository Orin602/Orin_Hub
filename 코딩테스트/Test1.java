
/* 몫 구하기 */

import java.util.Scanner;

public class Test1 {
    /*
     * 정수 num1, num2가 매개변수로 주어지고, 
     * num1을 num2로 +, -, *, / 연산의 값을 return하는 solution1,2,3,4 함수 작성
     */
    // num1 + num2
    // public static int solution1(int num1, int num2) {
    //     int result = 0;
    //     result = num1 + num2;
    //     return result;
    // }
    // public static void main(String[] args) {
    //     int num1 = 10;
    //     int num2 = 5;
    //     int result = solution(num1, num2);
    //     System.out.println(num1 + " + " + num2 + " = " + result);
    // }

    // num1 - num2
    // public static int solution2(int num1, int num2) {
    //     int result = 0;
    //     result = num1 - num2;
    //     return result;
    // }
    // public static void main(String[] args) {
    //     int num1 =10;
    //     int num2 = 5;
    //     int result = solution2(num1, num2);
    //     System.out.print(num1 + " - " + num2 + " = " + result);
    // }

    // num1 * num2
    // public static int solution3(int num1, int num2) {
    //     int result = 0;
    //     result = num1 * num2;
    //     return result;
    // }
    // public static void main(Stirng[] args) {
    //     int num1 = 10;
    //     int num2 = 5;
    //     int result = solution3(num1, num2);
    //     System.out.print(num1 + " * " + num2 + " = " + result);
    // }

    // num1 / num2
    // public static int solution4(int num1, int num2) {
    //     int result = 0;
    //     result = num1 / num2;
    //     return result;
    // }
    // public static void main(String[] args) {
    //     int num1 = 10;
    //     int num2 = 5;
    //     int result = solution4(num1, num2);
    //     System.out.print(num1 + " / " + num2 + " = " + result);
    // }

    public static int solution(int num1, int num2) {
        if (num2 == 0) {
            throw new IllegalArgumentException("0으로 나눌 수 없습니다.");
        }
        return num1 / num2;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("첫 번째 숫자를 입력하세요: ");
        int num1 = scanner.nextInt();

        System.out.print("두 번째 숫자를 입력하세요: ");
        int num2 = scanner.nextInt();

        try {
            int result = solution(num1, num2);
            System.out.println(num1 + " / " + num2 + " = " + result);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        scanner.close();
    }
}