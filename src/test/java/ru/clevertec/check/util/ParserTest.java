package ru.clevertec.check.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.dto.ArgsContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {
    @Mock
    private Writer mockWriter;

    private Parser parser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        parser = new Parser(mockWriter);
    }

    @Test
    void testParseArgsSuccessful() {
        String[] args = {
                "1-2",
                "2-3",
                "discountCard=1234",
                "balanceDebitCard=500.50",
                "saveToFile=output.txt",
                "datasource.url=jdbc:db",
                "datasource.username=user",
                "datasource.password=pass"
        };

        ArgsContext context = parser.parseArgs(args);

        assertEquals(2, context.getPurchases().size());
        assertEquals(1234, context.getDiscountCard());
        assertEquals(500.50, context.getBalanceDebitCard());
        assertEquals("output.txt", context.getSaveToFile());
        assertEquals("jdbc:db", context.getDatasource().get("datasource.url"));
        assertEquals("user", context.getDatasource().get("datasource.username"));
        assertEquals("pass", context.getDatasource().get("datasource.password"));
    }

    @Test
    void testParseArgsNoArgs() {
        String[] args = {};

        assertThrows(RuntimeException.class, () -> parser.parseArgs(args));
    }

    @Test
    void testParseArgsInvalidDiscountCard() {
        String[] args = {"discountCard=12ab"};

        assertThrows(RuntimeException.class, () -> parser.parseArgs(args));
    }

    @Test
    void testParseArgsInvalidBalanceDebitCard() {
        String[] args = {"balanceDebitCard=abc"};

        assertThrows(RuntimeException.class, () -> parser.parseArgs(args));
    }

    @Test
    void testParseArgsUnknownParameter() {
        String[] args = {"unknownParam=value"};

        assertThrows(RuntimeException.class, () -> parser.parseArgs(args));
    }

    @Test
    void testParseArgsNoIdQuantity() {
        String[] args = {
                "discountCard=1234",
                "balanceDebitCard=500.50",
                "saveToFile=output.txt",
                "datasource.url=jdbc:db",
                "datasource.username=user",
                "datasource.password=pass"
        };

        assertThrows(RuntimeException.class, () -> parser.parseArgs(args));
    }

}