package com.ca.smartresponse;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.ca.smartresponse.parser.Parser;
import com.itko.lisa.repo.ProjectAssetComboBox;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.utils.ui.UIUtils;
import com.itko.util.StrUtil;
import com.itko.util.netbeans.EditorType;
import com.itko.util.netbeans.NetBeansEditorPanel;

public class PnlParserConfig extends JPanel {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws IOException {
		int MIN_WIDTH = 708;
		int MIN_HEIGHT = 690;
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		final JFrame testFrame = new JFrame();
		
		final PnlParserConfig pnlParserConfig = new PnlParserConfig(new TestExec(null, "", null, 0, 0));
		
		//Tamanho e posição
		testFrame.setBounds(0, 0, MIN_WIDTH, MIN_HEIGHT);
		testFrame.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		int w = testFrame.getSize().width;
		int h = testFrame.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		testFrame.setLocation(x, y);
		
		testFrame.setTitle("SmartResponse Test");
		testFrame.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentResized(ComponentEvent e) {
				pnlParserConfig.setBounds(0, 0, testFrame.getWidth() - 21, testFrame.getHeight() - 49);
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
		
		testFrame.addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowOpened(WindowEvent e) {}
		});
		
		testFrame.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		testFrame.getContentPane().setLayout(null);
		testFrame.getContentPane().add(pnlParserConfig);
		testFrame.setVisible(true);
	}

	private JTabbedPane tabParserConfig = new JTabbedPane();
	
	private JPanel pnlParser = new JPanel();
	private JLabel lblServiceImageLocator = new JLabel("Service Image Locator:");
	private ProjectAssetComboBox cmbImageLocator;
	private JButton btnOpen = new JButton("Open");
	private JLabel lblDefaultParse = new JLabel("Default parser behaviour:");
	private JComboBox<String> cmbDefaultParse = new JComboBox<String>();
	private JLabel lblPreAndPos = new JLabel("Pre and Pos response processing:");
	private JComboBox<String> cmbPrePos = new JComboBox<String>();
	private JCheckBox ckSupressNewLine = new JCheckBox("Supress new line");
	private JTabbedPane tabParserScripts = new JTabbedPane();
	private JScrollPane scrPreScript = new JScrollPane();
	private NetBeansEditorPanel txtPreScript = new NetBeansEditorPanel(EditorType.BEANSHELL);
	private JScrollPane scrPosScript = new JScrollPane();
	private NetBeansEditorPanel txtPosScript = new NetBeansEditorPanel(EditorType.BEANSHELL);
	
	private JPanel pnlTest = new JPanel();
	private JLabel lblResponse = new JLabel("Response:");
	private JScrollPane scrResponseTest = new JScrollPane();
	private NetBeansEditorPanel txtResponseTest = new NetBeansEditorPanel(EditorType.PLAIN);
	private JButton btnRun = new JButton("Run");
	private JTabbedPane tabTestResult = new JTabbedPane();
	private JScrollPane scrResult = new JScrollPane();
	private JTextArea txtResult = new JTextArea();
	private JScrollPane scrOutput = new JScrollPane();
	private JTextArea txtOutput = new JTextArea();
	
	public PnlParserConfig(final TestExec testExec) {
		setLayout(null);
		
		tabParserConfig.addTab("Parser Config", null, pnlParser, null);
		tabParserConfig.addTab("Test", null, pnlTest, null);
		tabParserConfig.setBounds(12, 13, 664, 620);
		
		add(tabParserConfig);

		//Aba PARSER
		pnlParser.setLayout(null);
		
		cmbImageLocator = ProjectAssetComboBox.createVSIsCB();
		
		lblServiceImageLocator.setHorizontalAlignment(SwingConstants.RIGHT);
		lblServiceImageLocator.setBounds(12, 13, 201, 16);
		cmbImageLocator.setBounds(216, 10, 356, 22);
		btnOpen.setBounds(584, 9, 63, 25);
		lblDefaultParse.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDefaultParse.setBounds(12, 49, 201, 16);
		cmbDefaultParse.setBounds(216, 46, 431, 22);
		lblPreAndPos.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPreAndPos.setBounds(12, 84, 201, 16);
		cmbPrePos.setBounds(216, 81, 431, 22);
		ckSupressNewLine.setBounds(216, 112, 137, 25);
		tabParserScripts.setBounds(12, 146, 635, 433);

		cmbDefaultParse.setModel(new DefaultComboBoxModel<String>(new String[] {"Parse all, unless there is the \"DO_NOT_PARSE\" instruction", "Do not parse all, unless there is the \"DO_PARSE\" instruction"}));
		cmbPrePos.setModel(new DefaultComboBoxModel<String>(new String[] {"Aways process the pre and pos responses", "Process the pre and pos responses depending on the parser behaviour"}));
		ckSupressNewLine.setSelected(true);
		
		pnlParser.add(lblServiceImageLocator);
		pnlParser.add(cmbImageLocator);
		pnlParser.add(btnOpen);
		pnlParser.add(lblDefaultParse);
		pnlParser.add(cmbDefaultParse);
		pnlParser.add(lblPreAndPos);
		pnlParser.add(cmbPrePos);
		pnlParser.add(ckSupressNewLine);
		pnlParser.add(tabParserScripts);
		
		tabParserScripts.addTab("Pre Response Script", null, scrPreScript, null);
		tabParserScripts.addTab("Pos Response Script", null, scrPosScript, null);
		
		scrPreScript.setViewportView(txtPreScript);
		scrPosScript.setViewportView(txtPosScript);
		
		
		//Aba TEST
		pnlTest.setLayout(null);
		
		lblResponse.setBounds(12, 13, 95, 16);
		scrResponseTest.setBounds(12, 34, 635, 304);
		btnRun.setBounds(550, 351, 97, 25);
		tabTestResult.setBounds(12, 389, 635, 188);

		tabTestResult.addTab("Result", null, scrResult, null);
		tabTestResult.addTab("Log Output", null, scrOutput, null);
		
		txtResult.setEditable(false);
		txtOutput.setEditable(false);
		
		scrResponseTest.setViewportView(txtResponseTest);
		scrResult.setViewportView(txtResult);
		scrOutput.setViewportView(txtOutput);

		pnlTest.add(lblResponse);
		pnlTest.add(scrResponseTest);
		pnlTest.add(btnRun);
		pnlTest.add(tabTestResult);
		
		addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentResized(ComponentEvent e) {
				tabParserConfig.setBounds(0, 0, getWidth(), getHeight());
				
				cmbImageLocator.setBounds(216, 10, getWidth() - 307, 22);
				btnOpen.setBounds(getWidth() - 82, 9, 63, 25);
				cmbDefaultParse.setBounds(216, 46, getWidth() - 235, 22);
				cmbPrePos.setBounds(216, 81, getWidth() - 235, 22);
				tabParserScripts.setBounds(12, 146, getWidth() - 30, getHeight() - 180);
				
				scrResponseTest.setBounds(12, 34, getWidth() - 30, 304);
				tabTestResult.setBounds(12, 389, getWidth() - 30, getHeight() - 423);
				btnRun.setBounds(getWidth() - 118, 351, 97, 25);
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
		});

		btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String source = cmbImageLocator.getSelectedFileName();
				if (!StrUtil.isEmpty(source)) {
					UIUtils.loadVSImageIntoEditor(source);
				}
			}
		});
		
		btnRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					txtResult.setText("");
					txtOutput.setText("");
					
					String responseBody = txtResponseTest.getText();
					Parser parser = new Parser();
					
					if (cmbPrePos.getSelectedIndex() == 0 || (cmbPrePos.getSelectedIndex() == 1 && !parser.isIgnoreFile(responseBody, cmbDefaultParse.getSelectedIndex() == 0)))
						responseBody = "<%".concat(txtPreScript.getText()).concat("%>").
							concat(responseBody).
							concat("<%").concat(txtPosScript.getText()).concat("%>");
					
					responseBody = parser.parseText(responseBody, testExec, cmbDefaultParse.getSelectedIndex() == 0, ckSupressNewLine.isSelected());
					txtResult.setText(responseBody);
				}
				catch (Exception x) {
					x.printStackTrace();
					
					StringBuilder errorMsg = new StringBuilder();
					errorMsg.append(x.getMessage());

					Throwable cause = x.getCause();
					int deepCount = 0;
					while (cause != null && deepCount < 15) {
						errorMsg.append("\n at ").append(cause.getMessage());
						cause = cause.getCause();
						deepCount++;
					}
					
					txtOutput.setText(errorMsg.toString());
				}
			}
		});
		
		setVisible(true);
	}

	public JComboBox<String> getCmbDefaultParse() {
		return cmbDefaultParse;
	}

	public JComboBox<String> getCmbPrePos() {
		return cmbPrePos;
	}

	public NetBeansEditorPanel getTxtPreScript() {
		return txtPreScript;
	}

	public NetBeansEditorPanel getTxtPosScript() {
		return txtPosScript;
	}

	public JTabbedPane getTabParserScripts() {
		return tabParserScripts;
	}

	public void setTabParserScripts(JTabbedPane tabParserScripts) {
		this.tabParserScripts = tabParserScripts;
	}

	public JCheckBox getCkSupressNewLine() {
		return ckSupressNewLine;
	}

	public NetBeansEditorPanel getTxtResponseTest() {
		return txtResponseTest;
	}

	public ProjectAssetComboBox getCmbImageLocator() {
		return cmbImageLocator;
	}
}
