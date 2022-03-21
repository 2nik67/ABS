package load;

import client.Client;
import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;
import load.jaxb.schema.generated.AbsCategories;
import load.jaxb.schema.generated.AbsLoan;
import load.jaxb.schema.generated.AbsLoans;
import load.jaxb.schema.generated.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.LinkPermission;

public abstract class LoadFile {

    public static void main(String[] args) {
        readFile();
    }

    public static void readFile(){

        try {
            File file=new File("engine/src/resources/ex1-small.xml");


            JAXBContext jaxbContext=JAXBContext.newInstance(AbsLoans.class);
            Unmarshaller unmarshaller= jaxbContext.createUnmarshaller();
            AbsLoans categories=(AbsLoans) unmarshaller.unmarshal(file);


        }catch (JAXBException e){
            e.printStackTrace();
        }
    }

}
