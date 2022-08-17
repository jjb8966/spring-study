package hello.typeconverter.converter;

import hello.typeconverter.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String> {

    @Override
    public String convert(IpPort source) {
        log.info("IpPortToStringConverter 동작");

        return source.getIp() + ":" + source.getPort();
    }
}
