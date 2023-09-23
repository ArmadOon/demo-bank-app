package com.martinPluhar.Bankapplication.services.intfc;

import com.martinPluhar.Bankapplication.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);

}
