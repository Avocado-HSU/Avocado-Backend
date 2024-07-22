package dismas.com.avocado.domain.attendance;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

/**
 * 일정 엔티티 정의
 * 일정에 대한 내용을 저장, 관리한다.
 *
 * @version 1.0
 * @since 2024-07-08
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Jacksonized
@Getter
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_id")
    private Long id;

    private LocalDate date;

    // 특정 휴일 및 이벤트 일정 설정
    private String eventName;
    private String eventDescription;

    // 제공할 포인트
    private Long providePoint;
}
