package ru.clevertec.check.service.check;

import ru.clevertec.check.dto.CheckRequest;
import ru.clevertec.check.dto.ProductQuantity;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.exception.NotFoundException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.discountCard.DiscountCardService;
import ru.clevertec.check.service.product.ProductService;
import ru.clevertec.check.util.Calculator;
import ru.clevertec.check.util.Writer;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CheckServiceImpl implements CheckService {
    private final ProductService productService;
    private final DiscountCardService discountCardService;

    public CheckServiceImpl(
            ProductService productService,
            DiscountCardService discountCardService
    ) {
        this.productService = productService;
        this.discountCardService = discountCardService;
    }

    @Override
    public File createCheck(CheckRequest checkRequest) throws InternalServerException, NotEnoughMoneyException, BadRequestException {

        List<ProductQuantity> productQuantities = checkRequest.getProducts();
        List<Long> ids = productQuantities.stream()
                .map(ProductQuantity::getId)
                .toList();
        List<Product> products = productService.getProductByIds(ids);

        DiscountCard discountCard = null;
        if (checkRequest.getDiscountCard() != null) {
            try {
                discountCard = discountCardService.getDiscountByNumber(checkRequest.getDiscountCard());
            } catch (NotFoundException e) {
                discountCard = discountCardService.addDiscountCard(new DiscountCard(checkRequest.getDiscountCard(), 2));
            }
        }

        Map<Long, Integer> purchases = productQuantities.stream()
                .collect(Collectors.groupingBy(ProductQuantity::getId, Collectors.summingInt(ProductQuantity::getQuantity)));

        var balance = checkRequest.getBalanceDebitCard();
        var paySlips = Calculator.purchaseCalculate(
                purchases,
                products,
                discountCard,
                balance
        );

        return Writer.writeToFile(paySlips, discountCard);
    }

}
