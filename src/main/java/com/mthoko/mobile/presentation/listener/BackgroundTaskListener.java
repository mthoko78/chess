package com.mthoko.mobile.presentation.listener;

import com.mthoko.mobile.exception.ApplicationException;

public interface BackgroundTaskListener<T> {
    
    void onStart();

    void onFailure(ApplicationException exception);

    void onSuccess(T object);

    void onCancelled();
}