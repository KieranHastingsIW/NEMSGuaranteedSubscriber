package nz.govt.tewhatuora.Service;



import java.text.SimpleDateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solace.messaging.receiver.InboundMessage;

import nz.govt.tewhatuora.Entity.DeathNotice;

public class EventLoader {

    // Where users will implement their own code that handles what is done with the
    // Events.
    // below is an example of code that just prints the Event paylod to the comand
    // line
    public static void processEvent(InboundMessage message) {
        // System.out.printf("Raw Message \n%s \n", message);
        // System.out.printf("Message dump \n%s \n", message.dump());

        // String nhi = message.getPayloadAsString().split("\",\"")[0].replaceAll("{",
        // "");
        // String date = message.getPayloadAsString().split(",")[1].replaceAll("}", "");
        // System.out.printf("Payload as String \n%s \n", message.getPayloadAsString());
        String jsonString = message.getPayloadAsString();

        Gson gson = new GsonBuilder().create();
        DeathNotice deathNotice = gson.fromJson(jsonString, DeathNotice.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date=sdf.format(deathNotice.getDeathDate());  

        String content = "|| NHI: " + deathNotice.getNhi() + " || Date: " + date + " || Topic: " + message.getDestinationName() + " ||";
        String border = "=".repeat(content.length());

        System.out.printf("%s\n%s\n%s\n", border, content, border );
     
                

    }

}
