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

	private final int			NUMBER_ROUNDS	= 10;
	private final Random	rng						= new Random();
	private Button				startButton, resetButton, topLeft, topRight,
												bottomLeft, bottomRight;
	private TextView			alert, correct, wrong;
	private String				correctColor;
	private int						correctSelections, wrongSelections;

	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_alert_test );
		correctColor 			= null;
		correctSelections = 0;
		wrongSelections 	= 0;
		RunGame();
	}

	private void RunGame() {
		SetButtons();
		SetTextViews();
	}

	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.activity_alert_test, menu );
		return true;
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
					reset();
					updateText();
					selectNewColor();
					startButton.setClickable( false );
					startButton.setText( R.string.start_button_started );
				}
			}
		} );

		resetButton.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				reset();
				updateText();
				startButton.setClickable( true );
				startButton.setText( R.string.start_button );
			}
		} );

		topLeft.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				if ( correctColor != null ) {
					if ( correctColor.equals( "red" ) ) {// hard coded bad but unsure of a
																								// better way as stands.
						correctSelections++;
					} else {
						wrongSelections++;
					}
					updateText();
				}
			}
		} );

		topRight.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				if ( correctColor != null ) {
					if ( correctColor.equals( "blue" ) ) {// hard coded bad but unsure of
																								// a better way as stands.
						correctSelections++;
					} else {
						wrongSelections++;
					}
					updateText();
				}
			}
		} );

		bottomLeft.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				if ( correctColor != null ) {
					if ( correctColor.equals( "green" ) ) {// hard coded bad but unsure of
																									// a better way as stands.
						correctSelections++;
					} else {
						wrongSelections++;
					}
					updateText();
				}
			}
		} );

		bottomRight.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				if ( correctColor != null ) {
					if ( correctColor.equals( "pink" ) ) {// hard coded bad but unsure of
																								// a better way as stands.
						correctSelections++;
					} else {
						wrongSelections++;
					}
					updateText();
				}
			}
		} );
	}

	private void reset() {
		correctColor = null;
		correctSelections = 0;
		wrongSelections = 0;
	}

	private void updateText() {
		correct.setText( "" + correctSelections );
		wrong.setText( "" + wrongSelections );
		if ( correctColor == null ) {
			alert.setText( "" );
		} else if ( ( correctSelections + wrongSelections ) == NUMBER_ROUNDS) {
			evaluateUser();
			correctColor = null;
		} else {
			selectNewColor();
		}
	}

	private void selectNewColor() {
		int index = rng.nextInt( R.color.class.getDeclaredFields().length );
		correctColor = R.color.class.getDeclaredFields()[index].getName();
		alert.setText( getString( R.string.press ) + " " + correctColor );
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
}