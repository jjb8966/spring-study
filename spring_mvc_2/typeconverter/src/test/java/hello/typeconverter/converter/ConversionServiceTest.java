package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import static org.assertj.core.api.Assertions.assertThat;

public class ConversionServiceTest {

    @Test
    void conversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();

        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
        String convertString = conversionService.convert(new IpPort("127.0.0.1", 8080), String.class);

        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));
        assertThat(convertString).isEqualTo("127.0.0.1:8080");
    }
}
