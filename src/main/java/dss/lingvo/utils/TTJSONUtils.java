package dss.lingvo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import dss.lingvo.utils.models.input.multilevel.TTJSONMultiLevelInputModel;
import dss.lingvo.utils.models.input.singlelevel.TTJSONInputModel;
import dss.lingvo.utils.models.output.TTJSONOutputModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TTJSONUtils {
    private static TTJSONUtils ttjsonReader = new TTJSONUtils();

    public static TTJSONUtils getInstance() {
        return ttjsonReader;
    }

    public TTJSONInputModel readJSONDescription(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        URL tmpRes = classLoader.getResource(fileName);
        if (tmpRes == null) {
            return null;
        }
        File file = new File(tmpRes.getFile());
        return mapper.readValue(file, TTJSONInputModel.class);
    }

    public TTJSONMultiLevelInputModel readJSONMultiLevelDescription(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        File file = new File(fileName);
        return mapper.readValue(file, TTJSONMultiLevelInputModel.class);
    }

    public void writeResultToJSON(String fileName, TTJSONOutputModel jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Convert object to JSON string and save into a file directly
        mapper.writeValue(new File(fileName), jsonString);
    }
}
