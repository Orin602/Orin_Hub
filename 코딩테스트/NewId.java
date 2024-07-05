import java.util.Scanner;

public class NewId {
    /*
     * 아이디 규칙에 맞지 않는 아이디를 입력했을 때,
     * 입력된 아이디와 유사하면서 규칙에 맞는 아이디를 추천해주는 프로그램

     * 규칙 :
     * 1. 아이디의 길이는 3자 이상 15자 이하여야 합니다.
     * 2. 아이디는 알파벳 소문자, 숫자, 빼기(-), 밑줄(_), 마침표(.) 문자만 사용할 수 있습니다.
     * 3. 마침표(.)는 처음과 끝에 사용할 수 없으며 또한 연속으로 사용할 수 없습니다.
     */


    // 1단계 new_id의 모든 대문자를 대응되는 소문자로 치환합니다.
    // 2단계 new_id에서 알파벳 소문자, 숫자, 빼기(-), 밑줄(_), 마침표(.)를
    // 제외한 모든 문자를 제거합니다.
    // 3단계 new_id에서 마침표(.)가 2번 이상 연속된 부분을 하나의 마침표(.)로 치환합니다.
    // 4단계 new_id에서 마침표(.)가 처음이나 끝에 위치한다면 제거합니다.
    // 5단계 new_id가 빈 문자열이라면, new_id에 "a"를 대입합니다.
    // 6단계 new_id의 길이가 16자 이상이면, new_id의 첫 15개의 문자를 제외한 나머지
    //  문자들을 모두 제거합니다. 만약 제거 후 마침표(.)가 new_id의 끝에 위치한다면
    //  끝에 위치한 마침표(.) 문자를 제거합니다.
    // 7단계 new_id의 길이가 2자 이하라면, new_id의 마지막 문자를 new_id의 길이가
    //  3이 될 때까지 반복해서 끝에 붙입니다.

    public static String solution(String new_id) {
        // 1단계(대문자를 소문자로 치환)
        new_id = new_id.toLowerCase();
        // 2단계(소문자, 숫자, -_. 뺴기)
        new_id = new_id.replaceAll("[^a-z0-9-_.]", "");
        // 3단계(. 2개 이상을 .로 치환)
        new_id = new_id.replaceAll("[.]{2,}", ".");
        // 4단계(.가 처음이나 끝에 있으면 삭제)
        new_id = new_id.replaceAll("^[.]|[.]$", "");
        // 5단계(빈 문자영일 경우 a를 대입)
        if(new_id.isEmpty()) {
            new_id = "a";
        }
        // 6단계(16문자 이상이면 처음 15문자 이후는 삭제, 
        //  삭제 후 처음 또는 마지막에 . 가 들어가면 삭제)
        if(new_id.length() >= 16) {
            new_id = new_id.substring(0, 15);
            // 4단계와 동일
            new_id = new_id.replaceAll("^[.]|[.]$", "");
        }
        // 7단계(길이가 2자 이하라면 마지막 문자를 길이가 3이 될 때까지 반복)
        while (new_id.length() <= 2) {
            new_id += new_id.charAt(new_id.length() -1);
        }
        
        return new_id;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("새로운 아이디를 입력하세요.");
        String new_id = scan.nextLine();
        String result = solution(new_id);
        System.out.println("추천 ID : " + result);
    }
}
