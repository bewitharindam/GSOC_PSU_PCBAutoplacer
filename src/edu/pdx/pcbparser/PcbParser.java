package edu.pdx.pcbparser;

import java.io.*;
import java.util.*;

public class PcbParser {
	private String pcbInputFile, strLine="", layerName="", layerType="", netName="", moduleType="", moduleLayer="",moduleName="";
	private String startIndex="";
	private char endChar='a';
	private List<PcbLayers> layerList = new ArrayList<PcbLayers>();
	private List<PcbNets> netList = new ArrayList<PcbNets>();
	private List<PcbModules> moduleList = new ArrayList<PcbModules>();
	private int layerId=0, numberOfNets=0, netId=0, moduleId=0, angleZ=0, numIndex=0;
	private float positionX=0.00f, positionY=0.00f, componentWidth=0.00f,componentHeight=0.00f;
	private float heightMin=999.99f, heightMax=0.00f, widthMin=999.99f, widthMax=0.00f;
	private boolean isLayer = true, isNets = true;
	BufferedReader br;
	FileInputStream fstream;
	
	public PcbParser(String pcbInputFile) {
      this.pcbInputFile =  pcbInputFile;     	
	}
	
	public void pcbParse(){
      try{
        fstream = new FileInputStream(pcbInputFile);
        br = new BufferedReader(new InputStreamReader(fstream));  
        while ((strLine = br.readLine()) != null) {
          if (strLine.contains("(nets ")){
            startIndex = "(nets ";
            endChar = ')';
            numIndex = 6;
            numberOfNets = Integer.parseInt(getSubstring(startIndex,endChar,numIndex));
          }				         
          if (strLine.contains("(layers") && isLayer){
            getLayers();         
          }
          if (strLine.contains("(net 0") && isNets){
            getNets();         
          }
          if (strLine.contains("(module ")){
            getModules();         
          }
        }
        br.close();
        //testing
        for(int j=0;j<netList.size();j++)
            System.out.println(netList.get(j).netId+" "+netList.get(j).netName+" "+"\n");
        //System.out.println("numberOfNets  ::"+numberOfNets);
      }
      catch (Exception e){
    	  System.err.println("Error: " + e.getMessage());
      }
	}
	
	public void getLayers(){
      isLayer = false;
      for(int i=0; i<15; i++){
        try {
          strLine = br.readLine();
          StringTokenizer stringTokenizer = new StringTokenizer(strLine, "() ");
          while(stringTokenizer.hasMoreTokens()){
            layerId = Integer.parseInt(stringTokenizer.nextElement().toString());
            layerName = stringTokenizer.nextElement().toString();
            layerType = stringTokenizer.nextElement().toString();
            layerList.add(new PcbLayers(layerId, layerName, layerType));
          }
        }
		catch (Exception e) {
			e.printStackTrace();
		}
	   }//for loop
	}
	public void getNets(){
      isNets = false;
      for(int i=0; i<numberOfNets; i++){
        try {
          StringTokenizer stringTokenizer = new StringTokenizer(strLine, "() ");
          while(stringTokenizer.hasMoreTokens()){
        	String net = stringTokenizer.nextElement().toString();
            netId = Integer.parseInt(stringTokenizer.nextElement().toString());
            netName = stringTokenizer.nextElement().toString();
            netList.add(new PcbNets(netId, netName));
          }
          strLine = br.readLine();
        }
    	catch (Exception e) {
          e.printStackTrace();
        }
      }
	}
	public void getModules(){
      try {
        PcbModules modules = new PcbModules();
        List<Float> widths = new ArrayList<Float>();
        List<Float> heights = new ArrayList<Float>();
        modules.moduleId = moduleId;
        moduleId++;
        modules.moduleType = getSubstring("(module ",' ',8);
        modules.moduleLayer = getSubstring("(layer ",')',7); 
		strLine = br.readLine();
		String[] coords = getSubstring("(at ", ')', 4).split(" ");
		modules.positionX = Float.parseFloat(coords[0]);
		modules.positionY = Float.parseFloat(coords[1]);
		if(coords.length==3)
          modules.angleZ = Integer.parseInt(coords[2]);
		while(true){
		  String[] position;
          strLine = br.readLine();
          if(strLine.contains("fp_text reference")){
            modules.moduleName = getSubstring("reference ",' ', 10);
          }
          /*if(strLine.contains("fp_text value")){
            modules.moduleNameValue = getSubstring("value ",' ', 6);
          }*/ //future use
          if(strLine.contains("fp_line")){
            position = getSubstring("(start ",')', 7).split(" ");
            widths.add(Float.parseFloat(position[0]));
            heights.add(Float.parseFloat(position[1]));
            position = getSubstring("(end ",')', 5).split(" ");
            widths.add(Float.parseFloat(position[0]));
            heights.add(Float.parseFloat(position[1]));
            /*if(Float.parseFloat(position[0])<widthMin){
              widthMin = Float.parseFloat(position[0]); 
            }
            if(Float.parseFloat(position[0])>widthMax){
              widthMax = Float.parseFloat(position[0]); 
            }
            if(Float.parseFloat(position[1])<heightMin){
              heightMin = Float.parseFloat(position[1]); 
            }
            if(Float.parseFloat(position[1])>heightMax){
              heightMax = Float.parseFloat(position[1]); 
            }
            position = getSubstring("(end ",')', 5).split(" ");
            if(Float.parseFloat(position[0])<widthMin){
              widthMin = Float.parseFloat(position[0]); 
            }
            if(Float.parseFloat(position[0])>widthMax){
              widthMax = Float.parseFloat(position[0]); 
            }
            if(Float.parseFloat(position[1])<heightMin){
              heightMin = Float.parseFloat(position[1]); 
            }
            if(Float.parseFloat(position[1])>heightMax){
              heightMax = Float.parseFloat(position[1]); 
            }*/
          }
          if(strLine.contains("fp_circle")){
            position = getSubstring("(end ",')', 5).split(" ");
            widths.add(Float.parseFloat(position[0]));
            heights.add(Float.parseFloat(position[1]));
          }
          if(strLine.contains("pad")){
            break;
          }
		}//end while(true)
	  } catch (Exception e) {
		e.printStackTrace();
	  }
    }
	public String getSubstring(String startIndex, char endChar, int numIndex){
		int start = strLine.indexOf(startIndex) + numIndex;
        int end = start;
        for (final char c : strLine.substring(start).toCharArray()) {
          if (c == endChar) {
            break;
          }
          ++end;
        }				
      return (strLine.substring(start, end));
	}
}