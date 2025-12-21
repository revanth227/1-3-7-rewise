package com.example.rewise.exceptions;

public class TopicNotFound extends RuntimeException{
    public TopicNotFound(String message){
         super(message);
    }
}
