package com.coffee.roastnbrew.utils;

import com.coffee.roastnbrew.exceptions.APIException;
import com.coffee.roastnbrew.exceptions.CoffeeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.json.JsonStructure;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.URIBuilder;

@Slf4j
public class RestUtils {
    
    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";
    public static final String REQUEST_METHOD_DELETE = "DELETE";
    public static final String REQUEST_METHOD_PUT = "PUT";
    public static final String REQUEST_METHOD_PATCH = "PATCH";

    public static <T> Response ok(T data) {
        return ok(data, null);
    }

    public static <T> Response ok(T data, NewCookie cookie) {

        ApiResponse<T> successResponse = ApiResponse.successResponse(data);

        Response.ResponseBuilder builder = Response.ok(successResponse, MediaType.APPLICATION_JSON_TYPE);
        if (cookie != null) {
            builder.cookie(cookie);
        }
        return builder.build();
    }

    public static Response noContentResponse() {
        return Response.noContent().build();
    }
    
    public static Response request(
        String url, String method, Object payload, MultivaluedMap<String, String> params,
        Map<String, String> headers) throws CoffeeException {
        
        return request(url, method, RestUtils.toJSONString(payload), params, headers);
    }
    
    public static Response request(String url, String method, String payload,
        MultivaluedMap<String, String> params, Map<String, String> headers) throws CoffeeException {
        
        return request(url, method, payload, params, headers, -1, -1);
    }
    
    public static Response request(String url, String method, Object payload,
        MultivaluedMap<String, String> params, Map<String, String> headers, int connTimeoutMs,
        int readTimeoutMs) throws CoffeeException {
        
        return request(url, method, RestUtils.toJSONString(payload), params, headers,
            connTimeoutMs, readTimeoutMs);
    }
    
    private static String toJSONString(Object payload) throws CoffeeException {
        if (payload instanceof JsonStructure) {
            return payload.toString();
        } else {
            return JSONUtils.objectToJsonString(payload);
        }
    }
    
    public static Response request(String url, String method, String payload,
        MultivaluedMap<String, String> params, Map<String, String> headers, int connTimeoutMs,
        int readTimeoutMs) throws CoffeeException {
        
        URL urlObject;
        try {
            new URI(url);
            urlObject = getURL(url, params);
        } catch (URISyntaxException | MalformedURLException e) {
            log.error("Unable to create URL Object from url : {}", url, e);
            throw new CoffeeException(e);
        }
        
        HttpURLConnection conn = null;
        try {
            headers = headers != null ? headers : new HashMap<>();
            
            conn = (HttpURLConnection) urlObject.openConnection();
            conn.setRequestMethod(method);
            
            if (connTimeoutMs > 0) {
                conn.setConnectTimeout(connTimeoutMs);
            }
            
            if (readTimeoutMs > 0) {
                conn.setReadTimeout(readTimeoutMs);
            }
            
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
            
            if (!headers.containsKey("Content-Type")) {
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            }
            
            if (!method.equals(REQUEST_METHOD_GET) && payload != null) {
                conn.setDoOutput(true);
                
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(payload.getBytes("UTF-8"));
                }
            }
            return getResponse(conn);
        } catch (SocketTimeoutException e) {
            throw new APIException(Status.GATEWAY_TIMEOUT.getStatusCode(), "");
        } catch (IOException e) {
            log.warn("error while reading from url : {}", urlObject.toString(), e);
            throw new CoffeeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
    
    protected static Response getResponse(HttpURLConnection conn) throws IOException, APIException {
        int responseCode = conn.getResponseCode();
        
        Response.ResponseBuilder responseBuilder = Response.status(responseCode);
        
        //Get Response
        InputStream is;
        if (responseCode >= 200 && responseCode < 300) {
            is = conn.getInputStream();
        } else if (responseCode == 502 || responseCode == 503 || responseCode == 504) {
            throw new APIException(responseCode, "failure");
        } else {
            is = conn.getErrorStream();
        }
        
        if (is != null) {
            String responseBody = IOUtils.toString(is, "UTF-8");
            responseBuilder.entity(responseBody);
        }
        
        Map<String, List<String>> headers = conn.getHeaderFields();
        if (headers != null) {
            for (String header : headers.keySet()) {
                for (String value : headers.get(header)) {
                    responseBuilder.header(header, value);
                }
            }
        }
        
        return responseBuilder.build();
    }
    
    public static URL getURL(String url, MultivaluedMap<String, String> params) throws URISyntaxException, MalformedURLException {
        URIBuilder builder = new URIBuilder(url);
        if(params != null) {
            for(String key : params.keySet()) {
                List<String> values = params.get(key);
                for (String eachValue : values) {
                    builder.addParameter(key, eachValue);
                }
            }
        }
        
        return builder.build().toURL();
    }
}
