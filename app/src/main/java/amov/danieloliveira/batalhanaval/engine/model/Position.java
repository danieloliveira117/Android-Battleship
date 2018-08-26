package amov.danieloliveira.batalhanaval.engine.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import amov.danieloliveira.batalhanaval.Consts;
import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidPositionException;

/**
 * Posições de 1 a 8, A a H
 */
public class Position {
    private boolean valid;
    private char letter;
    private int num;

    Position() {
        valid = false;
    }

    public Position(Position position) {
        this.letter = position.getLetter();
        this.num = position.getNum();
        this.valid = position.isValid();
    }

    public Position(String position) throws InvalidPositionException {
        if (position == null || position.length() != 2) {
            throw new InvalidPositionException();
        }

        this.letter = Character.toUpperCase(position.charAt(0));
        this.num = Character.getNumericValue(position.charAt(1));

        validatePosition(letter, num);
    }

    public Position(char letter, int num) throws InvalidPositionException {
        this.letter = Character.toUpperCase(letter);
        this.num = num;

        validatePosition(this.letter, this.num);
    }

    public Position(int letter, int num) throws InvalidPositionException {
        this.letter = (char) ('A' + (letter - 1));
        this.num = num;

        validatePosition(this.letter, this.num);
    }

    public Position(int position) throws InvalidPositionException {
        this.num = (position % Consts.MAXROWS) + 1;
        int temp = (position / Consts.MAXCOLUMNS);
        this.letter = (char) ('A' + temp);

        validatePosition(this.letter, this.num);
    }

    private void validatePosition(char letter, int num) throws InvalidPositionException {
        int letterAsInt = (letter - 'A') + 1;

        this.valid = false;

        if (num < 1 || num > Consts.MAXROWS) {
            throw new InvalidPositionException();
        }

        if (letterAsInt < 1 || letterAsInt > Consts.MAXCOLUMNS) {
            throw new InvalidPositionException();
        }

        this.valid = true;
    }

    public int getColor() {
        return R.color.cellColorPrimary;

        /*if ((letter + num) % 2 == 0) {
            return R.color.cellColorPrimary;
        } else {
            return R.color.cellColorAlternative;
        }*/
    }

    public int getLetterAsInt() {
        return ((int) letter - 'A') + 1;
    }

    public char getLetter() {
        return letter;
    }

    public int getNum() {
        return num;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public String toString() {
        return letter + "" + num;
    }

    public void decrementVertical() throws InvalidPositionException {
        int temp = this.num - 1;
        validatePosition(this.letter, temp);
        this.num = temp;
    }

    public void decrementHorizontal() throws InvalidPositionException {
        char temp = (char) (this.letter - 1);
        validatePosition(temp, this.num);
        this.letter = temp;
    }

    public void incrementVertical() throws InvalidPositionException {
        int temp = this.num + 1;
        validatePosition(this.letter, temp);
        this.num = temp;
    }

    public void incrementHorizontal() throws InvalidPositionException {
        char temp = (char) (this.letter + 1);
        validatePosition(temp, this.num);
        this.letter = temp;
    }

    public Set<Position> getAdjacent() {
        Set<Position> adjacentPositions = new HashSet<>();

        // TOP LEFT
        try {
            Position new_pos = new Position(this);

            new_pos.incrementVertical();
            new_pos.incrementHorizontal();

            adjacentPositions.add(new_pos);
        } catch (InvalidPositionException ignored) {
        }

        // TOP
        try {
            Position new_pos = new Position(this);

            new_pos.incrementVertical();

            adjacentPositions.add(new_pos);
        } catch (InvalidPositionException ignored) {
        }

        // TOP RIGHT
        try {
            Position new_pos = new Position(this);

            new_pos.incrementVertical();
            new_pos.decrementHorizontal();

            adjacentPositions.add(new_pos);
        } catch (InvalidPositionException ignored) {
        }

        // LEFT
        try {
            Position new_pos = new Position(this);

            new_pos.incrementHorizontal();

            adjacentPositions.add(new_pos);
        } catch (InvalidPositionException ignored) {
        }

        // RIGHT
        try {
            Position new_pos = new Position(this);

            new_pos.decrementHorizontal();

            adjacentPositions.add(new_pos);
        } catch (InvalidPositionException ignored) {
        }

        // BOTTOM LEFT
        try {
            Position new_pos = new Position(this);

            new_pos.decrementVertical();
            new_pos.incrementHorizontal();

            adjacentPositions.add(new_pos);
        } catch (InvalidPositionException ignored) {
        }

        // BOTTOM
        try {
            Position new_pos = new Position(this);

            new_pos.decrementVertical();

            adjacentPositions.add(new_pos);
        } catch (InvalidPositionException ignored) {
        }

        // BOTTOM RIGHT
        try {
            Position new_pos = new Position(this);

            new_pos.decrementVertical();
            new_pos.decrementHorizontal();

            adjacentPositions.add(new_pos);
        } catch (InvalidPositionException ignored) {
        }

        return adjacentPositions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        return letter == position.letter && num == position.num;
    }

    @Override
    public int hashCode() {
        int result = (int) letter;
        result = 31 * result + num;
        return result;
    }
}
