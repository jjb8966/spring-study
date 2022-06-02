package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StringToIpPortConverterTest {

    @Test
    void stringToIpPort() {
        StringToIpPortConverter stringToIpPortConverter = new StringToIpPortConverter();
        IpPort ipPort = stringToIpPortConverter.convert("127.0.0.1:8080");
        String ip = ipPort.getIp();
        int port = ipPort.getPort();

        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));
    }

    @Test
    void IpPortToString() {
        IpPortToStringConverter ipPortToStringConverter = new IpPortToStringConverter();

        String convertString = ipPortToStringConverter.convert(new IpPort("127.0.0.1", 8080));

        assertThat(convertString).isEqualTo("127.0.0.1:8080");
    }
}