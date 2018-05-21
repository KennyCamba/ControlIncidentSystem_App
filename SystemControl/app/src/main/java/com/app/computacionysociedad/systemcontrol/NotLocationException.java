package com.app.computacionysociedad.systemcontrol;

public class NotLocationException extends Exception {

    public NotLocationException(){
        super("Single exception: not location found");
    }

    public NotLocationException(String message){
        super(message);
    }
}