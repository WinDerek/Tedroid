package com.jianfenghou.tedroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

public class NextBlockView extends View {
    private Paint paint = new Paint();
    private float measuredWidth;
    private float measuredHeight;
    private int type;

    private boolean[][] board = new boolean[2][4];

    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case DRAW_BLOCK:
                    NextBlockView.this.invalidate();

                    break;

                default:
                    break;
            }
        }
    };

    private static final int DRAW_BLOCK = 0x00;

    public NextBlockView(Context context) {
        super(context);
    }

    public NextBlockView(Context context, AttributeSet a) {
        super(context, a);
    }

    public NextBlockView(Context context, AttributeSet a, int b) {
        super(context, a, b);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the background
        canvas.drawColor(getResources().getColor(R.color.color_main_view_background));

        // Draw the pixels
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
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

        float left = this.measuredWidth * j / 4;
        float top = this.measuredWidth * i / 4;
        float right = this.measuredWidth * (j + 1) / 4;
        float bottom = this.measuredWidth * (i + 1) / 4;
        float margin = this.measuredWidth / 40;
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

    public void drawNextBlock(int nextBlockType) {
        this.type = nextBlockType;

        switch (nextBlockType) {
            case 0:
                this.type = 0;


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Change the board
                        NextBlockView.this.board = new boolean[][] {
                                {true, true, false, false},
                                {true, true, false, false}
                        };

                        // Send the message to the handler
                        NextBlockView.this.handler.sendEmptyMessage(DRAW_BLOCK);
                    }
                }).start();

                break;

            case 1:
                this.type = 1;


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Change the board
                        NextBlockView.this.board = new boolean[][] {
                                {false, false, true, false},
                                {true, true, true, false}
                        };

                        // Send the message to the handler
                        NextBlockView.this.handler.sendEmptyMessage(DRAW_BLOCK);
                    }
                }).start();

                break;

            case 2:
                this.type = 2;


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Change the board
                        NextBlockView.this.board = new boolean[][] {
                                {true, false, false, false},
                                {true, true, true, false}
                        };

                        // Send the message to the handler
                        NextBlockView.this.handler.sendEmptyMessage(DRAW_BLOCK);
                    }
                }).start();

                break;

            case 3:
                this.type = 3;


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Change the board
                        NextBlockView.this.board = new boolean[][] {
                                {false, true, true, false},
                                {true, true, false, false}
                        };

                        // Send the message to the handler
                        NextBlockView.this.handler.sendEmptyMessage(DRAW_BLOCK);
                    }
                }).start();

                break;

            case 4:
                this.type = 4;


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Change the board
                        NextBlockView.this.board = new boolean[][] {
                                {true, true, false, false},
                                {false, true, true, false}
                        };

                        // Send the message to the handler
                        NextBlockView.this.handler.sendEmptyMessage(DRAW_BLOCK);
                    }
                }).start();

                break;

            case 5:
                this.type = 5;


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Change the board
                        NextBlockView.this.board = new boolean[][] {
                                {false, false, false, false},
                                {true, true, true, true}
                        };

                        // Send the message to the handler
                        NextBlockView.this.handler.sendEmptyMessage(DRAW_BLOCK);
                    }
                }).start();

                break;

            case 6:
                this.type = 6;


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Change the board
                        NextBlockView.this.board = new boolean[][] {
                                {false, true, false, false},
                                {true, true, true, false}
                        };

                        // Send the message to the handler
                        NextBlockView.this.handler.sendEmptyMessage(DRAW_BLOCK);
                    }
                }).start();

                break;

            default:
                break;
        }
    }

    public int getType() {
        return this.type;
    }
}
