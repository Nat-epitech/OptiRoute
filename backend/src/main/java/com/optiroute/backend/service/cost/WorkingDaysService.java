package com.optiroute.backend.service.cost;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.stereotype.Service;

// TODO: Rajouter les jours fériés francais
@Service
public class WorkingDaysService {

    public int getWorkingDaysInYear(int year) {

        int count = 0;

        LocalDate date = LocalDate.of(year,1,1);
        LocalDate end = LocalDate.of(year,12,31);

        while (!date.isAfter(end)) {

            DayOfWeek day = date.getDayOfWeek();

            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                count++;
            }

            date = date.plusDays(1);
        }

        return count;
    }

    public int getWorkingDaysInMonth(YearMonth month) {

        int count = 0;

        LocalDate date = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        while (!date.isAfter(end)) {

            DayOfWeek day = date.getDayOfWeek();

            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                count++;
            }

            date = date.plusDays(1);
        }

        return count;
    }

    public int getWorkingDaysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            return 0;
        }

        int count = 0;
        LocalDate date = startDate;

        while (!date.isAfter(endDate)) {
            DayOfWeek day = date.getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                count++;
            }
            date = date.plusDays(1);
        }

        return count;
    }
}
