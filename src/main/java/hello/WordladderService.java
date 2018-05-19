package hello;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class WordladderService {
    public ArrayList<String> wordLadder(String start, String end){
        ArrayList<String> result = new ArrayList<>();
        Map<String, Integer> word_sheet = new HashMap<String, Integer>();
        //read_dict
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
            /* 读入TXT文件 */
            String pathname = "./dictionary.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
            File filename = new File(pathname); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言

            String line = br.readLine();

            while (line != null) {
                word_sheet.put(line,-1);
                line = br.readLine(); // 一次读入一行数据
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (start.equals("")) {
            result.add("Empty start word");
            return result;
        }
        if (word_sheet.get(start) == null) {
            result.add("Wrong start word");
            return result;
        }
        if (end.equals("")) {
            result.add("Empty end word");
            return result;
        }
        if (word_sheet.get(end) == null || start.length() != end.length() || start.equals(end)) {
            result.add("Wrong end word");
            return result;
        }
        return Word_ladder(word_sheet, start, end);
    }

    private ArrayList<String> Word_ladder(Map<String, Integer> word_sheet, String first,String last){
        ArrayList<String> result = new ArrayList<>();
        Vector<String> v = new Vector<String>(0);
        v.addElement(first);
        int len = first.length();
        int i = 0;
        while( i < v.size() && !v.get(i).equals(last)){
            for(int j = 0; j < len; j++){
                for(int k = 0; k < 26; k++){
                    char x = "abcdefghijklmnopqrstuvwxyz".charAt(k);
                    String s1 = v.get(i).substring(0,j);
                    String s2 = v.get(i).substring(j+1);
                    String s = s1 + String.valueOf(x) + s2;
                    if(word_sheet.get(s)!=null){
                        if(word_sheet.get(s).equals(-1)){
                            word_sheet.put(s, i);
                            v.addElement(s);
                        }
                    }
                }
            }
            i++;
        }
        if(i >= v.size()){
            System.out.println("failure");
            result.add("failure");
            return result;
        }
        if(v.get(i).equals(last)){
            Vector<String> rlist = new Vector<String>(0);
            rlist.addElement(v.get(i));
            int temp = word_sheet.get(v.get(i));
            while(temp != 0){
                rlist.addElement(v.get(temp));
                temp = word_sheet.get(v.get(temp));
            }
            rlist.addElement(first);
            Enumeration vEnum = rlist.elements();
            while(vEnum.hasMoreElements())
                result.add((String)vEnum.nextElement());
        }
        return result;
    }
}
