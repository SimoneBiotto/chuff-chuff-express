package it.zuppa.chuff.exception;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.common.exception.ChuffChuffException;
import lombok.Getter;

@Getter
public class DomainException extends ChuffChuffException {
  private Class<? extends BaseEntity> domainClass;
  private final Reason reason;

  public DomainException(String message, Reason reason) {
    super(reason.message + ": " + message);
    this.reason = reason;
  }

  public DomainException(String message, Reason reason, Class<? extends BaseEntity> domainClass) {
    super(reason.message + " (" + domainClass.getName() + "): " + message);
    this.reason = reason;
    this.domainClass = domainClass;
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
