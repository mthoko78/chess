package com.mthoko.learners.domain.sms;

import com.mthoko.learners.common.util.HttpManager;
import com.mthoko.learners.common.util.RequestPackage;
import com.mthoko.learners.exception.ApplicationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmsServiceImplTest {


    @Test
    void should_throw_application_exception_when_sms_body_length_out_of_bounds() {
        String bodyOutOfRange = "With Clickatell's SMS Platform, you're able to send a default of three SMS message parts and we ensure that your SMS is received as one message. This default can be changed to suit your specific needs and the number of concatenated SMS message parts you want to send. Every part is charged at the rate of one SMS, which is standard with all mobile providers. Unsure of using concatenated SMS? Sign up for a free SMS Platform account and enjoy unlimited free testing in our sandbox environment. If you're looking for a little more information, have a look at our uncomplicated guide to concatenated SMS.With Clickatell's SMS Platform, you're able to send a default of three SMS message parts and we ensure that your SMS is received as one message. This default can be changed to suit your specific needs and the number of concatenated SMS message parts you want to send. Every part is charged at the rate of one SMS, wh ";
        Sms sms = new Sms();
        sms.setRecipient("0684243087");
        sms.setBody(bodyOutOfRange);
        SmsRepo smsRepo = Mockito.mock(SmsRepo.class);
        MessageResponseRepo messageResponseRepo = Mockito.mock(MessageResponseRepo.class);
        SmsService smsService = new SmsServiceImpl(
                smsRepo,
                null,
                null,
                null,
                messageResponseRepo,
                null,
                null);

        Assertions.assertThrows(ApplicationException.class, () -> {
            smsService.sendSms(sms);
        });
    }

}
