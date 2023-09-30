package com.martinPluhar.Bankapplication.config;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PdfConfiguration {
    @Bean
    public Font contentFont() {
        return new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    }
}
