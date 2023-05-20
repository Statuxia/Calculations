package me.statuxia;

import me.statuxia.Exceptions.ArraySizeException;
import me.statuxia.Exceptions.DurationTimeException;
import me.statuxia.Exceptions.PeriodCollision;
import me.statuxia.Exceptions.WorkingTimeException;

import java.time.LocalTime;

public class Calculations {

    public static void main(String[] args) {

        LocalTime[] startTimes = new LocalTime[]{
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                LocalTime.of(15, 0),
                LocalTime.of(15, 30),
                LocalTime.of(16, 50)
        };

        int[] durations = new int[]{
                60, 30, 10, 10, 40
        };
        LocalTime beginWorkingTime = LocalTime.of(8, 0);
        LocalTime endWorkingTime = LocalTime.of(18, 0);
        int consultationTime = 30;

        try {
            String[] a = availablePeriods(startTimes, durations, beginWorkingTime, endWorkingTime, consultationTime);
            for (String s : a) {
                System.out.println(s);
            }
        } catch (WorkingTimeException | DurationTimeException | PeriodCollision | ArraySizeException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] availablePeriods(LocalTime[] startTimes, int[] durations,
                                            LocalTime beginWorkingTime, LocalTime endWorkingTime,
                                            int consultationTime) throws WorkingTimeException, DurationTimeException, PeriodCollision, ArraySizeException {
        Schedule schedule = new Schedule(startTimes, durations, beginWorkingTime, endWorkingTime, consultationTime);
        return schedule.getFreePeriods();
    }
}