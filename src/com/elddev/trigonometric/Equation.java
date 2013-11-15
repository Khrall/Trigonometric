package com.elddev.trigonometric;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.View;

public class Equation extends View {
	
	public static final String ADD = "+", SUB = "-", DIV = "/", MUL = "*", EXP = "^", EQS = "=";
	public static final String[] operators = {"+", "-", "/", "*", "^", "="};
	
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Node equationRoot;
	private Position cursor = new Position(20, 20);
	
	public Equation(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void setEquation(String equation) {
		equationRoot = new Node();
		growEquationTree(equationRoot, equation);
	}
	
	private void init() {
		paint.setColor(0xff101010);
		paint.setTextSize(10);
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawTree(equationRoot, canvas);
	}
	
	private void drawTree(Node root, Canvas canvas) {
		if(root.context.equals(EXP)) {
			drawTree(root.left, canvas);
			paint.setTextSize(7);
			cursor.y -= 5;
			drawTree(root.right, canvas);
			paint.setTextSize(10);
			cursor.y += 5;
		} else if(root.context.equals(MUL)){
			if(root.left != null) { drawTree(root.left, canvas); }
			if(root.right != null) { drawTree(root.right, canvas); }
		} else {
			if(root.left != null) { drawTree(root.left, canvas); }
			canvas.drawText(root.context, cursor.x, cursor.y, paint);
			cursor.x += paint.measureText(root.context);
			if(root.right != null) { drawTree(root.right, canvas); }
		}
	}

	private void growEquationTree(Node root, String equation) {
		int splitPos = -1;
		int length = 0;
		
		if(equation.contains(EQS)) {
			root.context = EQS;
			splitPos = equation.indexOf(EQS) - 1;
			length = 3;
		} else if(equation.contains(ADD)) {
			root.context = ADD;
			splitPos = equation.indexOf(ADD) - 1;
			length = 3;
		} else if(equation.contains(" ")) {
			root.context = MUL;
			splitPos = equation.indexOf(" ");
			length = 1;
		} else if(equation.contains(EXP)) {
			root.context = EXP;
			splitPos = equation.indexOf(EXP);
			length = 1;
		} else {
			root.context = equation;
		}
		
		if(splitPos > 0) {
			root.left = new Node();
			root.right = new Node();
			growEquationTree(root.left, equation.substring(0, splitPos));
			growEquationTree(root.right, equation.substring(splitPos + length, equation.length()));
		}
	}
	

	class Node {
		Node left, right, parent;
		int length;
		String context;
		
		public String toString() {
			String s = "";
			if(left != null) { s += left.toString(); }
			s += context + " | ";
			if(right != null) { s += right.toString(); }
			return s;
		}
	}
	
	class Position {
		int x, y;
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
}
