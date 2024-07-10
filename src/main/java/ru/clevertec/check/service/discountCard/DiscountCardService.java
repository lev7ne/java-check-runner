package ru.clevertec.check.service.discountCard;

import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotFoundException;
import ru.clevertec.check.model.DiscountCard;

public interface DiscountCardService {
    DiscountCard getDiscountCard(long id) throws NotFoundException, InternalServerException;

    DiscountCard getDiscountByNumber(int number) throws NotFoundException, InternalServerException;

    DiscountCard addDiscountCard(DiscountCard discountCard) throws InternalServerException;

    void updateDiscountCard(long id, DiscountCard discountCard) throws InternalServerException, NotFoundException;

    void deleteDiscountCard(long id) throws InternalServerException, NotFoundException;


}
