package com.sleepy4k.practice.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@Service
public class ValidationService {
  @Autowired
  private Validator validator;

  public void validate(Object request) {
    Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);

    if (constraintViolations.size() > 0) {
      throw new ConstraintViolationException(constraintViolations);
    }
  }
}
