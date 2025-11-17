package com.easyonbid.dto.response;

import java.util.Collections;
import java.util.List;

public class SaveResult<T> {


    private final List<T> success;
    private final List<T> failure;

    public SaveResult(List<T> success, List<T> failure) {
        this.success = Collections.unmodifiableList(success);
        this.failure = Collections.unmodifiableList(failure);
    }

    public List<T> getSuccess() { return success; }

    public List<T> getFailure() { return failure; }

    public int getTotal() { return success.size() + failure.size(); }
    
    
}
