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

	private final int			NUMBER_ROUNDS					= 10;
	private final Random	RNG										= new Random();
	private final String	CORRECT_COLOR_STRING	= "CORRECT_COLOR_STRING";
	private final String	GUESS_COUNT_INTARRAY	= "GUESS_COUNT_INTARRAY";
	private Button				startButton, resetButton, topLeft, topRight,
												bottomLeft, bottomRight;
	private TextView			alert, correct, wrong;
	private String				correctColor;
	private int						correctSelections, wrongSelections;
	private int[]					fromSavedState;

	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.main );
		if( savedInstanceState != null && savedInstanceState.containsKey( CORRECT_COLOR_STRING ) ){
			correctColor 			= savedInstanceState.getString( CORRECT_COLOR_STRING );
			fromSavedState 		= savedInstanceState.getIntArray( GUESS_COUNT_INTARRAY );
			correctSelections = fromSavedState[0];
			wrongSelections		= fromSavedState[1];
		} else {
			correctColor 			= null;
			correctSelections = 0;
			wrongSelections 	= 0;
		}
		RunGame();
	}
	
	public void onSaveInstanceState( Bundle savedInstanceState ) {
		super.onSaveInstanceState( savedInstanceState );
		savedInstanceState.putString( CORRECT_COLOR_STRING, correctColor );
		savedInstanceState.putIntArray( GUESS_COUNT_INTARRAY, new int[]{ correctSelections, wrongSelections }  );
	}

	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.activity_alert_test, menu );
		return true;
	}
	
	private void RunGame() {
		SetButtons();
		SetTextViews();
		updateText();
	}

	private void SetTextViews() {
		alert 	= (TextView) findViewById( R.id.alert_dynamic_text );
		correct = (TextView) findViewById( R.id.correct_dynamic_text );
		wrong 	= (TextView) findViewById( R.id.wrong_dynamic_text );
		correct.setText( "" + correctSelections );
		wrong.setText( "" + wrongSelections );
	}

	private void SetButtons() {
		startButton = (Button) findViewById( R.id.start_button );
		resetButton = (Button) findViewById( R.id.reset_button );
		topLeft 		= (Button) findViewById( R.id.top_left_button );
		topRight 		= (Button) findViewById( R.id.top_right_button );
		bottomLeft 	= (Button) findViewById( R.id.bottom_left_button );
		bottomRight = (Button) findViewById( R.id.bottom_right_button );
		setClickListeners();
	}

	private void setClickListeners() {
		startButton.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				if ( correctColor == null ) {
					reset();//just in case
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
				if ( correctColor != null ) {
					buttonSelection("red");
				}
			}
		} );

		topRight.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				if ( correctColor != null ) {
					buttonSelection("blue");
				}
			}
		} );

		bottomLeft.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				if ( correctColor != null ) {
					buttonSelection("green");
				}
			}
		} );

		bottomRight.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				if ( correctColor != null ) {
					buttonSelection("pink");
				}
			}
		} );
	}

	private void reset() {
		correctColor = null;
		correctSelections = 0;
		wrongSelections = 0;
		startButton.setClickable( true );
		startButton.setText( R.string.start_button );
	}

	private void updateText() {
		correct.setText( "" + correctSelections );
		wrong.setText( "" + wrongSelections );
		if ( correctColor == null && ( correctSelections + wrongSelections ) == 0 ) {
			alert.setText( "" );
		} else if ( ( correctSelections + wrongSelections ) >= NUMBER_ROUNDS) {
			if( startButton.isClickable() ){
				turnOffStartButton();
			}
			evaluateUser();
			correctColor = null;
		} else {
			if( startButton.isClickable() ){
				turnOffStartButton();
			}
			alert.setText( getString( R.string.press ) + " " + correctColor );
		}
	}

	private void selectNewColor() {
		int index = RNG.nextInt( R.color.class.getDeclaredFields().length );
		correctColor = R.color.class.getDeclaredFields()[index].getName();
	}

	private void evaluateUser() {
		if ( correctSelections < 4 ) {
			alert.setText( R.string.dont_walk);
		} else if ( correctSelections < 6 ) {
			alert.setText( R.string.dont_drive);
		} else if ( correctSelections < 8 ) {
			alert.setText( R.string.pay_attention);
		} else if ( correctSelections < 10 ) {
			alert.setText( R.string.ok);
		} else {
			alert.setText( R.string.perfect);
		}
	}
	
	private void turnOffStartButton() {
		startButton.setClickable( false );
		startButton.setText( R.string.start_button_started );
	}

	private void buttonSelection(String color) {
		if ( correctColor.equals( color ) ) {
			correctSelections++;
		} else {
			wrongSelections++;
		}
		selectNewColor();
		updateText();
	}
}