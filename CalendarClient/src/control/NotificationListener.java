package control;

import java.io.IOException;

import javafx.application.Platform;



public class NotificationListener extends Thread {

	

	

	public void run() {
		while(true) {
			try {
				String str = Client.hasNotifications().trim();
				if(str.equals("true")) {
					System.out.println("Nye varlser!");	
					//MainApp.rootController.setNotificationBold();
					Platform.runLater(() -> MainApp.rootController.setNotificationBold());
				}
				else {
					//MainApp.rootController.removeNotificationBold();
					Platform.runLater(() -> MainApp.rootController.removeNotificationBold());
				}
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
}
