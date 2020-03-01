package models;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.Date;  
import java.util.HashMap;

import daos.ClickDao;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import entities.Click;
import entities.Impression;
import entities.Impression.Gender;
import entities.Impression.Age;
import entities.Impression.Context;
import entities.Impression.Income;
import entities.ServerEntry;


public class ReaderCSV {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	// TODO : see what is going to get passed from the controller and change file reading accordingly
	
	public static void readCSV( String filename, String type) {
		// type is either : click, impression, server

        String camp = "test"; // TODO : get current campaign
		
		Path pathToFile = Paths.get(filename);
		try (BufferedReader br = Files.newBufferedReader(pathToFile)) {
            String line = br.readLine();            
            line = br.readLine();


            // loop through all lines
            while (line != null) {

            	String[] contents = line.split(",");
            	
                switch (type){
                	case "click":                		
                		ClickDao clickDao = new ClickDao();

                        LocalDateTime clickDate = LocalDateTime.parse(contents[0], ReaderCSV.formatter);
                        Long clickId = Long.parseLong( contents[1] );
                        Double clickCost = Double.parseDouble(contents[2]);
                        
                        Click newClick = new Click(camp, clickId, clickDate, clickCost);
                        //System.out.println(newClick);
                        clickDao.save(newClick);
                        
                		break;
                	case "impression":
                		ImpressionDao impressionDao = new ImpressionDao();

                		LocalDateTime impressionDate = LocalDateTime.parse(contents[0], ReaderCSV.formatter);
                        Long impressionId = Long.parseLong( contents[1] );
                        
                		Gender impressionGender = returnGender( contents[2] );
                		Age impressionAge = returnAge( contents[3] );
                		Income impressionIncome = returnIncome( contents[4] );
                        Context impressionContext = returnContext( contents[5] );
                        Double impressionCost = Double.parseDouble( contents[6] );
                        
                        Impression newImpression = new Impression(camp, impressionDate, impressionId, impressionGender, impressionAge, impressionIncome, impressionContext, impressionCost);
                        impressionDao.save(newImpression);

                		break;
                	case "server":
                		
                		ServerEntryDao serverDao = new ServerEntryDao();
                		
                		LocalDateTime serverEntryDate = LocalDateTime.parse(contents[0], ReaderCSV.formatter);
                        Long serverId = Long.parseLong( contents[1] );
                        
                        LocalDateTime serverExitDate = null;
                        if( !contents[2].equals("n/a") ) {
                        	serverExitDate = LocalDateTime.parse(contents[2], ReaderCSV.formatter);
                        }
                        
                        int serverPageView = Integer.parseInt(contents[3]);
                		Boolean serverConversion = (contents[4] == "Yes" ? true : false);
                        
                		ServerEntry serverEntry = new ServerEntry(camp, serverEntryDate, serverId, serverExitDate, serverPageView, serverConversion);
                		serverDao.save(serverEntry);
                		
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
	
	public static Gender returnGender(String gender) {
		HashMap<String, Gender> genders = new HashMap<String, Gender>();
		genders.put("Male", Gender.MALE);
		genders.put("Female", Gender.FEMALE);
		
		return genders.get(gender);
	}
	
	private static Age returnAge(String age) {
		HashMap<String, Age> ages = new HashMap<String, Age>();
		ages.put("<25", Age.LESS25);
		ages.put("25-34", Age.FROM25TO34);
		ages.put("35-44", Age.FROM35TO44);
		ages.put("45-54", Age.FROM45TO54);
		ages.put(">54", Age.OVER54);
		
		return ages.get(age);
	}
	
	private static Income returnIncome(String income) {
		HashMap<String, Income> incomes = new HashMap<String, Income>();
		incomes.put("Low", Income.LOW);
		incomes.put("Medium", Income.MEDIUM);
		incomes.put("High", Income.HIGH);
		
		return incomes.get(income);
	}
	
	private static Context returnContext(String context) {
		HashMap<String, Context> contexts = new HashMap<String, Context>();
		contexts.put("Blog", Context.BLOG);
		contexts.put("News", Context.NEWS);
		contexts.put("Shopping", Context.SHOPPING);
		contexts.put("Social Media", Context.SOCIALMEDIA);
		
		return contexts.get(context);
	}

}
