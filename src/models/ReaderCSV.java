package models;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.Date;  

import daos.ClickDao;
import entities.Click;

public class ReaderCSV {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	// TODO : see what is going to get passed from the controller and change file reading accordingly
	public static void readCSV( String filename, String type) {
		// type is either : click, impression, server
		
		Path pathToFile = Paths.get(filename);
		try (BufferedReader br = Files.newBufferedReader(pathToFile)) {
            String line = br.readLine();            
            line = br.readLine();


            // loop through all lines
            while (line != null) {

            	String[] contents = line.split(",");
            	
                switch (type){
                	case "click":
                		// TODO : if a constructor with parameters or something is available, refactor to use that
                		// save new click
                		ClickDao clickDao = new ClickDao();
                        Click newClick = new Click();

                        newClick.setDate( LocalDateTime.parse(contents[0], ReaderCSV.formatter));
                        newClick.setId( Long.parseLong( contents[1] ));
                        newClick.setClickCost(Double.parseDouble(contents[2]));
                        
                        clickDao.save(newClick);
                        
                		break;
                	case "impression":
                		
                		break;
                	case "server":
                		
                		break;
                	default:
                		// implement some error message
                		break;
                }
                
                line = br.readLine();
                break;
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
	}
}
