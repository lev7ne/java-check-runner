package ru.clevertec.check.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.config.DatabaseConfig;
import ru.clevertec.check.dao.DiscountCardDAO;
import ru.clevertec.check.dao.ProductDAO;
import ru.clevertec.check.dto.CheckRequest;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.exception.NotFoundException;
import ru.clevertec.check.service.check.CheckService;
import ru.clevertec.check.service.check.CheckServiceImpl;
import ru.clevertec.check.service.discountCard.DiscountCardServiceImpl;
import ru.clevertec.check.service.product.ProductServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet(value = "/check")
public class CheckServlet extends HttpServlet {
    private CheckService checkService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        super.init();
        this.mapper = new ObjectMapper();
        try {
            Connection conn = new DatabaseConfig().getConnection();
            this.checkService = new CheckServiceImpl(
                    new ProductServiceImpl(new ProductDAO(conn)),
                    new DiscountCardServiceImpl(new DiscountCardDAO(conn))
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CheckRequest checkRequest = mapper.readValue(req.getReader(), CheckRequest.class);

        resp.setContentType("text/csv");
        resp.setHeader("Content-Disposition", "attachment; filename=\"check.csv\"");

        try {
            File file = checkService.createCheck(checkRequest);

            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = resp.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            } finally {
                if (file.exists()) {
                    file.delete();
                }
            }

        } catch (NotEnoughMoneyException | InternalServerException | NotFoundException | BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

}
