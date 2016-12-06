package com.bartovapps.flowerscatalog.parsers;

import com.bartovapps.flowerscatalog.data.Flower;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by BartovMoti on 11/22/14.
 */
public class FlowerXMLParser {

    public static ArrayList<Flower> parseFeed (String content){
        try{
            boolean inDataItemTag = false;
            String currentTagName = "";
            Flower flower = null;
            ArrayList<Flower> flowersList = new ArrayList<Flower>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT){

                switch (eventType){
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if(currentTagName.equals("product")){
                            inDataItemTag = true;
                            flower = new Flower();
                            flowersList.add(flower);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("product")){
                            inDataItemTag = false;
                        }
                        currentTagName = "";
                        break;
                    case XmlPullParser.TEXT:
                        if((inDataItemTag == true) && (flower != null)){
                            if (currentTagName.equals("productId")){
                                    flower.setProductId(Integer.parseInt(parser.getText()));
                            }if(currentTagName.equals("name")){
                                flower.setName(parser.getText());
                            }if(currentTagName.equals("instructions")){
                                flower.setInstructions(parser.getText());
                            }if(currentTagName.equals("category")){
                                flower.setCategory(parser.getText());
                            }if(currentTagName.equals("price")){
                                flower.setPrice(Double.parseDouble(parser.getText()));
                            }if(currentTagName.equals("photo")){
                                flower.setImageUrl(parser.getText());
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
            return flowersList;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }




}
