package amov.danieloliveira.batalhanaval.engine.model;

import amov.danieloliveira.batalhanaval.Consts;
import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidPositionException;

/**
 * Posições de 1 a 8, A a H
 */
public class Position {
    private char letter;
    private int num;
    private int color;

    public Position() {}

    public Position(String position) throws InvalidPositionException {
        if (position == null || position.length() != 2) {
            throw new InvalidPositionException();
        }

        this.letter = Character.toUpperCase(position.charAt(0));
        this.num = Character.getNumericValue(position.charAt(1));

        validatePosition(letter, num);

        updateColor();
    }

    public Position(char letter, int num) throws InvalidPositionException {
        this.letter = Character.toUpperCase(letter);
        this.num = num;

        validatePosition(this.letter, this.num);

        updateColor();
    }

    public Position(int letter, int num) throws InvalidPositionException {
        this.letter = (char) ('A' + (letter - 1));
        this.num = num;

        validatePosition(this.letter, this.num);

        updateColor();
    }

    public void validatePosition(char letter, int num) throws InvalidPositionException {
        int letterAsInt = (letter - 'A') + 1;

        if (num < 1 || num > Consts.MAXROWS) {
            throw new InvalidPositionException();
        }

        if (letterAsInt < 1 || letterAsInt > Consts.MAXCOLUMNS) {
            throw new InvalidPositionException();
        }
    }

    private void updateColor() {
        if ((letter + num) % 2 == 0) {
            color = R.color.cellColorPrimary;
        } else {
            color = R.color.cellColorAlternative;
        }
    }

    public int getColor() {
        return color;
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

    @Override
    public String toString() {
        return letter + "" + num;
    }
}
