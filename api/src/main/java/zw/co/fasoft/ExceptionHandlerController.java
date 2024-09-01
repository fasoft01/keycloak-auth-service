package zw.co.fasoft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import zw.co.fasoft.exceptions.*;
import zw.co.fasoft.exceptions.Error;

import java.nio.file.FileAlreadyExistsException;

import static zw.co.fasoft.exceptions.Error.of;

/**
 * @author Fasoft
 * @date 30/May/2024
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class ExceptionHandlerController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);




    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    Error recordNotFoundError(RecordNotFoundException e) {
        LOGGER.info("Record not found error: {}", e.getMessage());
        return Error.of(404, e.getMessage());
    }

    @ExceptionHandler(AccountNotFullySetupException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody
    Error accountNotFullySetup(AccountNotFullySetupException e) {
        LOGGER.info("Account not fully setup");
        return Error.of(422, e.getMessage());
    }

    @ExceptionHandler(FileAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    Error recordAlreadyExistError(FileAlreadyExistsException e) {
        LOGGER.info("File already exist error: {}", e.getMessage());
        return Error.of(409, e.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    Error httpClientErrorException(HttpClientErrorException e) {
        LOGGER.info("Error occured while processing request: Details {}", e.getMessage());
        return Error.of(400, e.getMessage());
    }


    @ExceptionHandler(DocumentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    Error documentNotFoundException(DocumentNotFoundException e) {
        LOGGER.info("Document does not exist error: {}", e.getMessage());
        return Error.of(404, e.getMessage());
    }


    @ExceptionHandler(IncorrectUsernameOrPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody
    Error incorrectUsernameOrPassword(IncorrectUsernameOrPasswordException e) {
        LOGGER.info("Incorrect Username or Password : {}", e.getMessage());
        return Error.of(401, e.getMessage());
    }

    @ExceptionHandler(FailedToProcessRequestException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public @ResponseBody
    Error failedToProcessRequest(FailedToProcessRequestException e) {
        LOGGER.info("Failed to process Request :{}", e.getMessage());
        return Error.of(408, e.getMessage());
    }


    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    Error emailAlreadyExistsException(EmailAlreadyExistsException e) {
        LOGGER.info("Email Already exists:{}", e.getMessage());
        return Error.of(409, e.getMessage());
    }

    @ExceptionHandler(IncorrectFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    Error incorrectFormatException(IncorrectFormatException e) {
        LOGGER.info("Incorrect Format:{}", e.getMessage());
        return Error.of(400, e.getMessage());
    }
}