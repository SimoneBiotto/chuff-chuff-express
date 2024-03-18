package it.zuppa.chuff.common.valueObject;

import java.util.UUID;

public class TrainId extends BaseId<UUID> {
    protected TrainId(UUID value) {
        super(value);
    }
}
