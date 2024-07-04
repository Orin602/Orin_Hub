import java.util.Scanner;

public class Pcce {
    /* PCCE 기출 1번 / 출력
     * 주어진 초기 코드는 변수에 데이터를 저장하고 출력하는 코드입니다. 
     * 아래와 같이 출력되도록 빈칸을 채워 코드를 완성해 주세요
     * Spring is beginning
     * 13
     * 310
     */
    // public static void main(String[] args) {
    //     String msg = "";    // Spring is beginning
    //     int val1 = 0;       // 3
    //     String val2 = "";   // 3

    //     System.out.println(msg);
    //     System.out.println(val1 + 10);
    //     System.out.println(val2 + "10");
    // }

    /* PCCE 기출 2번 / 피타고라스의 정리
     * 직각삼각형의 한 변의 길이를 나타내는 정수 a와 빗변의 길이를 나타내는 정수 c가 주어질 때,
     * 다른 한 변의 길이의 제곱, b_square 을 출력하도록 한 줄을 수정해 코드를 완성해 주세요.
     */
    // public static void main(String[] args) {
    //     Scanner sc = new Scanner(System.in);
    //     int a = sc.nextInt();
    //     int c = sc.nextInt();

    //     int b_square = c - a; >> int b_square = c*c - a*a;

    //     System.out.println(b_square);
    // }

    /* PCCE 기출 3번 / 나이 계산
     * 한국식 나이 : 현재 연도 - 출생 연도 + 1
     * 연 나이 : 현재 연도 - 출생 연도
     * 
     * 출생 연도를 나타내는 정수 year와 구하려는 나이의 종류를 나타내는 문자열 age_type이 주어질 때
     * 2030년에 몇 살인지 출력하도록 빈칸을 채워 코드를 완성해 주세요.
     * age_type이 "Korea"라면 한국식 나이를, "Year"라면 연 나이를 출력합니다.
     */
    // public static void main(String[] args) {
    //     Scanner sc = new Scanner(System.in);
    //     int year = sc.nextInt();
    //     String age_type = sc.next();
    //     int answer = 0;

    //     if (age_type.equals("Korea")) {
    //         answer = 2030 - year + 1;
    //     }
    //     else if (age_type.equals("Year")) {
    //         answer = 2030 - year;
    //     }

    //     System.out.println(answer);
    //     sc.close();
    // }

    /* PCCE 기출 4번 / 저축
     * 첫 달에 저축하는 금액을 나타내는 정수 start,
     * 두 번째 달 부터 70만 원 이상 모일 때까지 매월 저축하는 금액을 나타내는 정수 before,
     * 100만 원 이상 모일 때 까지 매월 저축하는 금액을 나타내는 정수 after가 주어질 때,
     * 100만 원 이상을 모을 때까지 걸리는 개월 수를 출력하도록 빈칸을 채워 코드를 완성해 주세요.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int start = sc.nextInt();
        int before = sc.nextInt();
        int after = sc.nextInt();

        int money = start;
        int month = 1;
        while (money < 70) {
            money += before;
            month++;
        }
        while (money < 100) {
            money += after;
            month++;
        }

        System.out.println(month);
        sc.close();
    }
}


