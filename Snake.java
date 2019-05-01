package com.javarush.games.snake;

import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private Direction direction = Direction.LEFT;
    public boolean isAlive = true;
    private static final String HEAD_SIGN = "\uD83D\uDC7D";
    private static final String BODY_SIGN = "\u26AB";

    Snake(int x, int y) {
        GameObject headOfSnake = new GameObject(x, y);                      // устанавливаем стартовые координаты змейки
        GameObject bodyOfSnake = new GameObject(x + 1, y);
        GameObject bodyOfSnake2 = new GameObject(x + 2, y);

        snakeParts.add(headOfSnake);                                        // добавляем части змейки в коллекцию
        snakeParts.add(bodyOfSnake);
        snakeParts.add(bodyOfSnake2);
    }

    private List<GameObject> snakeParts = new ArrayList<GameObject>();

    public void draw(Game game) {
        game.setCellValueEx(snakeParts.get(0).x, snakeParts.get(0).y, Color.NONE, HEAD_SIGN, isAlive ? Color.BLACK : Color.RED, 75); // у умершей змейки цвет красный у живой черный
        for (int i = 1; i < snakeParts.size(); i++) {
            game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, isAlive ? Color.BLACK : Color.RED, 75);
        }
    }

    public void setDirection(Direction direction) {
        if (direction == Direction.LEFT && this.direction == Direction.RIGHT) {         // проверки чтобы змейка не могла разворачиваться на 180 градусов
            return;
        }
        if (direction == Direction.UP && this.direction == Direction.DOWN) {
            return;
        }
        if (direction == Direction.RIGHT && this.direction == Direction.LEFT) {
            return;
        }
        if (direction == Direction.DOWN && this.direction == Direction.UP) {
            return;
        }

        switch (this.direction) {                                                       // проверка чтобы игнорировались несколько нажатий за один ход
            case LEFT:
            case RIGHT:
                if (snakeParts.get(0).x == snakeParts.get(1).x) return;
                break;
            case UP:
            case DOWN:
                if (snakeParts.get(0).y == snakeParts.get(1).y) return;
                break;
        }

        this.direction = direction;                                                     // устанавливаем новое направление движения
    }

    public void move(Apple apple) {
        GameObject newHead = createNewHead();

        if (newHead.x < 0 || newHead.x >= SnakeGame.WIDTH || newHead.y < 0 || newHead.y >= SnakeGame.HEIGHT) {          // если змейка вышла за границы поля она умирает
            isAlive = false;
        } else if (checkCollision(newHead)) {                                    // проверяем созданную голову на соответствие координатам змеи
            isAlive = false;
        } else {
            if (apple.x == newHead.x && apple.y == newHead.y) {
                apple.isAlive = false;
                snakeParts.add(0, newHead);
            } else {
                snakeParts.add(0, newHead);                               // если змейка в границах поля присваиваем новые координаты головы,
                removeTail();                                                   // удаляем хвост
            }
        }
    }

    public GameObject createNewHead() {                                      // рисуем новую голову во время движения
        GameObject newGameObject = null;
        if (direction == Direction.LEFT) {
            newGameObject = new GameObject(snakeParts.get(0).x - 1, snakeParts.get(0).y);
        } else if (direction == Direction.RIGHT) {
            newGameObject = new GameObject(snakeParts.get(0).x + 1, snakeParts.get(0).y);
        } else if (direction == Direction.UP) {
            newGameObject = new GameObject(snakeParts.get(0).x, snakeParts.get(0).y - 1);
        } else if (direction == Direction.DOWN) {
            newGameObject = new GameObject(snakeParts.get(0).x, snakeParts.get(0).y + 1);
        }
        return newGameObject;
    }

    public void removeTail() {                                               // удаляем последний элемент змейки
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject gameObject) {                    // проверка на соответствия координатам тела змейки
        for (GameObject iter : snakeParts) {
            if (iter.x == gameObject.x && iter.y == gameObject.y) {
                return true;
            }
        }
        return false;
    }

    public int getLength() {
        return snakeParts.size();
    }
}
