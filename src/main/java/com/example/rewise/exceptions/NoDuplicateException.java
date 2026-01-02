package com.example.rewise.exceptions;

public class NoDuplicateException extends RuntimeException{
    public NoDuplicateException(String message){
        super(message);
    }
}
