package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, NavigableMap<TimeOfDay, List<TrainingSession>>> timetable =
            new EnumMap<>(DayOfWeek.class);

    private final Map<Coach, Integer> coachesCounter = new HashMap<>();


    public void addNewTrainingSession(TrainingSession trainingSession) {
        timetable
                .computeIfAbsent(trainingSession.getDayOfWeek(), d -> new TreeMap<>())
                .computeIfAbsent(trainingSession.getTimeOfDay(), t -> new ArrayList<>())
                .add(trainingSession);

        Coach currentCoach = trainingSession.getCoach();
        coachesCounter.put(currentCoach, coachesCounter.getOrDefault(currentCoach, 0) + 1);
    }

    public NavigableMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(
            DayOfWeek dayOfWeek) {

        NavigableMap<TimeOfDay, List<TrainingSession>> trainingsForDay =
                timetable.get(dayOfWeek);

        if (trainingsForDay == null) {
            return new TreeMap<>();
        }

        return trainingsForDay;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(
            DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {

        NavigableMap<TimeOfDay, List<TrainingSession>> dayMap =
                timetable.get(dayOfWeek);

        if (dayMap == null) {
            return Collections.emptyList();
        }

        return dayMap.getOrDefault(timeOfDay, Collections.emptyList());
    }

    public Map<Coach, Integer> getCountByCoaches() {
        return coachesCounter.entrySet().stream()
                .sorted(Map.Entry.<Coach, Integer>comparingByValue().reversed())
                .collect(
                        LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        Map::putAll
                );
    }
}
