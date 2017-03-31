package dss.lingvo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import dss.lingvo.utils.models.TTJSONModel;

import java.io.File;
import java.io.IOException;

public class TTJSONReader {
    private static TTJSONReader ttjsonReader = new TTJSONReader();

    public static TTJSONReader getInstance(){
        return ttjsonReader;
    }

    public TTJSONModel readJSONDescription(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        TTJSONModel ttjsonModel = mapper.readValue(file, TTJSONModel.class);
        System.out.println(ttjsonModel);
        return ttjsonModel;
    }
}
