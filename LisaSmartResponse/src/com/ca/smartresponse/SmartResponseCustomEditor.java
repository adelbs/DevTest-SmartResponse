package com.ca.smartresponse;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.itko.lisa.editor.CustomEditor;
import com.itko.lisa.editor.TestNodeInfo;

public class SmartResponseCustomEditor extends CustomEditor {

	private static final long serialVersionUID = 1L;

	private PnlParserConfig pnlParserConfig;
	private TestNodeInfo nodeInfo;

	@Override
	public void display() {
		if (pnlParserConfig == null) {
			nodeInfo = getController().getTestNode();
			
			setLayout(null);
			
			pnlParserConfig = new PnlParserConfig(nodeInfo.getTestCaseInfo().getTestExec());
			pnlParserConfig.setBounds(0, 0, getWidth(), getHeight());
			
			addComponentListener(new ComponentListener() {
				@Override
				public void componentShown(ComponentEvent e) {}
				@Override
				public void componentResized(ComponentEvent e) {
					pnlParserConfig.setBounds(0, 0, getWidth(), getHeight());
				}
				@Override
				public void componentMoved(ComponentEvent e) {}
				@Override
				public void componentHidden(ComponentEvent e) {}
			});

			add(pnlParserConfig);
			
			if (nodeInfo.getAttribute("SmartResponse-DefaultParser") != null)
				pnlParserConfig.getCmbDefaultParse().setSelectedIndex(Integer.parseInt(nodeInfo.getAttribute("SmartResponse-DefaultParser").toString()));

			if (nodeInfo.getAttribute("SmartResponse-PrePosProcessing") != null)
				pnlParserConfig.getCmbPrePos().setSelectedIndex(Integer.parseInt(nodeInfo.getAttribute("SmartResponse-PrePosProcessing").toString()));

			if (nodeInfo.getAttribute("SmartResponse-PreResponse") != null)
				pnlParserConfig.getTxtPreScript().setText(nodeInfo.getAttribute("SmartResponse-PreResponse").toString());

			if (nodeInfo.getAttribute("SmartResponse-PosResponse") != null)
				pnlParserConfig.getTxtPosScript().setText(nodeInfo.getAttribute("SmartResponse-PosResponse").toString());

			if (nodeInfo.getAttribute("SmartResponse-SupressNewLine") != null)
				pnlParserConfig.getCkSupressNewLine().setSelected(nodeInfo.getAttribute("SmartResponse-SupressNewLine").equals("true"));

			if (nodeInfo.getAttribute("SmartResponse-ResponseTest") != null)
				pnlParserConfig.getTxtResponseTest().setText(nodeInfo.getAttribute("SmartResponse-ResponseTest").toString());

			if (nodeInfo.getAttribute("SmartResponse-ImageLocator") != null)
				pnlParserConfig.getCmbImageLocator().setSelectedFileName(nodeInfo.getAttribute("SmartResponse-ImageLocator").toString());

		}
	}

	@Override
	public String isEditorValid() {
		return null;
	}

	@Override
	public void save() {
		nodeInfo.removeAttribute("SmartResponse-ImageLocator");
		nodeInfo.removeAttribute("SmartResponse-DefaultParser");
		nodeInfo.removeAttribute("SmartResponse-PrePosProcessing");
		nodeInfo.removeAttribute("SmartResponse-PreResponse");
		nodeInfo.removeAttribute("SmartResponse-PosResponse");
		nodeInfo.removeAttribute("SmartResponse-SupressNewLine");
		nodeInfo.removeAttribute("SmartResponse-ResponseTest");
		
		nodeInfo.putAttribute("SmartResponse-ImageLocator", pnlParserConfig.getCmbImageLocator().getSelectedFileName() != null ? pnlParserConfig.getCmbImageLocator().getSelectedFileName() : "");
		nodeInfo.putAttribute("SmartResponse-DefaultParser", String.valueOf(pnlParserConfig.getCmbDefaultParse().getSelectedIndex()));
		nodeInfo.putAttribute("SmartResponse-PrePosProcessing", String.valueOf(pnlParserConfig.getCmbPrePos().getSelectedIndex()));
		nodeInfo.putAttribute("SmartResponse-PreResponse", pnlParserConfig.getTxtPreScript().getText());
		nodeInfo.putAttribute("SmartResponse-PosResponse", pnlParserConfig.getTxtPosScript().getText());
		nodeInfo.putAttribute("SmartResponse-SupressNewLine", String.valueOf(pnlParserConfig.getCkSupressNewLine().isSelected()));
		nodeInfo.putAttribute("SmartResponse-ResponseTest", pnlParserConfig.getTxtResponseTest().getText());
	}
}
