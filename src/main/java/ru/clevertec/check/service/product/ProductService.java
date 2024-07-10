package ru.clevertec.check.service.product;

import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotFoundException;
import ru.clevertec.check.model.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(long id) throws InternalServerException, NotFoundException;

    List<Product> getProductByIds(List<Long> ids) throws InternalServerException;

    void addProduct(Product product) throws InternalServerException;

    void updateProduct(long id, Product product) throws InternalServerException, NotFoundException;

    void deleteProduct(long id) throws InternalServerException, NotFoundException;
}
