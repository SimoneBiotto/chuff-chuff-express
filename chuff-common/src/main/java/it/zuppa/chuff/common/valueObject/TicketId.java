package it.zuppa.chuff.common.valueObject;

import java.util.UUID;

public class TicketId extends BaseId<UUID> {
    protected TicketId(UUID value) {
        super(value);
    }
}
