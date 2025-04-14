package com.events.service;

import com.events.model.Event;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.jws.soap.SOAPBinding.Use;
import jakarta.xml.bind.annotation.XmlMimeType;
import jakarta.xml.ws.soap.MTOM;

import java.util.List;

@MTOM
@WebService(
        endpointInterface = "com.events.service.EventsService",
        serviceName = "EventsService"
)
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public interface EventsService {
    @WebMethod(
            action = "https://events.org/events/getEventsByDate",
            operationName = "findEventsByDate"
    )
    List<Event> findEventsByDate(String date);

    @WebMethod(
            action = "https://events.org/events/getEventsByWeek",
            operationName = "findEventsByWeek"
    )
    List<Event> findEventsByWeek(int weekNumber, int year);

    @WebMethod(
            action = "https://events.org/events/getEventById",
            operationName = "findEventById"
    )
    Event findEventById(long eventId);

    @WebMethod(
            action = "https://events.org/events/addEvent",
            operationName = "addEvent"
    )
    Event addEvent(Event event);

    @WebMethod(
            action = "https://events.org/events/updateEvent",
            operationName = "updateEvent"
    )
    Event updateEvent(long eventId, Event updatedEvent);

    @WebMethod(
            action = "https://events.org/events/deleteEvent",
            operationName = "deleteEvent"
    )
    boolean deleteEvent(long eventId);

    @WebMethod(
            action = "https://events.org/events/getEvents",
            operationName = "findAllEvents"
    )
    List<Event> findAllEvents();

    @WebMethod(
            action = "https://events.org/events/getEventsReport",
            operationName = "generateEventsReport"
    )
    @XmlMimeType("application/pdf")
    byte[] generateEventsReportAsPdf(String date);
}