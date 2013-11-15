package com.elddev.trigonometric;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Equation extends View {
	
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private String equation = "";
	
	public Equation(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setEquation(String equation) {
		this.equation = equation;
	}
	
	private void init() {
		paint.setColor(0xff101010);
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		for(int i = 0; i < equation.length(); i++) {
			
		}
		
	}
}
