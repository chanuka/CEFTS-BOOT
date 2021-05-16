package com.epic.cefts.handler;

import java.io.Serializable;

public class Message implements Serializable {
    public int number;

    public Message(int number) {
        this.number = number;
    }
}
