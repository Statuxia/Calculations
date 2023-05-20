package me.statuxia;

import me.statuxia.Exceptions.ArraySizeException;
import me.statuxia.Exceptions.DurationTimeException;
import me.statuxia.Exceptions.PeriodCollision;
import me.statuxia.Exceptions.WorkingTimeException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CalculationsTests {

    LocalTime[] startTimes;
    int[] durations;
    LocalTime beginWorkingTime;
    LocalTime endWorkingTime;
    int consultationTime;
    String[] result;

    @Before
    public void setup() {
        startTimes = new LocalTime[]{
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                LocalTime.of(15, 0),
                LocalTime.of(15, 30),
                LocalTime.of(16, 50)
        };

        durations = new int[]{
                60, 30, 10, 10, 40
        };
        beginWorkingTime = LocalTime.of(8, 0);
        endWorkingTime = LocalTime.of(18, 0);
        consultationTime = 30;
        result = new String[]{
                "08:00-08:30",
                "08:30-09:00",
                "09:00-09:30",
                "09:30-10:00",
                "11:30-12:00",
                "12:00-12:30",
                "12:30-13:00",
                "13:00-13:30",
                "13:30-14:00",
                "14:00-14:30",
                "14:30-15:00",
                "15:40-16:10",
                "16:10-16:40",
                "17:30-18:00"};
    }

    @Test
    public void nullPointerExceptionTest() {
        assertThrows(NullPointerException.class, () -> {
            Calculations.availablePeriods(null, durations, beginWorkingTime, endWorkingTime, consultationTime);
        });
    }

    @Test
    public void arraySizeExceptionTest() {
        assertThrows(ArraySizeException.class, () -> {
            Calculations.availablePeriods(new LocalTime[]{}, durations, beginWorkingTime, endWorkingTime, consultationTime);
        });
    }

    @Test
    public void durationTimeExceptionTest() {

        assertThrows(DurationTimeException.class, () -> {
            Calculations.availablePeriods(startTimes, durations, beginWorkingTime, endWorkingTime, 0);
        });
    }

    @Test
    public void assertTrue() throws WorkingTimeException, PeriodCollision, ArraySizeException, DurationTimeException {
        assertEquals(Calculations.availablePeriods(startTimes, durations, beginWorkingTime, endWorkingTime, consultationTime), result);
    }
}
