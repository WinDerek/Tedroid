package com.jianfenghou.tedroid;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnTouchListener {
    // The private views
    private MainView mainView;
    private NextBlockView nextBlockView;
    private Toolbar toolbar;
    private Button button_start_or_pause;
    private TextView textView_score;
    private TextView textView_level;
    private TextView textView_highestScore;
    private Switch switch_backgroundMusic;

    // The state of the current game
    private boolean isStarted = false;
    private boolean isGoing = false;

    private int currentScore = 0;

    private int mode = TRAINING_MODE;
    private int level = 1;

    private Timer timer;

    // The the MediaPlayer
    private MediaPlayer mediaPlayer;

    private boolean isMusicOn = true;
    private int highestScore = 0;

    private Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case NORMAL_DOWN:
                    // If the current block is good to move down
                    if (MainActivity.this.mainView.canDown()) {
                        MainActivity.this.mainView.normalDown();
                    }

                    // If the current block is blocked
                    else {
                        // Check if there is any row that can be eliminated in the MainView
                        int count = MainActivity.this.mainView.checkRows();
                        MainActivity.this.currentScore += count * 10;
                        MainActivity.this.textView_score
                                .setText(Integer.toString(MainActivity.this.currentScore));

                        // For the Timer
                        MainActivity.this.timer.cancel();
                        MainActivity.this.timer.purge();

                        // If the game is over
                        if (MainActivity.this.mainView
                                .isGameOver(MainActivity.this.nextBlockView.getType())) {
                            // Reset the game by calling the on click event of the button reset
                            MainActivity.this.reset();
                        }

                        // If the game is good to continue
                        else {
                            // Pushes the next block into the MainView and generate a new next block
                            MainActivity.this.pushNextBlock();

                            // Set the timer to perform normal down of the block in the MainView
                            if (MainActivity.this.mode == LEVEL_MODE) {
                                MainActivity.this.setNewTimer(1000);
                            } else {
                                switch (MainActivity.this.level) {
                                    case 1:
                                        MainActivity.this.setNewTimer(1000);

                                        break;

                                    case 2:
                                        MainActivity.this.setNewTimer(800);

                                        break;

                                    case 3:
                                        MainActivity.this.setNewTimer(600);

                                        break;

                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    break;

                case DROP_DOWN:
                    if (MainActivity.this.mainView.canDown()) {
                        MainActivity.this.mainView.normalDown();
                    } else {
                        MainActivity.this.mainView.checkRows();
                    }

                    break;

                case LEFT:
                    if (MainActivity.this.mainView.canLeft()) {
                        MainActivity.this.mainView.moveLeft();
                    }

                    break;

                case RIGHT:
                    if (MainActivity.this.mainView.canRight()) {
                        MainActivity.this.mainView.moveRight();
                    }

                    break;

                case ROTATE:
                    if (MainActivity.this.mainView.canRotate()) {
                        MainActivity.this.mainView.rotate();
                    }

                    break;

                case START:
                    // Set the text of the button start or pause to "Pause"
                    MainActivity.this.button_start_or_pause.setText("Pause");

                    // Change the state of the game
                    MainActivity.this.isStarted = true;
                    MainActivity.this.isGoing = true;

                    // Generate the first next block
                    MainActivity.this.generateNextBlock();

                    // Push the next block into the MainView and generate a new next block
                    MainActivity.this.pushNextBlock();

                    // If it's in the level mode
                    if (MainActivity.this.mode == LEVEL_MODE) {
                        MainActivity.this.mainView.setLevel(MainActivity.this.level);
                    }

                    // Set the timer to perform normal down of the block in the MainView
                    if (MainActivity.this.mode == LEVEL_MODE) {
                        MainActivity.this.setNewTimer(1000);
                    } else {
                        switch (MainActivity.this.level) {
                            case 1:
                                MainActivity.this.setNewTimer(1000);

                                break;

                            case 2:
                                MainActivity.this.setNewTimer(800);

                                break;

                            case 3:
                                MainActivity.this.setNewTimer(600);

                                break;

                            default:
                                break;
                        }
                    }

                    break;

                case PAUSE:
                    // Set the text of the button start/pause to "Resume"
                    MainActivity.this.button_start_or_pause.setText("Resume");

                    // Change the state of the game
                    MainActivity.this.isStarted = true;
                    MainActivity.this.isGoing = false;

                    // Suspend the timer to pause the game
                    MainActivity.this.timer.cancel();
                    MainActivity.this.timer.purge();

                    break;

                case RESUME:
                    // Set the text of the button start/pause to "Pause"
                    MainActivity.this.button_start_or_pause.setText("Pasue");

                    // Change the state of the game
                    MainActivity.this.isStarted = true;
                    MainActivity.this.isGoing = true;

                    // Set the timer to perform normal down of the block in the MainView
                    if (MainActivity.this.mode == LEVEL_MODE) {
                        MainActivity.this.setNewTimer(1000);
                    } else {
                        switch (MainActivity.this.level) {
                            case 1:
                                MainActivity.this.setNewTimer(1000);

                                break;

                            case 2:
                                MainActivity.this.setNewTimer(800);

                                break;

                            case 3:
                                MainActivity.this.setNewTimer(600);

                                break;

                            default:
                                break;
                        }
                    }


                    break;

                case RESET:
                    // Reset the state of the game
                    MainActivity.this.isStarted = false;
                    MainActivity.this.isGoing = false;

                    // Reset the text of the button start or pause to "Start"
                    MainActivity.this.button_start_or_pause.setText("Start");

                    // For the timer
                    MainActivity.this.timer.cancel();
                    MainActivity.this.timer.purge();

                    // Show the game over animation in the MainView
                    MainActivity.this.showGameOverAnimation();

                    // For the highest score
                    if (MainActivity.this.currentScore > MainActivity.this.highestScore) {
                        MainActivity.this.highestScore = MainActivity.this.currentScore;
                        SharedPreferences sharedPreferences = MainActivity.this.getPreferences(0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("highestScore", MainActivity.this.highestScore);
                        editor.commit();
                        MainActivity.this.textView_highestScore
                                .setText(Integer.toString(MainActivity.this.highestScore));
                    }

                    // Reset the current score of the game
                    MainActivity.this.currentScore = 0;
                    MainActivity.this.textView_score.setText("0");

                    break;

                case SET_ROW_ALL_FILLED:
                    MainActivity.this.mainView.setRowAllFilledAt(message.getData().getInt("row"));

                    break;

                case RESET_MAIN_VIEW:
                    MainActivity.this.mainView.reset();

                    break;

                default:
                    break;
            }
        }
    };

    // The what values of the messages
    private static final int NORMAL_DOWN = 0x00;
    private static final int DROP_DOWN = 0x01;
    private static final int LEFT = 0x02;
    private static final int RIGHT = 0x03;
    private static final int ROTATE = 0x04;
    private static final int START = 0x05;
    private static final int PAUSE = 0x06;
    private static final int RESUME = 0x07;
    private static final int RESET = 0x08;
    private static final int SET_ROW_ALL_FILLED = 0x09;
    private static final int RESET_MAIN_VIEW = 0x0A;

    // The game modes
    private static final int TRAINING_MODE = 0x00;
    private static final int LEVEL_MODE = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the private views
        initViews();

        // For the Toolbar
        this.setSupportActionBar(this.toolbar);
        this.toolbar.setTitleTextColor(this.getResources().getColor(R.color.colorPrimaryDark));

        // Set the OnClickListeners
        this.button_start_or_pause.setOnClickListener(this);
        this.findViewById(R.id.button_reset_main_activity).setOnClickListener(this);
        this.findViewById(R.id.button_exit_main_activity).setOnClickListener(this);
        this.findViewById(R.id.image_button_left_main_activity).setOnClickListener(this);
        this.findViewById(R.id.image_button_rotate_main_activity).setOnClickListener(this);
        this.findViewById(R.id.image_button_right_main_activity).setOnClickListener(this);

        // Set the OnTouchListener
        this.findViewById(R.id.image_button_down_main_activity).setOnTouchListener(this);

        // Initialize the MediaPlayer
        this.mediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic);
        this.mediaPlayer.setLooping(true);

        // Get the highest score in history
        this.getHighestScore();

        // Set the highest score
        this.textView_highestScore.setText(Integer.toString(this.highestScore));

        // Get the previous settings of the user
        this.getSettings();

        // If the music is on
        if (this.isMusicOn) {
            // Start the background music
            this.mediaPlayer.start();

            // Set the state of the Switch for the background music
            this.switch_backgroundMusic.setChecked(true);
        } else {
            // Set the state of the Switch for the background music
            this.switch_backgroundMusic.setChecked(false);
        }

        // Set the OnCheckedChangeListener onto the Switch for background music
        this.switch_backgroundMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences settings = MainActivity.this.getPreferences(0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("isMusicOn", true);
                    editor.commit();

                    MainActivity.this.isMusicOn = true;

                    MainActivity.this.mediaPlayer.start();
                } else {
                    SharedPreferences settings = MainActivity.this.getPreferences(0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("isMusicOn", false);
                    editor.commit();

                    MainActivity.this.isMusicOn = false;

                    MainActivity.this.mediaPlayer.pause();
                }
            }
        });
    }

    private void getSettings() {
        /* Gets the settings of the user */

        SharedPreferences settings = getPreferences(0);
        this.isMusicOn = settings.getBoolean("isMusicOn", true);
    }

    private void getHighestScore() {
        /* Gets the highest score */

        SharedPreferences sharedPreferences = this.getPreferences(0);
        this.highestScore = sharedPreferences.getInt("highestScore", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main_activity, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_toolbar_main_activity:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setCancelable(true);
                alertDialog.setTitle("模式选择");
                final String[] modes = { "训练模式", "闯关模式" };
                alertDialog.setItems(modes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case 0:
                                MainActivity.this.mode = TRAINING_MODE;
                                MainActivity.this.selectLevel();

                                break;

                            case 1:
                                MainActivity.this.mode = LEVEL_MODE;
                                MainActivity.this.selectLevel();

                                break;

                            default:
                                break;
                        }
                    }
                });
                alertDialog.show();

                break;

            default:
                break;
        }

        return true;
    }

    private void initViews() {
        /* Initializes the private views */

        this.mainView = (MainView) findViewById(R.id.main_view_main_activity);
        this.nextBlockView = (NextBlockView) findViewById(R.id.next_block_view_main_activity);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar_main_activity);
        this.button_start_or_pause =
                (Button) findViewById(R.id.button_start_or_pause_main_activity);
        this.textView_score = (TextView) findViewById(R.id.text_view_score_main_activity);
        this.textView_level = (TextView) findViewById(R.id.text_view_level_main_activity);
        this.textView_highestScore = (TextView) findViewById(R.id.text_view_highest_score_main_activity);
        this.switch_backgroundMusic =
                (Switch) findViewById(R.id.switch_background_music_main_activity);
    }

    private void generateNextBlock() {
        /* Generates the next block and shows it in the NextBlockView */

        Random random = new Random();
        int randomNum = random.nextInt(7);
        this.nextBlockView.drawNextBlock(randomNum);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start_or_pause_main_activity:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (MainActivity.this.isGoing) {
                            MainActivity.this.refreshHandler.sendEmptyMessage(PAUSE);
                        } else if (MainActivity.this.isStarted) {
                            MainActivity.this.refreshHandler.sendEmptyMessage(RESUME);
                        } else {
                            MainActivity.this.refreshHandler.sendEmptyMessage(START);
                        }
                    }
                }).start();

                break;

            case R.id.button_reset_main_activity:
                if (this.isStarted) {
                    this.reset();
                }

                break;

            case R.id.button_exit_main_activity:
                this.exit();

                break;

            case R.id.image_button_left_main_activity:
                if (this.isGoing) {
                    this.moveLeft();
                }

                break;

            case R.id.image_button_rotate_main_activity:
                if (this.isGoing) {
                    this.rotate();
                }

                break;

            case R.id.image_button_down_main_activity:
                if (this.isGoing) {
                    this.goDown();
                }

                break;

            case R.id.image_button_right_main_activity:
                if (this.isGoing) {
                    this.moveRight();
                }

                break;

            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!MainActivity.this.isGoing) {
            return true;
        }

        if (view.getId() == R.id.image_button_down_main_activity) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    MainActivity.this.resetTimer(100);

                    break;

                case MotionEvent.ACTION_UP:
                    MainActivity.this.resetTimer(1000);

                    break;

                default:
                    break;
            }
        }

        return true;
    }

    private void moveRight() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.refreshHandler.sendEmptyMessage(RIGHT);
            }
        }).start();
    }

    private void moveLeft() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.refreshHandler.sendEmptyMessage(LEFT);
            }
        }).start();
    }

    private void rotate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.refreshHandler.sendEmptyMessage(ROTATE);
            }
        }).start();
    }

    private void goDown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.refreshHandler.sendEmptyMessage(DROP_DOWN);
            }
        }).start();
    }

    private void reset() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.refreshHandler.sendEmptyMessage(RESET);
            }
        }).start();
    }

    private void exit() {
        /* Exits the game */

        // Perform back pressed
        this.onBackPressed();
    }

    private void pushNextBlock() {
        /* Pushes the next block into the MainView and generates a new next block */

        // Initialize the current block of the MainView
        this.mainView.pushBlock(MainActivity.this.nextBlockView.getType());

        // Refresh the MainView
        this.mainView.invalidate();

        // Generate a new next block
        this.generateNextBlock();
    }

    private void resetTimer(int milliseconds) {
        /* Sets a new Timer to perform normal down of the block in the MainView and cancels the old timer*/

        // Cancel the previous timer
        this.timer.cancel();
        this.timer.purge();

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Send the Message to the handler to perforn normal down
                MainActivity.this.refreshHandler.sendEmptyMessage(NORMAL_DOWN);

                // For garbage collection
                System.gc();
            }
        }, 0, milliseconds);
    }

    private void setNewTimer(int milliseconds) {
        /* Sets a new Timer to perform normal down of the block in the MainView */

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Send the Message to the handler to perforn normal down
                MainActivity.this.refreshHandler.sendEmptyMessage(NORMAL_DOWN);

                // For garbage collection
                System.gc();
            }
        }, 1 * 1000, milliseconds);
    }

    private void toast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        // If the game is going
        if (this.isGoing) {
            // Perform the click of the button start or pause to pause the current game
            this.button_start_or_pause.performClick();
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Are you sure to EXIT?");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // For the MediaPlayer
                MainActivity.this.mediaPlayer.stop();
                MainActivity.this.mediaPlayer.release();

                MainActivity.this.finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    private void selectLevel() {
        /* Selects a level in the training mode */

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("选择关卡");
        String[] levels = { "第一关", "第二关", "第三关" };
        alertDialog.setItems(levels, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.level = which + 1;

                // Change the TextView level
                MainActivity.this.textView_level.setText(Integer.toString(MainActivity.this.level));
            }
        });
        alertDialog.show();
    }

    private void showGameOverAnimation() {
        /* Shows the game over animation */

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            int row = 19;

            @Override
            public void run() {
                if (this.row >= 0) {
                    Message message = new Message();
                    message.what = SET_ROW_ALL_FILLED;
                    Bundle bundle = new Bundle();
                    bundle.putInt("row", row);
                    message.setData(bundle);
                    MainActivity.this.refreshHandler.sendMessage(message);

                    this.row--;
                } else {
                    // Reset the MainView
                    MainActivity.this.refreshHandler.sendEmptyMessage(RESET_MAIN_VIEW);

                    MainActivity.this.timer.cancel();
                    MainActivity.this.timer.purge();
                }
            }
        }, 0, 200);
    }
}
