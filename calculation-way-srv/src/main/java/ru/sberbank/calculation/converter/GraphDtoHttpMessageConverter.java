package ru.sberbank.calculation.converter;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import ru.sberbank.inkass.dto.GraphDto;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

//@Component
public class GraphDtoHttpMessageConverter implements HttpMessageConverter<GraphDto> {
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.ALL);
    }

    @Override
    public GraphDto read(Class<? extends GraphDto> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        final Class<? extends HttpInputMessage> aClass = inputMessage.getClass();
        byte[] b = new byte[1000000];
        final int read = inputMessage.getBody().read(b);
        String s = new String(b);
        System.out.println(s);
        return null;
    }

    @Override
    public void write(GraphDto graphDto, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

    }
}
