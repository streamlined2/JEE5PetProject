package com.project.interfacebuilder.http;

import java.awt.Color;
import java.awt.Font;

public final class Helpers {
	
	private Helpers(){}

	public static String getColorCode(Color color) {
		StringBuilder colorCode=new StringBuilder();
		colorCode.
			append("rgb(").
			append(Integer.toString(color.getRed())).
			append(",").
			append(Integer.toString(color.getGreen())).
			append(",").
			append(Integer.toString(color.getBlue())).
			append(")");
		return colorCode.toString();
	}

	public static String getFontStyle(Font font){
		int style=font.getStyle();
		StringBuilder builder=new StringBuilder();
		if((style&Font.ITALIC)!=0) builder.append("italic ");
		if((style&Font.BOLD)!=0) builder.append("bold ");
		return builder.toString();
	}

	public static String getStyle(Font font){
		StringBuilder builder=new StringBuilder();
		builder.
			append("style=\"font: ").
			append(getFontStyle(font)).append(" ").
			append(font.getSize()).
			append("px ").
			append(font.getFontName()).append(",").
			append(font.getFamily()).
			append(";\" ");
		return builder.toString();
		
	}

}
