package hello.typeconverter.converter;

import hello.typeconverter.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {

    @Override
    public IpPort convert(String source) {
        log.info("StringToIpPortConverter 동작");

        String[] ipPort = source.split(":");
        String ip = ipPort[0];
        int port = Integer.parseInt(ipPort[1]);

        return new IpPort(ip, port);
    }
}
