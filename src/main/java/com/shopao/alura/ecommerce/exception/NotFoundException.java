package com.shopao.alura.ecommerce.exception;

public class NotFoundException extends DomainException {
  public NotFoundException(String message) {
    super(message);
  }
}
