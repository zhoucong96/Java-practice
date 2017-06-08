package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        //test 1
        URL path = Main.class.getResource("test.html");
        File f = new File(path.getFile());
        //test2
        URL path2 = Main.class.getResource("test2.html");
        File f2 = new File(path2.getFile());

        String output = highlight(f);
        String output2 = highlight(f2);
        System.out.println(output);
        System.out.println(output2);
    }

    public static String highlight(File f){
        String s = "";
        try{
            //read the html file and print the original html files out
            BufferedReader reader = new BufferedReader(new FileReader(f));
            //variable declaration
            String line = null;
            Stack<String> colorStack = new Stack<String>();
            ArrayList<String> colorTrack = new ArrayList<String>();

            while ((line = reader.readLine()) != null) {
                //line operations

                //loop through the line and read all tags in current line
                //a list to hold all tags
                ArrayList<String> tags = new ArrayList<String>();
                Pattern p = Pattern.compile("</*\\w*>");
                Matcher m = p.matcher(line);
                while(m.find()) {
                    String tag = m.group().toString();
                    tags.add(tag);
                }

                //<!DOCTYPE html>
                if(line.toString().equals("<!DOCTYPE html>")) {
                    tags.add(line);
                }

                //use string builder to for line operations
                StringBuilder sb = new StringBuilder(line);

                //if no tags, add color that is at the top of the stack
                if(tags.isEmpty()){
                    //if same color, then don't add it
                    if(colorTrack.get(colorTrack.size()-1) != colorStack.peek()) {
                        sb.insert(0, colorStack.peek());
                        colorTrack.add(colorStack.peek());
                    }
                }
                //if there are tags
                else{
                    //loop through each tags
                    for (String tag: tags) {
                        //if it is <br>, just add colors, no pushing or popping
                        if(tag.equals("<br>")) {
                            sb.insert(sb.indexOf(tag), getHtmlColor(tag));
                            colorTrack.add(getHtmlColor(tag));
                            continue;
                        }
                        //if it's a closing tag, add corresponding color, pop the color from stack
                        if (tag.startsWith("</")){
                            //if this line only has the closing tag, print the color
                            //excluding the attribute tags
                            if(!tag.toLowerCase().equals("</a>")) {
                                if (tags.size() == 1) {
                                    if (colorTrack.get(colorTrack.size() - 1) != getHtmlColor(tag)) {
                                        sb.insert(sb.indexOf(tag), getHtmlColor(tag));
                                        colorTrack.add(getHtmlColor(tag));
                                    }
                                }
                                if (colorTrack.get(colorTrack.size() - 1) != getHtmlColor(tag)) {
                                    sb.insert(sb.indexOf(tag), getHtmlColor(tag));
                                    colorTrack.add(getHtmlColor(tag));
                                }
                                colorStack.pop();
                            }

                        }
                        //if it is not a closing tag, add corresponding color, push the color onto stack
                        else{
                            if(getHtmlColor(tag) != null){
                                sb.insert(sb.indexOf(tag), getHtmlColor(tag));
                                colorTrack.add(getHtmlColor(tag));
                                colorStack.push(getHtmlColor(tag));
                            }
                        }
                    }
                }
                line = sb.toString();

                //record this line and advance
                s += line + "\n";
            }
            //System.out.println(s);
            return s;
        }
        catch(Exception e){
            System.out.println("Error!");
        }
        return s;
    }

    //method to get color
    public static String getHtmlColor(String tag){
        String color = null;
        switch (tag.toLowerCase()){
            case "<html>":
            case "</html>":
                color = "\\color[RED]";
                break;
            case "<head>":
            case "</head>":
                color = "\\color[YELLOW]";
                break;
            case "<title>":
            case "</title>":
                color = "\\color[GREEN]";
                break;
            case "<body>":
            case "</body>":
                color = "\\color[TURQUOISE]";
                break;
            case "<br>":
                color = "\\color[PINK]";
                break;
            case "<h1>":
            case "</h1>":
                color = "\\color[DARKGREEN]";
                break;
            case "<p>":
            case "</p>":
                color = "\\color[DARKGRAY]";
                break;
        }
        return color;
    }
}
