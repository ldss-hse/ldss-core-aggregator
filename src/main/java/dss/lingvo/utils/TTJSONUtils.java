package dss.lingvo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import dss.lingvo.utils.models.input.multilevel.TTJSONMultiLevelInputModel;
import dss.lingvo.utils.models.input.singlelevel.TTJSONInputModel;
import dss.lingvo.utils.models.output.TTJSONOutputModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TTJSONUtils {
    private static TTJSONUtils ttjsonReader = new TTJSONUtils();

    public static TTJSONUtils getInstance() {
        return ttjsonReader;
    }

    public TTJSONInputModel readJSONDescription(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
//        //Get file from resources folder
//        ClassLoader classLoader = getClass().getClassLoader();
//        URL tmpRes = classLoader.getResource(fileName);
//        if (tmpRes == null) {
//            return null;
//        }
//        File file = new File(tmpRes.getFile());
//        System.out.println(file.getAbsolutePath());
        InputStream modelFileStream = getClass().getResourceAsStream('/'+fileName);
        return mapper.readValue(modelFileStream, TTJSONInputModel.class);
    }

    public TTJSONMultiLevelInputModel readJSONMultiLevelDescription(String fileName, boolean isResourceFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        File file = null;
        if (isResourceFile) {
            //Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();
            URL tmpRes = classLoader.getResource(fileName);
            if (tmpRes == null) {
                return null;
            }
            file = new File(tmpRes.getFile());
        } else {
            file = new File(fileName);
        }

        return mapper.readValue(file, TTJSONMultiLevelInputModel.class);
    }

    public void writeResultToJSON(String fileName, TTJSONOutputModel jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Convert object to JSON string and save into a file directly
        mapper.writeValue(new File(fileName), jsonString);
    }
}
