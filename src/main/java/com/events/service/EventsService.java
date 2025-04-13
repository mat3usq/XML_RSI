package com.events.service;

import com.events.model.Event;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.jws.soap.SOAPBinding.Use;

import java.util.List;

@WebService(
        endpointInterface = "com.events.service.EventsService",
        serviceName = "EventsService"
)
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public interface EventsService {
    @WebMethod(
            action = "https://events.org/events/getEvents",
            operationName = "getEventsOperation"
    )
    List<Event> getEvents();
}
