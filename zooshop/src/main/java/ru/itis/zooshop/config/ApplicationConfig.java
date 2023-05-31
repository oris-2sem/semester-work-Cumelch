package ru.itis.zooshop.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter((Converter<String, LocalDate>) mappingContext -> {

            LocalDate parse = LocalDate
                    .parse(mappingContext.getSource(),
                            DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            return parse;
        });

        modelMapper.addConverter((Converter<LocalDateTime, LocalDate>) mappingContext -> mappingContext.getSource().toLocalDate());

        modelMapper.addConverter((Converter<String, LocalDateTime>) mappingContext -> {
            LocalDateTime parse = LocalDateTime.parse(mappingContext.getSource(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return parse;
        });

        modelMapper.addConverter((Converter<String, LocalTime>) mappingContext -> {
            LocalTime parse = LocalTime.parse(mappingContext.getSource(),
                    DateTimeFormatter.ofPattern("HH:mm:ss"));
            return parse;
        });

        return modelMapper;
    }
}
