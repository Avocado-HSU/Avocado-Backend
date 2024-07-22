package dismas.com.avocado.config.openFeign.FreeDictionary;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FreeDictionaryErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();


    @Override
    public Exception decode(String methodKey, Response response) {
        if(response.status() >= 500){
            return new RuntimeException();
        }
        return errorDecoder.decode(methodKey, response);
    }
}
