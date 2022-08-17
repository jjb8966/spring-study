package hello.typeconverter.formatter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.text.ParseException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MyNumberFormatterTest {

    @Test
    void formatter() throws ParseException {
        MyNumberFormatter formatter = new MyNumberFormatter();

        Number stringToNumber = formatter.parse("1,000", Locale.KOREA);
        String numberToString = formatter.print(1000, Locale.KOREA);

        assertThat(stringToNumber).isEqualTo(1000L);
        assertThat(numberToString).isEqualTo("1,000");
    }

    @Test
    void conversion_service() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

        conversionService.addFormatter(new MyNumberFormatter());
        Number stringToNumber = conversionService.convert("1,000", Number.class);

        assertThat(stringToNumber).isEqualTo(1000L);
    }
}