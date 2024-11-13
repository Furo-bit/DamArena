package com.project.damarena.model;

import java.util.Objects;

/**
 * <p>CheckersMove class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
public class CheckersMove {
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;
    private CheckersPawn pawn;

    /**
     * <p>Constructor for CheckersMove.</p>
     *
     * @param startRow a int
     * @param startCol a int
     * @param endRow a int
     * @param endCol a int
     */
    public CheckersMove(int startRow, int startCol, int endRow, int endCol) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
    }

    /**
     * <p>Getter for the field <code>startRow</code>.</p>
     *
     * @return a int
     */
    public int getStartRow() {
        return startRow;
    }

    /**
     * <p>Getter for the field <code>startCol</code>.</p>
     *
     * @return a int
     */
    public int getStartCol() {
        return startCol;
    }

    /**
     * <p>Getter for the field <code>endRow</code>.</p>
     *
     * @return a int
     */
    public int getEndRow() {
        return endRow;
    }

    /**
     * <p>Getter for the field <code>endCol</code>.</p>
     *
     * @return a int
     */
    public int getEndCol() {
        return endCol;
    }

    /**
     * <p>Getter for the field <code>pawn</code>.</p>
     *
     * @return a {@link com.project.damarena.model.CheckersPawn} object
     */
    public CheckersPawn getPawn() {
        return pawn;
    }

    /**
     * <p>Setter for the field <code>startRow</code>.</p>
     *
     * @param startRow a int
     */
    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    /**
     * <p>Setter for the field <code>startCol</code>.</p>
     *
     * @param startCol a int
     */
    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    /**
     * <p>Setter for the field <code>endRow</code>.</p>
     *
     * @param endRow a int
     */
    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    /**
     * <p>Setter for the field <code>endCol</code>.</p>
     *
     * @param endCol a int
     */
    public void setEndCol(int endCol) {
        this.endCol = endCol;
    }

    /**
     * <p>Setter for the field <code>pawn</code>.</p>
     *
     * @param pawn a {@link com.project.damarena.model.CheckersPawn} object
     */
    public void setPawn(CheckersPawn pawn) {
        this.pawn = pawn;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "CheckersMove{" +
                "startRow=" + startRow +
                ", startCol=" + startCol +
                ", endRow=" + endRow +
                ", endCol=" + endCol +
                '}';
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckersMove that = (CheckersMove) o;
        return startRow == that.startRow && startCol == that.startCol && endRow == that.endRow && endCol == that.endCol;
    }
}
