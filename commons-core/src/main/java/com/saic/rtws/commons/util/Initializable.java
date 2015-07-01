package com.saic.rtws.commons.util;

import com.saic.rtws.commons.exception.InitializationException;

public interface Initializable {
    public void initialize() throws InitializationException;
    public void dispose();
}
