package com.elddev.trigonometric;

import com.elddev.eqnodes.EqNode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class EquationView extends View {
	
	String equationString;
	float startX = 10f, startY = 30f;
	Paint paint, eraser;
	EqNode equation;
	
	public static final float[] FONT_SIZES = {20f, 16f, 10f, 6f};
	public static final float[] DEPTH_OFFSETS = {1f, 2f, 1f, 0f};
	
	public EquationView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.getTheme().obtainStyledAttributes(
	        attrs,
	        R.styleable.EquationView,
	        0, 0);

	   try {
	       equationString = a.getString(R.styleable.EquationView_equation);
	   } finally {
	       a.recycle();
	   }
	   
	   if(!isInEditMode()) {
		   init();
	   }
	}

	private void init() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.DKGRAY);
		paint.setTextSize(FONT_SIZES[0]);
		eraser = new Paint();
		eraser.setColor(getResources().getColor(R.color.theme_white));
		equation = new EqNode(equationString);
	}
	
	public void setEquation(String equationString) {
		this.equationString = equationString;
		equation = new EqNode(equationString);
	}
	
	public void setCursor(float startX, float startY) {
		this.startX = startX;
		this.startY = startY;
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(!isInEditMode()) {
		EqNode root = equation.getRootNode();
		float[] cursor = {startX, startY};
		int depth = 0;
		
		drawNode(root, cursor, depth, canvas);
		}
	}

	private void drawNode(EqNode root, float[] cursor, int depth, Canvas canvas) {
		
		float textSize = FONT_SIZES[depth];
		paint.setTextSize(textSize);
		
		if(root.operands.isEmpty()) { 
			canvas.drawText(root.contents, cursor[0], cursor[1], paint);
			cursor[0] += paint.measureText(root.contents);
		} else {
			
			switch(root.operator) {
			
			case EqNode.plus: case EqNode.minus:
				drawNode(root.operands.get(0), cursor, depth, canvas);
				String opString = " " + String.valueOf(root.operator) + " ";
				
				canvas.drawText(opString, cursor[0], cursor[1], paint);
				cursor[0] += paint.measureText(opString);
				
				drawNode(root.operands.get(1), cursor, depth, canvas);
				break;
			
			case EqNode.parenthesis:
				switch(root.stateOfParenthesis) {
				
				case EqNode.OPERAND_INSIDE:
					canvas.drawText("(", cursor[0], cursor[1], paint);
					cursor[0] += paint.measureText("(");
					drawNode(root.operands.get(0), cursor, depth, canvas);
					canvas.drawText(")", cursor[0], cursor[1], paint);
					cursor[0] += paint.measureText(")");
					break;
				
				case EqNode.OPERAND_BEFORE:
					drawNode(root.operands.get(0), cursor, depth, canvas);
					canvas.drawText("(", cursor[0], cursor[1], paint);
					cursor[0] += paint.measureText("(");
					drawNode(root.operands.get(1), cursor, depth, canvas);
					canvas.drawText(")", cursor[0], cursor[1], paint);
					cursor[0] += paint.measureText(")");
					break;
					
				case EqNode.OPERAND_AFTER:
					canvas.drawText("(", cursor[0], cursor[1], paint);
					cursor[0] += paint.measureText("(");
					drawNode(root.operands.get(0), cursor, depth, canvas);
					canvas.drawText(")", cursor[0], cursor[1], paint);
					cursor[0] += paint.measureText(")");
					drawNode(root.operands.get(1), cursor, depth, canvas);
					break;
					
				case EqNode.ALL_OPERANDS:
					drawNode(root.operands.get(0), cursor, depth, canvas);
					canvas.drawText("(", cursor[0], cursor[1], paint);
					cursor[0] += paint.measureText("(");
					drawNode(root.operands.get(1), cursor, depth, canvas);
					canvas.drawText(")", cursor[0], cursor[1], paint);
					cursor[0] += paint.measureText(")");
					drawNode(root.operands.get(2), cursor, depth, canvas);
					break;
				}
				break;	
				
			case EqNode.brackets:
				switch(root.stateOfBrackets) {
					case EqNode.OPERAND_INSIDE:
						drawOperation(root.bracketOperation, root.operands.get(0), root.operands.get(1), cursor, depth, canvas);
						break;
					
					case EqNode.OPERAND_BEFORE:
						drawNode(root.operands.get(0), cursor, depth, canvas);
						drawOperation(root.bracketOperation, root.operands.get(1), root.operands.get(2), cursor, depth, canvas);
						break;
						
					case EqNode.OPERAND_AFTER:
						drawOperation(root.bracketOperation, root.operands.get(0), root.operands.get(1), cursor, depth, canvas);
						drawNode(root.operands.get(2), cursor, depth, canvas);
						break;
						
					case EqNode.ALL_OPERANDS:
						drawNode(root.operands.get(0), cursor, depth, canvas);
						drawOperation(root.bracketOperation, root.operands.get(1), root.operands.get(2), cursor, depth, canvas);
						drawNode(root.operands.get(3), cursor, depth, canvas);
						break;
					
				}
				break;
			
			}
		}
		
	}
	
	
	private void drawOperation(String operation, EqNode op1, EqNode op2, float[] cursor, int depth, Canvas canvas) {
		
		float textSize = FONT_SIZES[depth];
		paint.setTextSize(textSize);
		
		float yIncrease = 0.00f;
		
		if(operation.equals(EqNode.POW)) {
			drawNode(op1, cursor, depth, canvas);
			yIncrease = textSize * 5/9;
			cursor[1] -= yIncrease;
			cursor[0] += DEPTH_OFFSETS[depth];
			drawNode(op2, cursor, depth + 1, canvas);
			cursor[1] += yIncrease;
			paint.setTextSize(textSize);
			
		} else if(operation.equals(EqNode.FRAC)) {
			cursor[0] += 10f;
			
			// Decide vertical placement offset of the numerator and denominator
			yIncrease = textSize * 4/5;
			
			// Remember where we started drawing text (horizontally)
			float rememberX = cursor[0];
			
			// Move the cursor up to the numerator, and draw the inner expression
			cursor[1] -= yIncrease;
			drawNode(op1, cursor, depth, canvas);
			float dx1 = cursor[0] - rememberX; // Remember how far we got horizontally
			
			// Reset the cursor, and move offset vertically into the denominator's position, then draw the inner expression
			cursor[0] = rememberX;
			cursor[1] += yIncrease;
			yIncrease = textSize;
			cursor[1] += yIncrease;
			drawNode(op2, cursor, depth, canvas);
			float dx2 = cursor[0] - rememberX; // Remember how far we got horizontally
			
			// Reset the cursor vertically
			cursor[1] -= yIncrease;
			
			//Expression above is smaller than the one below
			if(dx1 < dx2) {
				canvas.drawRect(rememberX - 1f, cursor[1] - 100f, rememberX + dx1, cursor[1] - textSize/3, eraser);
				cursor[0] = rememberX + (dx2 - dx1)/2;
				yIncrease = textSize * 4/5;
				cursor[1] -= yIncrease;
				drawNode(op1, cursor, depth, canvas);
				cursor[1] += yIncrease;
				cursor[0] = rememberX + dx2;
				
			} else {
				canvas.drawRect(rememberX - 1f, cursor[1] - textSize/3, rememberX + dx2, cursor[1] + 100f, eraser);
				cursor[0] = rememberX + (dx1 - dx2)/2;
				yIncrease = textSize;
				cursor[1] += yIncrease;
				drawNode(op2, cursor, depth, canvas);
				cursor[1] -= yIncrease;
				cursor[0] = rememberX + dx1;
			}
			
			canvas.drawLine(cursor[0] + 5f, cursor[1] - textSize/3, rememberX - 5f, cursor[1] - textSize/3, paint);
			cursor[0] += 5f;
		}
		
		
	}
	

}
