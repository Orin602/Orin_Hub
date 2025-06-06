public class ExceptionEX {
    public static void main(String[] args) {
        try { // 첫 번째 예외 처리 : 0으로 나누기기
            int a = 5 / 0;
        } catch(ArithmeticException e) {
            System.out.println("예외 발생 : " + e.getMessage());
        } finally {
            System.out.println("무조건 실행 : finally");
        }

        try { // 두 번째 예외 처리 : riskyMethod 호출
            riskyMethod();
        } catch (Exception e) {
            System.out.println("riskyMethod 예외 발생 : " + e.getMessage());
        }
    }

    static void riskyMethod() throws Exception {
        throw new Exception("예외 발생 -> riskyMethod");
    }
}

/* 
    5 / 0 ==> ArithmeticException 발생 -> catch 블록 실행 후 finally 무조건 실행.
    riskyMethod() -> 강제로 예외 발생 -> throws 되어 올라온 예외를 main()에서 try-catch로 처리
*/