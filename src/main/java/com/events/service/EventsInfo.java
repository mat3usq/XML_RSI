package com.events.service;

import com.events.model.Event;
import com.events.model.EventType;
import com.events.model.LocalDateTimeAdapter;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import java.time.LocalDateTime;
import java.util.List;

@WebService(
        endpointInterface = "com.events.service.EventsService",
        serviceName = "EventsInfoService",
        portName = "EventsInfoPort",
        targetNamespace = "http://service.events.com/"
)
@XmlSeeAlso({LocalDateTimeAdapter.class})
public class EventsInfo implements EventsService {
    @Override
    public List<Event> getEvents() {
        return List.of(
                new Event("Festiwal Kulinarny", EventType.GASTRONOMY,
                        LocalDateTime.of(2025, 4, 20, 12, 0),
                        "Targi produktów regionalnych"),

                new Event("Maraton Białostocki", EventType.SPORT,
                        LocalDateTime.of(2025, 4, 21, 9, 0),
                        "Doroczny bieg ulicami miasta")
        );
    }
}
