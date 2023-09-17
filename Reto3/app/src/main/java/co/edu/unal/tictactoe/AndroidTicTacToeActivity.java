package co.edu.unal.tictactoe;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

public class AndroidTicTacToeActivity extends Activity {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        human = 0;
        ties = 0;
        android = 0;

        mBoardButtons = new Button[9];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);

        mInfoTextView = (TextView) findViewById(R.id.information);
        mHumanTextView = (TextView) findViewById(R.id.Human);
        mTiesTextView = (TextView) findViewById(R.id.Ties);
        mAndroidTextView = (TextView) findViewById(R.id.Android);
        mGame = new TicTacToeGame();

        startNewGame();
    }

    // Set up the game board.
    private void startNewGame() {

        mGameOver = false;
        mGame.clearBoard();
        int first = mRand.nextInt(2);

        // Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }

        if(first == 1){
            // Human goes first
            mInfoTextView.setText(R.string.first_human);
        }else{
            // Android goes first
            mInfoTextView.setText(R.string.turn_computer);
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
            mInfoTextView.setText(R.string.turn_human);
        }
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

    private void setMove(char player, int location) {

        if(!mGameOver) {
            mGame.setMove(player, location);
            mBoardButtons[location].setEnabled(false);
            mBoardButtons[location].setText(String.valueOf(player));
            if (player == TicTacToeGame.HUMAN_PLAYER)
                mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
            else
                mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("New Game");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startNewGame();
        return true;
    }

}
