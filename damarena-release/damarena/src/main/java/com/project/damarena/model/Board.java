package com.project.damarena.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>Board class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
public class Board {

    /** Constant <code>NOT_VALID='/'</code> */
    public static final char NOT_VALID = '/';
    /** Constant <code>EMPTY='e'</code> */
    public static final char EMPTY = 'e';

    public static char PLAYER_COLOR;
    public static char PLAYER_COLOR_KING;
    public static char OPPONENT_COLOR;
    public static char OPPONENT_COLOR_KING;

    private char[][] board;
    private HashMap<String, CheckersPawn> pawnsPositions = new HashMap<>();

    public Board(char PLAYER_COLOR, char PLAYER_COLOR_KING, char OPPONENT_COLOR, char OPPONENT_COLOR_KING) {
        this.PLAYER_COLOR = PLAYER_COLOR;
        this.PLAYER_COLOR_KING = PLAYER_COLOR_KING;
        this.OPPONENT_COLOR = OPPONENT_COLOR;
        this.OPPONENT_COLOR_KING = OPPONENT_COLOR_KING;

        char[][] board = new char[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 1) {
                    if (i < 3) {
                        board[i][j] = OPPONENT_COLOR;
                        pawnsPositions.put("(" + i + "," + j + ")", new CheckersPawn(i, j, "OPPONENT_COLOR"));
                    } else if (i > 4) {
                        board[i][j] = PLAYER_COLOR;
                        pawnsPositions.put("(" + i + "," + j + ")", new CheckersPawn(i, j, "PLAYER_COLOR"));
                    } else {
                        board[i][j] = EMPTY;
                    }
                } else {
                    board[i][j] = NOT_VALID;
                }
            }
        }
        this.board = board;

    }

    /**
     * <p>Getter for the field <code>board</code>.</p>
     *
     * @return an array of {@link char} objects
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * <p>Setter for the field <code>board</code>.</p>
     *
     * @param board an array of {@link char} objects
     */
    public void setBoard(char[][] board) {
        this.board = board;
    }

    public char getPLAYER_COLOR() {
        return PLAYER_COLOR;
    }

    public char getPLAYER_COLOR_KING() {
        return PLAYER_COLOR_KING;
    }

    public char getOPPONENT_COLOR() {
        return OPPONENT_COLOR;
    }

    public char getOPPONENT_COLOR_KING() {
        return OPPONENT_COLOR_KING;
    }

    public void setPLAYER_COLOR(char PLAYER_COLOR) {
        this.PLAYER_COLOR = PLAYER_COLOR;
    }

    public void setPLAYER_COLOR_KING(char PLAYER_COLOR_KING) {
        this.PLAYER_COLOR_KING = PLAYER_COLOR_KING;
    }

    public void setOPPONENT_COLOR(char OPPONENT_COLOR) {
        this.OPPONENT_COLOR = OPPONENT_COLOR;
    }

    public void setOPPONENT_COLOR_KING(char OPPONENT_COLOR_KING) {
        this.OPPONENT_COLOR_KING = OPPONENT_COLOR_KING;
    }

    public HashMap<String, CheckersPawn> getPawnsPositions() {
        return pawnsPositions;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                string.append(this.board[i][j]).append(" ");
            }
            string.append("\n");
        }

        return string.toString();
    }

    /**
     * <p>isCellFree.</p>
     *
     * @param row a int
     * @param col a int
     * @return a boolean
     */
    public boolean isCellFree(int row, int col) {
        if (row < 0 || row > 7)
            return false;
        if (col < 0 || col > 7)
            return false;

        return !pawnsPositions.containsKey("(" + row + "," + col + ")");
    }

    /**
     * <p>getPawnAtPosition.</p>
     *
     * @param row a int
     * @param col a int
     * @return a {@link com.project.damarena.model.CheckersPawn} object
     */
    public CheckersPawn getPawnAtPosition(int row, int col) {
        return pawnsPositions.get("(" + row + "," + col + ")");
    }

    public void updateBoard(CheckersMove move) {
        int startingCol = move.getStartCol();
        int startingRow = move.getStartRow();
        int endCol = move.getEndCol();
        int endRow = move.getEndRow();
        CheckersPawn pawn = getPawnAtPosition(startingRow, startingCol);
        String color = pawn.getColor();

        board[startingRow][startingCol] = EMPTY;
        board[endRow][endCol] = color.equals("OPPONENT_COLOR") ? OPPONENT_COLOR : PLAYER_COLOR;

        if (Math.abs(endRow - startingRow) == 2) {
            int takenPawnRow = (endRow + startingRow) / 2;
            int takenPawnCol = (endCol + startingCol) / 2;
            pawnsPositions.remove("(" + takenPawnRow + "," + takenPawnCol + ")");
            board[takenPawnRow][takenPawnCol] = EMPTY;
        }

        pawn.setRow(endRow);
        pawn.setCol(endCol);
        if ((endRow == 0 || pawn.isKing()) && pawn.getColor().equals("PLAYER_COLOR")) {
            pawn.setIsKing(true);
            board[endRow][endCol] = PLAYER_COLOR_KING;
        }

        if ((endRow == 7 || pawn.isKing()) && pawn.getColor().equals("OPPONENT_COLOR")) {
            pawn.setIsKing(true);
            board[endRow][endCol] = OPPONENT_COLOR_KING;
        }

        pawnsPositions.remove("(" + startingRow + "," + startingCol + ")");
        pawnsPositions.put("(" + endRow + "," + endCol + ")", pawn);
    }

    /**
     * <p>makeRandomMove.</p>
     *
     * @return a {@link com.project.damarena.model.CheckersMove} object
     */
    public CheckersMove makeRandomMove() {
        ArrayList<CheckersMove> possibleTakes = new ArrayList<>();
        ArrayList<CheckersMove> possibleMoves = new ArrayList<>();

        for (CheckersPawn pawn : pawnsPositions.values()) {
            if (pawn.getColor().equals("OPPONENT_COLOR")) {
                //check for possible takes
                if (isCellFree(pawn.getRow() + 2, pawn.getCol() + 2)) {
                    CheckersPawn takenPawn = getPawnAtPosition(pawn.getRow() + 1, pawn.getCol() + 1);
                    if (takenPawn != null && takenPawn.getColor().equals("PLAYER_COLOR")) {
                        possibleTakes.add(new CheckersMove(pawn.getRow(), pawn.getCol(), pawn.getRow() + 2, pawn.getCol() + 2));
                    }
                } else if (isCellFree(pawn.getRow() + 2, pawn.getCol() - 2)) {
                    CheckersPawn takenPawn = getPawnAtPosition(pawn.getRow() + 1, pawn.getCol() - 1);
                    if (takenPawn != null && takenPawn.getColor().equals("PLAYER_COLOR")) {
                        possibleTakes.add(new CheckersMove(pawn.getRow(), pawn.getCol(), pawn.getRow() + 2, pawn.getCol() - 2));
                    }
                }

                if (pawn.isKing()) {
                    if (isCellFree(pawn.getRow() - 2, pawn.getCol() - 2)) {
                        CheckersPawn takenPawn = getPawnAtPosition(pawn.getRow() - 1, pawn.getCol() - 1);
                        if (takenPawn != null && takenPawn.getColor().equals("PLAYER_COLOR")) {
                            possibleTakes.add(new CheckersMove(pawn.getRow(), pawn.getCol(), pawn.getRow() - 2, pawn.getCol() - 2));
                        }
                    } else if (isCellFree(pawn.getRow() - 2, pawn.getCol() + 2)) {
                        CheckersPawn takenPawn = getPawnAtPosition(pawn.getRow() - 1, pawn.getCol() + 1);
                        if (takenPawn != null && takenPawn.getColor().equals("PLAYER_COLOR")) {
                            possibleTakes.add(new CheckersMove(pawn.getRow(), pawn.getCol(), pawn.getRow() - 2, pawn.getCol() + 2));
                        }
                    }

                }

                //check for regular moves
                if (isCellFree(pawn.getRow() + 1, pawn.getCol() + 1))
                    possibleMoves.add(new CheckersMove(pawn.getRow(), pawn.getCol(), pawn.getRow() + 1, pawn.getCol() + 1));

                if (isCellFree(pawn.getRow() + 1, pawn.getCol() - 1))
                    possibleMoves.add(new CheckersMove(pawn.getRow(), pawn.getCol(), pawn.getRow() + 1, pawn.getCol() - 1));

                if (pawn.isKing()) {
                    if (isCellFree(pawn.getRow() - 1, pawn.getCol() - 1))
                        possibleMoves.add(new CheckersMove(pawn.getRow(), pawn.getCol(), pawn.getRow() - 1, pawn.getCol() - 1));

                    if (isCellFree(pawn.getRow() - 1, pawn.getCol() + 1))
                        possibleMoves.add(new CheckersMove(pawn.getRow(), pawn.getCol(), pawn.getRow() - 1, pawn.getCol() + 1));
                }
            }
        }

        System.out.println("#possible computer takes = " + possibleTakes.size());
        for (CheckersMove move :
                possibleTakes) {
            System.out.println(move.toString());
        }
        if (!possibleTakes.isEmpty()) {
            CheckersMove moveToTake = possibleTakes.get((int) (Math.random() * possibleTakes.size()));
            System.out.println("This is the move I will make: ");
            System.out.println(moveToTake.toString());
            int takenPawnRow = (moveToTake.getEndRow() + moveToTake.getStartRow()) / 2;
            int takenPawnCol = (moveToTake.getEndCol() + moveToTake.getStartCol()) / 2;
            System.out.println("Coordinates of the pawn I'm taking\nrow= " + takenPawnRow + " col= " + takenPawnCol);
            CheckersPawn takenPawn = getPawnAtPosition(takenPawnRow, takenPawnCol);
            pawnsPositions.remove("(" + takenPawn.getRow() + "," + takenPawn.getCol() + ")", takenPawn);
            board[takenPawn.getRow()][takenPawn.getCol()] = EMPTY;
            System.out.println("Gulp!");
            return moveToTake;
        }

        if (possibleMoves.isEmpty())
            return null;

        return possibleMoves.get((int) (Math.random() * possibleMoves.size()));
    }

    /**
     * <p>restartBoard.</p>
     */
    public void restartBoard() {
        pawnsPositions.clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 1) {
                    if (i < 3) {
                        board[i][j] = OPPONENT_COLOR;
                        pawnsPositions.put("(" + i + "," + j + ")", new CheckersPawn(i, j, "OPPONENT_COLOR"));
                    } else if (i > 4) {
                        board[i][j] = PLAYER_COLOR;
                        pawnsPositions.put("(" + i + "," + j + ")", new CheckersPawn(i, j, "PLAYER_COLOR"));
                    } else {
                        board[i][j] = EMPTY;
                    }
                } else {
                    board[i][j] = NOT_VALID;
                }
            }
        }

    }
}
//(✿◕‿◕✿)
