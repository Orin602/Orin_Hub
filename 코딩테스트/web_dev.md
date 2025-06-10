// AJAX 예시와 설명
$.ajax({
    url : 'url',  // 요청을 보낼 서버의 URL(백엔드 Controller의 매핑 주소)
    method : 'GET',     // HTTP 메서드(GET, POST, DELETE, UPDATE 등)
    // type : 'GET', 'method'와 'type'은 동일함 (jQuery 1.9 이후부터 'method' 권장)
    data : {},  // 서버로 전송할 데이터(GET이면 쿼리파라미터로, POST면 body로 전송)
    success : function(response) {  // 요청이 성공했을 때 실행되는 콜백 함수
        console.log(response); 
    },
    error : function(error) {   // 요청이 실패했을 때 실행되는 콜백 함수
        console.log(error);
    }
});

//Fetch API 예시와 설명
fetch('url')  // 서버의 URL로 요청을 보냄(기본은 GET 요청)
    .then(response => response.json())  // 응답(response)을 JSON 객체로 변환 
    .then(data => {
        // JSON 데이터 사용
        console.log(data); // ex: [{id:1, name:'홍길동'}, {...}]
    })
    .catch(error => {   // 네트워크 에러 또는 요청 실패 처리
        console.log(error);
    });

// Fetch API POST 요청 예시
fetch('url', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},  // 보내는 데이터의 형식 명시
    body: JSON.stringify({  // JSON 데이터를 문자열로 변환 후 전송
        name: '홍길동',
        age: 99
    })
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.log(error));

// async / await로 Fetch 쓰기
async function fetchData() {
    try {
        const response = await fetch('url');
        const data = await response.json();
        console.log(data);
    } catch (error) {
        console.log(error);
    }
}


// ArrayList와 LinkedList의 차이, 어떤 경우에 어떤걸 선택할지
/*
ArrayList는 내부적으로 배열을 사용하고 인덱스로 요소에 빠르게 접근이 가능하지만,
중간 삽입 및 삭제 시 모든 요소를 이동시켜야 해 느리다.
LinkedList는 노드 기반 자료구조로 삽입 및삭제가 빠르지만, 인덱스로 접근할 때는 처음부터 
탐색해야 해서 느리다.
*/

/* 
int a = 10;
int b = 20;
int result = (a > b) ? a : b;
System.out.println(result);
삼항 연산자 조건식 ? 참일때 값 : 거짓일 때 값값
>> 20
*/

// Employee 테이블에서 부서별 평균 급여를 조회하되, 평균 급여가 3000 이상인 부서만 출력
/*
select d.department_name, avg(e.salary) as avg_sal from employee e
join department d on e.department_id = d.department_id
group by d.department_name having avg(e.salary) >= 3000;
*/

// 테이블 A(id, name), 테이블 B(id, grade), A와 B를 id로 조인해 grade가 A인 name들 조회
/*
select a.name from A a
join B b on a.id = b.id
where b.grade = 'A'
*/

// HTTP 요청 방식 중 안전하고, idempotent한 방식과 이유는?
/*
GET 방식, 같은 요청을 여러 번 보내도 결과가 같기 때문에 안전하고, idempotent하다.
(PUT/DELETE는 idempotent하지만 safe하지 않음)
*/

// Controller에서 사용자의 요청을 받아 Service와 연결하는 기본적인 코드 형태를 작성
/*
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
    private final EmployeeService employeeService;
    
    // 생성자 주입
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) {
        EmployeeDto dto = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto dto) {
        employeeService.saveEmployee(dto);
        return ResponseEntity.ok("등록 완료");
    }
}
*/


