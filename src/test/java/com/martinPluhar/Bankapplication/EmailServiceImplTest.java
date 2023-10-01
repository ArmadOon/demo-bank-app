package com.martinPluhar.Bankapplication;
import com.martinPluhar.Bankapplication.dto.EmailDetails;
import com.martinPluhar.Bankapplication.services.impl.EmailServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

public class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendEmailAlert_Success() {
        // Arrange
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient("test@example.com");
        emailDetails.setSubject("Test Subject");
        emailDetails.setMessageBody("Test Message");

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setFrom(emailService.getSenderEmail());
        expectedMessage.setTo(emailDetails.getRecipient());
        expectedMessage.setSubject(emailDetails.getSubject());
        expectedMessage.setText(emailDetails.getMessageBody());

        doNothing().when(javaMailSender).send(expectedMessage);

        // Act
        emailService.sendEmailAlert(emailDetails);

        // Assert
        verify(javaMailSender, times(1)).send(expectedMessage);
    }

    @Test(expected = MailException.class)
    public void testSendEmailAlert_Failure() {
        // Arrange
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient("test@example.com");
        emailDetails.setSubject("Test Subject");
        emailDetails.setMessageBody("Test Message");

        // Simulujte, že odeslání emailu selže
        doThrow(new MailException("Simulated Mail Exception") {}).when(javaMailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmailAlert(emailDetails);
    }
}