package ru.clevertec.check.service.check;

import ru.clevertec.check.dto.CheckRequest;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.exception.NotFoundException;

import java.io.File;


public interface CheckService {
    File createCheck(CheckRequest checkRequest)
            throws InternalServerException, NotEnoughMoneyException, NotFoundException, BadRequestException;
}
