
import edu.jsu.mcis.*;
import java.util.*;

	public class VolumeCalculator {

		
   		public static void main(String[] args){
   		
   			ArgumentParser Ap= new ArgumentParser();
   			Ap.addPositionalArgument("length",Argument.Type.INT);
			Ap.addPositionalArgument("width",Argument.Type.INT);
			Ap.addPositionalArgument("height",Argument.Type.INT);
			Ap.addNamedArgument("Type");
			Ap.addNamedArgument("Color");
		
			Ap.parse(args);
			int length= Ap.getValue("length");
			int width= Ap.getValue("width");
			int height= Ap.getValue("height");
			String type=Ap.getValue("Type");
			String color=Ap.getValue("Color");
			
			System.out.println("The length is : " + length);
			System.out.println("The width is : " + width);
			System.out.println("The height is : " + height);
			System.out.println("The Type Value equals : " + type);
			System.out.println("The Color Value equals : " + color);
			

   	
   	
   	
   	}









}