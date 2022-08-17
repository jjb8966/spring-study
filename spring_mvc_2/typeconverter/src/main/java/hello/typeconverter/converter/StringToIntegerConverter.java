package hello.typeconverter.converter;

import org.springframework.core.convert.converter.Converter;

public class StringToIntegerConverter implements Converter<String, Number> {

    @Override
    public Number convert(String source) {
//        return Integer.parseInt(source);    // 원시 타입 int 리턴함
        return Integer.valueOf(source);     // wrapper 클래스 Integer 리턴함
    }
}
