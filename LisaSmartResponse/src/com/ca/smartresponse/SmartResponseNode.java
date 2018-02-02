package com.ca.smartresponse;

import java.util.List;

import org.w3c.dom.Element;

import com.ca.smartresponse.parser.Parser;
import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.lisa.vse.stateful.ConversationalStep;
import com.itko.lisa.vse.stateful.model.TransientResponse;

public class SmartResponseNode extends TestNode {

	private ConversationalStep imageStep = new ConversationalStep();
	
	private String imageLocator = "";
	private String defaultParser = "";
	private String prePosProcessing = "";
	private String preResponse = "";
	private String posResponse = "";
	private String supressNewLine = "";
	private String responseTest = "";
	
	@Override
	public String getTypeName() throws Exception {
		return "Smart Response";
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void execute(TestExec testExec) throws TestRunException {
		
		try {
			//Image embedded step
			String nextNode = testExec.getCurrentNode().getElseNode();
			imageStep.setName("Name");
			imageStep.setThink("0");
			imageStep.setTest(getTest());
			imageStep.setVSISource(imageLocator);
			imageStep.setOutputAsObject(true);
			imageStep.prepare(getTest());
			imageStep.executeNode(testExec);
			testExec.setNextNode(nextNode);
			
			//Smart response parser
			List resp = (List) testExec.getStateObject("lisa.vse.response");
			TransientResponse trResponse = (TransientResponse) resp.get(0);
			Parser parser = new Parser();
			
			if (trResponse != null && !trResponse.isBinary() && trResponse.getBodyAsString() != null) {
			
				String responseBody = trResponse.getBodyAsString();
				
				if (prePosProcessing.equals("0") || (prePosProcessing.equals("1") && !parser.isIgnoreFile(responseBody, defaultParser.equals("0"))))
					responseBody = "<%".concat(preResponse).concat("%>").concat(responseBody).concat("<%").concat(posResponse).concat("%>");
				
				responseBody = parser.parseText(testExec.parseInState(responseBody), testExec, defaultParser.equals("0"), supressNewLine.equals("true"));
				trResponse.setBody(responseBody);
				testExec.setLastResponse(responseBody);
			}
		}
		catch (Exception x) {
			x.printStackTrace();
		}
	}
	
	@Override
	public void initialize(TestCase testCase, Element element) throws TestDefException {
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			if (element.getChildNodes().item(i).getNodeName().equals("SmartResponse-ImageLocator"))
				this.imageLocator = element.getChildNodes().item(i).getTextContent();
			else if (element.getChildNodes().item(i).getNodeName().equals("SmartResponse-DefaultParser"))
				this.defaultParser = element.getChildNodes().item(i).getTextContent();
			else if (element.getChildNodes().item(i).getNodeName().equals("SmartResponse-PrePosProcessing"))
				this.prePosProcessing = element.getChildNodes().item(i).getTextContent();
			else if (element.getChildNodes().item(i).getNodeName().equals("SmartResponse-PreResponse"))
				this.preResponse = element.getChildNodes().item(i).getTextContent();
			else if (element.getChildNodes().item(i).getNodeName().equals("SmartResponse-PosResponse"))
				this.posResponse = element.getChildNodes().item(i).getTextContent();
			else if (element.getChildNodes().item(i).getNodeName().equals("SmartResponse-SupressNewLine"))
				this.supressNewLine = element.getChildNodes().item(i).getTextContent();
			else if (element.getChildNodes().item(i).getNodeName().equals("SmartResponse-ResponseTest"))
				this.responseTest = element.getChildNodes().item(i).getTextContent();
		}
	}

	public String getDefaultParser() {
		return defaultParser;
	}

	public String getPrePosProcessing() {
		return prePosProcessing;
	}

	public String getPreResponse() {
		return preResponse;
	}

	public String getPosResponse() {
		return posResponse;
	}

	public String getSupressNewLine() {
		return supressNewLine;
	}

	public String getResponseTest() {
		return responseTest;
	}

	public String getImageLocator() {
		return imageLocator;
	}
	
}
