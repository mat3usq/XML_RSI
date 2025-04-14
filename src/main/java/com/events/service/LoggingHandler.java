package com.events.service;

import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

import javax.xml.namespace.QName;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Set;
import java.io.ByteArrayOutputStream;

public class LoggingHandler implements SOAPHandler<SOAPMessageContext> {

    private static final String LOG_FILE = "logs.txt";

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            boolean isOutbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            context.getMessage().writeTo(out);
            String soapMessage = out.toString("UTF-8");
            out.close();

            String logEntry = "===== " + (isOutbound ? "OUTBOUND" : "INBOUND") + " MESSAGE =====\n" + soapMessage + "\n\n";

            bw.write(logEntry);
            bw.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String faultMessage = "===== FAULT MESSAGE =====\n";

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            context.getMessage().writeTo(out);
            faultMessage += out.toString("UTF-8") + "\n\n";
            out.close();

            bw.write(faultMessage);
            bw.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public void close(MessageContext context) {
    }
}