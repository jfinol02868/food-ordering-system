package com.food.ordering.system.domain.valueobject;

import java.util.UUID;

public class OderId extends BaseId<UUID>{

    public OderId(UUID value) {
        super(value);
    }
}
