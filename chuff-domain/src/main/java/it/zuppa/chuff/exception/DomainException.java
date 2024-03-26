package it.zuppa.chuff.exception;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.common.exception.ChuffChuffException;

public class DomainException extends ChuffChuffException {
  private Class<? extends BaseEntity> domainClass;
  private Reason reason;

  public DomainException(String message, Reason reason) {
    super(reason.message + ": " + message);
  }

  public DomainException(String message, Reason reason, Class<? extends BaseEntity> domainClass) {
    super(reason.message + " (" + domainClass.getName() + "): " + message);
  }

  public enum Reason {
    DUPLICATE_RESOURCE("Resource Duplicated"),
    RESOURCE_NOT_FOUND("Resource Not Found"),
    GENERIC_ERROR("Generic Error");

    public final String message;

    Reason(String s) {
      message = s;
    }
  }
}
