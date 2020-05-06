package model;

public class Cell {
    public int x;
    public int y;
    public int color;
    public Checker checker;

    public Cell(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
    }

    public Checker getChecker() {
        return checker;
    }

    public boolean hasChecker() {
        return getChecker() != null;
    }
}