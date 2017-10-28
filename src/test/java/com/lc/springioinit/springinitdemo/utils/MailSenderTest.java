package com.lc.springioinit.springinitdemo.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MailSenderTest {
    @Test
    public void send() throws Exception {
        try {
            new MailSender()
                    .title("Test send mail")
                    .content("Test Test Test ...")
                    .contentType(MailContentTypeEnum.TEXT)
                    .targets(new ArrayList<String>(){
                        {
                            add("");
                        }
                    })
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}