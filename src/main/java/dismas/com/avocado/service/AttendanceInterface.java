package dismas.com.avocado.service;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.domain.attendance.Attendance;
import dismas.com.avocado.domain.attendance.Date;
import dismas.com.avocado.dto.WeekDTO;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceInterface {
   void AttendanceChack(Member member, LocalDate date);

   WeekDTO ShowAttendance(Member member);

   void UpdateDate(LocalDate date, String eventName, String eventDescription, String providePoint);

   void Month();

   void EveryMonth();
}
