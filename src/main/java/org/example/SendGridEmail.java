package org.example;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class SendGridEmail {

    public void sendMail() throws IOException {

        Email from = new Email("mohanapraneswaran@gmail.com");
        Email to = new Email("mohanpraneswaran@gmail.com");

        String subject = "zip files";
        Content content = new Content("text/html", "these zip file contains txt files");

        Mail mail = new Mail(from, subject, to, content);
        try (final InputStream inputStream = Files.newInputStream(Paths.get("txtZip.zip"))) {
            final Attachments attachments = new Attachments
                    .Builder("txtZip.zip", inputStream)
                    .withType("application/zip")
                    .build();
            mail.addAttachments(attachments);
        }
        SendGrid sg = new SendGrid(System.getenv("SEND_GRID_API"));
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());


    }

    public static void main(String... args) throws IOException {

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                CreateZip createZip = new CreateZip();
                try {
                    createZip.filesToZip();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                SendGridEmail sendGridEmail = new SendGridEmail();
                try {
                    sendGridEmail.sendMail();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 300000, 300000);
    }
}
