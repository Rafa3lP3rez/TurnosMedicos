package com.consultoriomedico.repository;

import com.consultoriomedico.domain.PropertiesConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.Builder;
import org.apache.log4j.Logger;

@Builder
public class SmsSender {
    private static final Logger log = Logger.getLogger(SmsSender.class);

    public void sendSms(String numberToSend, String messageToSend) {
        PropertiesConfig propConfig = new PropertiesConfig();

        try {
            Twilio.init(propConfig.getPropertyConfig("ACCOUNT_SID"), propConfig.getPropertyConfig("AUTH_TOKEN"));
            Message message = Message.creator(new PhoneNumber(numberToSend), propConfig.getPropertyConfig("MESSAGING_SERVICE_SID"), messageToSend).create();
            log.info(String.format("[TwilioSmsSender][sendSms] Respuesta del servicio Twilio: %s", message.getSid()));
        } catch (Exception e) {
            log.error(e);
        }
    }
}
