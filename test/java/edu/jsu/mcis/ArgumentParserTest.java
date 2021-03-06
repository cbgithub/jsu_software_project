package edu.jsu.mcis;

import org.junit.*;
import static org.junit.Assert.*;
import edu.jsu.mcis.*;
import org.junit.rules.ExpectedException;
import java.util.*;
import org.w3c.dom.Document;
import javax.xml.parsers.*;


public class ArgumentParserTest {

	public ArgumentParser ap;
	public XML xml;

	@Before
	public void startUp(){
		ap = new ArgumentParser();
		xml = new XML();
	}

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test (expected=MissingArgumentException.class)
	public void testOnly1numberEntered() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		String[] data = {"7"};
		ap.parse(data);
	}

	@Test (expected=MissingArgumentException.class)
	public void testLessThan3NumbersEntered() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		String[] data = {"7","5"};
		ap.parse(data);
	}

	@Test (expected=UnknownArgumentException.class)
	public void testMoreThan3NumbersEntered() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		String[] data = {"7","5", "3", "7"};
		ap.parse(data);
	}

	@Test
	public void testGetLength() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		String[] data = {"7","5", "3"};
		ap.parse(data);
		assertEquals(ap.getValue("length"), "7");
	}

	@Test
	public void testHelpMessageCalled() {
		expectedEx.expect(HelpMessageException.class);
		String message = ("usage: java VolumeCalculator [length][width][height]" +
		"\n" + "Calculate the volume of a box." + "\n" + "positional arguments:" +
		"\n" +   "length the length of the box (FLOAT)"  + "\n" +
		"width the width of the box (FLOAT)" + "\n" +
		"height the height of the box (FLOAT)");
		expectedEx.expectMessage(message);
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length", Argument.Type.FLOAT, "the length of the box");
		ap.addPositionalArgument("width", Argument.Type.FLOAT, "the width of the box");
		ap.addPositionalArgument("height", Argument.Type.FLOAT, "the height of the box");
		String[] data = {"-h"};
		ap.parse(data);
	}

	@Test
	public void testGetDefaultType() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		ap.addNamedArgument("Type", "t", Argument.Type.STRING, "Box");
		String[] data = {"7","5", "3"};
		ap.parse(data);
		assertEquals("Box", ap.getValue("Type"));
	}

	@Test
	public void testGetType() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		ap.addNamedArgument("Type", "t" , Argument.Type.STRING, "Box");
		String[] data = {"7","5", "3", "--Type","circle"};
		ap.parse(data);
		assertEquals("circle", ap.getValue("Type"));
	}

	@Test
	public void testGetColor() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		ap.addNamedArgument("Type", "t",Argument.Type.STRING, "Box");
		ap.addNamedArgument("Color", "c", Argument.Type.STRING, "Red");
		String[] data = {"7","5", "3", "--Type","circle","--Color","Blue"};
		ap.parse(data);
		assertEquals("Blue", ap.getValue("Color"));
	}

	@Test
	public void testGetPizza() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		ap.addNamedArgument("Pizza", "p", Argument.Type.STRING, "pepperoni");
		ap.addNamedArgument("Color", "c", Argument.Type.STRING, "Red");
		String[] data = {"7","5", "3", "--Pizza","cheese","--Color","Blue"};
		ap.parse(data);
		assertEquals("cheese", ap.getValue("Pizza"));
	}

	@Test
	public void testGetDigit() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		ap.addNamedArgument("Type", "t",Argument.Type.STRING, "Box");
		ap.addNamedArgument("Digits", "d", Argument.Type.INT, "4");
		String[] data = {"7","5", "3", "--Type","circle","--Digits","1"};
		ap.parse(data);
		assertEquals(1, ap.getValue("Digits"));
	}

	@Test
	public void testGetTypeAnywhere() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		ap.addNamedArgument("Type", "t", Argument.Type.STRING, "Box");
		ap.addNamedArgument("Digits", "d", Argument.Type.INT, "4");
		String[] data = {"--Type","circle","7","5","--Digits","8","3"};
		ap.parse(data);
		assertEquals("circle", ap.getValue("Type"));
	}

	@Test
	public void testGetDigitsAnywhere() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		ap.addNamedArgument("Type", "t", Argument.Type.STRING, "Box");
		ap.addNamedArgument("Digits", "d", Argument.Type.INT, "4");
		String[] data = {"--Type","circle","2","5","--Digits","7","3"};
		ap.parse(data);
		assertEquals(7, ap.getValue("Digits"));
	}

	@Test
	public void testGetLengthAnywhere() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		ap.addNamedArgument("Type", "t", Argument.Type.STRING, "Box");
		ap.addNamedArgument("Digits","d", Argument.Type.INT, "4");
		String[] data = {"--Type","circle","2","5","--Digits","7","3"};
		ap.parse(data);
		assertEquals("2", ap.getValue("length"));
	}

	@Test
	public void testGetDigits() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		ap.addNamedArgument("Digits", "d", Argument.Type.INT, "4");
		String[] data = {"7","5", "3"};
		ap.parse(data);
		assertEquals(4, ap.getValue("Digits"));
	}

	@Test
	public void testTypeInt() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length",Argument.Type.INT);
		ap.addPositionalArgument("width",Argument.Type.INT);
		ap.addPositionalArgument("height",Argument.Type.INT);
		String[] data = {"7","5", "3"};
		ap.parse(data);
		int num=7;
		assertEquals(ap.getValue("length"), num);
	}

	@Test
	public void testTypeBoolean() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length",Argument.Type.INT);
		ap.addPositionalArgument("Dog",Argument.Type.BOOLEAN);
		ap.addPositionalArgument("height",Argument.Type.STRING);
		String[] data = {"7","true", "3"};
		ap.parse(data);
		boolean value=true;
		assertEquals(true, ap.getValue("Dog"));
	}

	@Test
	public void testTypeString() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length",Argument.Type.INT);
		ap.addPositionalArgument("Dog",Argument.Type.STRING);
		ap.addPositionalArgument("height",Argument.Type.FLOAT);
		String[] data = {"7","true", "4.0"};
		ap.parse(data);
		assertEquals(ap.getValue("Dog"), "true");
	}

	@Test (expected=UnknownArgumentException.class)
	public void testUnknownArgument() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length",Argument.Type.INT);
		ap.addPositionalArgument("Dog",Argument.Type.STRING);
		ap.addPositionalArgument("height",Argument.Type.FLOAT);
		String[] data = {"7","true", "4.0"};
		ap.parse(data);
		assertEquals(ap.getValue("Type"), "circle");
	}


	@Test
	public void testTypeFloat() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length",Argument.Type.INT);
		ap.addPositionalArgument("Dog",Argument.Type.FLOAT);
		ap.addPositionalArgument("height",Argument.Type.FLOAT);
		String[] data = {"7","8", "4"};
		ap.parse(data);
		float number=8.0f;
		assertEquals(ap.getValue("Dog"), number);
	}

	@Test (expected=IncorrectDataTypeException.class)
	public void testInvalidTypeFloat() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length",Argument.Type.FLOAT);
		ap.addPositionalArgument("width",Argument.Type.FLOAT);
		ap.addPositionalArgument("height",Argument.Type.FLOAT);
		String[] data = {"7","something", "4"};
		ap.parse(data);
	}

	@Test
	public void testIncorrectDataTypeExceptionMessageCorrect() {
		expectedEx.expect(IncorrectDataTypeException.class);
		String message = ("usage: java VolumeCalculator [length][width][height]" +
		"\n" + "VolumeCalculator.java: error: argument width: invalid FLOAT value: something");
		expectedEx.expectMessage(message);
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length",Argument.Type.FLOAT);
		ap.addPositionalArgument("width",Argument.Type.FLOAT);
		ap.addPositionalArgument("height",Argument.Type.FLOAT);
		String[] data = {"7","something", "4"};
		ap.parse(data);
	}

	@Test
	public void testHelpMessageStringBuilder() {
		expectedEx.expect(MissingArgumentException.class);
		String message = ("usage: java VolumeCalculator [length][width][height]" +
		"\n" + "VolumeCalculator.java error: The following arguments are required  width height");
		expectedEx.expectMessage(message);
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		String[] data = {"1"};
		ap.parse(data);
	}

	@Test (expected=IncorrectDataTypeException.class)
	public void testInvalidTypeInteger() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length",Argument.Type.FLOAT);
		ap.addPositionalArgument("width",Argument.Type.INT);
		ap.addPositionalArgument("height",Argument.Type.FLOAT);
		String[] data = {"7","something", "4"};
		ap.parse(data);
	}

	@Test
	public void testGetHelpMessageShortNameAnywhere() {
		expectedEx.expect(HelpMessageException.class);
		String message = ("usage: java VolumeCalculator [length][width][height]" +
		"\n" + "Calculate the volume of a box." + "\npositional arguments:" +
		"\n" +   "length the length of the box (FLOAT)"  + "\n" +
		"width the width of the box (FLOAT)" + "\nheight the height of the box (FLOAT)");
		expectedEx.expectMessage(message);
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length", Argument.Type.FLOAT, "the length of the box");
		ap.addPositionalArgument("width", Argument.Type.FLOAT, "the width of the box");
		ap.addPositionalArgument("height", Argument.Type.FLOAT, "the height of the box");
		String[] data = {"7","5", "-h", "4"};
		//System.out.println("|" + message + "|");
		//try {
			ap.parse(data);
		//}
		//catch(Exception e) {
		//	System.out.println("|" + e.getMessage() + "|");
		//}
	}

	@Test
	public void testGetHelpMessageLongNameAnywhere() {
		expectedEx.expect(HelpMessageException.class);
		String message = ("usage: java VolumeCalculator [length][width][height]" +
		"\n" + "Calculate the volume of a box." + "\n" + "positional arguments:" +
		"\n" +   "length the length of the box (FLOAT)"  + "\n" +
		"width the width of the box (FLOAT)" + "\n" +
		"height the height of the box (FLOAT)");
		expectedEx.expectMessage(message);
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length", Argument.Type.FLOAT, "the length of the box");
		ap.addPositionalArgument("width", Argument.Type.FLOAT, "the width of the box");
		ap.addPositionalArgument("height", Argument.Type.FLOAT, "the height of the box");
		String[] data = {"7","5", "--help", "4"};
		ap.parse(data);
	}

	@Test
	public void testGetHelpMessageAnywhereWithNamedArguments() {
		expectedEx.expect(HelpMessageException.class);
		String message = ("usage: java VolumeCalculator [length][width][height][Type][t][Digits][d]" +
		"\n" + "Calculate the volume of a box." + "\n" + "positional arguments:" +
		"\n" + "length the length of the box (FLOAT)"  + "\n" +
		"width the width of the box (FLOAT)" + "\n" + "height the height of the box (FLOAT)\n" +
		"Type  (STRING)\n" + "Type  (STRING)\n" + "Digits  (INT)\n" + "Digits  (INT)");
		expectedEx.expectMessage(message);
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length", Argument.Type.FLOAT, "the length of the box");
		ap.addPositionalArgument("width", Argument.Type.FLOAT, "the width of the box");
		ap.addPositionalArgument("height", Argument.Type.FLOAT, "the height of the box");
		ap.addNamedArgument("Type", "t", Argument.Type.STRING, "Box");
		ap.addNamedArgument("Digits", "d", Argument.Type.INT, "4");
		String[] data = {"7","--Type","circle","5", "-h", "4","--Digits","2"};
		ap.parse(data);
	}

	@Test
	public void testShortNamedArgument() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		ap.addNamedArgument("Type", "t", Argument.Type.STRING, "Box");
		ap.addNamedArgument("Digits","d", Argument.Type.INT, "4");
		String[] data = {"7","-t","circle","5", "4","--Digits","2"};
		ap.parse(data);
		assertEquals("circle",ap.getValue("Type"));
	}

	@Test
	public void testShortNamedColorArgument() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length");
		ap.addPositionalArgument("width");
		ap.addPositionalArgument("height");
		ap.addNamedArgument("color", "c", Argument.Type.STRING, "Blue");
		ap.addNamedArgument("Digits", "d", Argument.Type.INT, "4");
		String[] data = {"7","-c","red","5", "4","--Digits","2"};
		ap.parse(data);
		assertEquals("red",ap.getValue("color"));
	}

	@Test
	public void testGetHelp() {
		expectedEx.expect(HelpMessageException.class);
		String message = ("usage: java VolumeCalculator [length][width][height]" +
		"\n" + "Calculate the volume of a box." + "\n" + "positional arguments:" +
		"\n" +   "length the length of the box (FLOAT)"  + "\n" +
		"width the width of the box (FLOAT)" + "\n" +
		"height the height of the box (FLOAT)");
		expectedEx.expectMessage(message);
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box.");
		ap.addPositionalArgument("length", Argument.Type.FLOAT, "the length of the box");
		ap.addPositionalArgument("width", Argument.Type.FLOAT, "the width of the box");
		ap.addPositionalArgument("height", Argument.Type.FLOAT, "the height of the box");
		String[] data = {"--help"};
		ap.parse(data);
	}

	@Test
	public void testGetProgramName() {
		ap.assignProgramName("VolumeCalculator");
		assertEquals (ap.getProgramName(), "VolumeCalculator");
	}

	@Test
	public void testAssignProgramDescription() {
		ap.assignProgramDescription("Calculate the volume of a box");
		assertEquals (ap.getProgramDescription(), "Calculate the volume of a box");
	}

	@Test
  public void testCreatingNewXML() throws Exception{
    ap.addPositionalArgument("pizza", Argument.Type.FLOAT, "pepperoni");
		ap.addPositionalArgument("drink", Argument.Type.FLOAT, "sprite");
		ap.addPositionalArgument("snack", Argument.Type.FLOAT, "hunny bun");
		ap.addNamedArgument("Pizza", "p", Argument.Type.STRING, "pepperoni");
		ap.addNamedArgument("Color", "c", Argument.Type.STRING, "Red");
    xml.saveXML("newfile.xml", ap);
  }

	@Test
	public void testWeGetFloatDataTypeFromArguments() {
		ap.assignProgramName("VolumeCalculator");
		ap.assignProgramDescription("Calculate the volume of a box");
		ap = XML.loadXML("arguments.xml");
		String[] inp = {"6", "7", "8"};
		ap.parse(inp);
		assertEquals(6.0f, ap.getValue("length"));
		assertEquals(7.0f, ap.getValue("width"));
		assertEquals(8.0f, ap.getValue("height"));
	}

	@Test
	public void testLoadXMLGetPositionalArgumentValue() {
		ap = XML.loadXML("arguments.xml");
		String[] inp = {"233.5", "52.9", "88.6"};
		ap.parse(inp);
		assertEquals(233.5f, ap.getValue("length"));
		assertEquals(52.9f, ap.getValue("width"));
		assertEquals(88.6f, ap.getValue("height"));
	}

	@Test
	public void testWeDontLoadABadFile() {
		expectedEx.expect(FileErrorException.class);
		String message = ("Error with file: badFile.xml");
		expectedEx.expectMessage(message);
		ap = xml.loadXML("badFile.xml");
	}

	@Test
	public void testLoadXMLGetsOtherDataTypes() {
		ap = XML.loadXML("testXml.xml");
		String[] inp = {"0.04", "23", "Jimmy John", "--payrate", "40", "-c", "true"};
		ap.parse(inp);
		assertEquals(0.04f, ap.getValue("accountBalance"));
		assertEquals(23, ap.getValue("myAge"));
		assertEquals("Jimmy John", ap.getValue("myName"));
		assertEquals(40.0f, ap.getValue("payrate"));
		assertEquals(true, ap.getValue("cool"));
	}

	@Test
	public void testSaveXMLHasFileErrorException() {
		expectedEx.expect(FileErrorException.class);
		String message = ("Error with file: ");
		expectedEx.expectMessage(message);
		xml.saveXML("", ap);
	}
}
