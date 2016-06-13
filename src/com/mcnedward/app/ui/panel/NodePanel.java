package com.mcnedward.app.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * @author Edward McNealy <edwardmcn64@gmail.com> - Oct 26, 2015
 *
 */
public class NodePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private boolean smallIcons;
	
	private JLabel label;
	private Color color;
	
	private int x, y, width, height;

	public NodePanel(String text, Color color, boolean smallIcons) {
		super();
		this.color = color;
		this.smallIcons = smallIcons;

		setPreferredSize(new Dimension(80, 60));
		setLayout(new BorderLayout(0, 0));
		
		label = new JLabel(text);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		add(label, BorderLayout.CENTER);
		
		setIconSize();
	}
	
	public NodePanel(String text, Color color) {
		this(text, color, false);
	}
	
	private void setIconSize() {
		if (smallIcons) {
			x = 40;
			y = 20;
			width = 30;
			height = 30;
			label.setFont(new Font("Segoe UI", Font.PLAIN, 10));
			label.setBorder(new EmptyBorder(10, 30, 0, 0));
		} else {
			x = 15;
			y = 0;
			width = 50;
			height = 50;
			label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			label.setBorder(new EmptyBorder(0, 2, 10, 0));
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(color);
		g.drawOval(x, y, width, height);
	}
	
	public String getText() {
		return label.getText();
	}
	
	public void setText(String text) {
		label.setText(text);
	}
	
	@Override
	public String toString() {
		return "NodePanel - " + getText();
	}

}
