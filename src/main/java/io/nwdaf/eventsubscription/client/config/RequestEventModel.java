package io.nwdaf.eventsubscription.client.config;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import io.nwdaf.eventsubscription.client.model.Accuracy.AccuracyEnum;
import io.nwdaf.eventsubscription.client.model.ExpectedAnalyticsType.ExpectedAnalyticsTypeEnum;
import io.nwdaf.eventsubscription.client.model.MatchingDirection.MatchingDirectionEnum;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RequestEventModel {
	private String event;
	private String notificationMethod;
	private List<String>optionals = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null,null));
	private List<String> anaMeta = new ArrayList<String>();
	private List<String> anaMetaInd = new ArrayList<String>(Arrays.asList(null,null,null));
	private List<String> aggrNwdafIds = new ArrayList<String>();
	private List<String> dataStatProps = new ArrayList<String>();
	private List<String> accPerSubset = new ArrayList<String>();
	private List<String>args = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null,null,null));
	private List<List<String>>nfLoadLvlThds = new ArrayList<List<String>>();
	private List<String> supis = new ArrayList<String>();
	private List<String> intGroupIds = new ArrayList<String>();
	private List<String> nfInstanceIds = new ArrayList<String>();
	private List<String> nfSetIds = new ArrayList<String>();
	private List<String> appIds = new ArrayList<String>();
	private List<String> dnns = new ArrayList<String>();
	private List<String> dnais = new ArrayList<String>();
	private List<String> ladnDnns = new ArrayList<String>();
	private List<String> nfTypes = new ArrayList<String>();
	private List<List<List<List<String>>>> visitedAreas = new ArrayList<List<List<List<String>>>>();
	private List<List<List<String>>> nsiIdInfos = new ArrayList<List<List<String>>>();
	private List<Integer> nsiLevelThrds = new ArrayList<Integer>();
	private List<List<String>> qosFlowRetThds = new ArrayList<List<String>>();
	private List<String> ranUeThrouThds = new ArrayList<String>();
	private List<List<String>> snssaia = new ArrayList<List<String>>();
	private List<List<String>> congThresholds = new ArrayList<List<String>>();
	private List<List<String>> nwPerfRequs = new ArrayList<List<String>>();
	private List<List<String>> bwRequs = new ArrayList<List<String>>();
	private List<List<String>> excepRequs = new ArrayList<List<String>>();
	private List<List<List<String>>> ratFreqs = new ArrayList<List<List<String>>>();
	private List<String> listOfAnaSubsets = new ArrayList<String>();
	private List<List<List<List<String>>>> disperReqs = new ArrayList<List<List<List<String>>>>();
	private List<List<String>> redTransReqs = new ArrayList<List<String>>();
	private List<List<List<String>>> wlanReqs = new ArrayList<List<List<String>>>();
	private List<List<String>> appServerAddrs = new ArrayList<List<String>>();
	private List<List<List<List<String>>>> dnPerfReqs = new ArrayList<List<List<List<String>>>>();
	private List<List<List<String>>> networkArea = new ArrayList<List<List<String>>>();
	private List<String> qosRequ = new ArrayList<String>();
	private List<List<List<List<List<List<String>>>>>> exptUeBehav = new ArrayList<List<List<List<List<List<String>>>>>>();
	private List<String> upfInfo = new ArrayList<String>();
	//show booleans
	private Boolean showExtraRepReq=false,showNfTypes=false,showTgtUe=false,showLoadLevelThreshold=false,showSnssais=false,showAnySlice=false,showNsiIdInfos=false,showNsiLevelThrds=false,showListOfAnaSubsets=false,showNetworkArea=false,showSupis=false,showNfLoadLvlThds=false,showNfInstanceIds=false,showNfSetIds=false,showMatchingDir=false,showIntGroupIds=false,showBwRequs=false,showRatFreqs=false,showUpfInfo=false,showAppServerAddrs=false,showDnns=false,showLadnDnns=false,showVisitedAreas=false,showQosFlowRetThds=false,showRanUeThrouThds=false,showExptAnaType=false,showExptUeBehav=false,showGpsis=false,showCongThresholds=false,showMaxTopAppUlNbr=false,showMaxTopAppDlNbr=false,showDisperReqs=false,showRedTransReqs=false,showWlanReqs=false,showDnais=false,showDnPerfReqs=false,showAppIds=false;
	//optionals
	private Integer maxObjectNbr;
	private Integer maxSupiNbr;
	private String startTs;
	private String endTs;
	private AccuracyEnum accuracy;
	private String timeAnaNeeded;
	private Integer offsetPeriod;

	//args
	private Boolean anyUE;
	private Boolean anySlice;
	private Integer loadLevelThreshold;
	private MatchingDirectionEnum matchingDir;
	private Integer maxTopAppUlNbr;
	private Integer maxTopAppDlNbr;
	private Integer repetitionPeriod;
	private ExpectedAnalyticsTypeEnum exptAnaType;
	
	public void setAllLists() {
		if(maxObjectNbr!=null) {
			optionals.set(0, String.valueOf(maxObjectNbr));
		}
		if(maxSupiNbr!=null) {
			optionals.set(1, String.valueOf(maxSupiNbr));
		}
		if(startTs!=null) {
			optionals.set(2, startTs + ZonedDateTime.now().getOffset().getId());
		}
		if(endTs!=null) {
			optionals.set(3, endTs + ZonedDateTime.now().getOffset().getId());
		}
		if(accuracy!=null) {
			optionals.set(4, accuracy.toString());
		}
		if(timeAnaNeeded!=null) {
			optionals.set(5, timeAnaNeeded + ZonedDateTime.now().getOffset().getId());
		}
		if(offsetPeriod!=null) {
			optionals.set(6, String.valueOf(offsetPeriod));
		}

		if(anyUE!=null) {
			args.set(0,Boolean.toString(anyUE).toUpperCase());
		}
		if(anySlice!=null) {
			args.set(1,Boolean.toString(anySlice).toUpperCase());
		}
		if(loadLevelThreshold!=null) {
			args.set(2, String.valueOf(loadLevelThreshold));
		}
		if(matchingDir!=null) {
			args.set(3, matchingDir.toString());
		}
		if(maxTopAppUlNbr!=null) {
			args.set(4, String.valueOf(maxTopAppUlNbr));
		}
		if(maxTopAppDlNbr!=null) {
			args.set(5, String.valueOf(maxTopAppDlNbr));
		}
		if(repetitionPeriod!=null) {
			args.set(6, String.valueOf(repetitionPeriod));
		}
		if(exptAnaType!=null) {
			args.set(7, exptAnaType.toString());
		}
		for(int i=0;i<aggrNwdafIds.size();i++){
			addAnaMetaInd(aggrNwdafIds.get(i));
		}
		for(int i=0;i<this.nsiIdInfos.size();i++) {
			if(this.nsiIdInfos.get(i).size()<2) {
				this.nsiIdInfos.get(i).add(new ArrayList<String>());
			}
		}
		while(this.networkArea.size()<4) {
				this.networkArea.add(new ArrayList<List<String>>());
		}

		for(int i=0;i<this.wlanReqs.size();i++) {
			if(this.wlanReqs.get(i).size()<3) {
				this.wlanReqs.get(i).add(new ArrayList<String>());
			}
		}
	}
	
	public void addAnaMeta(String item) {
		this.anaMeta.add(item);
	}
	public void removeAnaMeta(Integer i) {
		this.anaMeta.remove((int)i);
	}
	public String getAnaMeta(Integer i) {
		return this.anaMeta.get(i);
	}
	public void setAnaMeta(String item,Integer i) {
		this.anaMeta.set(i, item);
	}

	public void addAnaMetaInd(String item) {
		this.anaMetaInd.add(item);
	}
	public void removeAnaMetaInd(Integer i) {
		this.anaMetaInd.remove((int)i);
	}
	public String getAnaMetaInd(Integer i) {
		return this.anaMetaInd.get(i);
	}
	public void setAnaMetaInd(String item,Integer i) {
		this.anaMetaInd.set(i, item);
	}

	public void addAggrNwdafIds(String item) {
		this.aggrNwdafIds.add(item);
	}
	public void removeAggrNwdafIds(Integer i) {
		this.aggrNwdafIds.remove((int)i);
	}
	public String getAggrNwdafIds(Integer i) {
		return this.aggrNwdafIds.get(i);
	}
	public void setAggrNwdafIds(String item,Integer i) {
		this.aggrNwdafIds.set(i, item);
	}

	public void addDataStatProps(String item) {
		this.dataStatProps.add(item);
	}
	public void removeDataStatProps(Integer i) {
		this.dataStatProps.remove((int)i);
	}
	public String getDataStatProps(Integer i) {
		return this.dataStatProps.get(i);
	}
	public void setDataStatProps(String item,Integer i) {
		this.dataStatProps.set(i, item);
	}

	public void addAccPerSubset(String item) {
		this.accPerSubset.add(item);
	}
	public void removeAccPerSubset(Integer i) {
		this.accPerSubset.remove((int)i);
	}
	public String getAccPerSubset(Integer i) {
		return this.accPerSubset.get(i);
	}
	public void setAccPerSubset(String item,Integer i) {
		this.accPerSubset.set(i, item);
	}

	public void addNfLoadLvlThds(List<String> item) {
		if(item==null || item.size()==0) {
			item = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null,null,null,null,null,null));
		}
		this.nfLoadLvlThds.add(item);
	}
	public void removeNfLoadLvlThds(Integer i) {
		this.nfLoadLvlThds.remove((int)i);
	}
	public void setNfLoadLvlThds(List<String> item,Integer i) {
		if(item!=null) {
			this.nfLoadLvlThds.set(i, item);
		}
		
	}
	public List<String> getNfLoadLvlThds(Integer i) {
		return nfLoadLvlThds.get(i);
	}
	public void setNfLoadLvlThds(String item,Integer i,Integer j) {
		if(this.nfLoadLvlThds.get(i)!=null) {
			this.nfLoadLvlThds.get(i).set(j, item);
		}
	}
	public String getNfLoadLvlThds(Integer i,Integer j) {
		return nfLoadLvlThds.get(i).get(j);
	}

	public void addSupis(String item) {
		this.supis.add(item);
	}
	public void removeSupis(Integer i) {
		this.supis.remove((int)i);
	}
	public String getSupis(Integer i) {
		return this.supis.get(i);
	}
	public void setSupis(String item,Integer i) {
		this.supis.set(i, item);
	}

	public void addIntGroupIds(String item) {
		this.intGroupIds.add(item);
	}
	public void removeIntGroupIds(Integer i) {
		this.intGroupIds.remove((int)i);
	}
	public String getIntGroupIds(Integer i) {
		return this.intGroupIds.get(i);
	}
	public void setIntGroupIds(String item,Integer i) {
		this.intGroupIds.set(i, item);
	}

	public void addNfInstanceIds(String item) {
		this.nfInstanceIds.add(item);
	}
	public void removeNfInstanceIds(Integer i) {
		this.nfInstanceIds.remove((int)i);
	}
	public String getNfInstanceIds(Integer i) {
		return this.nfInstanceIds.get(i);
	}
	public void setNfInstanceIds(String item,Integer i) {
		this.nfInstanceIds.set(i, item);
	}

	public void addNfSetIds(String item) {
		this.nfSetIds.add(item);
	}
	public void removeNfSetIds(Integer i) {
		this.nfSetIds.remove((int)i);
	}
	public String getNfSetIds(Integer i) {
		return this.nfSetIds.get(i);
	}
	public void setNfSetIds(String item,Integer i) {
		this.nfSetIds.set(i, item);
	}

	public void addAppIds(String item) {
		this.appIds.add(item);
	}
	public void removeAppIds(Integer i) {
		this.appIds.remove((int)i);
	}
	public String getAppIds(Integer i) {
		return this.appIds.get(i);
	}
	public void setAppIds(String item,Integer i) {
		this.appIds.set(i, item);
	}

	public void addDnns(String item) {
		this.dnns.add(item);
	}
	public void removeDnns(Integer i) {
		this.dnns.remove((int)i);
	}
	public String getDnns(Integer i) {
		return this.dnns.get(i);
	}
	public void setDnns(String item,Integer i) {
		this.dnns.set(i, item);
	}

	public void addDnais(String item) {
		this.dnais.add(item);
	}
	public void removeDnais(Integer i) {
		this.dnais.remove((int)i);
	}
	public String getDnais(Integer i) {
		return this.dnais.get(i);
	}
	public void setDnais(String item,Integer i) {
		this.dnais.set(i, item);
	}

	public void addLadnDnns(String item) {
		this.ladnDnns.add(item);
	}
	public void removeLadnDnns(Integer i) {
		this.ladnDnns.remove((int)i);
	}
	public String getLadnDnns(Integer i) {
		return this.ladnDnns.get(i);
	}
	public void setLadnDnns(String item,Integer i) {
		this.ladnDnns.set(i, item);
	}

	public void addNfTypes(String item) {
		this.nfTypes.add(item);
	}
	public void removeNfTypes(Integer i) {
		this.nfTypes.remove((int)i);
	}
	public String getNfTypes(Integer i) {
		return this.nfTypes.get(i);
	}
	public void setNfTypes(String item,Integer i) {
		this.nfTypes.set(i, item);
	}

	public List<List<List<List<String>>>> getVisitedAreas() {
		return visitedAreas;
	}
	public void setVisitedAreas(List<List<List<List<String>>>> visitedAreas) {
		this.visitedAreas = visitedAreas;
	}

	public void addNsiIdInfos(List<List<String>> item) {
		if(item==null) {
			item = new ArrayList<List<String>>(Arrays.asList(new ArrayList<String>(Arrays.asList(null,null)),new ArrayList<String>()));
			for(int i=0;i<this.nsiIdInfos.size();i++) {
				for(int j=0;j<this.nsiIdInfos.get(i).size();j++){
					if(this.nsiIdInfos.get(i).get(0).get(j)!=null){
						if(this.nsiIdInfos.get(i).get(0).get(j)=="") {
							this.nsiIdInfos.get(i).get(0).set(j,null);
						}
					}
				}
				if(this.nsiIdInfos.get(i).size()<2) {
					this.nsiIdInfos.get(i).add(new ArrayList<String>());
				}
				for(int j=0;j<this.nsiIdInfos.get(i).get(1).size();j++) {
					if(this.nsiIdInfos.get(i).get(1).get(j)!=null){
						if(this.nsiIdInfos.get(i).get(1).get(j)=="") {
							this.nsiIdInfos.get(i).get(1).set(j,null);
						}
					}
				}
				if(this.nsiIdInfos.get(i).get(1).size()==0) {
					this.nsiIdInfos.get(i).get(1).add(null);
				}
			}
		}
		this.nsiIdInfos.add(item);
	}
	public void removeNsiIdInfos(Integer i) {
		this.nsiIdInfos.remove((int)i);
	}
	public void setNsiIdInfos(List<List<String>> item,Integer i) {
		this.nsiIdInfos.set(i, item);
	}
	public List<String> getNsiIdInfosList(Integer i){
		return this.nsiIdInfos.get(i).get(1);
	}
	public String getNsiIdInfos(Integer i,Integer j) {
		return this.nsiIdInfos.get(i).get(0).get(j);
	}
	public String getNsiIdInfosItem(Integer i,Integer j) {
		return this.nsiIdInfos.get(i).get(1).get(j);
	}
	public void setNsiIdInfos(String item,Integer i,Integer j) {
		this.nsiIdInfos.get(i).get(0).set(j,item);
	}
	public void setNsiIdInfosItem(String item,Integer i,Integer j) {
		this.nsiIdInfos.get(i).get(1).set(j,item);
	}
	public void addNsiIdInfosItem(Integer rowId) {
		for(int i=0;i<this.nsiIdInfos.size();i++) {
			if(this.nsiIdInfos.get(i).size()<2) {
				this.nsiIdInfos.get(i).add(new ArrayList<String>());
			}
		}
		this.nsiIdInfos.get(rowId).get(1).add(null);
	}
	public void removeNsiIdInfos(Integer i,Integer j) {
		this.nsiIdInfos.get(i).remove((int)j);
	}
	
	public void addNsiLevelThrds(Integer item) {
		this.nsiLevelThrds.add(item);
	}
	public void removeNsiLevelThrds(Integer i) {
		this.nsiLevelThrds.remove((int)i);
	}
	public Integer getNsiLevelThrds(Integer i) {
		return this.nsiLevelThrds.get(i);
	}
	public void setNsiLevelThrds(Integer item,Integer i) {
		this.nsiLevelThrds.set(i, item);
	}

	public void addQosFlowRetThds(List<String> item) {
		if(item==null || item.size()==0) {
			item = new ArrayList<String>(Arrays.asList(null,null,null));
		}
		this.qosFlowRetThds.add(item);
	}
	public void removeQosFlowRetThds(Integer i) {
		this.qosFlowRetThds.remove((int)i);
	}
	public void setQosFlowRetThds(List<String> item,Integer i) {
		if(item!=null) {
			this.qosFlowRetThds.set(i, item);
		}
		
	}
	public List<String> getQosFlowRetThds(Integer i) {
		return qosFlowRetThds.get(i);
	}
	public void setQosFlowRetThds(String item,Integer i,Integer j) {
		if(this.qosFlowRetThds.get(i)!=null) {
			this.qosFlowRetThds.get(i).set(j, item);
		}
	}
	public String getQosFlowRetThds(Integer i,Integer j) {
		return qosFlowRetThds.get(i).get(j);
	}

	public void addRanUeThrouThds(String item) {
		this.ranUeThrouThds.add(item);
	}
	public void removeRanUeThrouThds(Integer i) {
		this.ranUeThrouThds.remove((int)i);
	}
	public String getRanUeThrouThds(Integer i) {
		return this.ranUeThrouThds.get(i);
	}
	public void setRanUeThrouThds(String item,Integer i) {
		this.ranUeThrouThds.set(i, item);
	}

	public void addSnssaia(List<String> item) {
		if(item==null || item.size()==0) {
			item = new ArrayList<String>(Arrays.asList(null,null));
		}
		this.snssaia.add(item);
	}
	public void removeSnssaia(Integer i) {
		this.snssaia.remove((int)i);
	}
	public void setSnssaia(List<String> item,Integer i) {
		if(item!=null) {
			this.snssaia.set(i, item);
		}
	}
	public List<String> getSnssaia(Integer i) {
		return snssaia.get(i);
	}
	public void setSnssaia(String item,Integer i,Integer j) {
		if(this.snssaia.get(i)!=null) {
			this.snssaia.get(i).set(j, item);
		}
	}
	public String getSnssaia(Integer i,Integer j) {
		return snssaia.get(i).get(j);
	}

	public void addCongThresholds(List<String> item) {
		if(item==null || item.size()==0) {
			item = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null,null,null,null,null,null));
		}
		this.congThresholds.add(item);
	}
	public void removeCongThresholds(Integer i) {
		this.congThresholds.remove((int)i);
	}
	public void setCongThresholds(List<String> item,Integer i) {
		if(item!=null) {
			this.congThresholds.set(i, item);
		}
	}
	public List<String> getCongThresholds(Integer i) {
		return congThresholds.get(i);
	}
	public void setCongThresholds(String item,Integer i,Integer j) {
		if(this.congThresholds.get(i)!=null) {
			this.congThresholds.get(i).set(j, item);
		}
	}
	public String getCongThresholds(Integer i,Integer j) {
		return congThresholds.get(i).get(j);
	}

	public void addNwPerfRequs(List<String> item) {
		if(item==null || item.size()==0) {
			item = new ArrayList<String>(Arrays.asList(null,null,null));
		}
		this.nwPerfRequs.add(item);
	}
	public void removeNwPerfRequs(Integer i) {
		this.nwPerfRequs.remove((int)i);
	}
	public void setNwPerfRequs(List<String> item,Integer i) {
		if(item!=null) {
			this.nwPerfRequs.set(i, item);
		}
	}
	public List<String> getNwPerfRequs(Integer i) {
		return nwPerfRequs.get(i);
	}
	public void setNwPerfRequs(String item,Integer i,Integer j) {
		if(this.nwPerfRequs.get(i)!=null) {
			this.nwPerfRequs.get(i).set(j, item);
		}
	}
	public String getNwPerfRequs(Integer i,Integer j) {
		return nwPerfRequs.get(i).get(j);
	}

	public void addBwRequs(List<String> item) {
		if(item==null || item.size()==0) {
			item = new ArrayList<String>(Arrays.asList(null,null,null,null,null));
		}
		this.bwRequs.add(item);
	}
	public void removeBwRequs(Integer i) {
		this.bwRequs.remove((int)i);
	}
	public void setBwRequs(List<String> item,Integer i) {
		if(item!=null) {
			this.bwRequs.set(i, item);
		}
	}
	public List<String> getBwRequs(Integer i) {
		return bwRequs.get(i);
	}
	public void setBwRequs(String item,Integer i,Integer j) {
		if(this.bwRequs.get(i)!=null) {
			this.bwRequs.get(i).set(j, item);
		}
	}
	public String getBwRequs(Integer i,Integer j) {
		return bwRequs.get(i).get(j);
	}

	public void addExcepRequs(List<String> item) {
		if(item==null || item.size()==0) {
			item = new ArrayList<String>(Arrays.asList(null,null,null));
		}
		this.excepRequs.add(item);
	}
	public void removeExcepRequs(Integer i) {
		this.excepRequs.remove((int)i);
	}
	public void setExcepRequs(List<String> item,Integer i) {
		if(item!=null) {
			this.excepRequs.set(i, item);
		}
	}
	public List<String> getExcepRequs(Integer i) {
		return excepRequs.get(i);
	}
	public void setExcepRequs(String item,Integer i,Integer j) {
		if(this.excepRequs.get(i)!=null) {
			this.excepRequs.get(i).set(j, item);
		}
	}
	public String getExcepRequs(Integer i,Integer j) {
		return excepRequs.get(i).get(j);
	}

	public List<List<List<String>>> getRatFreqs() {
		return ratFreqs;
	}
	public void setRatFreqs(List<List<List<String>>> ratFreqs) {
		this.ratFreqs = ratFreqs;
	}

	public void addListOfAnaSubsets(String item) {
		this.listOfAnaSubsets.add(item);
	}
	public void removeListOfAnaSubsets(Integer i) {
		this.listOfAnaSubsets.remove((int)i);
	}
	public String getListOfAnaSubsets(Integer i) {
		return this.listOfAnaSubsets.get(i);
	}
	public void setListOfAnaSubsets(String item,Integer i) {
		this.listOfAnaSubsets.set(i, item);
	}

	public List<List<List<List<String>>>> getDisperReqs() {
		return disperReqs;
	}
	public void setDisperReqs(List<List<List<List<String>>>> disperReqs) {
		this.disperReqs = disperReqs;
	}

	public void addRedTransReqs(List<String> item) {
		if(item==null || item.size()==0) {
			item = new ArrayList<String>(Arrays.asList(null,null));
		}
		this.redTransReqs.add(item);
	}
	public void removeRedTransReqs(Integer i) {
		this.redTransReqs.remove((int)i);
	}
	public void setRedTransReqs(List<String> item,Integer i) {
		if(item!=null) {
			this.redTransReqs.set(i, item);
		}
	}
	public List<String> getRedTransReqs(Integer i) {
		return redTransReqs.get(i);
	}
	public void setRedTransReqs(String item,Integer i,Integer j) {
		if(this.redTransReqs.get(i)!=null) {
			this.redTransReqs.get(i).set(j, item);
		}
	}
	public String getRedTransReqs(Integer i,Integer j) {
		return redTransReqs.get(i).get(j);
	}

	public List<List<List<String>>> getWlanReqs() {
		return wlanReqs;
	}
	public void setWlanReqs(List<List<List<String>>> wlanReqs) {
		this.wlanReqs = wlanReqs;
	}
	public void addWlanReqs(List<List<String>> item) {
		if(item==null) {
			item = new ArrayList<List<String>>(Arrays.asList(new ArrayList<String>(Arrays.asList(null,null)),new ArrayList<String>(),new ArrayList<String>()));
			for(int i=0;i<this.wlanReqs.size();i++) {
				for(int j=0;j<this.wlanReqs.get(i).size();j++){
					if(this.wlanReqs.get(i).get(0).get(j)!=null){
						if(this.wlanReqs.get(i).get(0).get(j)=="") {
							this.wlanReqs.get(i).get(0).set(j,null);
						}
					}
				}
				while(this.wlanReqs.get(i).size()<3) {
					this.wlanReqs.get(i).add(new ArrayList<String>());
				}
				for(int j=0;j<this.wlanReqs.get(i).get(1).size();j++) {
					if(this.wlanReqs.get(i).get(1).get(j)!=null){
						if(this.wlanReqs.get(i).get(1).get(j)=="") {
							this.wlanReqs.get(i).get(1).set(j,null);
						}
					}
				}
				if(this.wlanReqs.get(i).get(1).size()==0) {
					this.wlanReqs.get(i).get(1).add(null);
				}
			}
		}
		this.wlanReqs.add(item);
	}
	public void removeWlanReqs(Integer i) {
		this.wlanReqs.remove((int)i);
	}
	public void setWlanReqs(List<List<String>> item,Integer i) {
		this.wlanReqs.set(i, item);
	}
	public List<String> getWlanReqs(Integer i){
		return this.wlanReqs.get(i).get(1);
	}
	public String getWlanReqs(Integer i,Integer j) {
		return this.wlanReqs.get(i).get(0).get(j);
	}
	public String getWlanReqsItem(Integer i,Integer j) {
		return this.wlanReqs.get(i).get(1).get(j);
	}
	public void setWlanReqs(String item,Integer i,Integer j) {
		this.wlanReqs.get(i).get(0).set(j,item);
	}
	public void setWlanReqsItem(String item,Integer i,Integer j) {
		this.wlanReqs.get(i).get(1).set(j,item);
	}
	public void addWlanReqsItem(Integer rowId) {
		for(int i=0;i<this.wlanReqs.size();i++) {
			if(this.wlanReqs.get(i).size()<3) {
				this.wlanReqs.get(i).add(new ArrayList<String>());
			}
		}
		this.wlanReqs.get(rowId).get(1).add(null);
	}
	public void removeWlanReqs(Integer i,Integer j) {
		this.wlanReqs.get(i).remove((int)j);
	}

	public void addAppServerAddrs(List<String> item) {
		if(item==null || item.size()==0) {
			item = new ArrayList<String>(Arrays.asList(null,null,null,null));
		}
		this.appServerAddrs.add(item);
	}
	public void removeAppServerAddrs(Integer i) {
		this.appServerAddrs.remove((int)i);
	}
	public void setAppServerAddrs(List<String> item,Integer i) {
		if(item!=null) {
			this.appServerAddrs.set(i, item);
		}
	}
	public List<String> getAppServerAddrs(Integer i) {
		return appServerAddrs.get(i);
	}
	public void setAppServerAddrs(String item,Integer i,Integer j) {
		if(this.appServerAddrs.get(i)!=null) {
			this.appServerAddrs.get(i).set(j, item);
		}
	}
	public String getAppServerAddrs(Integer i,Integer j) {
		return appServerAddrs.get(i).get(j);
	}

	public List<List<List<List<String>>>> getDnPerfReqs() {
		return dnPerfReqs;
	}
	public void setDnPerfReqs(List<List<List<List<String>>>> dnPerfReqs) {
		this.dnPerfReqs = dnPerfReqs;
	}

	public void addNetworkArea(List<List<String>> item) {
		if(item==null) {
			item = new ArrayList<List<String>>();
		}
		this.networkArea.add(item);
	}
	public void removeNetworkArea(Integer i) {
		this.networkArea.remove((int)i);
	}
	public void setNetworkArea(List<List<String>> item,Integer i) {
		this.networkArea.set(i, item);
	}
	public List<List<String>> getNetworkAreaList(Integer i){
		while(this.networkArea.size()<4){
			this.networkArea.add(new ArrayList<List<String>>());
		}
		return this.networkArea.get(i);
	}
	public List<String> getNetworkArea(Integer i,Integer j) {
		return this.networkArea.get(i).get(j);
	}
	public String getNetworkArea(Integer i,Integer j,Integer k) {
		return this.networkArea.get(i).get(j).get(k);
	}
	public void setNetworkArea(String item,Integer i,Integer j,Integer k) {
		this.networkArea.get(i).get(j).set(k,item);
	}
	public void addNetworkAreaItem(Integer i) {
		while(this.networkArea.size()<4){
			this.networkArea.add(new ArrayList<List<String>>());
		}
		if(i==0||i==1||i==3){
			this.networkArea.get(i).add(new ArrayList<>(Arrays.asList(null,null,null,null)));
		}
		if(i==2){
			this.networkArea.get(i).add(new ArrayList<>(Arrays.asList(null,null,null,null,null,null,null,null,null,null)));
		}
	}
	public void removeNetworkArea(Integer i,Integer j) {
		this.networkArea.get(i).remove((int)j);
	}

	public void addQosRequ(String item) {
		this.qosRequ.add(item);
	}
	public void removeQosRequ(Integer i) {
		this.qosRequ.remove((int)i);
	}
	public String getQosRequ(Integer i) {
		return this.qosRequ.get(i);
	}
	public void setQosRequ(String item,Integer i) {
		this.qosRequ.set(i, item);
	}

	public List<List<List<List<List<List<String>>>>>> getExptUeBehav() {
		return exptUeBehav;
	}
	public void setExptUeBehav(List<List<List<List<List<List<String>>>>>> exptUeBehav) {
		this.exptUeBehav = exptUeBehav;
	}

	public void addUpfInfo(String item) {
		this.upfInfo.add(item);
	}
	public void removeUpfInfo(Integer i) {
		this.upfInfo.remove((int)i);
	}
	public String getUpfInfo(Integer i) {
		return this.upfInfo.get(i);
	}
	public void setUpfInfo(String item,Integer i) {
		this.upfInfo.set(i, item);
	}
	
	
}
