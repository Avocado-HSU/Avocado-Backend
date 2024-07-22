package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.attendance.Attendance;
import dismas.com.avocado.domain.attendance.Day;
import dismas.com.avocado.dto.mainPage.WeeklyAttendanceDto;
import dismas.com.avocado.mapper.MainPageMapper;
import dismas.com.avocado.repository.attendance.AttendanceRepository;
import dismas.com.avocado.repository.attendance.DayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final DayRepository dayRepository;
    private final MemberService memberService;

    private final MainPageMapper mainPageMapper;
//
//    /** 클라이언트로부터 받은 TimeStamp로 출석 검증, 검증 결과 출석이 되있지 않으면 출석체크 함.*/
//    @Override
//    public void AttendanceChack(Member member, LocalDate date){
//        Attendance toDay = attendanceRepository.findToDay(member, date);
//        if(toDay == null){
//            Date day = dateRepository.findByDate(date);
//            if(day == null) Month();
//            Attendance attendance = Attendance.builder().member(member).date(day).build();
//            attendanceRepository.save(attendance);
//        }
//    }
//
//    /** 출석을 일주일 단위로 보여줌 */
//    @Override
//    public WeekDTO ShowAttendance(Member member) {
//        int weeks=0; Date monday=null;
//        List<Date> month = dateRepository.findAll();
//        for(Date date : month)
//            if(date.getDate().getDayOfWeek().getValue() == 1 && date.getDate().getDayOfMonth() <= LocalDate.now().getDayOfMonth()) {
//                monday = date;
//                weeks++;
//            }
//        List<Attendance> attendances = attendanceRepository.findByWeek(member, monday.getDate(), monday.getDate().plusDays(6));
//        return new WeekDTO(weeks, attendances);
//    }
//
//    /** date 업데이트 */
//    @Override
//    public void UpdateDate(LocalDate date, String eventName, String eventDescription, String providePoint) {
//        Date day = dateRepository.findByDate(date);
//        day = Date.builder()
//                .id(day.getId())
//                .date(day.getDate())
//                .eventName(eventName)
//                .eventDescription(eventDescription)
//                .providePoint(providePoint)
//                .build();
//        dateRepository.save(day);
//
//
//    }
//
//    /** Date를 한달로 생성 */
//    @Override
//    public void Month() {
//        List<Date> month = new ArrayList<>();
//        LocalDate now = LocalDate.now();
//        int year = LocalDate.now().getYear();
//        int day = 0;
//
//        if(now.getMonth().getValue() == 2){
//            if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0 ) day=29;
//            else day=28;}
//        else if(now.getMonth().getValue() % 2 == 1) day=31;
//        else day=30;
//
//        for (int i =1;i<=day;i++){
//            Date date = Date.builder()
//                    .date(LocalDate.of(now.getYear(), now.getMonth().getValue(), i))
//                    .providePoint("50")
//                    .build();
//
//            month.add(date);
//        }
//        dateRepository.saveAll(month);
//
//    }
//
//    /** 매달 1일마다 자동으로 Date를 생성 */
//    @Override
//    @Scheduled(cron = "0 0 0 1 * *")
//    public void EveryMonth() {
//        Month();
//    }

    /**
     * Check Member Attendance (출석 체크 서비스)
     * LoginController에서 호출 필요
     *
     * @param member 사용자 ID
     * @param date 클라이언트 일자
     * @throws IllegalArgumentException 클라이언트 TimeStamp와 서버 TimeStamp 불일치
     * @throws RuntimeException createMonthDay 혹은 메서드 에러 - 추후 따로 에러 객체 설계할 것
     */
    public void checkAttendance(Member member, LocalDate date){

        // 서버 시간과 클라이언트 시간 검증
        if(!Objects.equals(date, LocalDate.now())){
            throw new IllegalArgumentException("클라이언트 TimeStamp 그리고 서버 TimeStamp 불일치");
        }


        if(attendanceRepository.findAttendanceByDate(member, date).isEmpty()){
            // 당일 출석케크가 진행되지 아니한 경우
            Optional<Day> dayOptional = dayRepository.findByDate(date);

            memberService.updatePoint(member, 100L);
            if(dayOptional.isEmpty())
            {
                // 날짜가 존재하지 아니한 경우
                createMonthDay();
                dayOptional = dayRepository.findByDate(date);
            }
            Day day = dayOptional.orElseThrow(() -> new RuntimeException("일자 조회 오류"));
            attendanceRepository.save(
                    Attendance.builder()
                            .member(member)
                            .day(day)
                            .build()
            );
        }
    }

    /**
     * Get Weekly Attendance Service (해당 주간 출석 내역 반환 서비스)
     * MainPage 컨트롤러에서 호출 필요
     *
     * @param member 사용자
     * @param date 클라이언트 TimeStamp
     * @return WeeklyAttendanceDto 월요일부터 일요일까지 출석여부를 boolean 타입 List로 반환
     */
    public WeeklyAttendanceDto getWeeklyAttendance(Member member, LocalDate date) {

        // 서버 시간과 클라이언트 시간 검증
        if(!Objects.equals(date, LocalDate.now())){
            throw new IllegalArgumentException("클라이언트 TimeStamp 그리고 서버 TimeStamp 불일치");
        }

        LocalDate monday = date.with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);

        List<Attendance> thisWeekly = attendanceRepository.findWeeklyAttendanceByMember(member, monday, sunday);

        return mainPageMapper.toAttendanceDto(thisWeekly);
    }


    /**
     * create Day Service ( 일정 생성 서비스 )
     * 매월 1일 Spring Sceduler를 통해 해당 메서드 수행
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void createMonthDay(){

        LocalDate today = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(today.getYear(), today.getMonth());
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
        // 이미 생성되어 있을 경우 별도 예외 처리 필요
        List<Day> days = new ArrayList<>();
        LocalDate pointerDate = firstDayOfMonth;
        while(!pointerDate.isAfter(lastDayOfMonth)){
            days.add(
                    Day.builder()
                            .date(pointerDate)
                            .providePoint(100L)
                            .build()
            );
            pointerDate = pointerDate.plusDays(1);
        }
        dayRepository.saveAll(days);
    }
}
