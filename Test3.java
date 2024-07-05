import java.util.Scanner;

public class Test3 {
    /* 행렬의 덧셈
     * 행렬의 덧셈은 행과 열의 크기가 같은 두 행렬의 같은 행, 같은 열의 값을 서로 더한 결과가 됩니다. 2개의 행렬 arr1과 arr2를 입력받아, 
     * 행렬 덧셈의 결과를 반환하는 함수, solution을 완성해주세요.
     */
    public static int[][] solution(int[][] arr1, int[][] arr2) {
        int rows = arr1.length;
        int cols = arr1[0].length;
        int[][] answer = new int[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                answer[i][j] = arr1[i][j] + arr2[i][j];
            }
        }
        return answer;
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("행렬의 행과 열의 수를 입력하세요:");
        int rows = scan.nextInt();
        int cols = scan.nextInt();

        int[][] arr1 = new int[rows][cols];
        int[][] arr2 = new int[rows][cols];

        System.out.println("첫 번째 행렬의 요소를 입력하세요 : ");
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                arr1[i][j] = scan.nextInt();
            }
        }

        System.out.println("두 번째 행렬의 요소를 입력하세요 : ");
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                arr2[i][j] = scan.nextInt();
            }
        }

        int[][] answer = solution(arr1, arr2);
        System.out.println("결과 행렬 : ");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(answer[i][j] + " ");
            }
            System.out.println();
        }
    }
}
