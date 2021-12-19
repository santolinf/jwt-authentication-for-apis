package com.manning.liveproject.simplysend.api.config;

import com.manning.liveproject.simplysend.api.dto.PagedResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;

import static com.manning.liveproject.simplysend.api.ApiConstants.*;

// https://www.baeldung.com/rest-api-pagination-in-spring
@ControllerAdvice
public class PageResponseHandler implements ResponseBodyAdvice<PagedResponse> {

    private static final String CUSTOM_EXPOSED_HEADERS = String.join(",",
            HEADER_LINK,
            HEADER_X_PAGE_NUMBER,
            HEADER_X_PAGE_SIZE,
            HEADER_X_TOTAL_ELEMENTS,
            HEADER_X_TOTAL_PAGES
    );

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return PagedResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public PagedResponse beforeBodyWrite(
            PagedResponse page,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        HttpHeaders headers = response.getHeaders();
        headers.set("Access-Control-Expose-Headers", CUSTOM_EXPOSED_HEADERS);
        var links = links(page, request);
        if (!links.isBlank()) {
            headers.set(HEADER_LINK, links);
        }

        var pageNumber = page.getPageNumber();
        headers.set(HEADER_X_PAGE_NUMBER, Integer.toString(pageNumber));
        headers.set(HEADER_X_PAGE_SIZE, Integer.toString(page.getPageSize()));
        headers.set(HEADER_X_TOTAL_ELEMENTS, Long.toString(page.getTotalElements()));
        headers.set(HEADER_X_TOTAL_PAGES, Integer.toString(page.getTotalPages()));

        return page;
    }
    private String links(PagedResponse page, ServerHttpRequest request) {
        var links = new ArrayList<String>();
        var builder = UriComponentsBuilder.fromUri(request.getURI());
        if (request.getURI().getHost() == "localhost") {
            builder.port(request.getURI().getPort());
        }

        if (page.getFirstPageNumber() != null) {
            var link = replacePageAndSize(builder, page.getFirstPageNumber(), page.getPageSize());
            links.add("<" + link.toUriString() + ">; rel=\"first\"");
        }

        if (page.getPreviousPageNumber() != null) {
            var link = replacePageAndSize(builder, page.getPreviousPageNumber(), page.getPageSize());
            links.add("<" + link.toUriString() + ">; rel=\"prev\"");
        }

        if (page.getNextPageNumber() != null) {
            var link = replacePageAndSize(builder, page.getNextPageNumber(), page.getPageSize());
            links.add("<" + link.toUriString() + ">; rel=\"next\"");
        }

        if (page.getLastPageNumber() != null) {
            var link = replacePageAndSize(builder, page.getLastPageNumber(), page.getPageSize());
            links.add("<$" + link.toUriString() + "}>; rel=\"last\"");
        }

        return String.join(",", links);
    }

    private UriComponentsBuilder replacePageAndSize(UriComponentsBuilder builder, Integer pageNumber, Integer pageSize) {
        var clone = builder.cloneBuilder();
        clone.replaceQueryParam("page", pageNumber);
        clone.replaceQueryParam("limit", pageSize);
        return clone;
    }
}
