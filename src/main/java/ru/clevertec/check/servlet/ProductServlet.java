package ru.clevertec.check.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.config.DatabaseConfig;
import ru.clevertec.check.dao.ProductDAO;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotFoundException;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.product.ProductService;
import ru.clevertec.check.service.product.ProductServiceImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    ObjectMapper mapper = new ObjectMapper();
    private ProductService productService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            Connection conn = new DatabaseConfig().getConnection();
            this.productService = new ProductServiceImpl(new ProductDAO(conn));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));
        resp.setContentType("application/json");

        try {
            Product product = productService.getProductById(id);
            resp.getWriter().print(mapper.writeValueAsString(product));
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (InternalServerException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\":\"INTERNAL_SERVER_ERROR\"}");
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"message\":\"NOT_FOUND\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Product newProduct = mapper.readValue(req.getReader(), Product.class);
            productService.addProduct(newProduct);
            resp.setStatus(HttpServletResponse.SC_CREATED);

        } catch (InternalServerException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\":\"INTERNAL_SERVER_ERROR\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));
        try {
            Product updatedProduct = mapper.readValue(req.getReader(), Product.class);
            productService.updateProduct(id, updatedProduct);
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (InternalServerException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\":\"INTERNAL_SERVER_ERROR\"}");
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"message\":\"NOT_FOUND\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        try {
            productService.deleteProduct(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (InternalServerException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\":\"INTERNAL_SERVER_ERROR\"}");
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"message\":\"NOT_FOUND\"}");
        }
    }
}
