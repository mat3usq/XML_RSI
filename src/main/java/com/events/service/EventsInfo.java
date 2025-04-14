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
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
        events.add(new Event("Koncert Coldplay", EventType.CONCERT, LocalDateTime.of(2024, 6, 20, 20, 0), "Koncert na Stadionie Narodowym w Warszawie"));
        events.add(new Event("Premiera filmu", EventType.CINEMA, LocalDateTime.of(2024, 5, 15, 18, 30), "Premiera najnowszego filmu Christophera Nolana"));
        events.add(new Event("Wystawa sztuki", EventType.EXHIBITION, LocalDateTime.of(2024, 7, 10, 10, 0), "Wystawa współczesnej sztuki polskiej"));
        events.add(new Event("Spektakl teatralny", EventType.THEATER, LocalDateTime.of(2024, 6, 5, 19, 0), "Hamlet w Teatrze Narodowym"));
        events.add(new Event("Warsztaty programowania", EventType.WORKSHOP, LocalDateTime.of(2024, 5, 25, 9, 0), "Warsztaty z Java i Spring Boot"));
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
    public byte[] generateEventsReportAsPdf(String date) {
        List<Event> eventsForDate = findEventsByDate(date);
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = null;
            float currentY = 700;
            boolean firstPage = true;

            try {
                for (Event event : eventsForDate) {
                    if (currentY < 50) {
                        if (contentStream != null) {
                            contentStream.endText();
                            contentStream.close();
                        }
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(50, 700);
                        currentY = 700;
                        firstPage = false;
                    }

                    if (contentStream == null) {
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(50, currentY);
                        if (firstPage) {
                            contentStream.showText("Events Report for " + date);
                            contentStream.newLineAtOffset(0, -20);
                            contentStream.showText("Total events: " + eventsForDate.size());
                            currentY -= 50;
                        }
                    }

                    contentStream.showText(String.format(
                            "Event: %s | Type: %s | Time: %s",
                            event.getName(),
                            event.getType(),
                            event.getDateTime().toLocalTime()
                    ));
                    contentStream.newLineAtOffset(0, -15);
                    currentY -= 15;
                }

                if (contentStream != null) {
                    contentStream.endText();
                }
            } finally {
                if (contentStream != null) {
                    contentStream.close();
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}