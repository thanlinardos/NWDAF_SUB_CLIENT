package io.nwdaf.eventsubscription.client.requestbuilders;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.nwdaf.eventsubscription.utilities.Constants;
import io.nwdaf.eventsubscription.utilities.ParserUtil;
import io.nwdaf.eventsubscription.utilities.Regex;
import io.nwdaf.eventsubscription.model.MatchingDirection.MatchingDirectionEnum;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RequestSubscriptionModel implements Serializable{
	@JsonProperty("id")
	@Id
	private Long id;
	@JsonProperty("optionals")
	private List<String> optionals = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null,null,null,null,null,null,null,null,null));
	@JsonProperty("partitionCriteria")
	private List<String> partitionCriteria = new ArrayList<String>();
	@JsonProperty("nfAnaEvents")
	private List<String> nfAnaEvents = new ArrayList<String>();
	@JsonProperty("ueAnaEvents")
	private List<List<List<String>>> ueAnaEvents = new ArrayList<List<List<String>>>();
	@JsonProperty("taiList")
	private List<List<String>> taiList = new ArrayList<List<String>>();
	
	private final List<String> taiAttributes = new ArrayList<String>(Arrays.asList("mcc","mnc","tac","nid"));
	private final List<String> taiPatterns = new ArrayList<String>(Arrays.asList(Regex.mcc,Regex.mnc,Regex.tac,Regex.nid));
	
	private Boolean showConsNfInfo = false,showPrevSub = false,showEvtReq = false,showFeatures=false,showNotificationMethod = false,showResult=true,showNotifBody=true;
	
	private Boolean immRep;
	private NotificationMethodEnum notificationMethod;
	private MatchingDirectionEnum matchingDir;
	private Integer maxReportNbr;
	private String monDur;
	private Integer repPeriod;
	private Integer sampRatio;
	private Integer grpRepTime;
	private String producerId;
	private String producerSetId;
	private String subscriptionId;
	private NotificationFlagEnum notifFlag;
	private String notifCorrId;
	private String nfId;
	private String nfSetId;
	private String notificationURI;
	//features
	private String supportedFeatures = Constants.supportedFeatures;
	private List<Integer> supportedFeaturesList = convertFeaturesToList(Constants.supportedFeatures);
	private List<Boolean> supportedFeaturesBooleans = convertListToBooleans(convertFeaturesToList(Constants.supportedFeatures));
	private List<RequestEventModel> eventList = new ArrayList<RequestEventModel>();
	private List<String> supportedFeaturesNames = Constants.supportedFeaturesNames;
	private List<String> supportedFeaturesDesc = Constants.supportedFeaturesDesc;
	//other responses
	private List<String> failEventReportsEvents = new ArrayList<>();
	private List<String> failEventReportsCodes = new ArrayList<>();
	public void setAllLists() {
		supportedFeaturesList = convertBooleansToList(supportedFeaturesBooleans);
		supportedFeatures = convertListToFeatures(supportedFeaturesList);
		if(immRep!=null) {
			this.optionals.set(0,Boolean.toString(immRep).toUpperCase());
		}
		if(notificationMethod!=null) {
			this.optionals.set(1, notificationMethod.toString());
		}
		if(maxReportNbr!=null) {
			this.optionals.set(2, String.valueOf(maxReportNbr));
		}
		if(monDur!=null && monDur!="") {
			monDur = monDur + ZonedDateTime.now().getOffset().getId();
			this.optionals.set(3,monDur);
		}
		if(repPeriod!=null) {
			this.optionals.set(4, String.valueOf(repPeriod));
		}
		if(sampRatio!=null) {
			this.optionals.set(5, String.valueOf(sampRatio));
		}
		if(grpRepTime!=null) {
			this.optionals.set(6, String.valueOf(grpRepTime));
		}
		if(notifFlag!=null) {
			this.optionals.set(7, notifFlag.toString());
		}
		if(producerId!=null) {
			if(Pattern.matches(Regex.uuid, producerId)) {
				this.optionals.set(8, producerId);
			}
		}
		if(producerSetId!=null) {
			if(Pattern.matches(Regex.nf_set_id_plmn, producerSetId) || Pattern.matches(Regex.nf_set_id_snpn, producerSetId)) {
				this.optionals.set(9, producerSetId);
			}
		}
		if(subscriptionId!=null) {
			this.optionals.set(10, subscriptionId);
		}
		if(notifCorrId!=null) {
			this.optionals.set(11, notifCorrId);
		}
		if(nfId!=null) {
			if(Pattern.matches(Regex.uuid,nfId)) {
				this.optionals.set(12, nfId);
			}
		}
		if(nfSetId!=null) {
			if(Pattern.matches(Regex.nf_set_id_plmn, nfSetId) || Pattern.matches(Regex.nf_set_id_snpn, nfSetId)) {
				this.optionals.set(13, nfSetId);
			}
		}
		for(int i=0;i<this.ueAnaEvents.size();i++) {
			if(this.ueAnaEvents.get(i).size()<2) {
				this.ueAnaEvents.get(i).add(new ArrayList<String>());
			}
		}
	}

	public void setOptionalsItem(String item,Integer index) {
		this.optionals.set(index, item);
	}
	
	public String getPartitionCriteria(Integer i) {
		return partitionCriteria.get(i);
	}
	public void addPartitionCriteria(String item) {
		this.partitionCriteria.add(item);
	}
	public void removePartitionCriteria(Integer i) {
		this.partitionCriteria.remove((int)i);
	}
	public void setPartitionCriteria(String item,Integer i) {
		this.partitionCriteria.set(i, item);
	}
	
	public void addNfAnaEvents(String item) {
		this.nfAnaEvents.add(item);
	}
	public void removeNfAnaEvents(Integer i) {
		this.nfAnaEvents.remove((int)i);
	}
	public void setNfAnaEvents(String item,Integer i) {
		this.nfAnaEvents.set(i, item);
	}
	
	public void addUeAnaEvents(List<List<String>> item) {
		if(item==null) {
			item = new ArrayList<List<String>>(Arrays.asList(new ArrayList<String>(),new ArrayList<String>()));
			if(item.get(0).size()==0) {
				item.get(0).add(null);
			}
			for(int i=0;i<this.ueAnaEvents.size();i++) {
				if(this.ueAnaEvents.get(i).get(0).get(0)=="") {
					this.ueAnaEvents.get(i).get(0).set(0,null);
				}
				if(this.ueAnaEvents.get(i).size()<2) {
					this.ueAnaEvents.get(i).add(new ArrayList<String>());
				}
				for(int j=0;j<this.ueAnaEvents.get(i).get(1).size();j++) {
					if(this.ueAnaEvents.get(i).get(1).get(j)=="") {
						this.ueAnaEvents.get(i).get(1).set(j,null);
					}
				}
				if(this.ueAnaEvents.get(i).get(1).size()==0) {
					this.ueAnaEvents.get(i).get(1).add(null);
				}
			}
		}
		this.ueAnaEvents.add(item);
	}
	public void removeUeAnaEvents(Integer i) {
		this.ueAnaEvents.remove((int)i);
	}
	public void setUeAnaEvents(List<List<String>> item,Integer i) {
		this.ueAnaEvents.set(i, item);
	}
	public List<String> getUeAnaEventsList(Integer i){
		return this.ueAnaEvents.get(i).get(1);
	}
	public String getUeAnaEvents(Integer i) {
		return this.ueAnaEvents.get(i).get(0).get(0);
	}
	public String getUeAnaEvents(Integer i,Integer j) {
		return this.ueAnaEvents.get(i).get(1).get(j);
	}
	public void setUeAnaEvents(String item,Integer i) {
		this.ueAnaEvents.get(i).get(0).set(0,item);
	}
	public void setUeAnaEvents(String item,Integer i,Integer j) {
		this.ueAnaEvents.get(i).get(1).set(j,item);
	}
	public void addUeAnaEventsItem(Integer rowId) {
		for(int i=0;i<this.ueAnaEvents.size();i++) {
			if(this.ueAnaEvents.get(i).size()<2) {
				this.ueAnaEvents.get(i).add(new ArrayList<String>());
			}
		}
		this.ueAnaEvents.get(rowId).get(1).add(null);
	}
	public void addUeAnaEventsItem(Integer rowId,String item) {
		for(int i=0;i<this.ueAnaEvents.size();i++) {
			if(this.ueAnaEvents.get(i).size()<2) {
				this.ueAnaEvents.get(i).add(new ArrayList<String>());
			}
		}
		this.ueAnaEvents.get(rowId).get(1).add(item);
	}
	public void removeUeAnaEvents(Integer i,Integer j) {
		this.ueAnaEvents.get(i).get(1).remove((int)j);
	}

	public void addTaiList(List<String> item) {
		if(item==null || item.size()==0) {
			item = new ArrayList<String>();
			item.add(null);
			item.add(null);
			item.add(null);
			item.add(null);
		}
		this.taiList.add(item);
	}
	public void removeTaiList(Integer i) {
		this.taiList.remove((int)i);
	}
	public void setTaiList(List<String> item,Integer i) {
		if(item!=null) {
			this.taiList.set(i, item);
		}
		
	}
	public List<String> getTaiList(Integer i) {
		return taiList.get(i);
	}
	public void setTaiList(String item,Integer i,Integer j) {
		if(this.taiList.get(i)!=null) {
			if(Pattern.matches(taiPatterns.get(j), item)) {
				this.taiList.get(i).set(j, item);
			}
		}
	}
	public String getTaiList(Integer i,Integer j) {
		return taiList.get(i).get(j);
	}
	
	
	public void addEventList(RequestEventModel event) {
		if(event==null) {
			event = new RequestEventModel();
		}
		this.eventList.add(event);
	}
	public void removeEventList(Integer rowId) {
		this.eventList.remove((int)rowId);
	}

	private List<Integer> convertFeaturesToList(String features){
		int in = Integer.parseInt(features, 16);
        List<Integer> res = new ArrayList<>();

        for (int i = 1; i <= 24; i++) {
            int featureBit = 1 << (i - 1);
            if ((in & featureBit) != 0) {
                res.add(i);
            }
        }

        return res;
	}

	private String convertListToFeatures(List<Integer> list){
		int res = 0;
        for (int i : list) {
            if (i < 1 || i > 24) {
                throw new IllegalArgumentException("Each integer in the list must be between 1 and 24 (inclusive).");
            }

            res |= 1 << (i - 1);
        }

        return String.format("%06x", res);
	}
	private List<Boolean> convertListToBooleans(List<Integer> list){
		List<Boolean> res = new ArrayList<>();
		for(int i=1;i<25;i++){
			if(list.contains(i)){
				res.add(true);
			}
			else{
				res.add(false);
			}
		}
		return res;
	}
	private List<Integer> convertBooleansToList(List<Boolean> b){
		List<Integer> res = new ArrayList<>();
		for(int i=0;i<24;i++){
			if(b.get(i)){
				res.add(i+1);
			}
		}
		return res;
	}

	public RequestSubscriptionModel fromSubObject(NnwdafEventsSubscription sub){
		if(sub!=null){
			if(sub.getEvtReq()!=null){
				this.optionals.set(0,ParserUtil.safeParseString(sub.getEvtReq().isImmRep()));
				if(sub.getEvtReq().getNotifMethod()!=null){
					this.optionals.set(1,ParserUtil.safeParseString(sub.getEvtReq().getNotifMethod().getNotifMethod()));
				}
				this.optionals.set(2,ParserUtil.safeParseString(sub.getEvtReq().getMaxReportNbr()));
				this.optionals.set(3,ParserUtil.safeParseString(sub.getEvtReq().getMonDur()));
				this.optionals.set(4,ParserUtil.safeParseString(sub.getEvtReq().getRepPeriod()));
				this.optionals.set(5,ParserUtil.safeParseString(sub.getEvtReq().getSampRatio()));
				this.optionals.set(6,ParserUtil.safeParseString(sub.getEvtReq().getGrpRepTime()));
				if(sub.getEvtReq().getNotifFlag()!=null){
					this.optionals.set(7,ParserUtil.safeParseString(sub.getEvtReq().getNotifFlag().getNotifFlag()));
				}
				if(sub.getEvtReq().getPartitionCriteria()!=null){
					for(int i=0;i<sub.getEvtReq().getPartitionCriteria().size();i++){
						if(sub.getEvtReq().getPartitionCriteria().get(i)!=null){
							this.partitionCriteria.add(ParserUtil.safeParseString(sub.getEvtReq().getPartitionCriteria().get(i).getPartitionCriteria()));
						}
						else{
							this.partitionCriteria.add(null);
						}
					}
				}
			}
			if(sub.getPrevSub()!=null){
				this.optionals.set(8,ParserUtil.safeParseString(sub.getPrevSub().getProducerId()));
				this.optionals.set(9,ParserUtil.safeParseString(sub.getPrevSub().getProducerSetId()));
				this.optionals.set(10,ParserUtil.safeParseString(sub.getPrevSub().getSubscriptionId()));
				if(sub.getPrevSub().getNfAnaEvents()!=null){
					for(int i=0;i<sub.getPrevSub().getNfAnaEvents().size();i++){
						if(sub.getPrevSub().getNfAnaEvents().get(i)!=null){
							this.nfAnaEvents.add(ParserUtil.safeParseString(sub.getPrevSub().getNfAnaEvents().get(i).getEvent()));
						}
					}
				}
				if(sub.getPrevSub().getUeAnaEvents()!=null){
					for(int i=0;i<sub.getPrevSub().getUeAnaEvents().size();i++){
						if(sub.getPrevSub().getUeAnaEvents().get(i)!=null){
							for(int j=0;j<sub.getPrevSub().getUeAnaEvents().get(i).getAnaTypes().size();j++){
								if(sub.getPrevSub().getUeAnaEvents().get(i).getAnaTypes().get(j)!=null){
								this.addUeAnaEventsItem(i,ParserUtil.safeParseString(sub.getPrevSub().getUeAnaEvents().get(i).getAnaTypes().get(j).getEvent()));
								}
							}
							this.setUeAnaEvents(ParserUtil.safeParseString(sub.getPrevSub().getUeAnaEvents().get(i).getSupi()),i);
						}
					}
				}
			}
			this.optionals.set(11,sub.getNotifCorrId());
			if(sub.getConsNfInfo()!=null){
				this.optionals.set(12,ParserUtil.safeParseString(sub.getConsNfInfo().getNfId()));
				this.optionals.set(13,sub.getConsNfInfo().getNfSetId());
				if(sub.getConsNfInfo().getTaiList()!=null){
					for(int i=0;i<sub.getConsNfInfo().getTaiList().size();i++){
						if(sub.getConsNfInfo().getTaiList().get(i)!=null){
							String mcc = null,mnc = null;
							if(sub.getConsNfInfo().getTaiList().get(i).getPlmnId()!=null){
								mcc = sub.getConsNfInfo().getTaiList().get(i).getPlmnId().getMcc();
								mnc = sub.getConsNfInfo().getTaiList().get(i).getPlmnId().getMnc();
							}
							this.addTaiList(new ArrayList<>(Arrays.asList(mcc,mnc,sub.getConsNfInfo().getTaiList().get(i).getTac(),sub.getConsNfInfo().getTaiList().get(i).getNid())));
						}
					}
				}
			}
			if(sub.getEventSubscriptions()!=null){
				for(int i=0;i<sub.getEventSubscriptions().size();i++){
					this.eventList.add(new RequestEventModel().fromEventObject(sub.getEventSubscriptions().get(i)));
				}
			}
			if(sub.getFailEventReports()!=null){
				for(int i=0;i<sub.getFailEventReports().size();i++){
					if(sub.getFailEventReports().get(i)!=null){
						if(sub.getFailEventReports().get(i).getEvent()!=null){
						failEventReportsEvents.add(ParserUtil.safeParseString(sub.getFailEventReports().get(i).getEvent().getEvent()));
						}
						else{
							failEventReportsEvents.add(null);
						}
						if(sub.getFailEventReports().get(i).getFailureCode()!=null){
						failEventReportsCodes.add(ParserUtil.safeParseString(sub.getFailEventReports().get(i).getFailureCode().getFailureCode()));
						}
						else{
							failEventReportsCodes.add(null);
						}
					}
				}
			}
		}

		return this;
	}
}
