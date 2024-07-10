package ru.clevertec.check.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.config.DatabaseConfig;
import ru.clevertec.check.dao.DiscountCardDAO;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotFoundException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.service.discountCard.DiscountCardService;
import ru.clevertec.check.service.discountCard.DiscountCardServiceImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet("/discountcards")
public class DiscountCardServlet extends HttpServlet {
    ObjectMapper mapper = new ObjectMapper();
    private DiscountCardService discountCardService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            Connection conn = new DatabaseConfig().getConnection();
            this.discountCardService = new DiscountCardServiceImpl(new DiscountCardDAO(conn));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var discountCardId = req.getParameter("id");
        resp.setContentType("application/json");

        try {
            var id = Integer.parseInt(discountCardId);
            DiscountCard discountCard = discountCardService.getDiscountCard(id);
            resp.getWriter().print(mapper.writeValueAsString(discountCard));
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
            var newCard = mapper.readValue(req.getReader(), DiscountCard.class);
            discountCardService.addDiscountCard(newCard);
            resp.setStatus(HttpServletResponse.SC_CREATED);

        } catch (InternalServerException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\":\"INTERNAL_SERVER_ERROR\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        try {
            DiscountCard updatedCard = mapper.readValue(req.getReader(), DiscountCard.class);
            discountCardService.updateDiscountCard(id, updatedCard);
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
            discountCardService.deleteDiscountCard(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (InternalServerException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
