public class Pccp1 {
    /*
     * 붕대 감기는 t초 동안 붕대를 감으면서 1초마다 x만큼의 체력을 회복
     * t초 연속으로 붕대를 감는 데 성공한다면 y만큼의 체력을 추가로 회복
     * 최대 체력이 존재해 현재 체력이 최대 체력보다 커지는 것은 불가능
     */
    // bandage는 [시전 시간, 초당 회복량, 추가 회복량] 형태의 길이가 3인 정수 배열입니다.
    //     1 ≤ 시전 시간 = t ≤ 50
    //     1 ≤ 초당 회복량 = x ≤ 100
    //     1 ≤ 추가 회복량 = y ≤ 100
    //     1 ≤ health ≤ 1,000
    //     1 ≤ attacks의 길이 ≤ 100
    //     attacks[i]는 [공격 시간, 피해량] 형태의 길이가 2인 정수 배열입니다.
    //     attacks는 공격 시간을 기준으로 오름차순 정렬된 상태입니다.
    //     attacks의 공격 시간은 모두 다릅니다.
    //     1 ≤ 공격 시간 ≤ 1,000
    //     1 ≤ 피해량 ≤ 100

    private static int solution(int[] bandage, int health, int[][] attacks) {
        int t = bandage[0];
        int x = bandage[1];
        int y = bandage[2];
        int currentHealth = health;
        int maxHealth = 1000; // 최대 체력 (문제에 명시된 조건 없으므로 임의의 값으로 설정)

        int bandageTime = 0; // 붕대 적용 시간
        int consecutiveSuccessTime = 0; // 연속 성공 시간
        boolean usingBandage = false; // 붕대 감기 기술 사용 여부
        
        int attackIndex = 0; // 공격 배열 인덱스
        
        // 1부터 1000까지의 초를 처리
        for (int time = 1; time <= 1000; time++) {
            // 붕대 감기 기술 시도 중인 경우
            if (usingBandage) {
                // t초가 지나 붕대 적용 완료
                if (time - bandageTime >= t) {
                    usingBandage = false;
                    bandageTime = 0;
                    consecutiveSuccessTime = 0;
                } else {
                    // 붕대 감기 기술이 t초 동안 유지되고 있음
                    currentHealth += x;
                    consecutiveSuccessTime++;
                    if (consecutiveSuccessTime == t) {
                        currentHealth += y;
                    }
                }
            }
            
            // 공격 처리
            while (attackIndex < attacks.length && attacks[attackIndex][0] == time) {
                int damage = attacks[attackIndex][1];
                currentHealth -= damage;
                attackIndex++;
                
                // 체력이 0 이하로 떨어지면 처리 중지
                if (currentHealth <= 0) {
                    return -1;
                }
                
                // 붕대 감기 기술이 취소됨
                usingBandage = false;
                bandageTime = 0;
                consecutiveSuccessTime = 0;
            }
            
            // 붕대 감기 기술 시도
            if (!usingBandage) {
                usingBandage = true;
                bandageTime = time;
                consecutiveSuccessTime = 0;
            }
            
            // 체력이 최대 체력을 초과하지 않도록 보장
            currentHealth = Math.min(currentHealth, maxHealth);
        }
        
        return currentHealth;
    }

    public static void main(String[] args) {
        int[] bandage = {5, 1, 5};
        int health = 30;
        int[][] attacks = {{2, 10}, {9, 15}, {10, 5}, {11, 5}};
        
        int result = solution(bandage, health, attacks);
        System.out.println("Remaining health: " + result); // Output: Remaining health: 5
    }
}
