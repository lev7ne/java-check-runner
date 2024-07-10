package ru.clevertec.check.service.check;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.dto.CheckRequest;
import ru.clevertec.check.dto.ProductQuantity;
import ru.clevertec.check.exception.NotFoundException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.discountCard.DiscountCardService;
import ru.clevertec.check.service.product.ProductService;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;


class CheckServiceImplTest {
    @Mock
    private ProductService productService;

    @Mock
    private DiscountCardService discountCardService;

    @InjectMocks
    private CheckServiceImpl checkService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCheckWithValidData() throws Exception {

        var context = new CheckRequest();
        context.setProducts(Arrays.asList(new ProductQuantity(1, 2), new ProductQuantity(2, 3)));
        context.setDiscountCard(12345);
        context.setBalanceDebitCard(1000);

        List<Product> products = Arrays.asList(
                new Product(1L, "Product A", 10.0, 10, false),
                new Product(2, "Product B", 20.0, 5, true)
        );

        DiscountCard discountCard = new DiscountCard(12345, 10);

        when(productService.getProductByIds(anyList())).thenReturn(products);
        when(discountCardService.getDiscountByNumber(12345)).thenReturn(discountCard);

        File checkFile = checkService.createCheck(context);

        verify(productService).getProductByIds(anyList());
        verify(discountCardService).getDiscountByNumber(12345);
        assertNotNull(checkFile);
    }

    @Test
    public void testCreateCheckWithNewDiscountCard() throws Exception {

        var context = new CheckRequest();
        context.setProducts(Arrays.asList(new ProductQuantity(1, 2), new ProductQuantity(2, 3)));
        context.setDiscountCard(12345);
        context.setBalanceDebitCard(1000);

        List<Product> products = Arrays.asList(
                new Product(1L, "Product A", 10.0, 10, false),
                new Product(2, "Product B", 20.0, 5, true)
        );

        DiscountCard newDiscountCard = new DiscountCard(12345, 2);

        when(productService.getProductByIds(anyList())).thenReturn(products);
        when(discountCardService.getDiscountByNumber(12345)).thenThrow(new NotFoundException("Discount card not found"));
        when(discountCardService.addDiscountCard(any(DiscountCard.class))).thenReturn(newDiscountCard);

        File checkFile = checkService.createCheck(context);

        verify(productService).getProductByIds(anyList());
        verify(discountCardService).getDiscountByNumber(12345);
        verify(discountCardService).addDiscountCard(any(DiscountCard.class));
        assertNotNull(checkFile);
    }

    @Test
    public void testCreateCheckWithoutDiscountCard() throws Exception {

        var context = new CheckRequest();
        context.setProducts(Arrays.asList(new ProductQuantity(1, 2), new ProductQuantity(2, 3)));
        context.setBalanceDebitCard(1000);

        List<Product> products = Arrays.asList(
                new Product(1L, "Product A", 10.0, 10, false),
                new Product(2, "Product B", 20.0, 5, true)
        );

        when(productService.getProductByIds(anyList())).thenReturn(products);

        File checkFile = checkService.createCheck(context);

        verify(productService).getProductByIds(anyList());
        verify(discountCardService, never()).getDiscountByNumber(anyInt());
        assertNotNull(checkFile);
    }

}

