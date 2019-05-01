package com.javarush.games.snake;

import com.javarush.engine.cell.*;
import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;


public class Apple extends GameObject {
    public boolean isAlive = true;

    Apple(int x, int y) {
        super(x, y);
    }               // конструктор со стартовыми координатами еды

    private static final String APPLE_SIGN = "\uD83C\uDF57";

    public void draw(Game game) {
        game.setCellValueEx(x, y, Color.NONE, APPLE_SIGN, Color.BROWN, 75);

    }
}
