package com.kieran;

import java.io.IOException;
import java.util.Properties;

import com.kieran.Connection.PropertiesLoader;
import com.kieran.Connection.SolaceConnector;
import com.solace.messaging.MessagingService;
import com.solace.messaging.config.profile.ConfigurationProfile;
import com.solace.messaging.receiver.PersistentMessageReceiver;
import com.solace.messaging.resources.Queue;

public class Main {

    private static final String API = "Java";

    private static volatile int msgRecvCounter = 0;                 // num messages received
    private static volatile boolean hasDetectedRedelivery = false;  // detected any messages being redelivered?
    private static volatile boolean isShutdown = false;             // are we done?

    /** This is the main app.  Use this type of app for receiving Guaranteed messages (e.g. via a queue endpoint). */
    public static void main(String[] args) throws InterruptedException, IOException {


        // if (args.length < 3) {  // Check command line arguments
        //     System.out.printf("Usage: <host:port> <message-vpn> <client-username> [password]%n%n");
        //     System.exit(-1);
        // }
        Properties appProperties = PropertiesLoader.loadProperties();
        final Properties properties =  SolaceConnector.setProperties(appProperties);
        final String QUEUE_NAME = appProperties.getProperty("solace.broker.queue");
        final MessagingService messagingService = MessagingService.builder(ConfigurationProfile.V1)
                .fromProperties(properties)
                .build();
        messagingService.connect();  // blocking connect
        final PersistentMessageReceiver receiver = messagingService
                .createPersistentMessageReceiverBuilder()
                .build(Queue.durableExclusiveQueue(QUEUE_NAME));
        try {
            receiver.start();
        } catch (RuntimeException e) {
            System.err.printf("%n*** Could not establish a connection to queue '%s': %s%n", QUEUE_NAME, e.getMessage());
            System.err.println("  or see the SEMP CURL scripts inside the 'semp-rest-api' directory.");
            System.err.println("NOTE: see HowToEnableAutoCreationOfMissingResourcesOnBroker.java sample for how to construct queue with consumer app.");
            System.err.println("Exiting.");
            return;

        }
        // asynchronous anonymous receiver message callback
        receiver.receiveAsync(message -> {
            msgRecvCounter++;
            if (message.isRedelivered()) {  // useful check
                // this is the broker telling the consumer that this message has been sent and not ACKed before.
                // this can happen if an exception is thrown, or the broker restarts, or the network disconnects
                // perhaps an error in processing? Should do extra checks to avoid duplicate processing
                hasDetectedRedelivery = true;
            }
            // Messages are removed from the broker queue when the ACK is received.
            // Therefore, DO NOT ACK until all processing/storing of this message is complete.
            // NOTE that messages can be acknowledged from any thread.

            receiver.ack(message);  // ACKs are asynchronous
        });

        // async queue receive working now, so time to wait until done...
        System.out.println("connected, and running. Press [ENTER] to quit.");
        while (System.in.available() == 0 && !isShutdown) {
            Thread.sleep(4000);  // wait 4 second
            System.out.printf("%s Received msgs/s: %,d%n",API,msgRecvCounter);  // simple way of calculating message rates
            msgRecvCounter = 0;
            if (hasDetectedRedelivery) {
                System.out.println("*** Redelivery detected ***");
                hasDetectedRedelivery = false;  // only show the error once per second
            }
        }
        isShutdown = true;
        receiver.terminate(1500L);
        Thread.sleep(1000);
        messagingService.disconnect();
        System.out.println("Main thread quitting.");
    }


}
