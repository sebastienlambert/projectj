package projectj.infrastructure.persistence;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {
    @Override
    public Date convertToDatabaseColumn(LocalDate localDateTime) {
        return (localDateTime == null) ? null : Date.valueOf(localDateTime);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        return (date == null) ? null : date.toLocalDate();
    }
}
