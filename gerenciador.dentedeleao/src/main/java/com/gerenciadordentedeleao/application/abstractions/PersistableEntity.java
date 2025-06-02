package com.gerenciadordentedeleao.application.abstractions;

import java.util.UUID;

public interface PersistableEntity {

    UUID getId();

    boolean setAsDeleted();

}
