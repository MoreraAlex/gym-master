package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, NavigableMap<TimeOfDay, List<TrainingSession>>> timetable =
            new EnumMap<>(DayOfWeek.class);

    public void addNewTrainingSession(TrainingSession trainingSession) {
        timetable
                .computeIfAbsent(trainingSession.getDayOfWeek(), d -> new TreeMap<>())
                .computeIfAbsent(trainingSession.getTimeOfDay(), t -> new ArrayList<>())
                .add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        NavigableMap<TimeOfDay, List<TrainingSession>> dayMap =
                timetable.getOrDefault(dayOfWeek, Collections.emptyNavigableMap());

        List<TrainingSession> result = new ArrayList<>();
        for (List<TrainingSession> sessions : dayMap.values()) {
            result.addAll(sessions);
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(
            DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {

        return timetable
                .getOrDefault(dayOfWeek, Collections.emptyNavigableMap())
                .getOrDefault(timeOfDay, Collections.emptyList());
    }

    public Map<Coach, Integer> getCountByCoaches() {
        Map<Coach, Integer> counts = new HashMap<>();

        for (NavigableMap<TimeOfDay, List<TrainingSession>> day : timetable.values()) {
            for (List<TrainingSession> sessions : day.values()) {
                for (TrainingSession session : sessions) {
                    counts.merge(session.getCoach(), 1, Integer::sum);
                }
            }
        }

        return counts.entrySet().stream()
                .sorted(Map.Entry.<Coach, Integer>comparingByValue().reversed())
                .collect(
                        LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        Map::putAll
                );
    }
}
