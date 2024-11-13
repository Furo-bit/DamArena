package com.project.damarena.model;

public class MultiPlayerBoard {
    private Piece[][] pieces;

    public Piece[][] getPieces() {
        return pieces;
    }

    public void setupInitialPosition() {
        pieces = new Piece[8][8];
        // Initialize pieces in starting positions for player with color 'b' (black)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 != 0) {
                    pieces[row][col] = new Piece('b'); // 'b' for black
                } else {
                    pieces[row][col] = new Piece('e'); // 'e' for empty
                }
            }
        }

        // Initialize empty spaces in starting positions for player with color 'e'
        for (int row = 3; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                pieces[row][col] = new Piece('e'); // 'e' for empty
            }
        }

        for (int row = 5; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 != 0) {
                    pieces[row][col] = new Piece('w'); // 'b' for black
                } else {
                    pieces[row][col] = new Piece('e'); // 'e' for empty
                }
            }
        }
    }

    public boolean isValidMove(Move move, Player player) {
        // First, check if there are any captures available for the current player
        boolean captureAvailable = isCaptureAvailable(player.getColor());

        if (captureAvailable) {
            return isValidCaptureMove(move, player);
        } else {
            return isValidSimpleMove(move);
        }
    }

    private boolean isCaptureAvailable(char playerColor) {
        // Implement logic to check if any capture is possible for the current player
        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[row].length; col++) {
                if (pieces[row][col] != null && pieces[row][col].getPlayerColor() == playerColor) {
                    if (canCaptureFrom(row, col, playerColor)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canCaptureFrom(int row, int col, char playerColor) {
        // Define opponent color
        char opponentColor = (playerColor == 'w') ? 'b' : 'w';

        // Check all four possible capture directions
        Piece selectedPiece = pieces[row][col];
        int[][] directions;
        if (selectedPiece.isKing()) {
            directions = new int[][]{{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};
        } else {
            directions = new int[][]{{-1, 1}, {-1, -1}};
        }

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            int captureRow = row + 2 * direction[0];
            int captureCol = col + 2 * direction[1];

            if (isWithinBounds(newRow, newCol) && isWithinBounds(captureRow, captureCol)) {
                if (pieces[newRow][newCol].getPlayerColor() == opponentColor
                        && pieces[captureRow][captureCol].getPlayerColor() == 'e') {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    private boolean isValidCaptureMove(Move move, Player player) {
        System.out.println("Capture is available, checking if move is capture");
        System.out.println(move);
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        if (!isWithinBounds(startX, startY) || !isWithinBounds(endX, endY)) {
            return false;
        }

        int dx = endX - startX;
        int dy = endY - startY;

        if (Math.abs(dx) == 2 && Math.abs(dy) == 2) {
            int middleX = (startX + endX) / 2;
            int middleY = (startY + endY) / 2;
            Piece middlePiece = pieces[middleX][middleY];

            System.out.println("I'm capturin this piece");
            System.out.println("(" + middleX + ", " + middleY + ")");
            System.out.println(middlePiece);

            if (middlePiece.getPlayerColor() != 'e' && middlePiece.getPlayerColor() != player.getColor() && pieces[endX][endY].getPlayerColor() == 'e') {
                return true;
            }
        }

        return false;
    }

    private boolean isValidSimpleMove(Move move) {
        System.out.println("Checking move " + move);
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        if (!isWithinBounds(startX, startY) || !isWithinBounds(endX, endY)) {
            System.out.println("Move not valid: end not within bounds");
            return false;
        }

        Piece piece = pieces[startX][startY];
        if (piece == null) {
            System.out.println("Move not valid: piece is null");
            return false;
        }

        if (pieces[endX][endY] == null) {
            System.out.println("Move not valid: landing square is null");
            return false;
        }

        int dx = endX - startX;
        int dy = endY - startY;
        System.out.println("startX: " + startX + ", startY: " + startY);
        System.out.println("endX: " + endX + ", endY: " + endY);
        System.out.println("dx:" + dx + ", dy:" + dy);

        if (Math.abs(dx) == 1 && Math.abs(dy) == 1) {
            if (piece.isKing() && pieces[endX][endY].getPlayerColor() == 'e') {
                return true;
            } else if (dx == -1 && pieces[endX][endY].getPlayerColor() == 'e') {
                return true;
            }
        }

        return false;
    }

    public void updateBoard(Move move) {
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();

        // Move the piece
        pieces[endX][endY] = pieces[startX][startY];
        pieces[startX][startY] = new Piece('e');

        // Check if it was a capture move and remove the captured piece
        if (Math.abs(endX - startX) == 2 && Math.abs(endY - startY) == 2) {
            int midX = (startX + endX) / 2;
            int midY = (startY + endY) / 2;
            pieces[midX][midY] = new Piece('e');
        }

        if (endX == 0) {
            pieces[endX][endY].makeKing();
        }

    }

    public char hasPlayerWon(Player player) {
        System.out.println("Checking if someone won");
        int whitePieces = 0;
        int blackPieces = 0;
        Piece[][] pieces = player.getBoard().getPieces();
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if(pieces[i][j].getPlayerColor() == 'w') {
                    whitePieces++;
                }

                if(pieces[i][j].getPlayerColor() == 'b') {
                    blackPieces++;
                }
            }
        }

        System.out.println("Remaining whites: " + whitePieces);
        System.out.println("Remaining blacks: " + blackPieces);

        if (whitePieces == 0 && blackPieces != 0) {
            System.out.println("Black won");
            return 'b';
        }

        if (whitePieces != 0 && blackPieces == 0) {
            System.out.println("White won");
            return 'w';
        }

        return 'e';
    }

    public void printBoard() {
        for (Piece[] piece : pieces) {
            for (Piece value : piece) {
                if (value == null) {
                    System.out.print("[ ]");
                } else {
                    System.out.print("[" + value.getPlayerColor() + "]");
                }
            }
            System.out.println();
        }
    }

    public void flip() {
        int size = pieces.length;

        for (int x = 0; x < size / 2; x++) {
            for (int y = 0; y < size; y++) {
                // Swap pieces[x][y] with pieces[size - 1 - x][size - 1 - y]
                Piece temp = pieces[x][y];
                pieces[x][y] = pieces[size - 1 - x][size - 1 - y];
                pieces[size - 1 - x][size - 1 - y] = temp;
            }
        }
    }

    public MultiPlayerBoard getFlippedBoard() {
        MultiPlayerBoard flippedBoard = new MultiPlayerBoard();
        flippedBoard.setupInitialPosition();
        int size = pieces.length;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                flippedBoard.getPieces()[x][y] = pieces[size - 1 - x][size - 1 - y];
            }
        }

        return flippedBoard;
    }
}

