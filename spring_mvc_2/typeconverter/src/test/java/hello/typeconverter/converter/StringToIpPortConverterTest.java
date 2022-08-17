package hello.typeconverter.converter;

import hello.typeconverter.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StringToIpPortConverterTest {

    @Test
    void convert() {
        StringToIntegerConverter stringToIntegerConverter = new StringToIntegerConverter();
        StringToIpPortConverter stringToIpPortConverter = new StringToIpPortConverter();
        IpPortToStringConverter ipPortToStringConverter = new IpPortToStringConverter();

        IpPort ipPort = new IpPort("127.0.0.1", 8080);

        assertThat(stringToIntegerConverter.convert("200")).isEqualTo(200);
        assertThat(stringToIpPortConverter.convert("127.0.0.1:8080")).isEqualTo(ipPort);
        assertThat(ipPortToStringConverter.convert(ipPort)).isEqualTo("127.0.0.1:8080");
    }

    @Test
    void conversion_service() {
        /**
         * DefaultConversionService -> 컨버터 등록 & 컨버터 사용
         *      ConversionRegistry -> 컨버터 등록
         *      ConversionService -> 컨버터 사용
         *      => 인터페이스 분리 원칙(ISP) 잘 지킴!
         */
        DefaultConversionService conversionService = new DefaultConversionService();

        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        IpPort ipPort = new IpPort("127.0.0.1", 8080);

        assertThat(conversionService.convert("127.0.0.1:8080", IpPort.class)).isEqualTo(ipPort);
        assertThat(conversionService.convert(ipPort, String.class)).isEqualTo("127.0.0.1:8080");
    }
}