package com.tzj.green.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvUtils  {



    public static <T> List<List<T>> getCsvList(MultipartFile file,Class<T> t){
        List<List<T>> strs = null;
        if (!file.isEmpty()){
            InputStreamReader isr = null;
            BufferedReader br = null;
            try{
                isr = new InputStreamReader(file.getInputStream(),"GBK");
                br = new BufferedReader(isr);
                String line = null;
                strs = new ArrayList<>();
                while ((line = br.readLine()) != null){
                    List<T> strings = (List<T>) Arrays.asList(line.split(","));
                    strs.add(strings);
                }
            }catch (Exception e){
            }finally {
                try {
                    if (br != null){
                        br.close();
                    }
                    if (isr != null){
                        isr.close();
                    }
                } catch (IOException e) {
                }
            }
        }
        return strs;
    }
}
