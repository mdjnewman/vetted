package me.mdjnewman.krafty.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

private val objectMapper = ObjectMapper().findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

fun MockHttpServletRequestBuilder.withJsonBody(body: Any): MockHttpServletRequestBuilder =
    this.content(body.toJson()).contentType(APPLICATION_JSON_UTF8)

fun Any.toJson(): String {
    return me.mdjnewman.krafty.test.objectMapper.writeValueAsString(this)
}
