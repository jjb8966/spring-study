package hello.typeconverter.formatter;

import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.text.ParseException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class MyNumberFormatterTest {

    @Test
    void StringToNumber() throws ParseException {
        MyNumberFormatter myNumberFormatter = new MyNumberFormatter();
        Number number = myNumberFormatter.parse("10,000", Locale.KOREA);
        String text = myNumberFormatter.print(10000, Locale.KOREA);

        assertThat(number).isEqualTo(10000L);
        assertThat(text).isEqualTo("10,000");
    }

    @Test
    void useFormattingConversionService() {
//        FormattingConversionService -> 포맷터를 지원하는 컨버젼 서비스
//        FormattingConversionService conversionService = new FormattingConversionService();

        // DefaultFormattingConversionService -> FormattingConversionService + 추가 기능
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addFormatter(new MyNumberFormatter());

        Number number = conversionService.convert("10,000", Number.class);
        String text = conversionService.convert(10000, String.class);

        assertThat(number).isEqualTo(10000L);
        assertThat(text).isEqualTo("10,000");
    }
}