package ru.clevertec.check.util;

import org.junit.jupiter.api.Test;
import ru.clevertec.check.dto.PaySlip;
import ru.clevertec.check.model.DiscountCard;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WriterTest {
    private final Writer writer = new Writer();

    @Test
    public void testWriteToFileSuccess() throws Exception {
        var paySlips = List.of(new PaySlip(1, "description", 100.0, 3.0, 97.0));
        var discountCard = new DiscountCard(0, 1234, 3);

        File resultFile = writer.writeToFile(paySlips, discountCard);

        assertTrue(resultFile.exists());
        assertTrue(resultFile.length() > 0);
    }

    @Test
    public void testWriteToFileWithEmptyPaySlips() throws Exception {
        List<PaySlip> paySlips = List.of();
        var discountCard = new DiscountCard(0, 1234, 3);

        File resultFile = writer.writeToFile(paySlips, discountCard);

        assertNull(resultFile);
    }

}