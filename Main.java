package com.goodgame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static Map<String, Long> resultedArmy = new LinkedHashMap<>();

    public static Map<String, Long> maxSoldiersForTroop = new LinkedHashMap<>();

    public static Map<String, Long> maxSoldiersLeftToRecruit =
            new LinkedHashMap<>();

    public static long maxPossibleArmy = 0;

    public static void main( String[] args ) {
        try {

            initMaps();

            Scanner keyboard = new Scanner( System.in );
            System.out.println( "Enter a number:" );
            long input = keyboard.nextLong();

            if( input > maxPossibleArmy ) {
                System.out.println(
                        "There are not enough soldiers for an army of " + input
                                + " men." );
                return;
            }
            if( input < resultedArmy.size() ) {
                System.out.println(
                        "There has to be at least one man in each troop. Number of troops is : "
                                + resultedArmy.size() );
                return;
            }
            int i = 0;

            long soldiersLeftToRecruit = input;
            long recruitedSoldiers = 0;

            for( Map.Entry<String, Long> entry : maxSoldiersForTroop.entrySet() ) {

                i++;

                String troopType = entry.getKey();

                if( i < maxSoldiersForTroop.size() ) {

                    do {

                        //there needs to be at least 1 men in each troop
                        long troopsLeftToGenerate =
                                maxSoldiersForTroop.size() - i;

                        //there cannot be more recruited soldiers
                        //than existing men in the troop
                        //and than soldiers left to recruit

                        long maxValueForRecruitedSoldiers =
                                Math.min( entry.getValue(),
                                          soldiersLeftToRecruit
                                                  - troopsLeftToGenerate );

                        recruitedSoldiers = 1 + (long) ( Math.random()
                                * ( maxValueForRecruitedSoldiers ) );

                    } while( soldiersLeftToRecruit - recruitedSoldiers
                            > maxSoldiersLeftToRecruit.get( troopType ) );

                    soldiersLeftToRecruit =
                            soldiersLeftToRecruit - recruitedSoldiers;

                    resultedArmy.put( troopType, recruitedSoldiers );

                } else if( i == maxSoldiersForTroop.size() ) {

                    //last army to populate with the remaining soldiers needed
                    resultedArmy.put( troopType, soldiersLeftToRecruit );
                }

            }

            for( Map.Entry<String, Long> entry : resultedArmy.entrySet() ) {
                String key = entry.getKey();
                Object value = entry.getValue();
                System.out.println( value + " " + key );
            }

        } catch( Exception e ) {
            e.printStackTrace();
        }
    }

    private static void initMaps() {

        try {
            File myObj = new File( "./army" );
            Scanner myReader = new Scanner( myObj );
            myReader.useDelimiter( " " );

            while( myReader.hasNextLine() ) {

                String line = myReader.nextLine();
                String[] splitted = line.split( "\\s+" );

                String troopName = splitted[0];
                Long troopMaxSoldiers = Long.parseLong( splitted[1] );

                resultedArmy.put( troopName, Long.valueOf( 0 ) );
                maxSoldiersForTroop.put( troopName, troopMaxSoldiers );
                maxSoldiersLeftToRecruit.put( troopName, Long.valueOf( 0 ) );
            }
            myReader.close();
        } catch( FileNotFoundException e ) {
            System.out.println( "An error occurred." );
            e.printStackTrace();
        }

        long maxArmy = 0;
        for( Long maxTroop : maxSoldiersForTroop.values() ) {
            maxArmy += maxTroop;
        }

        maxPossibleArmy = maxArmy;

        for( Map.Entry<String, Long> maxLeftToRecruit : maxSoldiersLeftToRecruit
                .entrySet() ) {
            maxArmy = maxArmy
                    - maxSoldiersForTroop.get( maxLeftToRecruit.getKey() );
            maxLeftToRecruit.setValue( maxArmy );
        }

    }

}
