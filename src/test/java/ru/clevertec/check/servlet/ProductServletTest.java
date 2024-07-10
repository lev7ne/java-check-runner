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
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.product.ProductService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductServletTest {
    @Mock
    private ProductService productService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private ProductServlet productServlet;

    private StringWriter responseWriter;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        responseWriter = new StringWriter();
        mapper = new ObjectMapper();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    public void testDoGetWithValidId() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        Product product = new Product(1L, "Product B", 20.0, 5, true);
        when(productService.getProductById(1L)).thenReturn(product);

        productServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(mapper.writeValueAsString(product), responseWriter.toString());
    }

    @Test
    public void testDoGetWithNotFound() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        when(productService.getProductById(1)).thenThrow(new NotFoundException("Product not found"));

        productServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assertEquals("{\"message\":\"NOT_FOUND\"}", responseWriter.toString());
    }

    @Test
    public void testDoPostWithValidData() throws Exception {
        Product newProduct = new Product("Product B", 20.0, 5, true);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(mapper.writeValueAsString(newProduct))));

        productServlet.doPost(request, response);

        verify(productService).addProduct(any(Product.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void testDoPostWithInternalServerError() throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{}")));
        doThrow(new InternalServerException("Error")).when(productService).addProduct(any(Product.class));

        productServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        assertEquals("{\"message\":\"INTERNAL_SERVER_ERROR\"}", responseWriter.toString());
    }

    @Test
    public void testDoPutWithValidData() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        Product updatedProduct = new Product(1L, "Product B", 20.0, 5, true);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(mapper.writeValueAsString(updatedProduct))));

        productServlet.doPut(request, response);

        verify(productService).updateProduct(eq(1L), any(Product.class));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoPutWithNotFound() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{}")));
        doThrow(new NotFoundException("NOT_FOUND")).when(productService).updateProduct(eq(1L), any(Product.class));

        productServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assertEquals("{\"message\":\"NOT_FOUND\"}", responseWriter.toString());
    }

    @Test
    public void testDoDeleteWithValidId() throws Exception {
        when(request.getParameter("id")).thenReturn("1");

        productServlet.doDelete(request, response);

        verify(productService).deleteProduct(1);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    public void testDoDeleteWithNotFound() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        doThrow(new NotFoundException("Product not found")).when(productService).deleteProduct(1);

        productServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assertEquals("{\"message\":\"NOT_FOUND\"}", responseWriter.toString());
    }

    @Test
    public void testDoGetWithInternalServerError() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        doThrow(new InternalServerException("Error")).when(productService).getProductById(1);

        productServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        assertEquals("{\"message\":\"INTERNAL_SERVER_ERROR\"}", responseWriter.toString());
    }
}