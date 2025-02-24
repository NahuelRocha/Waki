package com.app.waki.profile.domain.valueObject;

import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record ProfileUserId(UUID id) {

    public ProfileUserId {
        Assert.notNull(id, "id must not be null");
    }
}
