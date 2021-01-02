package com.rhdigital.rhclient.common.interfaces;

import com.rhdigital.rhclient.database.model.Program;

@FunctionalInterface
public interface OnClickCallback {
    public void invoke(Object object);
}
