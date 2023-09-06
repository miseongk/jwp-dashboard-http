package org.apache.coyote.http11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final int HTTP_METHOD_INDEX = 0;
    private static final int REQUEST_URI_INDEX = 1;
    private static final String QUERY_STRING_DELIMITER = "?";
    private static final int NON_EXIST = -1;
    private static final String QUERY_STRING_VALUE_DELIMITER = "&";
    private static final String KEY_AND_VALUE_DELIMITER = "=";
    private static final String EMPTY_LINE = "";
    private static final String HEADER_DELIMITER = ": ";
    private static final int EXIST_HEADER_VALUE = 2;
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    private final Map<String, BodyParser> bodyParsers = new HashMap<>();

    public HttpRequestParser() {
        init();
    }

    private void init() {
        bodyParsers.put("application/x-www-form-urlencoded", new FormBodyParser());
    }

    public HttpRequest parse(final BufferedReader reader) {
        final String firstLine = readLine(reader);
        final HttpMethod httpMethod = parseHttpMethod(firstLine);
        final String requestUri = parseRequestUri(firstLine);
        final QueryStrings queryStrings = parseQueryStrings(firstLine);
        final HttpHeaders httpHeaders = parseHttpHeaders(reader);
        final Map<String, String> requestBody = parseRequestBody(reader, httpHeaders.get("Content-Length"),
                bodyParsers.get(httpHeaders.getContentType()));

        return new HttpRequest(httpMethod, requestUri, queryStrings, httpHeaders, requestBody);
    }

    private String readLine(final BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (final IOException e) {
            throw new UncheckedIOException("요청을 읽어오는데 실패했습니다.", e);
        }
    }

    private HttpMethod parseHttpMethod(final String line) {
        return HttpMethod.valueOf(line.split(REQUEST_LINE_DELIMITER)[HTTP_METHOD_INDEX]);
    }

    private String parseRequestUri(final String line) {
        final String requestUri = line.split(REQUEST_LINE_DELIMITER)[REQUEST_URI_INDEX];
        final int queryStringBeginIndex = requestUri.indexOf(QUERY_STRING_DELIMITER);
        if (queryStringBeginIndex == NON_EXIST) {
            return requestUri;
        }
        return requestUri.substring(0, queryStringBeginIndex);
    }

    private QueryStrings parseQueryStrings(final String line) {
        final QueryStrings queryStrings = new QueryStrings();
        final String requestUri = line.split(REQUEST_LINE_DELIMITER)[REQUEST_URI_INDEX];
        final int queryStringBeginIndex = requestUri.indexOf(QUERY_STRING_DELIMITER);
        if (queryStringBeginIndex == NON_EXIST) {
            return queryStrings;
        }
        final String[] splitQueryStrings = requestUri.substring(queryStringBeginIndex + 1)
                .split(QUERY_STRING_VALUE_DELIMITER);
        for (final String splitQueryString : splitQueryStrings) {
            final String[] splitKeyValue = splitQueryString.split(KEY_AND_VALUE_DELIMITER);
            final String key = splitKeyValue[KEY_INDEX];
            final String value = splitKeyValue[VALUE_INDEX];
            queryStrings.add(key, value);
        }
        return queryStrings;
    }

    private HttpHeaders parseHttpHeaders(final BufferedReader reader) {
        String line = readLine(reader);
        final HttpHeaders httpHeaders = new HttpHeaders();
        while (!EMPTY_LINE.equals(line)) {
            final String[] header = line.split(HEADER_DELIMITER);
            if (header.length == EXIST_HEADER_VALUE) {
                final String key = header[KEY_INDEX];
                final String value = header[VALUE_INDEX];
                httpHeaders.add(key, value);
            }
            line = readLine(reader);
        }
        return httpHeaders;
    }

    private Map<String, String> parseRequestBody(final BufferedReader reader, final String contentLength,
                                                 final BodyParser bodyParser) {
        if (contentLength.isEmpty()) {
            return new HashMap<>();
        }
        final int length = Integer.parseInt(contentLength);
        final char[] buffer = new char[length];
        try {
            reader.read(buffer, 0, length);
        } catch (final IOException e) {
            throw new UncheckedIOException("요청을 읽어오는데 실패했습니다.", e);
        }
        return bodyParser.parse(new String(buffer));
    }
}
