package com.nnk.springboot.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;

/**
 * Configure {@link com.nnk.springboot.annotations.Password @Password } Annotation
 * with constraints :
 * at least 8 characters, 1 uppercase, 1 lowercase, 1 number, and 1 special character
 */
public class PasswordConstraintsValidator implements ConstraintValidator<Password, String> {

    private String message;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.message = constraintAnnotation.message(); // get the message in annotation parameter
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {

        PasswordValidator passwordValidator = new PasswordValidator(
            Arrays.asList(
                new LengthRule(8, 128),  //Length rule. Min 10 max 128 characters
                new CharacterRule(EnglishCharacterData.UpperCase, 1), //At least one upper case letter
                new CharacterRule(EnglishCharacterData.LowerCase, 1), //At least one lower case letter
                new CharacterRule(EnglishCharacterData.Digit, 1), //At least one number
                new CharacterRule(EnglishCharacterData.Special, 1), //At least one special characters
                new WhitespaceRule() // rejects passwords that contain whitespace characters
            )
        );

        RuleResult result = passwordValidator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }


        //Sending the message if failed validation.
        constraintValidatorContext
                .buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;

    }
}
