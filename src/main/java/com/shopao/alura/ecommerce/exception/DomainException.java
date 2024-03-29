package com.shopao.alura.ecommerce.exception;

public class DomainException extends RuntimeException {
  public DomainException(String message) {
    super(message);
  }
}
