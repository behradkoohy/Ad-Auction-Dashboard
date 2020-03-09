package models;


import daos.ClickDao;
import daos.DaoInjector;
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

	private static String CLICK_LOG_HEADER= "Date,ID,Click Cost";
	private static String SERVER_LOG_HEADER= "Entry Date,ID,Exit Date,Pages Viewed,Conversion";
	private static String IMPRESSION_LOG_HEADER= "Date,ID,Gender,Age,Income,Context,Impression Cost";

	public static void readCSV(String filename, String campaignName) {
		Path pathToFile = Paths.get(filename);
		try (BufferedReader br = Files.newBufferedReader(pathToFile)) {
			String line = br.readLine();
			if (line.equals(CLICK_LOG_HEADER)){
				readClickCSV(br, campaignName, DaoInjector.newClickDao());
			} else if (line.equals(SERVER_LOG_HEADER)){
				readServerEntryCSV(br, campaignName, DaoInjector.newServerEntryDao());
			} else if (line.equals(IMPRESSION_LOG_HEADER)){
				readImpressionCSV(br, campaignName, DaoInjector.newImpressionDao());
			} else {
				// throws some error message
			}
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
	}

	private static void readClickCSV(BufferedReader br, String campaignName, ClickDao clickDao) {
		try {
			List<Click> clicksToAdd = new ArrayList<>();
			int clickIdentifer = clickDao.getMaxIdentifier();

			String line = br.readLine();
			while (line != null) {
				String[] contents = line.split(",");

				LocalDateTime clickDate = LocalDateTime.parse(contents[0], ReaderCSV.formatter);
				long clickId = Long.parseLong( contents[1] );
				double clickCost = Double.parseDouble(contents[2]);
				clickIdentifer++;
				clicksToAdd.add(new Click(clickIdentifer, campaignName, clickId, clickDate, clickCost));

				line = br.readLine();
			}

			if(clicksToAdd.size() > 0) {
				clickDao.save(clicksToAdd);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static void readImpressionCSV(BufferedReader br, String campaignName, ImpressionDao impressionDao) {
		try {
			List<Impression> impressionsToAdd = new ArrayList<>();
			int impressionIdentifier = impressionDao.getMaxIdentifier();

			String line = br.readLine();
			while (line != null) {
				String[] contents = line.split(",");

				LocalDateTime impressionDate = LocalDateTime.parse(contents[0], ReaderCSV.formatter);
				long impressionId = Long.parseLong( contents[1] );
				Gender impressionGender = returnGender( contents[2] );
				Age impressionAge = returnAge( contents[3] );
				Income impressionIncome = returnIncome( contents[4] );
				Context impressionContext = returnContext( contents[5] );
				double impressionCost = Double.parseDouble( contents[6] );
				impressionIdentifier++;
				impressionsToAdd.add(new Impression(impressionIdentifier, campaignName, impressionDate, impressionId,
						impressionGender, impressionAge, impressionIncome, impressionContext, impressionCost));

				line = br.readLine();
			}

			if(impressionsToAdd.size() > 0) {
				impressionDao.save(impressionsToAdd);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static void readServerEntryCSV(BufferedReader br, String campaignName, ServerEntryDao serverEntryDao) {
		try {
			List<ServerEntry> serverEntriesToAdd = new ArrayList<>();
			int serverEntryIdentifier = serverEntryDao.getMaxIdentifier();

			String line = br.readLine();
			while (line != null) {
				String[] contents = line.split(",");

				LocalDateTime serverEntryDate = LocalDateTime.parse(contents[0], ReaderCSV.formatter);
				long serverId = Long.parseLong( contents[1] );

				LocalDateTime serverExitDate;
				if( !contents[2].equals("n/a") ) {
					serverExitDate = LocalDateTime.parse(contents[2], ReaderCSV.formatter);
				} else {
					serverExitDate = serverEntryDate;
				}

				int serverPageView = Integer.parseInt(contents[3]);
				boolean serverConversion = (contents[4].equals("Yes"));

				serverEntryIdentifier++;
				serverEntriesToAdd.add(new ServerEntry(serverEntryIdentifier, campaignName, serverEntryDate, serverId,
						serverExitDate, serverPageView, serverConversion));

				line = br.readLine();
			}

			if(serverEntriesToAdd.size() > 0) {
				serverEntryDao.save(serverEntriesToAdd);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Gender returnGender(String gender) {
		HashMap<String, Gender> genders = new HashMap<>();
		genders.put("Male", Gender.MALE);
		genders.put("Female", Gender.FEMALE);
		
		return genders.get(gender);
	}
	
	private static Age returnAge(String age) {
		HashMap<String, Age> ages = new HashMap<>();
		ages.put("<25", Age.LESS25);
		ages.put("25-34", Age.FROM25TO34);
		ages.put("35-44", Age.FROM35TO44);
		ages.put("45-54", Age.FROM45TO54);
		ages.put(">54", Age.OVER54);
		
		return ages.get(age);
	}
	
	private static Income returnIncome(String income) {
		HashMap<String, Income> incomes = new HashMap<>();
		incomes.put("Low", Income.LOW);
		incomes.put("Medium", Income.MEDIUM);
		incomes.put("High", Income.HIGH);
		
		return incomes.get(income);
	}
	
	private static Context returnContext(String context) {
		HashMap<String, Context> contexts = new HashMap<>();
		contexts.put("Blog", Context.BLOG);
		contexts.put("News", Context.NEWS);
		contexts.put("Shopping", Context.SHOPPING);
		contexts.put("Social Media", Context.SOCIALMEDIA);
		
		return contexts.get(context);
	}



}
