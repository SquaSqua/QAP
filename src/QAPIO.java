import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class QAPIO
{
    int instanceID;

    public void readDefinition(String instanceFile) throws FileNotFoundException
    {

            BufferedReader in = new BufferedReader(new FileReader(instanceFile));
        try {
            instanceID = Integer.parseInt(in.readLine());
            in.readLine();

            String nextLine = null;
            while((nextLine = in.readLine()) != null)
            {

                while
            }

        } catch (IOException e) {
            System.out.println("Nie znaloeziono pliku!");
        }



    }

}
