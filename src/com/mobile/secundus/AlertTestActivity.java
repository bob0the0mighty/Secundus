package com.mobile.secundus;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AlertTestActivity extends Activity {

	private final int				NUMBER_ROUNDS					= 10;
	private final Double		WORST									= 0.4;
	private final Double		WORSE									= 0.6;
	private final Double		BAD										= 0.8;
	private final Random		RNG										= new Random();
	private final String		CORRECT_COLOR_STRING	= "CORRECT_COLOR_STRING";
	private final String		GUESS_COUNT_INTARRAY	= "GUESS_COUNT_INTARRAY";
	private final String[]	colors 								= { "red", "blue", "green", "pink" };
	private int							correctSelections, wrongSelections;
	private int[]						fromSavedState;
	private Button					startButton, resetButton, topLeft, topRight,
													bottomLeft, bottomRight;
	private TextView				alert, correct, wrong;
	private String					correctColor;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.main );
		//First branch if the game state was saved due to orientation change, home button press, etc...
		if( savedInstanceState != null && savedInstanceState.containsKey( CORRECT_COLOR_STRING ) ){
			correctColor 			= savedInstanceState.getString( CORRECT_COLOR_STRING );
			fromSavedState 		= savedInstanceState.getIntArray( GUESS_COUNT_INTARRAY );
			correctSelections = fromSavedState[0];
			wrongSelections		= fromSavedState[1];
		} else { //new program instance
			correctColor 			= null;
			correctSelections = 0;
			wrongSelections 	= 0;
		}
		RunGame();
	}
	
	/*
	 * Ensures game state propigation when program re-ups for various
	 * reasons.  The correct color and current score are the important pieces.
	 * (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState( Bundle savedInstanceState ) {
		super.onSaveInstanceState( savedInstanceState );
		savedInstanceState.putString( CORRECT_COLOR_STRING, correctColor );
		savedInstanceState.putIntArray( GUESS_COUNT_INTARRAY, new int[]{ correctSelections, wrongSelections }  );
	}

	/*
	 * Shows no option menu
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate( R.menu.activity_alert_test, menu );
		return false;
	}
	
	/*
	 * Main logic entrance
	 */
	private void RunGame() {
		SetButtons();
		SetTextViews();
		updateText();
	}

	/*
	 * Sets pointers for text boxes and sets initial score.
	 */
	private void SetTextViews() {
		alert 	= (TextView) findViewById( R.id.alert_dynamic_text );
		correct = (TextView) findViewById( R.id.correct_dynamic_text );
		wrong 	= (TextView) findViewById( R.id.wrong_dynamic_text );
		correct.setText( "" + correctSelections );
		wrong.setText( "" + wrongSelections );
	}

	/*
	 * Sets pointers for buttons and then assigns onClickListeners 
	 * for each.
	 */
	private void SetButtons() {
		startButton = (Button) findViewById( R.id.start_button );
		resetButton = (Button) findViewById( R.id.reset_button );
		topLeft 		= (Button) findViewById( R.id.top_left_button );
		topRight 		= (Button) findViewById( R.id.top_right_button );
		bottomLeft 	= (Button) findViewById( R.id.bottom_left_button );
		bottomRight = (Button) findViewById( R.id.bottom_right_button );
		setClickListeners();
	}

	/*
	 * I chose this method as I'm more familiar with it than others allowed by android
	 * and, in the case of the color buttons, I couldn't find a way that I though was
	 * better than this for passing variables into a reusable function.  The end result 
	 * is less flexible than I like.
	 */
	private void setClickListeners() {
		startButton.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				if ( correctColor == null ) {
					reset();//ensure clean state
					selectNewColor();
					updateText();
					turnOffStartButton();
				}
			}
		} );

		resetButton.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				reset();
				updateText();
			}
		} );

		topLeft.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				buttonSelection(colors[0]);
			}
		} );

		topRight.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				buttonSelection(colors[1]);
			}
		} );

		bottomLeft.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				buttonSelection(colors[2]);
			}
		} );

		bottomRight.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				buttonSelection(colors[3]);
			}
		} );
	}

	/*
	 * Set game to start state
	 */
	private void reset() {
		correctColor = null;
		correctSelections = 0;
		wrongSelections = 0;
		startButton.setClickable( true );
		startButton.setText( R.string.start_button );
	}

	/*
	 * Manage the updating of various texts while game is in different states.
	 * I see before start, during play, and end game as the states that need to
	 * be handled.  The matching number of branches fills me with hope.
	 */
	private void updateText() {
		correct.setText( "" + correctSelections );
		wrong.setText( "" + wrongSelections );
		if ( correctColor == null && ( correctSelections + wrongSelections ) == 0 ) {
			alert.setText( "" );
		} else if ( ( correctSelections + wrongSelections ) >= NUMBER_ROUNDS) {
			turnOffStartButton();
			evaluateUser();
			startButton.setText( R.string.start_button_finished );
			correctColor = null;
		} else {
		  turnOffStartButton();
			alert.setText( getString( R.string.press ) + " " + correctColor );
		}
	}

	/*
	 * Select a new color from on in the string.xml file. Feels more Androidish than
	 * using the static local color array.  Would be more usefull if I could decoupl
	 * the clickListeners from the local array.
	 */
	private void selectNewColor() {
		int index = RNG.nextInt( R.color.class.getDeclaredFields().length );
		correctColor = R.color.class.getDeclaredFields()[index].getName();
	}

	/*
	 * Changed from given code in order to allow for
	 * changes to NUMBER_ROUNDS
	 */
	private void evaluateUser() {
		if ( correctSelections < NUMBER_ROUNDS * WORST ) {
			alert.setText( R.string.dont_walk);
		} else if ( correctSelections < NUMBER_ROUNDS * WORSE ) {
			alert.setText( R.string.dont_drive);
		} else if ( correctSelections < NUMBER_ROUNDS * BAD ) {
			alert.setText( R.string.pay_attention);
		} else if ( correctSelections < NUMBER_ROUNDS ) {
			alert.setText( R.string.ok);
		} else {
			alert.setText( R.string.perfect);
		}
	}
	
	/*
	 * Ensures game cannot restart while in progress without using the reset button
	 * first.
	 */
	private void turnOffStartButton() {
		if( startButton.isClickable() ) {
			startButton.setClickable( false );
			startButton.setText( R.string.start_button_started );
		}
	}

	/*
	 * Checks if clicked button is the correct one.
	 */
	private void buttonSelection(String color) {
		if ( correctColor != null ) {
			if ( correctColor.equals( color ) ) {
				correctSelections++;
			} else {
				wrongSelections++;
			}
			selectNewColor();
			updateText();
		}
	}
}