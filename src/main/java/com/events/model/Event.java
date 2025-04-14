package com.events.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;

@XmlAccessorType(XmlAccessType.FIELD)
public class Event {
    private static long globalId = 1;
    private final long id;
    private String name;
    private EventType type;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime dateTime;
    private String description;

    public Event(String name, EventType type, LocalDateTime dateTime, String description) {
        this.id = globalId++;
        this.name = name;
        this.type = type;
        this.dateTime = dateTime;
        this.description = description;
    }

    public Event() {
        this.id = globalId++;
    }

    public static long getGlobalId() {
        return globalId;
    }

    public static void setGlobalId(long globalId) {
        Event.globalId = globalId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeekNumber() {
        return dateTime.get(WeekFields.ISO.weekOfWeekBasedYear());
    }

    public int getMonthNumber() {
        return dateTime.getMonthValue();
    }

    public int getYearNumber() {
        return dateTime.getYear();
    }
}

