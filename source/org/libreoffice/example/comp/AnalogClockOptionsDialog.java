package org.libreoffice.example.comp;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AnalogClockOptionsDialog {
	public static void Show() {
		if (maFrame == null)
			CreateDialog();
		maFrame.setVisible(true);
		maFrame.toFront();
	}

	private static void CreateDialog() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		maFrame = new JFrame("Analog Clock Options");
		maFrame.setSize(600, 400);
		maFrame.setLayout(new GridBagLayout());
		final GridBagConstraints aConstraints = new GridBagConstraints();

		final ButtonGroup aColorButtonGroup = new ButtonGroup();
		JRadioButton aRadioButton = new JRadioButton("Clock face color");
		final JRadioButton aFirstRadioButton = aRadioButton;
		aColorButtonGroup.add(aRadioButton);
		aRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent aEvent) {
				ShowColor(ColorType.FaceColor);
			}
		});
		aConstraints.gridy = 0;
		aConstraints.anchor = GridBagConstraints.NORTHWEST;
		maFrame.add(aRadioButton, aConstraints);

		aRadioButton = new JRadioButton("Hour hand color");
		aColorButtonGroup.add(aRadioButton);
		aRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent aEvent) {
				ShowColor(ColorType.HourHandColor);
			}
		});
		aConstraints.gridy = 1;
		aConstraints.anchor = GridBagConstraints.NORTHWEST;
		maFrame.add(aRadioButton, aConstraints);

		aRadioButton = new JRadioButton("Minute hand color");
		aColorButtonGroup.add(aRadioButton);
		aRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent aEvent) {
				ShowColor(ColorType.MinuteHandColor);
			}
		});
		aConstraints.gridy = 2;
		aConstraints.anchor = GridBagConstraints.NORTHWEST;
		maFrame.add(aRadioButton, aConstraints);

		aRadioButton = new JRadioButton("Second hand color");
		aColorButtonGroup.add(aRadioButton);
		aRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent aEvent) {
				ShowColor(ColorType.SecondHandColor);
			}
		});
		aConstraints.gridy = 3;
		aConstraints.anchor = GridBagConstraints.NORTHWEST;
		maFrame.add(aRadioButton, aConstraints);

		final JButton aCloseButton = new JButton("Close");
		aCloseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent aEvent) {
				maFrame.setVisible(false);
				maFrame.dispose();
				maFrame = null;
			}
		});
		aConstraints.gridy = 10;
		aConstraints.gridx = 1;
		aConstraints.anchor = GridBagConstraints.EAST;
		maFrame.add(aCloseButton, aConstraints);

		aFirstRadioButton.setSelected(true);
		ShowColor(ColorType.FaceColor);

		maFrame.setVisible(true);
	}

	protected static void ShowColor(final ColorType eColorType) {
		if (maColorChooser != null)
			maFrame.remove(maColorChooser);

		final Color aColor = AnalogClockPanel.GetColor(eColorType);
		maColorChooser = new JColorChooser(aColor);
		maColorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent aEvent) {
				AnalogClockPanel.SetColor(eColorType, maColorChooser.getColor());
			}
		});

		final GridBagConstraints aConstraints = new GridBagConstraints();
		aConstraints.gridx = 1;
		aConstraints.gridy = 0;
		aConstraints.gridheight = 4;
		maFrame.add(maColorChooser, aConstraints);
		maFrame.validate();
	}

	private static JFrame maFrame = null;
	private static JColorChooser maColorChooser;
}
