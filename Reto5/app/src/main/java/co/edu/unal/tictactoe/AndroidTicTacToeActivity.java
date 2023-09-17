package co.edu.unal.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class AndroidTicTacToeActivity extends Activity {

    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mHumanMediaPlayerError;
    MediaPlayer mComputerMediaPlayer;


    private char Turno;
    // Represents the internal state of the game
    private TicTacToeGame mGame;
    private boolean  mGameOver;

    // Buttons making up the board
    private Button mBoardButtons[];
    // Various text displayed
    private TextView mInfoTextView;
    private TextView mHumanTextView;
    private TextView mAndroidTextView;
    private TextView mTiesTextView;

    private Random mRand = new Random();

    private int human;
    private int ties;

    private int android;
    private BoardView mBoardView;

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    static final int ABOUT_ID = 2;


    // Listen for touches on the board
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
// Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;
            int winner = mGame.checkForWinner();

            if (!mGameOver && mGame.getBoardOccupant(pos) == TicTacToeGame.OPEN_SPOT && getTurno() == TicTacToeGame.HUMAN_PLAYER ){
                setMove(TicTacToeGame.HUMAN_PLAYER, pos);
                mHumanMediaPlayer.start();
                mBoardView.setGame(mGame);

                setTurno(TicTacToeGame.COMPUTER_PLAYER);





                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            int winner = mGame.checkForWinner();

                            if (winner == 0) {
                                mInfoTextView.setText(R.string.turn_computer);
                                int move = mGame.getComputerMove();
                                setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                                winner = mGame.checkForWinner();
                            }
                            if (winner == 0) {
                                mInfoTextView.setText(R.string.turn_human);
                                setTurno(TicTacToeGame.HUMAN_PLAYER);
                            }else if (winner == 1) {
                                mGameOver = true;
                                ties++;
                                mInfoTextView.setText(R.string.result_tie);
                                mTiesTextView.setText(R.string.ties);
                                mTiesTextView.append(String.valueOf(ties));
                            } else if (winner == 2) {
                                mGameOver = true;
                                human++;
                                mInfoTextView.setText(R.string.result_human_wins);
                                mHumanTextView.setText(R.string.human);
                                mHumanTextView.append(String.valueOf(human));
                            } else {
                                mGameOver = true;
                                android++;
                                mInfoTextView.setText(R.string.result_computer_wins);
                                mAndroidTextView.setText(R.string.android);
                                mAndroidTextView.append(String.valueOf(android));
                            }

                        }
                    }, 1000);






            } else if (!mGameOver && mGame.getBoardOccupant(pos) != TicTacToeGame.OPEN_SPOT) {
                mHumanMediaPlayerError.start();

            }
// So we aren't notified of continued events when finger is moved
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        human = 0;
        ties = 0;
        android = 0;

        mGame = new TicTacToeGame();
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);


        mInfoTextView = (TextView) findViewById(R.id.information);
        mHumanTextView = (TextView) findViewById(R.id.Human);
        mTiesTextView = (TextView) findViewById(R.id.Ties);
        mAndroidTextView = (TextView) findViewById(R.id.Android);
        mGame = new TicTacToeGame();

        startNewGame();
    }

    // Set up the game board.
    private void startNewGame() {

        setTurno(TicTacToeGame.COMPUTER_PLAYER);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mGameOver = false;
                mGame.clearBoard();
                mBoardView.invalidate();
                int first = mRand.nextInt(2);

                if(first == 1){
                    // Human goes first
                    setTurno(TicTacToeGame.HUMAN_PLAYER);
                    mInfoTextView.setText(R.string.first_human);
                }else{
                    // Android goes first
                    setTurno(TicTacToeGame.COMPUTER_PLAYER);
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    mBoardView.setGame(mGame);
                    setTurno(TicTacToeGame.HUMAN_PLAYER);
                    mInfoTextView.setText(R.string.turn_human);
                }
            }
        }, 100);


    } // End of startNewGame

    // Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;
        public ButtonClickListener(int location) {
            this.location = location;
        }
        public void onClick(View view) {
            if (mBoardButtons[location].isEnabled()) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();
                if (!mGameOver) {
                    if (winner == 0) {
                        mInfoTextView.setText(R.string.turn_computer);
                        int move = mGame.getComputerMove();
                        setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                        winner = mGame.checkForWinner();
                    }

                    if (winner == 0)
                        mInfoTextView.setText(R.string.turn_human);
                    else if (winner == 1) {
                        mGameOver = true;
                        ties++;
                        mInfoTextView.setText(R.string.result_tie);
                        mTiesTextView.setText(R.string.ties);
                        mTiesTextView.append(String.valueOf(ties));
                    } else if (winner == 2) {
                        mGameOver = true;
                        human++;
                        mInfoTextView.setText(R.string.result_human_wins);
                        mHumanTextView.setText(R.string.human);
                        mHumanTextView.append(String.valueOf(human));
                    } else {
                        mGameOver = true;
                        android++;
                        mInfoTextView.setText(R.string.result_computer_wins);
                        mAndroidTextView.setText(R.string.android);
                        mAndroidTextView.append(String.valueOf(android));
                    }

                }
            }
        }
    }

    private boolean setMove(char player, int location) {

        if(!mGameOver) {
            if (player == mGame.HUMAN_PLAYER) {
                if (mGame.setMove(player, location)) {
                    mHumanMediaPlayer.start();
                    mBoardView.invalidate(); // Redraw the board
                    return true;
                }
            } else if (player == mGame.COMPUTER_PLAYER) {
                if (mGame.setMove(player, location)) {
                    mComputerMediaPlayer.start();
                    mBoardView.invalidate(); // Redraw the board
                    return true;
                }
            }
        }
            return false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.new_game) {
            startNewGame();
            return true;
        } else if (itemId == R.id.ai_difficulty) {
            showDialog(DIALOG_DIFFICULTY_ID);
            return true;
        } else if (itemId == R.id.quit) {
            showDialog(DIALOG_QUIT_ID);
            return true;
        }else if (itemId == R.id.about) {
            showDialog(ABOUT_ID);
            return true;
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {


            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);
                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};
                // TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
                // selected is the radio button that should be selected.
                int selected = 0;

                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss(); // Close dialog

                                // TODO: Set the diff level of mGame based on which item was selected.
                                // Display the selected difficulty level
                                if(item == 0){
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                }else if(item == 1){
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                }else if(item == 2){
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                }
                                Toast.makeText(getApplicationContext(), levels[item], Toast.LENGTH_SHORT).show();


                            }
                        });
                dialog = builder.create();
                break;

            case DIALOG_QUIT_ID:
            // Create the quit confirmation dialog
                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AndroidTicTacToeActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;


            case ABOUT_ID:
                builder = new AlertDialog.Builder(this);
                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.about_dialog, null);
                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();
                break;



        }
        return dialog;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bow_shoot);
        mHumanMediaPlayerError = MediaPlayer.create(getApplicationContext(), R.raw.steve);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.teleport);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mHumanMediaPlayer.release();
        mHumanMediaPlayerError.release();
        mComputerMediaPlayer.release();
    }


    public char getTurno() {
        return Turno;
    }

    public void setTurno(char turno) {
        Turno = turno;
    }

}
