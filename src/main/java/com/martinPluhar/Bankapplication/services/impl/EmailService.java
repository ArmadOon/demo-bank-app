package com.martinPluhar.Bankapplication.services.impl;

import com.martinPluhar.Bankapplication.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
