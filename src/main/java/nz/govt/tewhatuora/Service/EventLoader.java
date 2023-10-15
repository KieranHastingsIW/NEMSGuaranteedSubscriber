package nz.govt.tewhatuora.Service;

import com.solace.messaging.receiver.InboundMessage;

public class EventLoader {


    // Where users will implement their own code that handles what is done with the Events.
    // below is an example of code that just prints the Event paylod to the comand line 
    public static void displayEvent(InboundMessage  message){
        System.out.printf("Raw Message \n%s \n", message);
        System.out.printf("Message dump \n%s \n", message.dump());
        System.out.printf("Payload as String \n%s \n", message.getPayloadAsString());
        
    }
    
}
