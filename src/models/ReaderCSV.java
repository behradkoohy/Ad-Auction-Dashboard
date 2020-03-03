package models;


import daos.ClickDao;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import entities.Click;
import entities.Impression;
import entities.Impression.Age;
import entities.Impression.Context;
import entities.Impression.Gender;
import entities.Impression.Income;
import entities.ServerEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class ReaderCSV {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	static String CLICK_LOG_HEADER= "Date,ID,Click Cost";
	static String SERVER_LOG_HEADER= "Entry Date,ID,Exit Date,Pages Viewed,Conversion";
	static String IMPRESSION_LOG_HEADER= "Date,ID,Gender,Age,Income,Context,Impression Cost";
	// TODO : see what is going to get passed from the controller and change file reading accordingly
	
	public static void readCSV(String filename, String campaignName) {
		// type is either : click, impression, server
		String type = "";
        String camp = "test"; // TODO : get current campaign
		
		Path pathToFile = Paths.get(filename);
		try (BufferedReader br = Files.newBufferedReader(pathToFile)) {
			String line = br.readLine();
			if (line.equals(CLICK_LOG_HEADER)){
				type = "click";
			} else if (line.equals(SERVER_LOG_HEADER)){
				type = "server";
			} else if (line.equals(IMPRESSION_LOG_HEADER)){
				type = "impression";
			} else {
				// throws some error message
			}
			line = br.readLine();
//			System.out.println(line);
			LinkedList<String> brin = new LinkedList<>();
            // loop through all lines
			while (line != null){
				brin.add(line);
				line = br.readLine();

			}

			ClickDao clickDao = new ClickDao();
			ImpressionDao impressionDao = new ImpressionDao();
			ServerEntryDao serverEntryDao = new ServerEntryDao();

			List<Click> clicksToAdd = new ArrayList<>();
			List<Impression> impressionsToAdd = new ArrayList<>();
			List<ServerEntry> serverEntriesToAdd = new ArrayList<>();

			int clickIdentifer = clickDao.getMaxIdentifier();
			int impressionIdentifier = impressionDao.getMaxIdentifier();
			int serverEntryIdentifier = serverEntryDao.getMaxIdentifier();

            for (Iterator i = brin.iterator(); i.hasNext();) {
            	String[] contents = ((String) i.next()).split(",");
                switch (type){
                	case "click":
                        LocalDateTime clickDate = LocalDateTime.parse(contents[0], ReaderCSV.formatter);
                        Long clickId = Long.parseLong( contents[1] );
                        Double clickCost = Double.parseDouble(contents[2]);
                        clickIdentifer++;
                        clicksToAdd.add(new Click(clickIdentifer, campaignName, clickId, clickDate, clickCost));
                		break;
                	case "impression":
                		LocalDateTime impressionDate = LocalDateTime.parse(contents[0], ReaderCSV.formatter);
                        Long impressionId = Long.parseLong( contents[1] );
                		Gender impressionGender = returnGender( contents[2] );
                		Age impressionAge = returnAge( contents[3] );
                		Income impressionIncome = returnIncome( contents[4] );
                        Context impressionContext = returnContext( contents[5] );
                        Double impressionCost = Double.parseDouble( contents[6] );
                        impressionIdentifier++;
                        impressionsToAdd.add(new Impression(impressionIdentifier, campaignName, impressionDate, impressionId,
								impressionGender, impressionAge, impressionIncome, impressionContext, impressionCost));
                		break;
                	case "server":

                		LocalDateTime serverEntryDate = LocalDateTime.parse(contents[0], ReaderCSV.formatter);
                        Long serverId = Long.parseLong( contents[1] );

                        LocalDateTime serverExitDate;
                        if( !contents[2].equals("n/a") ) {
                        	serverExitDate = LocalDateTime.parse(contents[2], ReaderCSV.formatter);
                        } else {
                        	serverExitDate = serverEntryDate;
						}

                        int serverPageView = Integer.parseInt(contents[3]);
                		Boolean serverConversion = (contents[4] == "Yes" ? true : false);

                		serverEntryIdentifier++;
                		serverEntriesToAdd.add(new ServerEntry(serverEntryIdentifier, campaignName, serverEntryDate, serverId,
								serverExitDate, serverPageView, serverConversion));

                		break;
                	default:
                		// implement some error message
                		break;
                }
                
                line = br.readLine();

            }
            if(clicksToAdd.size() > 0) {
				clickDao.save(clicksToAdd);
			}
			if(impressionsToAdd.size() > 0) {
				impressionDao.save(impressionsToAdd);
			}
			if(serverEntriesToAdd.size() > 0) {
				serverEntryDao.save(serverEntriesToAdd);
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
