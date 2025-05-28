package com.bmilab.backend.domain.project.enums;

public enum ProjectAccessPermission {
    NONE,
    EDIT,
    DELETE;

    public boolean isNotGranted(ProjectAccessPermission permission) {
        return this.compareTo(permission) < 0;
    }
}
