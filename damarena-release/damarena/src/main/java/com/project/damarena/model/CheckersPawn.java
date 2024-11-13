package com.project.damarena.model;

/**
 * <p>CheckersPawn class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
public class CheckersPawn {
    private int row;
    private int col;
    private String color;

    private boolean isKing;

    /**
     * <p>Constructor for CheckersPawn.</p>
     *
     * @param row a int
     * @param col a int
     * @param color a {@link java.lang.String} object
     */
    public CheckersPawn(int row, int col, String color) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.isKing = false;
    }

    /**
     * <p>Getter for the field <code>row</code>.</p>
     *
     * @return a int
     */
    public int getRow() {
        return row;
    }

    /**
     * <p>Setter for the field <code>row</code>.</p>
     *
     * @param row a int
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * <p>Getter for the field <code>col</code>.</p>
     *
     * @return a int
     */
    public int getCol() {
        return col;
    }

    /**
     * <p>Setter for the field <code>col</code>.</p>
     *
     * @param col a int
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * <p>Getter for the field <code>color</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getColor() {
        return color;
    }

    /**
     * <p>isKing.</p>
     *
     * @return a boolean
     */
    public boolean isKing() {
        return isKing;
    }
    /**
     * <p>Setter for the field <code>isKing</code>.</p>
     *
     * @param isKing a boolean
     */
    public void setIsKing(boolean isKing) {
        this.isKing = isKing;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "CheckersPawn{" +
                "row=" + row +
                ", col=" + col +
                ", color='" + color + '\'' +
                ", isKing=" + isKing +
                '}';
    }
}
