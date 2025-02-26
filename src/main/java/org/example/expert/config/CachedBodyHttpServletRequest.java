package org.example.expert.config;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper { // HttpServletRequestWrapper 로 감쌈 (HttpServletRequest 처럼 다룰 수 있음)

    private final byte[] cachedBody;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = StreamUtils.copyToByteArray(requestInputStream); // 요청바디의 InputStream 을 Byte[] 에 복사.
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedBodyServletInputStream(this.cachedBody);
    }
}

class CachedBodyServletInputStream extends ServletInputStream {
    private final ByteArrayInputStream byteArrayInputStream;

    public CachedBodyServletInputStream(byte[] cachedBody) {
        this.byteArrayInputStream = new ByteArrayInputStream(cachedBody);
    }

    @Override
    public int read() { // Lv.4 byteArrayInputStream.read()를 호출해서 요청데이터를 한 바이트씩 반환
        return byteArrayInputStream.read(); // 이걸 구현하지 않으면 요청 본문을 읽을 수 없음.
    }

    @Override
    public boolean isFinished() { // Lv.4 더이상 읽을 데이터가 없으면 true 반환
        return byteArrayInputStream.available() == 0; // 이걸 구현하지 않으면 스프링이 스트림을 언제 닫아야 하는지 알 수 없어 정상적인 요청 처리가 안 될 가능성이 있음.
    }

    @Override
    public boolean isReady() { // Lv.4 스트림이 언제나 사용 가능하도록 true 반환
        return true; // 비동기 처리를 할때, 지금 스트림을 읽어도 되는지 체크하는데 사용됨. 비동기 처리를 지원하는 경우 동적으로 변경가능.
    }

    @Override
    public void setReadListener(ReadListener listener) { // Lv.4 비동기 방식으로 데이터를 읽을 때 필요함
        throw new UnsupportedOperationException(); // 현재 구현은 비동기 읽기를 지원하지 않으므로 예외를 던짐.
    }
}