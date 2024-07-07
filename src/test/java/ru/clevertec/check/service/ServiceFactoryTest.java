package ru.clevertec.check.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ServiceFactoryTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetConsoleCheckService() {
        String[] args = {
                "1-2",
                "discountCard=1234",
                "balanceDebitCard=500.50",
                "saveToFile=output.txt",
                "datasource.url=test",
                "datasource.username=test",
                "datasource.password=test"
        };

        CheckService checkService =
                ServiceFactory.getConsoleCheckService(args);

        assertNotNull(checkService, "Объект checkService не должен быть равен null");
        assertInstanceOf(ConsoleCheckService.class, checkService,
                "Объект checkService должен быть instanceof ConsoleCheckService");
    }

}