/*
1. JVM의 구조
JVM은 JAVA 프로그램을 실행하는 가상 머신으로 클래스로더 / 메모리 영역 / 실행 엔진으로 구성되어 있다.
메모리 영역은 메서드 영역, 힙, 스택 등으로 구성되어 있고, 클래스 로더는 클래스를 로드하고,
실행 엔진은 바이트코드를 기게어로 해석하거나 JIT 컴파일을 통해 실행한다.

2. 오버로딩과 오버라이딩의 차이
오버로딩은 같은 이름의 메서드를 매개변수만 다르게 해서 여러 개 정의하는 것이고, 컴파일 타임에 결정되지만,
오버라이딩은 부모 클래스의 메서드를 자식 클래스에서 재정의 하는 것으로, 런타임에 동작이 결정된다.
따라서 오버로딩은 같은 클래스 내에서 오버라이딩은 상속 관계에서 사용된다.

3. 자바에서 예외 처리 방식에는 어떤 것들이 있는지, try-catch-finally의 동작 순서
JAVA에서는 try-catch-finally 또는 throws 키워드를 통해 예외 처리를 하고,
try 블록에서 예외 발생 시 catch로 넘어가고, finally는 예외 발생 여부와 관계없이 
마지막에 항상 실행된다.

4. @RestController와 @Controller의 차이
Controller는 View를 반환할 떄 사용하고 JSP로 연결하지만,
RestController는 Controller + Responsebody 역할을 하고, JSON 형태로 데이터를 바로 반환한다.

5. 의존성 주입(DI)는 무엇인지, 어떤 방식이 있는지
DI는 객체가 직접 의존 객체를 생성하지 않고 외부에서 주입받는 방식이다.
Spring에서는 생성자 주입, 필드 주입, setter 주입 방식이 있고, 
생성자 주입이 불변성과 테스트 용이성 때문에 권장.

6. 스프링에서 Bean의 생명주기
Bean은 Spring Container에 의해 생성되고, 의존성 주입 후 초기화(init),사용 후 소멸(destroy).
초기화와 소멸 과정에서 @PostConstruct 와 @PreDestroy 또는 
InitializingBean, DisposableBean 인터페이스를 사용할 수 있다.

7. RDBMS와 NoSQL의 차이점은 (현재 나는 Oracle만 사용해봤음)
RDBMS는 정형화된 구조와 SQL 기반 관계형 데이터베이스, NoSQL은 비정형 데이터나 대용량 분산
처리에 적합한 비관계형 데이터베이스.
(Oracle은 RDBMS)

8. 트랜잭션의 4가지 성질(ACID)
트랜잭션은 데이터베이스의 하나의 작업 단위 4가지 성질이 있는데,
원자성(Atomicity) : 모두 실행되거나 실행되지 않음. 중간에 실패 시 rollback.
일관성(Consistency) : 트랜잭션 전/후 데이터의 일관성(무결성) 유지.
격리성(Isolation) : 동시에 여러 트랜잭션이 실행되도 서로 간섭하지 않음.
지속성(Durability) : 성공된 트랜잭션 결과는 영구 반영.

9. 쿠키와 세션의 차이
쿠키는 클라이언트에 저장, 세션은 서버에 저장된다.
쿠키는 사용자의 정보가 클라이언트(브라우저)에 노출되기 쉽고, 세션은 서버에서 관리하기에
보안이 더 높다.

10. RESTful API란
REST는 자원을 URI로 표현하고, HTTP 메서드(GET, POST, PUT, DELETE 등)로 행위를 명시하는 
아키텍처 스타일로, REST 원칙을 잘 지키는 API.

11. MVC 패턴의 흐름
사용자의 요청을 Controller가 받아서 Service를 호출하고, 결과를 Model에 담아서 View로 출력.
View는 Model 데이터를 기반으로 화면 출력.
Model은 데이터, View는 화면, Controller는 흐름을 제어한다.

12. 객체지향 프로그래밍(OOP)이 무엇인지, 어떤 특징이 있는지
OOP는 객체 단위로 설계하는 방식으로, 캡슐화, 상속, 다형성, 추상화의 특징이 있어 
유지보수와 확장성에 유리하다.
캡슐화(Encapsulation) : 데이터와 기능을 하나로 묶고, 외부에서 내부 구현을 감춤.
상속(Inheritance) : 부모 클래스의 속성과 메서드를 자식 클래스가 물려받음.
다형성(Polymorphism) : 하나의 메서드나 객체가 다양한 형태로 동작.
추상화(Abstraction) : 공통의 속성과 동작만 추출해 인터페이스나 추상 클래스로 표현.

13. 기본키(Primary Key), 외래키(Foreign Key), 제약조건(Unique)
기본키(Primary Key) : 테이블 내 유일하고 null이 아닌 값
외래키(Foreign Key) : 다른 테이블의 Primary Key를 참조
제약조건(Unique) : 중복되지 않는 값 허용, null 허용 가능

14. ORM이란
Object-Relational Mapping의 약자로, 객체와 테이블 간 매핑을 통해 SQL 없이 DB를 다루게 해준다. 대표적으로 JPA, Hibernate가 있다.

15. JPA란
Java의 ORM 으로, 개발자가 SQL을 작성하지 않아도 객체지향적으로 데이터베이스를 조작할 수 있다. Hibernate는 JPA의 구현체다.

16. Java에서 연산자 종류와 설명
산술 : 더하기(+), 빼기(-), 곱(*), 나누기(/(몫), %(나머지))
비교 : 같다(==), 같지 않다(!=), 크다(>), 작다(<), 크거나 같다(>=), 작거나 같다(<=)
논리 : AND(&& : 둘 다 True일 때 True), OR(|| : 둘 중 하나라도 True면 True), 
       NOT(! : 논리값 반전)
비트 연산 : 비트 단위로 연산하며, 정수형 자료에 사용
    %(AND : 둘 다 1이면 1), |(OR : 하나라도 1이면 1), ^(XOR : 다르면 1),
    ~(NOT : 반전 0 -> 1, 1 -> 0), <<(왼쪽 시프트 : 비트를 왼쪽으로 이동, 2배씩 곱 효과), 
    >>(오른쪽 시프트 : 부호를 유지하며 오른쪽 이동, 2로 나눈 효과),
    >>>(오른쪽 시프트 : 부호 무시, 0으로 채움)
대입 : (=), (+=), ...
삼항 연산자 : 조건식 ? 참일 때 값 : 거짓일 때 값

17. const, var, let의 차이와 설명
var : 함수 스코프(함수를 벗어나면 사용 불가), 중복 선언이 가능
let : 블록 스코프(블록을 벗어나면 사용 불가), 중복 선언이 불가능
const : 블록 스코프(블록을 벗어나면 사용 불가), 상수 선언 시 사용

18. private는 무너지, 다른 접근 제어자는 어떤게 있는지
private는 클래스 내부에서만 접근 가능한 제어자.
다른 제어자는 public, protexted, (default)가 있고, 각각의 접근 범위는
클래스, 패키지, 상속 관계에 따라 달라진다.

19. static 변수와 메서드는 언제 사용하는지

20. 추상 클래스와 인터페이스의 차이

21. 컬렉션 프레임워크의 종류와 특징

22. Spring의 주요 핵심 기능 3가지 (DI, AOP, IoC)

23. AOP란 뭔지, 언제 사용하는지

24. Spring Boot의 장점

25. 정규화와 비정규화란

26. get과 post의 차이

27. CORS란

28. JWT란 뭔지, 세션과의 차이


*/

/* 기술 테스트

1. 문자열인지 확인하는 함수 구현하기

2. 배열에서 중복 제거 후 정렬하기

3. 상속과 오버라이딩 활용 예제 구현하기

4. 급여가 평균보다 높은 사원 정보 출력

5. 사원 수가 5명 이상인 부서만 출력

6. 동물 클래스를 상속받은 고양이 클래스 구현하기

*/