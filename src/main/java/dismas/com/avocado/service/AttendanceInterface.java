package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.dto.WeekDTO;

import java.time.LocalDate;

public interface AttendanceInterface {
   void AttendanceChack(Member member, LocalDate date);

   WeekDTO ShowAttendance(Member member);

   void UpdateDate(LocalDate date, String eventName, String eventDescription, String providePoint);

   void Month();

   void EveryMonth();
}
