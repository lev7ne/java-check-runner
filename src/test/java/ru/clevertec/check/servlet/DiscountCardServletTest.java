package ru.clevertec.check.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotFoundException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.service.discountCard.DiscountCardService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class DiscountCardServletTest {
    @Mock
    private DiscountCardService discountCardService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private DiscountCardServlet discountCardServlet;

    private StringWriter responseWriter;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        responseWriter = new StringWriter();
        mapper = new ObjectMapper();
    }

    @Test
    public void testDoGetWithValidId() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        DiscountCard mockCard = new DiscountCard(1, 1111, 3);
        when(discountCardService.getDiscountCard(1)).thenReturn(mockCard);
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        discountCardServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        assertEquals(mapper.writeValueAsString(mockCard), responseWriter.toString());
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoGetNotFound() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        when(discountCardService.getDiscountCard(1)).thenThrow(new NotFoundException());
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        discountCardServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assertEquals("{\"message\":\"NOT_FOUND\"}", responseWriter.toString());
    }

    @Test
    public void testDoGetInternalServerError() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        when(discountCardService.getDiscountCard(1)).thenThrow(new InternalServerException());
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        discountCardServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        assertEquals("{\"message\":\"INTERNAL_SERVER_ERROR\"}", responseWriter.toString());
    }

    @Test
    public void testDoPost() throws Exception {
        DiscountCard newCard = new DiscountCard(1L, 2222, 5);
        String jsonProduct = mapper.writeValueAsString(newCard);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonProduct)));

        discountCardServlet.doPost(request, response);

        verify(discountCardService).addDiscountCard(newCard);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void testDoPutWithValidId() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        DiscountCard updatedCard = new DiscountCard(1L, 1111, 3);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(mapper.writeValueAsString(updatedCard))));
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        discountCardServlet.doPut(request, response);

        verify(discountCardService).updateDiscountCard(1L, updatedCard);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoPutNotFound() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        DiscountCard updatedCard = new DiscountCard(1, 1111, 3);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(mapper.writeValueAsString(updatedCard))));
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
        doThrow(new NotFoundException()).when(discountCardService).updateDiscountCard(1, updatedCard);

        discountCardServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assertEquals("{\"message\":\"NOT_FOUND\"}", responseWriter.toString());
    }

    @Test
    public void testDoPutInternalServerError() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        DiscountCard updatedCard = new DiscountCard(1, 1111, 3);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(mapper.writeValueAsString(updatedCard))));
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
        doThrow(new InternalServerException()).when(discountCardService).updateDiscountCard(1, updatedCard);

        discountCardServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        assertEquals("{\"message\":\"INTERNAL_SERVER_ERROR\"}", responseWriter.toString());
    }

    @Test
    public void testDoDeleteWithValidId() throws Exception {
        when(request.getParameter("id")).thenReturn("1");

        discountCardServlet.doDelete(request, response);

        verify(discountCardService).deleteDiscountCard(1);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    public void testDoDeleteNotFound() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        doThrow(new NotFoundException("NOT_FOUND")).when(discountCardService).deleteDiscountCard(1);

        discountCardServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void testDoDeleteInternalServerError() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        doThrow(new InternalServerException()).when(discountCardService).deleteDiscountCard(1);

        discountCardServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}