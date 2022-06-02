package hello.typeconverter.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Slf4j
public class MyNumberFormatter implements Formatter<Number> {   // 숫자 <-> 문자 사이 타입을 변환하는 포맷터

    @Override
    // String -> Number
    public Number parse(String text, Locale locale) throws ParseException {
        log.info("[String -> Number] text={}, locale={}", text, locale);
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        return numberFormat.parse(text);
    }

    @Override
    // Number -> String
    public String print(Number number, Locale locale) {
        log.info("[Number -> String] number={}, locale={}", number, locale);
        return NumberFormat.getInstance(locale).format(number);
    }
}
