package me.statuxia;

import me.statuxia.Exceptions.ArraySizeException;
import me.statuxia.Exceptions.DurationTimeException;
import me.statuxia.Exceptions.PeriodCollision;
import me.statuxia.Exceptions.WorkingTimeException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private final LocalTime[] startTimes;
    private final int[] durations;
    private final LocalTime beginWorkingTime;
    private final LocalTime endWorkingTime;
    private final int consultationTime;
    private final List<LocalTime[]> blockedPeriods = new ArrayList<>();
    private final LocalTime[] workingTime;

    public Schedule(LocalTime[] startTimes, int[] durations,
                    LocalTime beginWorkingTime, LocalTime endWorkingTime,
                    int consultationTime)
            throws WorkingTimeException, DurationTimeException, PeriodCollision, ArraySizeException {
        this.startTimes = startTimes;
        this.durations = durations;
        this.beginWorkingTime = beginWorkingTime;
        this.endWorkingTime = endWorkingTime;
        this.consultationTime = consultationTime;
        this.workingTime = new LocalTime[]{beginWorkingTime, endWorkingTime};

        validateNulls();
        validateData();
        validateBlockedPeriods();
    }

    public String[] getFreePeriods() {
        List<String> freePeriods = new ArrayList<>();
        LocalTime[] period = new LocalTime[]{beginWorkingTime, beginWorkingTime.plusMinutes(consultationTime)};

        while (isCollided(period)) {
            boolean collided = false;

            for (LocalTime[] blockedPeriod : blockedPeriods) {
                if (isCollided(period, blockedPeriod)) {
                    period[0] = blockedPeriod[1];
                    period[1] = period[0].plusMinutes(consultationTime);
                    collided = true;
                    break;
                }
            }

            if (!collided) {
                freePeriods.add(period[0].toString() + "-" + period[1].toString());
                period[0] = period[1];
                period[1] = period[0].plusMinutes(consultationTime);
            }

        }
        return freePeriods.toArray(new String[0]);
    }

    private void validateNulls() {
        if (startTimes == null) {
            throw new NullPointerException("Массив начал промежутков времени не может быть null");
        }

        for (LocalTime startTime : startTimes) {
            if (startTime == null) {
                throw new NullPointerException("Массив начал промежутков времени не может быть null");
            }
        }

        if (durations == null) {
            throw new NullPointerException("Массив длительности промежутков времени не может быть null");
        }

        if (beginWorkingTime == null) {
            throw new NullPointerException("Время начало рабочего дня сотрудника не может быть null");
        }

        if (endWorkingTime == null) {
            throw new NullPointerException("Время окончания рабочего дня сотрудника не может быть null");
        }
    }

    private void validateData() throws DurationTimeException, WorkingTimeException, ArraySizeException {
        if (consultationTime <= 0) {
            throw new DurationTimeException("Длительность консультации не может быть меньше единицы.");
        }

        for (int duration : durations) {
            if (duration <= 0) {
                throw new DurationTimeException("Длительность консультации не может быть меньше единицы.");
            }
        }

        if (startTimes.length != durations.length) {
            throw new ArraySizeException("Длина массивов списка занятых промежутков времени должна совпадать.");
        }

        if (beginWorkingTime.isAfter(endWorkingTime)) {
            throw new WorkingTimeException("Начало рабочего дня не может быть позже окончания.");
        }
    }

    private void validateBlockedPeriods() throws PeriodCollision {

        for (int i = 0; i < startTimes.length; i++) {
            LocalTime[] period = new LocalTime[]{startTimes[i], startTimes[i].plusMinutes(durations[i])};

            for (LocalTime[] blockedPeriod : blockedPeriods) {
                if (isCollided(period, blockedPeriod)) {
                    throw new PeriodCollision("Промежутки времени не должны пересекаться");
                }
                if (!isCollided(period)) {
                    throw new PeriodCollision("Занятые промежутки времени не должны выходить за рабочий день сотрудника");
                }
            }

            blockedPeriods.add(period);
        }
    }

    private boolean isCollided(LocalTime[] period, LocalTime[] period2) {
        return (!period[0].isBefore(period2[0].plusNanos(1)) || !period[1].isBefore(period2[0].plusNanos(1))) &&
                (!period[0].isAfter(period2[1].minusNanos(1)) || !period[1].isAfter(period2[1].minusNanos(1)));
    }

    private boolean isCollided(LocalTime[] period) {
        return isCollided(workingTime, period);
    }
}
