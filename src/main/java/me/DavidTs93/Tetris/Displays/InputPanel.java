package me.DavidTs93.Tetris.Displays;

import me.DavidTs93.Tetris.Info.Coordinates;
import me.DavidTs93.Tetris.Parts.TetrisPart;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class InputPanel extends JPanel implements Display {
	public static final int DEFAULT_TEXT_SIZE = 2;
	public static final int DEFAULT_TEXT_MAX_LENGTH = 10;
	public static final Coordinates DEFAULT_BUTTON_DIMENSIONS = new Coordinates(1,6);
	public static final Coordinates DEFAULT_TEXT_DIMENSIONS = new Coordinates(DEFAULT_TEXT_SIZE + 2,16);
	public static final Coordinates DEFAULT_TEXT_START = new Coordinates(1,1);
	private static final int DEFAULT_TOTAL_COLUMNS = DEFAULT_TEXT_START.column() * 2 + DEFAULT_TEXT_DIMENSIONS.column();
//	public static final Coordinates DEFAULT_BUTTONS_DIFF = new Coordinates(DEFAULT_BUTTON_DIMENSIONS.row() + 1,0);
	public static final Coordinates DEFAULT_SUBMIT_START = new Coordinates(DEFAULT_TEXT_START.add(DEFAULT_TEXT_DIMENSIONS).row() + 1,(DEFAULT_TOTAL_COLUMNS - DEFAULT_BUTTON_DIMENSIONS.column()) / 2);
//	public static final Coordinates DEFAULT_CANCEL_START = DEFAULT_SUBMIT_START.add(DEFAULT_BUTTONS_DIFF);
	public static final Coordinates DEFAULT_TOTAL_DIMENSIONS = new Coordinates(DEFAULT_SUBMIT_START.add(DEFAULT_BUTTON_DIMENSIONS).row() + 1,DEFAULT_TOTAL_COLUMNS);
	
	public static Coordinates defaultStart(Coordinates start,Coordinates dimensions,Coordinates inputPanelDimensions) {
		return new Coordinates(start.row() + (dimensions.row() - inputPanelDimensions.row()) / 2,start.column() + (dimensions.column() - inputPanelDimensions.column()) / 2);
	}
	
	protected final TetrisPart parent;
	private final Coordinates start;
	private final Coordinates dimensions;
	private final TextField textField;
	private final Button submit;
	
	public InputPanel(TetrisPart parent,Consumer<String> onSubmit,Consumer<String> onCancel) {
		this(parent,onSubmit,onCancel,defaultStart(new Coordinates(0,0),parent.dimensions(),DEFAULT_TOTAL_DIMENSIONS),DEFAULT_TOTAL_DIMENSIONS);
	}
	
	public InputPanel(TetrisPart parent,Consumer<String> onSubmit,Consumer<String> onCancel,Coordinates start,Coordinates dimensions) {
		this(parent,onSubmit,onCancel,start,dimensions,(float) DEFAULT_TEXT_SIZE,DEFAULT_TEXT_START,DEFAULT_TEXT_DIMENSIONS,DEFAULT_TEXT_MAX_LENGTH,null,DEFAULT_SUBMIT_START,DEFAULT_BUTTON_DIMENSIONS);
	}
	
	public InputPanel(TetrisPart parent,Consumer<String> onSubmit,Consumer<String> onCancel,Coordinates start,Coordinates dimensions,
					  Float squareSizeMultText,Coordinates startText,Coordinates dimensionsText,Integer maxLengthText,
					  Float squareSizeMultSubmit,Coordinates startSubmit,Coordinates dimensionsSubmit) {
		this.parent = parent;
		this.start = start;
		this.dimensions = dimensions;
		this.textField = new TextField(this,squareSizeMultText,startText,dimensionsText,maxLengthText).horizontal(SwingConstants.CENTER);
		this.submit = new Button(this,squareSizeMultSubmit,startSubmit,dimensionsSubmit).text("SUBMIT");
		setOpaque(true);
		setLayout(null);
		setFocusable(true);
		add(this.textField);
		add(this.submit);
		setBackground();
		setBorder();
		Action submitAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				onSubmit.accept(textField.getText());
			}
		};
		Action cancelAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				onCancel.accept(textField.getText());
			}
		};
		this.submit.addActionListener(submitAction);
		InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap actionMap = getActionMap();
		inputMap.put(KeyStroke.getKeyStroke("ESCAPE"),"cancel");
		actionMap.put("cancel",cancelAction);
		inputMap = this.textField.getInputMap(JComponent.WHEN_FOCUSED);
		actionMap = this.textField.getActionMap();
		inputMap.put(KeyStroke.getKeyStroke("ENTER"),"submit");
		actionMap.put("submit",submitAction);
	}
	
	@Override
	public boolean hasBorder() {
		return true;
	}
	
	public TetrisPart parent() {
		return parent;
	}
	
	@Override
	public int startRow() {
		return start.row();
	}
	
	@Override
	public int startColumn() {
		return start.column();
	}
	
	@Override
	public int rows() {
		return dimensions.row();
	}
	
	@Override
	public int columns() {
		return dimensions.column();
	}
	
	public void afterResize() {
		textField.resize();
		submit.resize();
		repaint();
		textField.grabFocus();
	}
}