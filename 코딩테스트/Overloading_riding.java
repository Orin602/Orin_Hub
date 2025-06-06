public class Overloading_riding {

    // 오버로딩 : 하나의 클래스 안에서 같은 이름의 메서드를 매개변수 타입, 개수, 순서 등을 다르게 해서 여러개로 재정의.
    class Calculator {
        int add(int a, int b) {
            return a+b;
        }
        double add(double a, double b) {
            return a+b;
        }
    }
    // 오버라이딩 : 부모 클래스에서 정의된 메서드를 자식 클래스에서 재정의.
    class Parent {
        void greet() {
            System.out.println("안녕 parent");
        }
    }
    class Child extends Parent {
        @Override
        void greet() {
            System.out.println("안녕 child");
        }
    }
    // 오버로딩은 다양한 입력에 대해 같은 기능을 제공, 오버라이딩은 부모 메서드의 동작을 변경하고 싶을 때 사용.
}
