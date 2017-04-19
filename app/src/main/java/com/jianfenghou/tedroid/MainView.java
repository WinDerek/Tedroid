package com.jianfenghou.tedroid;

import android.content.Context;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

public class MainView extends View {
    private Paint paint = new Paint();
    private float measuredWidth;
    private float measuredHeight;

    private boolean[][] board = new boolean[20][10];

    public int getCurrentX() {
        return currentX;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public int getCurrentType() {
        return currentType;
    }

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
    }

    public int getCurrentRotateTimes() {
        return currentRotateTimes;
    }

    public void setCurrentRotateTimes(int currentRotateTimes) {
        this.currentRotateTimes = currentRotateTimes;
    }

    // The information of the current block
    private int currentX;
    private int currentY;
    private int currentType;
    private int currentRotateTimes;
    private List<Coordinate> currentCoordinates = new ArrayList<Coordinate>();

    public MainView(Context context) {
        super(context);
    }

    public MainView(Context context, AttributeSet a) {
        super(context, a);
    }

    public MainView(Context context, AttributeSet a, int b) {
        super(context, a, b);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the background
        canvas.drawColor(getResources().getColor(R.color.color_main_view_background));

        // Draw the small pixels
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                // Change the color of the paint
                if (board[i][j]) {
                    this.paint.setColor(this.getResources().getColor(R.color.color_solid_pixel));
                } else {
                    this.paint.setColor(this.getResources().getColor(R.color.color_empty_pixel));
                }

                // Draw the rectangle using the paint
                this.drawRectAtPosition(i, j, canvas, paint);
            }
        }
    }

    private void drawRectAtPosition(int i, int j, Canvas canvas, Paint paint) {
        /* Draws the corresponding rectangle with the position (i, j) using the paint */

        // Calculate the unit of length
        float unit = 0.0f;
        float margin = 0.0f;
        if (this.measuredHeight >= this.measuredWidth * 2) {
            unit = this.measuredWidth / 10;
            margin = this.measuredWidth / 100;
        } else {
            unit = this.measuredHeight / 20;
            margin = this.measuredHeight / 200;
        }

        float left = unit * j;
        float top = unit * i;
        float right = unit * (j + 1);
        float bottom = unit * (i + 1);
        RectF rectF = new RectF(left + margin, top + margin, right - margin, bottom - margin);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectF, paint);

        Path path = new Path();
        path.moveTo(left + margin * 1 / 3, top + margin * 1 / 3);
        path.lineTo(right - margin * 1 / 3, top + margin * 1 / 3);
        path.lineTo(right - margin * 1 / 3, bottom - margin * 1 / 3);
        path.lineTo(left + margin * 1 / 3, bottom - margin * 1 / 3);
        path.lineTo(left + margin * 1 / 3, top + margin * 1 / 3);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Get the measured size of the view
        this.measuredWidth = this.getMeasuredWidth();
        this.measuredHeight = this.getMeasuredHeight();
    }

    public void normalDown() {
        this.currentX += 1;
        for (Coordinate coordinate : this.currentCoordinates) {
            this.board[coordinate.getX()][coordinate.getY()] = false;
        }
        for (Coordinate coordinate : this.currentCoordinates) {
            coordinate.setX(coordinate.getX() + 1);
            this.board[coordinate.getX()][coordinate.getY()] = true;
        }

        this.invalidate();
    }

    public void moveLeft() {
        this.currentY -= 1;
        for (Coordinate coordinate : this.currentCoordinates) {
            this.board[coordinate.getX()][coordinate.getY()] = false;
        }
        for (Coordinate coordinate : this.currentCoordinates) {
            coordinate.setY(coordinate.getY() - 1);
            this.board[coordinate.getX()][coordinate.getY()] = true;
        }

        this.invalidate();
    }

    public void moveRight() {
        this.currentY += 1;
        for (Coordinate coordinate : this.currentCoordinates) {
            this.board[coordinate.getX()][coordinate.getY()] = false;
        }
        for (Coordinate coordinate : this.currentCoordinates) {
            coordinate.setY(coordinate.getY() + 1);
            this.board[coordinate.getX()][coordinate.getY()] = true;
        }

        this.invalidate();
    }

    public void rotate() {
        List<Coordinate> nextCoordinates = Block.getRelativeCoordinates(this.currentType,
                (this.currentRotateTimes + 1) % 4);
        for (Coordinate coordinate : nextCoordinates) {
            if (this.currentType == 5) {
                if (this.currentRotateTimes == 0 || this.currentRotateTimes == 2) {
                    coordinate.setX(coordinate.getX() + this.currentX - 1);
                    coordinate.setY(coordinate.getY() + this.currentY + 1);
                } else {
                    coordinate.setX(coordinate.getX() + this.currentX + 1);
                    coordinate.setY(coordinate.getY() + this.currentY - 1);
                }
            } else {
                coordinate.setX(coordinate.getX() + this.currentX);
                coordinate.setY(coordinate.getY() + this.currentY);
            }
        }

        for (Coordinate coordinate : this.currentCoordinates) {
            this.board[coordinate.getX()][coordinate.getY()] = false;
        }

        for (Coordinate coordinate : nextCoordinates) {
            this.board[coordinate.getX()][coordinate.getY()] = true;
        }

        this.currentCoordinates = nextCoordinates;

        if (this.currentType == 5) {
            if (this.currentRotateTimes == 0 || this.currentRotateTimes == 2) {
                this.currentX -= 1;
                this.currentY += 1;
            } else {
                this.currentX += 1;
                this.currentY -= 1;
            }
        }
        this.currentRotateTimes = (this.currentRotateTimes + 1) % 4;

        this.invalidate();
    }

    public boolean canDown() {
        /* Judges whether the current block can move down */

        for (Coordinate coordinate : this.currentCoordinates) {
            if (isInCurrentCoordinates(new Coordinate(coordinate.getX() + 1, coordinate.getY()))) {
                continue;
            } else if (coordinate.getX() == 19) {
                return false;
            } else if (this.board[coordinate.getX() + 1][coordinate.getY()]) {
                return false;
            }
        }

        return true;
    }

    public boolean canLeft() {
        /* Judges whether the current block can move left */

        for (Coordinate coordinate : this.currentCoordinates) {
            if (isInCurrentCoordinates(new Coordinate(coordinate.getX(), coordinate.getY() - 1))) {
                continue;
            } else if (coordinate.getY() == 0) {
                return false;
            } else if (this.board[coordinate.getX()][coordinate.getY() - 1]) {
                return false;
            }
        }

        return true;
    }

    public boolean canRight() {
        /* Judges whether the current block can move right */

        for (Coordinate coordinate : this.currentCoordinates) {
            if (isInCurrentCoordinates(new Coordinate(coordinate.getX(), coordinate.getY() + 1))) {
                continue;
            } else if (coordinate.getY() == 9) {
                return false;
            } else if (this.board[coordinate.getX()][coordinate.getY() + 1]) {
                return false;
            }
        }

        return true;
    }

    public boolean canRotate() {
        /* Judges whether the current block can be rotated */

        List<Coordinate> nextCoordinates = Block.getRelativeCoordinates(this.currentType,
                (this.currentRotateTimes + 1) % 4);
        for (Coordinate coordinate : nextCoordinates) {
            if (this.currentType == 5) {
                if (this.currentRotateTimes == 0 || this.currentRotateTimes == 2) {
                    coordinate.setX(coordinate.getX() + this.currentX - 1);
                    coordinate.setY(coordinate.getY() + this.currentY + 1);
                } else {
                    coordinate.setX(coordinate.getX() + this.currentX + 1);
                    coordinate.setY(coordinate.getY() + this.currentY - 1);
                }
            } else {
                coordinate.setX(coordinate.getX() + this.currentX);
                coordinate.setY(coordinate.getY() + this.currentY);
            }
        }
        for (Coordinate coordinate : nextCoordinates) {
            if (this.isInCurrentCoordinates(coordinate)) {
                continue;
            } else if (coordinate.getX() < 0 ||
                    coordinate.getX() > 19 ||
                    coordinate.getY() < 0 ||
                    coordinate.getY() > 9) {
                return false;
            } else if (this.board[coordinate.getX()][coordinate.getY()]) {
                return false;
            }
        }

        return true;
    }

    public int checkRows() {
        /* Checks whether there is any row that can be eliminated and eliminate all
        * Returns the number of the rows that can be eliminated
        * */

        int count = 0;
        while (eliminateRow()) {
            count++;
        }

        return count;
    }

    private boolean eliminateRow() {
        /* Tries to eliminate the row closest to the bottom
         * Returns whether the elimination is successful
         * */

        // Try to eliminate the row
        for (int i = 19; i >= 0; i--) {
            // Judges whether the current row can be eliminated
            boolean canEliminate = true;
            for (int j = 0; j < 10; j++) {
                canEliminate = canEliminate && this.board[i][j];
            }

            // If the current row can be eliminated, eliminate it
            if (canEliminate) {
                for (int row = i; row >= 1; row--) {
                    for (int column = 0; column < 10; column++) {
                        this.board[row][column] = this.board[row - 1][column];
                    }
                }
                for (int column = 0; column < 10; column++) {
                    this.board[0][column] = false;
                }

                return true;
            }
        }

        return false;
    }

    private boolean isInCurrentCoordinates(Coordinate coordinate) {
        /* Judges whether the coordinate is in the current coordinates */

        for (Coordinate coord : this.currentCoordinates) {
            if (coordinate.getX() == coord.getX() && coordinate.getY() == coord.getY()) {
                return true;
            }
        }
        return false;
    }

    public boolean isGameOver(int type) {
        /* Judges whether the game is over if the next block goes down */

        for (Coordinate coordinate : Block.getRelativeCoordinates(type, 0)) {
            if (this.board[coordinate.getX() + 0][coordinate.getY() + 3]) {
                return true;
            }
        }

        return false;
    }

    public void reset() {
        /* Resets all the data and refresh the UI */

        // Reset the board
        for (int row = 0; row < 20; row++) {
            for (int column = 0; column < 10; column++) {
                this.board[row][column] = false;
            }
        }

        // Reset the information of the current block
        this.currentX = 0;
        this.currentY = 3;
        this.currentType = 0;
        this.currentRotateTimes = 0;
        this.currentCoordinates.clear();

        // Refresh the UI
        this.invalidate();
    }

    public void pushBlock(int type) {
        /* Pushes a new block into the board and refresh the information of the current block */

        this.currentType = type;
        this.currentX = 0;
        this.currentY = 3;
        this.currentRotateTimes = 0;
        this.currentCoordinates.clear();
        for (Coordinate coordinate : Block.getRelativeCoordinates(type, 0)) {
            this.board
                    [this.currentX + coordinate.getX()]
                    [this.currentY + coordinate.getY()]
                    = true;
            this.currentCoordinates.add(new Coordinate(
                    this.currentX + coordinate.getX(),
                    this.currentY + coordinate.getY()
            ));
        }
    }

    public void setLevel(int level) {
        /* Set the initial blocks at the bottom according to the level */

        Random random = new Random();
        for (int row = 19; row >= 20 - level; row--) {
            for (int column = 0; column < 10; column++) {
                this.board[row][column] = (random.nextInt(2) == 1);
            }
        }
    }

    public void setRowAllFilledAt(int row) {
        /* Sets all the blocks in the row filled */

        for (int column = 0; column < 10; column++) {
            this.board[row][column] = true;
        }

        this.invalidate();
    }
}
