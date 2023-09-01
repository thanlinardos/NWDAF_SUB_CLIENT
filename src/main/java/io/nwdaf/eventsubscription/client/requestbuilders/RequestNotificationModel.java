package io.nwdaf.eventsubscription.client.requestbuilders;

import java.util.ArrayList;
import java.util.List;

public class RequestNotificationModel {
	private String subscriptionId;
	private String notifCorrId;
	private String oldSubscriptionId;
	private List<List<List<List<List<List<List<String>>>>>>> notifications = new ArrayList<List<List<List<List<List<List<String>>>>>>>();
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public String getNotifCorrId() {
		return notifCorrId;
	}
	public void setNotifCorrId(String notifCorrId) {
		this.notifCorrId = notifCorrId;
	}
	public String getOldSubscriptionId() {
		return oldSubscriptionId;
	}
	public void setOldSubscriptionId(String oldSubscriptionId) {
		this.oldSubscriptionId = oldSubscriptionId;
	}
	public List<List<List<List<List<List<List<String>>>>>>> getNotifications() {
		return notifications;
	}
	public void setNotifications(List<List<List<List<List<List<List<String>>>>>>> notifications) {
		this.notifications = notifications;
	}
}
