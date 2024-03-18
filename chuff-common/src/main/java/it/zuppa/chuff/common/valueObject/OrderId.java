package it.zuppa.chuff.common.valueObject;

import java.util.UUID;

public class OrderId extends BaseId<UUID> {
    protected OrderId(UUID value) {
        super(value);
    }
}
