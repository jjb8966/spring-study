package hello.proxy.pureporxy.proxy;

import hello.proxy.pureporxy.proxy.code.CacheProxy;
import hello.proxy.pureporxy.proxy.code.ProxyPatternClient;
import hello.proxy.pureporxy.proxy.code.RealSubject;
import org.junit.jupiter.api.Test;

public class ProxyPatternTest {

    @Test
    void noProxyTest() {
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);
        client.execute();
        client.execute();
        client.execute();   // 3초 걸림
    }

    @Test
    void cacheProxyTest() {
        RealSubject realSubject = new RealSubject();
        CacheProxy cacheProxy = new CacheProxy(realSubject);
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);
        client.execute();
        client.execute();
        client.execute();   // 1초 걸림
    }
}
