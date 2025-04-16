package com.events.service;

import com.events.model.Event;
import com.events.model.EventType;
import com.events.model.LocalDateTimeAdapter;
import jakarta.jws.HandlerChain;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.soap.MTOM;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@MTOM
@WebService(
        endpointInterface = "com.events.service.EventsService",
        serviceName = "EventsInfoService",
        portName = "EventsInfoPort",
        targetNamespace = "http://service.events.com/"
)
@HandlerChain(file = "handlers.xml")
@XmlSeeAlso({LocalDateTimeAdapter.class})
public class EventsInfo implements EventsService {
    private final List<Event> events = new ArrayList<>();

    public EventsInfo() {
        events.add(new Event("Coldplay Concert", EventType.CONCERT, LocalDateTime.of(2024, 6, 20, 20, 0), "Concert at the National Stadium in Warsaw"));
        events.add(new Event("Music Festival", EventType.FESTIVAL, LocalDateTime.of(2024, 6, 21, 10, 0), "Annual music festival in the park"));
        events.add(new Event("Art Exhibition", EventType.EXHIBITION, LocalDateTime.of(2024, 6, 22, 12, 0), "Exhibition of contemporary Polish art"));
        events.add(new Event("Theatrical Performance", EventType.THEATER, LocalDateTime.of(2024, 6, 23, 19, 0), "Hamlet at the National Theater"));
        events.add(new Event("Soccer Match", EventType.SPORT, LocalDateTime.of(2024, 6, 24, 15, 0), "Friendly match between local teams"));
        events.add(new Event("Programming Workshop", EventType.WORKSHOP, LocalDateTime.of(2024, 6, 25, 9, 0), "Workshop on Java and Spring Boot"));
        events.add(new Event("Tech Conference", EventType.CONFERENCE, LocalDateTime.of(2024, 6, 26, 10, 0), "Annual tech conference with keynote speakers"));
        events.add(new Event("Book Fair", EventType.FAIR, LocalDateTime.of(2024, 6, 27, 11, 0), "Annual book fair with author signings"));
        events.add(new Event("Food Festival", EventType.GASTRONOMY, LocalDateTime.of(2024, 6, 28, 12, 0), "Festival celebrating local cuisine"));
        events.add(new Event("Historical Tour", EventType.HISTORY, LocalDateTime.of(2024, 6, 29, 10, 0), "Guided tour of historical sites"));
        events.add(new Event("Poetry Reading", EventType.LITERARY, LocalDateTime.of(2024, 6, 30, 18, 0), "Evening of poetry readings by local authors"));
        events.add(new Event("Children's Play", EventType.FOR_CHILDREN, LocalDateTime.of(2024, 7, 1, 11, 0), "Interactive play for children"));
        events.add(new Event("Movie Night", EventType.CINEMA, LocalDateTime.of(2024, 7, 2, 20, 0), "Outdoor movie screening in the park"));
        events.add(new Event("Community Cleanup", EventType.COMMUNITY, LocalDateTime.of(2024, 7, 3, 9, 0), "Community cleanup event at the local park"));
        events.add(new Event("Educational Seminar", EventType.EDUCATION, LocalDateTime.of(2024, 7, 4, 14, 0), "Seminar on educational trends and practices"));
    }

    @Override
    public List<Event> findEventsByDate(String date) {
        return events.stream()
                .filter(event -> event.getDateTime().toLocalDate().equals(LocalDate.parse(date)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findEventsByWeek(int weekNumber, int year) {
        return events.stream()
                .filter(event -> event.getWeekNumber() == weekNumber && event.getYearNumber() == year)
                .collect(Collectors.toList());
    }

    @Override
    public Event findEventById(long eventId) {
        try {
            return events.stream()
                    .filter(event -> event.getId() == eventId)
                    .findFirst()
                    .orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public Event addEvent(Event event) {
        if (event.getName() == null || event.getName().isEmpty() ||
                event.getDescription() == null || event.getDescription().isEmpty() ||
                event.getType() == null || event.getDateTime() == null) {
            return null;
        }

        Event newEvent = new Event(event.getName(), event.getType(), event.getDateTime(), event.getDescription());
        events.add(newEvent);
        return newEvent;
    }

    @Override
    public Event updateEvent(long eventId, Event updatedEvent) {
        if (updatedEvent == null) {
            return null;
        }

        try {
            Optional<Event> existingEvent = events.stream()
                    .filter(event -> event.getId() == eventId)
                    .findFirst();

            if (existingEvent.isPresent()) {
                Event eventToUpdate = existingEvent.get();
                eventToUpdate.setName(updatedEvent.getName());
                eventToUpdate.setType(updatedEvent.getType());
                eventToUpdate.setDateTime(updatedEvent.getDateTime());
                eventToUpdate.setDescription(updatedEvent.getDescription());
                return eventToUpdate;
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public boolean deleteEvent(long eventId) {
        try {
            return events.removeIf(event -> event.getId() == eventId);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public List<Event> findAllEvents() {
        return new ArrayList<>(events);
    }

    @Override
    public byte[] generateEventsReport(String date, boolean byWeek) {
        List<Event> events;

        if (date == null || date.isEmpty()) {
            events = findAllEvents();
        } else if (byWeek) {
            LocalDate localDate = LocalDate.parse(date);
            int weekNumber = localDate.get(WeekFields.ISO.weekOfWeekBasedYear());
            int year = localDate.getYear();
            events = findEventsByWeek(weekNumber, year);
        } else {
            events = findEventsByDate(date);
        }

        try {
            return new EventsReportPdfGenerator().generate(events);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> findAllEventsTypes() {
        return Arrays.stream(EventType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}