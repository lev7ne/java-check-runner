package ru.clevertec.check.service.product;

import ru.clevertec.check.dao.ProductDAO;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotFoundException;
import ru.clevertec.check.model.Product;

import java.util.List;


public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public Product getProductById(long id) throws InternalServerException, NotFoundException {
        return productDAO.findById(id);
    }

    @Override
    public List<Product> getProductByIds(List<Long> ids) throws InternalServerException {
        return productDAO.findByIds(ids);
    }

    @Override
    public void addProduct(Product product) throws InternalServerException {
        productDAO.addProduct(product);
    }

    @Override
    public void updateProduct(long id, Product product) throws InternalServerException, NotFoundException {
        productDAO.updateProduct(id, product);
    }

    @Override
    public void deleteProduct(long id) throws InternalServerException, NotFoundException {
        productDAO.deleteProduct(id);
    }

}
