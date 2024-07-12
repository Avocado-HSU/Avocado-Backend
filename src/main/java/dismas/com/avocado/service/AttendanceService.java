package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.attendance.Attendance;
import dismas.com.avocado.domain.attendance.Date;
import dismas.com.avocado.dto.WeekDTO;
import dismas.com.avocado.repository.MemberRepository;
import dismas.com.avocado.repository.attendance.AttendanceRepository;
import dismas.com.avocado.repository.attendance.DateRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService implements AttendanceInterface {

    private final AttendanceRepository attendanceRepository;

    private final DateRepository dateRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, DateRepository dateRepository) {
        this.attendanceRepository = attendanceRepository;
        this.dateRepository = dateRepository;
    }


    /** 클라이언트로부터 받은 TimeStamp로 출석 검증, 검증 결과 출석이 되있지 않으면 출석체크 함.*/
    @Override
    public void AttendanceChack(Member member, LocalDate date){
    Attendance toDay = attendanceRepository.findToDay(member, date);
        if(toDay == null){
            Date day = dateRepository.findByDate(date);
            if(day == null) Month();
            Attendance attendance = Attendance.builder().member(member).date(day).build();
            attendanceRepository.save(attendance);
        }
    }

    /** 출석을 일주일 단위로 보여줌 */
    @Override
    public WeekDTO ShowAttendance(Member member) {
        int weeks=0; Date monday=null;
        List<Date> month = dateRepository.findAll();
        for(Date date : month)
            if(date.getDate().getDayOfWeek().getValue() == 1 && date.getDate().getDayOfMonth() <= LocalDate.now().getDayOfMonth()) {
                monday = date;
                weeks++;
            }
        List<Attendance> attendances = attendanceRepository.findByWeek(member, monday.getDate(), monday.getDate().plusDays(6));
        return new WeekDTO(weeks, attendances);
    }

    /** date 업데이트 */
    @Override
    public void UpdateDate(LocalDate date, String eventName, String eventDescription, String providePoint) {
       Date day = dateRepository.findByDate(date);
       day = Date.builder()
               .id(day.getId())
               .date(day.getDate())
               .eventName(eventName)
               .eventDescription(eventDescription)
               .providePoint(providePoint)
               .build();
       dateRepository.save(day);


    }

    /** Date를 한달로 생성 */
    @Override
    public void Month() {
        List<Date> month = new ArrayList<>();
        LocalDate now = LocalDate.now();
        int year = LocalDate.now().getYear();
        int day = 0;

        if(now.getMonth().getValue() == 2){
            if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0 ) day=29;
            else day=28;}
        else if(now.getMonth().getValue() % 2 == 1) day=31;
        else day=30;

        for (int i =1;i<=day;i++){
            Date date = Date.builder()
                    .date(LocalDate.of(now.getYear(), now.getMonth().getValue(), i))
                    .providePoint("50")
                    .build();

            month.add(date);
        }
        dateRepository.saveAll(month);

    }

    /** 매달 1일마다 자동으로 Date를 생성 */
    @Override
    @Scheduled(cron = "0 0 0 1 * *")
    public void EveryMonth() {
        Month();
    }


}
