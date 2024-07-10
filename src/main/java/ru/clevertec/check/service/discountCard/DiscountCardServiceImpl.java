package ru.clevertec.check.service.discountCard;

import ru.clevertec.check.dao.DiscountCardDAO;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotFoundException;
import ru.clevertec.check.model.DiscountCard;

public class DiscountCardServiceImpl implements DiscountCardService {
    private final DiscountCardDAO discountCardDAO;

    public DiscountCardServiceImpl(DiscountCardDAO discountCardDAO) {
        this.discountCardDAO = discountCardDAO;
    }

    @Override
    public DiscountCard getDiscountCard(long id) throws NotFoundException, InternalServerException {
        return discountCardDAO.findById(id);
    }

    @Override
    public DiscountCard getDiscountByNumber(int number) throws NotFoundException, InternalServerException {
        return discountCardDAO.findByNumber(number);
    }

    @Override
    public DiscountCard addDiscountCard(DiscountCard discountCard) throws InternalServerException {
        return discountCardDAO.addDiscountCard(discountCard);
    }

    @Override
    public void updateDiscountCard(long id, DiscountCard discountCard) throws InternalServerException, NotFoundException {
        DiscountCard update = discountCardDAO.findById(id);

        discountCardDAO.updateDiscountCard(id, update);
    }

    @Override
    public void deleteDiscountCard(long id) throws InternalServerException, NotFoundException {
        discountCardDAO.deleteDiscountCard(id);
    }


}
