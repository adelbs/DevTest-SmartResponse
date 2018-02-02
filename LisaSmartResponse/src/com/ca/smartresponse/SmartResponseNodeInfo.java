package com.ca.smartresponse;

import java.io.PrintWriter;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.itko.lisa.editor.TestNodeInfo;
import com.itko.util.XMLUtils;

public class SmartResponseNodeInfo extends TestNodeInfo {

	@Override
	public String getHelpString() {
		return "Smart Response";
	}

	@Override
	public void initNewOne() {
		System.out.println("Init New One");
	}

	@Override
	public void writeSubXML(PrintWriter pw) {
		XMLUtils.streamTagAndChild(pw, "SmartResponse-ImageLocator", (String) getAttribute("SmartResponse-ImageLocator"));
		XMLUtils.streamTagAndChild(pw, "SmartResponse-DefaultParser", (String) getAttribute("SmartResponse-DefaultParser"));
		XMLUtils.streamTagAndChild(pw, "SmartResponse-PrePosProcessing", (String) getAttribute("SmartResponse-PrePosProcessing"));
		XMLUtils.streamTagAndChild(pw, "SmartResponse-PreResponse", (String) getAttribute("SmartResponse-PreResponse"));
		XMLUtils.streamTagAndChild(pw, "SmartResponse-PosResponse", (String) getAttribute("SmartResponse-PosResponse"));
		XMLUtils.streamTagAndChild(pw, "SmartResponse-SupressNewLine", (String) getAttribute("SmartResponse-SupressNewLine"));
		XMLUtils.streamTagAndChild(pw, "SmartResponse-ResponseTest", (String) getAttribute("SmartResponse-ResponseTest"));
		pw.flush();
	}

	@Override
	public void migrate(Object node) {
		putAttribute("SmartResponse-ImageLocator", ((SmartResponseNode) node).getImageLocator());
		putAttribute("SmartResponse-DefaultParser", ((SmartResponseNode) node).getDefaultParser());
		putAttribute("SmartResponse-PrePosProcessing", ((SmartResponseNode) node).getPrePosProcessing());
		putAttribute("SmartResponse-PreResponse", ((SmartResponseNode) node).getPreResponse());
		putAttribute("SmartResponse-PosResponse", ((SmartResponseNode) node).getPosResponse());
		putAttribute("SmartResponse-SupressNewLine", ((SmartResponseNode) node).getSupressNewLine());
		putAttribute("SmartResponse-ResponseTest", ((SmartResponseNode) node).getResponseTest());
	}

	@Override
	public String getEditorName() {
		return "Smart Response";
	}

	@Override
	public Icon getSmallIcon() {
		return new ImageIcon(this.getClass().getResource("/com/ca/smartresponse/resource/script.png"));
	}
	
	@Override
	public Icon getLargeIcon() {
		return new ImageIcon(this.getClass().getResource("/com/ca/smartresponse/resource/script.png"));
	}

}
