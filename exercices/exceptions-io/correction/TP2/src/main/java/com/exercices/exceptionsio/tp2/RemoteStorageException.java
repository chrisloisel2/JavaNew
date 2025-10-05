package com.exercices.exceptionsio.tp2;

public class RemoteStorageException extends StorageException {
    public RemoteStorageException(String message) {
        super(message);
    }

    public RemoteStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
