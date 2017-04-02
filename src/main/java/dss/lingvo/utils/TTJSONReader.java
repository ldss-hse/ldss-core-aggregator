package dss.lingvo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import dss.lingvo.utils.models.TTJSONModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TTJSONReader {
    private static TTJSONReader ttjsonReader = new TTJSONReader();

    public static TTJSONReader getInstance(){
        return ttjsonReader;
    }

    public TTJSONModel readJSONDescription(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        URL tmpRes = classLoader.getResource(fileName);
        if (tmpRes == null){
            return null;
        }
        File file = new File(tmpRes.getFile());
        return mapper.readValue(file, TTJSONModel.class);
    }
}
