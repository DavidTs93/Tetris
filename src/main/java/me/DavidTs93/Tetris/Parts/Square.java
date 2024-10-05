package me.DavidTs93.Tetris.Parts;

import java.awt.*;

public class Square {
	private static Polygon topSquarePolygon(int x,int y,int unit,int squareSize) {
		Polygon polygon = new Polygon();
		polygon.addPoint(x,y + squareSize);
		polygon.addPoint(x + unit,y + squareSize - unit);
		polygon.addPoint(x + unit,y + unit);
		polygon.addPoint(x + squareSize - unit,y + unit);
		polygon.addPoint(x + squareSize,y);
		polygon.addPoint(x,y);
		return polygon;
	}
	
	private static Polygon bottomSquarePolygon(int x,int y,int unit,int squareSize) {
		Polygon polygon = new Polygon();
		polygon.addPoint(x,y + squareSize);
		polygon.addPoint(x + unit,y + squareSize - unit);
		polygon.addPoint(x + squareSize - unit,y + squareSize - unit);
		polygon.addPoint(x + squareSize - unit,y + unit);
		polygon.addPoint(x + squareSize,y);
		polygon.addPoint(x + squareSize,y + squareSize);
		return polygon;
	}
	
	public static void drawSquare(Graphics g2d,int x,int y,int unit,int squareSize,Colors color) {
		g2d.setColor(color.body());
		g2d.fillRect(x,y,squareSize,squareSize);
		g2d.setColor(color.top());
		g2d.fillPolygon(topSquarePolygon(x,y,unit,squareSize));
		g2d.setColor(color.bottom());
		g2d.fillPolygon(bottomSquarePolygon(x,y,unit,squareSize));
	}
}