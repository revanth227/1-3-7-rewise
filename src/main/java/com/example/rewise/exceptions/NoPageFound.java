package com.example.rewise.exceptions;

public class NoPageFound extends RuntimeException{
    public NoPageFound (String message){
        super(message);
    }
}
