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
	Paint paint;
	EqNode equation;
	
	public static final float[] FONT_SIZES = {30f, 20f, 12f, 8f};
	public static final int[] DEPTH_OFFSETS = {5, 2, 1, 0};
	
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
	   
	   init();
	}

	private void init() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.DKGRAY);
		paint.setTextSize(30f);
		equation = new EqNode(equationString);
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		EqNode root = equation.getRootNode();
		float[] cursor = {20f, 40f};
		int depth = 0;
		
		drawNode(root, cursor, depth, canvas);
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
						drawOperation(root.operands.get(0), root.operands.get(1), cursor, depth, canvas);
						break;
					
					case EqNode.OPERAND_BEFORE:
						drawNode(root.operands.get(0), cursor, depth, canvas);
						drawOperation(root.operands.get(1), root.operands.get(2), cursor, depth, canvas);
						break;
						
					case EqNode.OPERAND_AFTER:
						drawOperation(root.operands.get(0), root.operands.get(1), cursor, depth, canvas);
						drawNode(root.operands.get(2), cursor, depth, canvas);
						break;
						
					case EqNode.ALL_OPERANDS:
						drawNode(root.operands.get(0), cursor, depth, canvas);
						drawOperation(root.operands.get(1), root.operands.get(2), cursor, depth, canvas);
						drawNode(root.operands.get(3), cursor, depth, canvas);
						break;
					
				}
				break;
			
			}
		}
		
	}
	
	
	private void drawOperation(EqNode op1, EqNode op2, float[] cursor, int depth, Canvas canvas) {
		
		float textSize = FONT_SIZES[depth];
		paint.setTextSize(textSize);
		
		drawNode(op1, cursor, depth, canvas);
		
		float yIncrease = textSize * 5/9;
		cursor[1] -= yIncrease;
		cursor[0] += DEPTH_OFFSETS[depth];
		drawNode(op2, cursor, depth + 1, canvas);
		cursor[1] += yIncrease;
		
	}
	

}
