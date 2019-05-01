package com.javarush.games.snake;

import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.*;

public class SnakeGame extends Game {
    private int turnDelay;                      // продолжительность хода
    public static final int WIDTH = 15;         // ширина игрового поля
    public static final int HEIGHT = 15;        // высота игрового поля
    private Snake snake;
    private Apple apple;
    private boolean isGameStopped;              // для проверки на окончание игры
    private static final int GOAL = 28;         // больше этого числа должна быть длина змейки для победы
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        score = 0;
        setScore(score);
        Snake snake1 = new Snake(WIDTH / 2, HEIGHT / 2);
        snake = snake1;
        createNewApple();                   // создание еды за которой охотится змейка
        isGameStopped = false;
        drawScene();
        turnDelay = 300;                    // продолжительность хода 300 мс/ход(задержка)
        setTurnTimer(turnDelay);
    }

    private void drawScene() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                setCellValueEx(i, j, Color.WHITESMOKE, "");         // устанавливаем цвет игрового поля
            }
        }
        snake.draw(this);
        apple.draw(this);
    }

    private void createNewApple() {                                                      // создание яблока с проверкой, чтобы яблока не появлялось на месте змейки
        do {
            apple = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
        } while (snake.checkCollision(apple));
    }

    private void gameOver() {                   // обработка окончания игры
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "Game over!", Color.RED, 62);
    }

    private void win() {                        // обработка победы
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "You win!", Color.YELLOW, 62);
    }

    @Override
    public void onTurn(int step) {
        snake.move(apple);
        if (apple.isAlive == false) {            // если еда не живая создаем новую еду
            createNewApple();
            score = score + 1;
            setScore(score);
            turnDelay = turnDelay - 10;
            setTurnTimer(turnDelay);
        }
        if (snake.isAlive == false) {            // если змейка не живая выводим сообщение об окончании игры
            gameOver();
        }
        if (snake.getLength() > GOAL) {          // если длинна змейки больше количества ходов победа
            win();
        }
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {                       // управление змейкой
        if (key == Key.SPACE && isGameStopped == true) {    // рестарт игры после проигрыша или победы
            createGame();
        }
        if (key == Key.LEFT) {
            snake.setDirection(Direction.LEFT);
        } else if (key == Key.RIGHT) {
            snake.setDirection(Direction.RIGHT);
        } else if (key == Key.UP) {
            snake.setDirection(Direction.UP);
        } else if (key == Key.DOWN) {
            snake.setDirection(Direction.DOWN);
        }

    }
}