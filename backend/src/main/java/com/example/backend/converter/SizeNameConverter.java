package com.example.backend.converter;

import com.example.backend.models.SizeName;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SizeNameConverter implements AttributeConverter<SizeName, String> {

    @Override
    public String convertToDatabaseColumn(SizeName attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public SizeName convertToEntityAttribute(String dbData) {
        return dbData != null ? SizeName.fromValue(dbData) : null;
    }
}
