package io.nwdaf.eventsubscription.client.config;


import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.qos.logback.core.pattern.parser.Parser;
import io.nwdaf.eventsubscription.client.Constants;
import io.nwdaf.eventsubscription.client.model.EventSubscription;
import io.nwdaf.eventsubscription.client.model.GADShape;
import io.nwdaf.eventsubscription.client.model.GeographicArea;
import io.nwdaf.eventsubscription.client.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.client.model.Point;
import io.nwdaf.eventsubscription.client.model.Polygon;
import io.nwdaf.eventsubscription.client.model.PointAltitude;
import io.nwdaf.eventsubscription.client.model.PointAltitudeUncertainty;
import io.nwdaf.eventsubscription.client.model.PointUncertaintyCircle;
import io.nwdaf.eventsubscription.client.model.PointUncertaintyEllipse;
import io.nwdaf.eventsubscription.client.model.Accuracy.AccuracyEnum;
import io.nwdaf.eventsubscription.client.model.CivicAddress;
import io.nwdaf.eventsubscription.client.model.EllipsoidArc;
import io.nwdaf.eventsubscription.client.model.ExpectedAnalyticsType.ExpectedAnalyticsTypeEnum;
import io.nwdaf.eventsubscription.client.model.MatchingDirection.MatchingDirectionEnum;
import io.nwdaf.eventsubscription.client.requestbuilders.ParserUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RequestEventModel {
	private String event;
	private String notificationMethod;
	private List<String>optionals = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null,null));
	private List<String> anaMeta = new ArrayList<String>();
	private List<String> anaMetaInd = new ArrayList<String>(Arrays.asList(null,null,null,null));
	private List<String> aggrNwdafIds = new ArrayList<String>();
	private List<String> dataStatProps = new ArrayList<String>();
	private List<String> accPerSubset = new ArrayList<String>();
	private List<String>args = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null,null,null));
	private List<List<String>>nfLoadLvlThds = new ArrayList<List<String>>();
	private List<String> supis = new ArrayList<String>();
	private List<String> gpsis = new ArrayList<String>();
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
	private List<String> qosRequ = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null));
	private List<List<List<List<List<List<String>>>>>> exptUeBehav = new ArrayList<List<List<List<List<List<String>>>>>>();
	private List<String> upfInfo = new ArrayList<String>(Arrays.asList(null,null,null,null,null));
	//show booleans
	private Boolean showOptionals=false,showButtons=false,showNotifMethod=true,showExtraRepReq=true,showQosRequ=true,showExcepRequs=true,showNwPerfRequs=true,showSnssaia=true,showNfTypes=true,showTgtUe=true,showLoadLevelThreshold=true,showAnySlice=true,showNsiIdInfos=true,showNsiLevelThrds=true,showListOfAnaSubsets=true,showNetworkArea=true,showSupis=true,showNfLoadLvlThds=true,showNfInstanceIds=true,showNfSetIds=true,showMatchingDir=true,showIntGroupIds=true,showBwRequs=true,showRatFreqs=true,showUpfInfo=true,showAppServerAddrs=true,showDnns=true,showLadnDnns=true,showVisitedAreas=true,showQosFlowRetThds=true,showRanUeThrouThds=true,showExptAnaType=true,showExptUeBehav=true,showGpsis=true,showCongThresholds=true,showMaxTopAppUlNbr=true,showMaxTopAppDlNbr=true,showDisperReqs=true,showRedTransReqs=true,showWlanReqs=true,showDnais=true,showDnPerfReqs=true,showAppIds=true;
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
		initExptUeBehav();
		for(int i=0;i<this.nfLoadLvlThds.size();i++){
			while(this.nfLoadLvlThds.get(i).size()<11){
				this.nfLoadLvlThds.get(i).add(null);
			}
		}
		for(int i=0;i<this.congThresholds.size();i++){
			while(this.congThresholds.get(i).size()<11){
				this.congThresholds.get(i).add(null);
			}
		}
		for(int i=0;i<this.dnPerfReqs.size();i++){
			while(this.dnPerfReqs.get(i).size()<2){
				this.dnPerfReqs.get(i).add(new ArrayList<>());
			}
			if(this.dnPerfReqs.get(i).get(0).size()<1){
				this.dnPerfReqs.get(i).get(0).add(new ArrayList<>());
			}
			while(this.dnPerfReqs.get(i).get(0).get(0).size()<2){
				this.dnPerfReqs.get(i).get(0).get(0).add(null);
			}
			for(int j=0;j<this.dnPerfReqs.get(i).get(1).size();j++){
				while(this.dnPerfReqs.get(i).get(1).get(j).size()<11){
					this.dnPerfReqs.get(i).get(1).get(j).add(null);
				}
			}
		}
		if(maxObjectNbr!=null) {
			optionals.set(0, String.valueOf(maxObjectNbr));
		}
		if(maxSupiNbr!=null) {
			optionals.set(1, String.valueOf(maxSupiNbr));
		}
		if(startTs!=null && startTs!="") {
			optionals.set(2, startTs + ZonedDateTime.now().getOffset().getId());
		}
		if(endTs!=null && endTs!="") {
			optionals.set(3, endTs + ZonedDateTime.now().getOffset().getId());
		}
		if(accuracy!=null) {
			optionals.set(4, accuracy.toString());
		}
		if(timeAnaNeeded!=null && timeAnaNeeded!="") {
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
		for(int i=0;i<this.snssaia.size();i++){
			while(this.snssaia.get(i).size()<2){
				this.snssaia.get(i).add(null);
			}
		}
		for(int i=0;i<this.wlanReqs.size();i++) {
			while(this.wlanReqs.get(i).size()<3) {
				this.wlanReqs.get(i).add(new ArrayList<>());
			}
			while(this.wlanReqs.get(i).get(0).size()<2) {
				this.wlanReqs.get(i).get(0).add(null);
			}
			for(int j=0;j<this.wlanReqs.get(i).size();j++){
				for(int k=0;k<this.wlanReqs.get(i).get(j).size();k++){
					if(this.wlanReqs.get(i).get(j).get(k)!=null){
						if(this.wlanReqs.get(i).get(j).get(k)=="") {
							this.wlanReqs.get(i).get(j).set(k,null);
			    		}
					}
				}
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

	public void addGpsis(String item) {
		this.gpsis.add(item);
	}
	public void removeGpsis(Integer i) {
		this.gpsis.remove((int)i);
	}
	public String getGpsis(Integer i) {
		return this.gpsis.get(i);
	}
	public void setGpsis(String item,Integer i) {
		this.gpsis.set(i, item);
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

	public List<List<String>> getVisitedAreasList(Integer i1,Integer j1){
		for(int i=0;i<this.visitedAreas.size();i++){
			while(this.visitedAreas.get(i).size()<4){
				this.visitedAreas.get(i).add(new ArrayList<List<String>>());
			}
			for(int j=0;j<this.visitedAreas.get(i).get(0).size();j++){
				while(this.visitedAreas.get(i).get(0).get(j).size()<4){
					this.visitedAreas.get(i).get(0).get(j).add(null);
				}
			}
			for(int j=0;j<this.visitedAreas.get(i).get(1).size();j++){
				while(this.visitedAreas.get(i).get(1).get(j).size()<4){
					this.visitedAreas.get(i).get(1).get(j).add(null);
				}
			}
			for(int j=0;j<this.visitedAreas.get(i).get(2).size();j++){
				while(this.visitedAreas.get(i).get(2).get(j).size()<10){
					this.visitedAreas.get(i).get(2).get(j).add(null);
				}
			}
			for(int j=0;j<this.visitedAreas.get(i).get(3).size();j++){
				while(this.visitedAreas.get(i).get(3).get(j).size()<4){
					this.visitedAreas.get(i).get(3).get(j).add(null);
				}
			}
		}
		return this.visitedAreas.get(i1).get(j1);
	}
	public String getVisitedAreas(Integer i1,Integer j1,Integer k1,Integer w1){
		for(int i=0;i<this.visitedAreas.size();i++){
			while(this.visitedAreas.get(i).size()<4){
				this.visitedAreas.get(i).add(new ArrayList<List<String>>());
			}
			for(int j=0;j<this.visitedAreas.get(i).get(0).size();j++){
				while(this.visitedAreas.get(i).get(0).get(j).size()<4){
					this.visitedAreas.get(i).get(0).get(j).add(null);
				}
			}
			for(int j=0;j<this.visitedAreas.get(i).get(1).size();j++){
				while(this.visitedAreas.get(i).get(1).get(j).size()<4){
					this.visitedAreas.get(i).get(1).get(j).add(null);
				}
			}
			for(int j=0;j<this.visitedAreas.get(i).get(2).size();j++){
				while(this.visitedAreas.get(i).get(2).get(j).size()<10){
					this.visitedAreas.get(i).get(2).get(j).add(null);
				}
			}
			for(int j=0;j<this.visitedAreas.get(i).get(3).size();j++){
				while(this.visitedAreas.get(i).get(3).get(j).size()<4){
					this.visitedAreas.get(i).get(3).get(j).add(null);
				}
			}
		}
		return this.visitedAreas.get(i1).get(j1).get(k1).get(w1);
	}
	public void addVisitedAreas(List<List<List<String>>> l){
		if(l==null){
			l = new ArrayList<>();
			l.add(new ArrayList<>());
			l.add(new ArrayList<>());
			l.add(new ArrayList<>());
			l.add(new ArrayList<>());
		}
		this.visitedAreas.add(l);
	}
	public void removeVisitedAreas(Integer i){
		this.visitedAreas.remove((int) i);
	}
	public void addVisitedAreasItem(List<String> item,Integer i1,Integer j1){
		while(this.visitedAreas.size()<i1+1){
			this.visitedAreas.add(new ArrayList<>());
		}
		for(int i=0;i<this.visitedAreas.size();i++){
			while(this.visitedAreas.get(i).size()<4){
				this.visitedAreas.get(i).add(new ArrayList<List<String>>());
			}
			for(int j=0;j<this.visitedAreas.get(i).get(0).size();j++){
				while(this.visitedAreas.get(i).get(0).get(j).size()<4){
					this.visitedAreas.get(i).get(0).get(j).add(null);
				}
			}
			for(int j=0;j<this.visitedAreas.get(i).get(1).size();j++){
				while(this.visitedAreas.get(i).get(1).get(j).size()<4){
					this.visitedAreas.get(i).get(1).get(j).add(null);
				}
			}
			for(int j=0;j<this.visitedAreas.get(i).get(2).size();j++){
				while(this.visitedAreas.get(i).get(2).get(j).size()<10){
					this.visitedAreas.get(i).get(2).get(j).add(null);
				}
			}
			for(int j=0;j<this.visitedAreas.get(i).get(3).size();j++){
				while(this.visitedAreas.get(i).get(3).get(j).size()<4){
					this.visitedAreas.get(i).get(3).get(j).add(null);
				}
			}
		}
		if(item == null){
			if(j1==2){
				item = new ArrayList<>(Arrays.asList(null,null,null,null,null,null,null,null,null,null));
			}
			else{
				item = new ArrayList<>(Arrays.asList(null,null,null,null));
			}
		}
		this.visitedAreas.get(i1).get(j1).add(item);
	}
	public void removeVisitedAreasItem(Integer i1,Integer j1, Integer k1){
		for(int i=0;i<this.visitedAreas.size();i++){
			while(this.visitedAreas.get(i).size()<4){
				this.visitedAreas.get(i).add(new ArrayList<List<String>>());
			}
			for(int j=0;j<this.visitedAreas.get(i).get(0).size();j++){
				while(this.visitedAreas.get(i).get(0).get(j).size()<4){
					this.visitedAreas.get(i).get(0).get(j).add(null);
				}
			}
			for(int j=0;j<this.visitedAreas.get(i).get(1).size();j++){
				while(this.visitedAreas.get(i).get(1).get(j).size()<4){
					this.visitedAreas.get(i).get(1).get(j).add(null);
				}
			}
			for(int j=0;j<this.visitedAreas.get(i).get(2).size();j++){
				while(this.visitedAreas.get(i).get(2).get(j).size()<10){
					this.visitedAreas.get(i).get(2).get(j).add(null);
				}
			}
			for(int j=0;j<this.visitedAreas.get(i).get(3).size();j++){
				while(this.visitedAreas.get(i).get(3).get(j).size()<4){
					this.visitedAreas.get(i).get(3).get(j).add(null);
				}
			}
		}
		this.visitedAreas.get(i1).get(j1).remove((int) k1);
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
	public void addNsiIdInfosItem(Integer rowId,String item) {
		for(int i=0;i<this.nsiIdInfos.size();i++) {
			if(this.nsiIdInfos.get(i).size()<2) {
				this.nsiIdInfos.get(i).add(new ArrayList<String>());
			}
		}
		this.nsiIdInfos.get(rowId).get(1).add(item);
	}
	public void removeNsiIdInfos(Integer i,Integer j) {
		this.nsiIdInfos.get(i).get(1).remove((int)j);
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
			if(snssaia.get(i).get(j)=="," || snssaia.get(i).get(j)==""){
			snssaia.get(i).set(j, null);
			}
			else{
				this.snssaia.get(i).set(j, item);
			}
		}
	}
	public String getSnssaia(Integer i,Integer j) {
		while(snssaia.get(i).size()<2){
			snssaia.get(i).add(null);
		}
		if(snssaia.get(i).get(j)=="," || snssaia.get(i).get(j)==""){
			snssaia.get(i).set(j, null);
		}
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

	public void addRatFreqs(List<List<String>> item){
		if(item == null){
			item = new ArrayList<List<String>>();
			List<String> item_1 = new ArrayList<>(Arrays.asList(null,null,null,null,null));
			List<String> item_2 = new ArrayList<>(Arrays.asList(null,null,null,null,null,null,null,null,null,null,null));
			item.add(item_1);
			item.add(item_2);
		}
		this.ratFreqs.add(item);
	}
	public void setRatFreqs(String item,Integer i,Integer j){
		this.ratFreqs.get(i).get(0).set(j,item);
	}
	public void setRatFreqsItem(String item,Integer i,Integer j){
		this.ratFreqs.get(i).get(1).set(j,item);
	}
	public void removeRatFreqs(Integer i){
		this.ratFreqs.remove((int)i);
	}
	public String getRatFreqs(Integer i,Integer j){
		if(this.ratFreqs.get(i).size()==0){
			this.ratFreqs.get(i).add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));
		}
		if(this.ratFreqs.get(i).size()==1){
			this.ratFreqs.get(i).add(new ArrayList<>(Arrays.asList(null,null,null,null,null,null,null,null,null,null,null)));
		}
		while(this.ratFreqs.get(i).get(0).size()<5){
			this.ratFreqs.get(i).get(0).add(null);
		}
		return this.ratFreqs.get(i).get(0).get(j);
	}
	public String getRatFreqsItem(Integer i,Integer j){
		if(this.ratFreqs.get(i).size()==0){
			this.ratFreqs.get(i).add(new ArrayList<>(Arrays.asList(null,null,null,null,null)));
		}
		if(this.ratFreqs.get(i).size()==1){
			this.ratFreqs.get(i).add(new ArrayList<>(Arrays.asList(null,null,null,null,null,null,null,null,null,null,null)));
		}
		while(this.ratFreqs.get(i).get(1).size()<10){
			this.ratFreqs.get(i).get(1).add(null);
		}
		return this.ratFreqs.get(i).get(1).get(j);
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


	public List<List<String>> getDisperReqsList(Integer i1,Integer j1){
		for(int i=0;i<this.disperReqs.size();i++){
			while(this.disperReqs.get(i).size()<3){
				this.disperReqs.get(i).add(new ArrayList<List<String>>());
			}
			if(this.disperReqs.get(i).get(0).size()<1){
				this.disperReqs.get(i).get(0).add(new ArrayList<>());
			}
			while(this.disperReqs.get(i).get(0).get(0).size()<3){
				this.disperReqs.get(i).get(0).get(0).add(null);
			}
			for(int j=0;j<this.disperReqs.get(i).get(1).size();j++){
				while(this.disperReqs.get(i).get(1).get(j).size()<3){
					this.disperReqs.get(i).get(1).get(j).add(null);
				}
			}
			for(int j=0;j<this.disperReqs.get(i).get(2).size();j++){
				while(this.disperReqs.get(i).get(2).get(j).size()<2){
					this.disperReqs.get(i).get(2).get(j).add(null);
				}
			}
		}
		return this.disperReqs.get(i1).get(j1);
	}
	public String getDisperReqs(Integer i1,Integer j1,Integer k1,Integer w1){
		for(int i=0;i<this.disperReqs.size();i++){
			while(this.disperReqs.get(i).size()<3){
				this.disperReqs.get(i).add(new ArrayList<>());
			}
			if(this.disperReqs.get(i).get(0).size()<1){
				this.disperReqs.get(i).get(0).add(new ArrayList<>());
			}
			while(this.disperReqs.get(i).get(0).get(0).size()<3){
				this.disperReqs.get(i).get(0).get(0).add(null);
			}
			for(int j=0;j<this.disperReqs.get(i).get(1).size();j++){
				while(this.disperReqs.get(i).get(1).get(j).size()<3){
					this.disperReqs.get(i).get(1).get(j).add(null);
				}
			}
			for(int j=0;j<this.disperReqs.get(i).get(2).size();j++){
				while(this.disperReqs.get(i).get(2).get(j).size()<2){
					this.disperReqs.get(i).get(2).get(j).add(null);
				}
			}
		}
		return this.disperReqs.get(i1).get(j1).get(k1).get(w1);
	}
	public String getDisperReqs(Integer i1,Integer j1){
		for(int i=0;i<this.disperReqs.size();i++){
			while(this.disperReqs.get(i).size()<3){
				this.disperReqs.get(i).add(new ArrayList<>());
			}
			if(this.disperReqs.get(i).get(0).size()<1){
				this.disperReqs.get(i).get(0).add(new ArrayList<>());
			}
			while(this.disperReqs.get(i).get(0).get(0).size()<3){
				this.disperReqs.get(i).get(0).get(0).add(null);
			}
			for(int j=0;j<this.disperReqs.get(i).get(1).size();j++){
				while(this.disperReqs.get(i).get(1).get(j).size()<3){
					this.disperReqs.get(i).get(1).get(j).add(null);
				}
			}
			for(int j=0;j<this.disperReqs.get(i).get(2).size();j++){
				while(this.disperReqs.get(i).get(2).get(j).size()<2){
					this.disperReqs.get(i).get(2).get(j).add(null);
				}
			}
		}
		return this.disperReqs.get(i1).get(0).get(0).get(j1);
	}
	public void addDisperReqs(List<List<List<String>>> l){
		if(l==null){
			l = new ArrayList<>();
			List<List<String>> litem = new ArrayList<>();
			List<String> llitem = new ArrayList<>(Arrays.asList(null,null,null));
			litem.add(llitem);
			l.add(litem);
			l.add(new ArrayList<>());
			l.add(new ArrayList<>());
		}
		this.disperReqs.add(l);
	}
	public void removeDisperReqs(Integer i){
		this.disperReqs.remove((int) i);
	}
	public void addDisperReqsItem(List<String> item,Integer i1,Integer j1){
		while(this.disperReqs.size()<i1+1){
			this.disperReqs.add(new ArrayList<>());
		}
		for(int i=0;i<this.disperReqs.size();i++){
			while(this.disperReqs.get(i).size()<3){
				this.disperReqs.get(i).add(new ArrayList<>());
			}
			if(this.disperReqs.get(i).get(0).size()<1){
				this.disperReqs.get(i).get(0).add(new ArrayList<>());
			}
			while(this.disperReqs.get(i).get(0).get(0).size()<3){
				this.disperReqs.get(i).get(0).get(0).add(null);
			}
			for(int j=0;j<this.disperReqs.get(i).get(1).size();j++){
				while(this.disperReqs.get(i).get(1).get(j).size()<3){
					this.disperReqs.get(i).get(1).get(j).add(null);
				}
			}
			for(int j=0;j<this.disperReqs.get(i).get(2).size();j++){
				while(this.disperReqs.get(i).get(2).get(j).size()<2){
					this.disperReqs.get(i).get(2).get(j).add(null);
				}
			}
		}
		if(item == null){
			if(j1==1){
				item = new ArrayList<>(Arrays.asList(null,null,null));
			}
			else{
				item = new ArrayList<>(Arrays.asList(null,null));
			}
		}
		this.disperReqs.get(i1).get(j1).add(item);
	}
	public void removeDisperReqsItem(Integer i1,Integer j1, Integer k1){
		for(int i=0;i<this.disperReqs.size();i++){
			while(this.disperReqs.get(i).size()<3){
				this.disperReqs.get(i).add(new ArrayList<List<String>>());
			}
			if(this.disperReqs.get(i).get(0).size()<1){
				this.disperReqs.get(i).get(0).add(new ArrayList<>());
			}
			while(this.disperReqs.get(i).get(0).get(0).size()<3){
				this.disperReqs.get(i).get(0).get(0).add(null);
			}
			for(int j=0;j<this.disperReqs.get(i).get(1).size();j++){
				while(this.disperReqs.get(i).get(1).get(j).size()<3){
					this.disperReqs.get(i).get(1).get(j).add(null);
				}
			}
			for(int j=0;j<this.disperReqs.get(i).get(2).size();j++){
				while(this.disperReqs.get(i).get(2).get(j).size()<2){
					this.disperReqs.get(i).get(2).get(j).add(null);
				}
			}
		}
		this.disperReqs.get(i1).get(j1).remove((int) k1);
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

	public void addWlanReqs(List<List<String>> item) {
		if(item==null) {
			item = new ArrayList<List<String>>(Arrays.asList(new ArrayList<String>(Arrays.asList(null,null)),new ArrayList<String>(),new ArrayList<String>()));
			for(int i=0;i<this.wlanReqs.size();i++) {
				while(this.wlanReqs.get(i).size()<3) {
					this.wlanReqs.get(i).add(new ArrayList<>());
				}
				while(this.wlanReqs.get(i).get(0).size()<2) {
					this.wlanReqs.get(i).get(0).add(null);
				}
				for(int j=0;j<this.wlanReqs.get(i).size();j++){
					for(int k=0;k<this.wlanReqs.get(i).get(j).size();k++){
						if(this.wlanReqs.get(i).get(j).get(k)!=null){
							if(this.wlanReqs.get(i).get(j).get(k)=="") {
								this.wlanReqs.get(i).get(j).set(k,null);
							}
						}
					}
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
	public List<String> getWlanReqsList(Integer i){
		while(this.wlanReqs.get(i).size()<3) {
				this.wlanReqs.get(i).add(new ArrayList<>());
		}
		return this.wlanReqs.get(i).get(1);
	}
	public List<String> getWlanReqsList2(Integer i){
		while(this.wlanReqs.get(i).size()<3) {
				this.wlanReqs.get(i).add(new ArrayList<>());
		}
		return this.wlanReqs.get(i).get(2);
	}
	public String getWlanReqs(Integer i,Integer j) {
		return this.wlanReqs.get(i).get(0).get(j);
	}
	public String getWlanReqsItem(Integer i,Integer j) {
		return this.wlanReqs.get(i).get(1).get(j);
	}
	public String getWlanReqsItem2(Integer i,Integer j) {
		return this.wlanReqs.get(i).get(2).get(j);
	}
	public void setWlanReqs(String item,Integer i,Integer j) {
		this.wlanReqs.get(i).get(0).set(j,item);
	}
	public void setWlanReqsItem(String item,Integer i,Integer j) {
		this.wlanReqs.get(i).get(1).set(j,item);
	}
	public void addWlanReqsItem(Integer rowId) {
		for(int i=0;i<this.wlanReqs.size();i++) {
			while(this.wlanReqs.get(i).size()<3) {
				this.wlanReqs.get(i).add(new ArrayList<>());
			}
			while(this.wlanReqs.get(i).get(0).size()<2) {
				this.wlanReqs.get(i).get(0).add(null);
			}
			for(int j=0;j<this.wlanReqs.get(i).size();j++){
				for(int k=0;k<this.wlanReqs.get(i).get(j).size();k++){
					if(this.wlanReqs.get(i).get(j).get(k)!=null){
						if(this.wlanReqs.get(i).get(j).get(k)=="") {
							this.wlanReqs.get(i).get(j).set(k,null);
			    		}
					}
				}
			}
		}
		this.wlanReqs.get(rowId).get(1).add(null);
	}
	public void removeWlanReqs(Integer i1,Integer j1) {
		for(int i=0;i<this.wlanReqs.size();i++) {
			while(this.wlanReqs.get(i).size()<3) {
				this.wlanReqs.get(i).add(new ArrayList<>());
			}
			while(this.wlanReqs.get(i).get(0).size()<2) {
				this.wlanReqs.get(i).get(0).add(null);
			}
			for(int j=0;j<this.wlanReqs.get(i).size();j++){
				for(int k=0;k<this.wlanReqs.get(i).get(j).size();k++){
					if(this.wlanReqs.get(i).get(j).get(k)!=null){
						if(this.wlanReqs.get(i).get(j).get(k)=="") {
							this.wlanReqs.get(i).get(j).set(k,null);
			    		}
					}
				}
			}
		}
		this.wlanReqs.get(i1).remove((int)j1);
	}
	public void addWlanReqsItem2(Integer rowId) {
		for(int i=0;i<this.wlanReqs.size();i++) {
			while(this.wlanReqs.get(i).size()<3) {
				this.wlanReqs.get(i).add(new ArrayList<>());
			}
			while(this.wlanReqs.get(i).get(0).size()<2) {
				this.wlanReqs.get(i).get(0).add(null);
			}
			for(int j=0;j<this.wlanReqs.get(i).size();j++){
				for(int k=0;k<this.wlanReqs.get(i).get(j).size();k++){
					if(this.wlanReqs.get(i).get(j).get(k)!=null){
						if(this.wlanReqs.get(i).get(j).get(k)=="") {
							this.wlanReqs.get(i).get(j).set(k,null);
			    		}
					}
				}
			}
		}
		this.wlanReqs.get(rowId).get(2).add(null);
	}
	public void removeWlanReqs2(Integer i1,Integer j1) {
		for(int i=0;i<this.wlanReqs.size();i++) {
			while(this.wlanReqs.get(i).size()<3) {
				this.wlanReqs.get(i).add(new ArrayList<>());
			}
			while(this.wlanReqs.get(i).get(0).size()<2) {
				this.wlanReqs.get(i).get(0).add(null);
			}
			for(int j=0;j<this.wlanReqs.get(i).size();j++){
				for(int k=0;k<this.wlanReqs.get(i).get(j).size();k++){
					if(this.wlanReqs.get(i).get(j).get(k)!=null){
						if(this.wlanReqs.get(i).get(j).get(k)=="") {
							this.wlanReqs.get(i).get(j).set(k,null);
			    		}
					}
				}
			}
		}
		this.wlanReqs.get(i1).get(2).remove((int)j1);
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

	public List<List<String>> getDnPerfReqsList(Integer i1,Integer j1){
		for(int i=0;i<this.dnPerfReqs.size();i++){
			while(this.dnPerfReqs.get(i).size()<2){
				this.dnPerfReqs.get(i).add(new ArrayList<>());
			}
			if(this.dnPerfReqs.get(i).get(0).size()<1){
				this.dnPerfReqs.get(i).get(0).add(new ArrayList<>());
			}
			while(this.dnPerfReqs.get(i).get(0).get(0).size()<2){
				this.dnPerfReqs.get(i).get(0).get(0).add(null);
			}
			for(int j=0;j<this.dnPerfReqs.get(i).get(1).size();j++){
				while(this.dnPerfReqs.get(i).get(1).get(j).size()<11){
					this.dnPerfReqs.get(i).get(1).get(j).add(null);
				}
			}
		}
		return this.dnPerfReqs.get(i1).get(j1);
	}
	public String getDnPerfReqs(Integer i1,Integer j1,Integer k1,Integer w1){
		for(int i=0;i<this.dnPerfReqs.size();i++){
			while(this.dnPerfReqs.get(i).size()<2){
				this.dnPerfReqs.get(i).add(new ArrayList<>());
			}
			if(this.dnPerfReqs.get(i).get(0).size()<1){
				this.dnPerfReqs.get(i).get(0).add(new ArrayList<>());
			}
			while(this.dnPerfReqs.get(i).get(0).get(0).size()<2){
				this.dnPerfReqs.get(i).get(0).get(0).add(null);
			}
			for(int j=0;j<this.dnPerfReqs.get(i).get(1).size();j++){
				while(this.dnPerfReqs.get(i).get(1).get(j).size()<11){
					this.dnPerfReqs.get(i).get(1).get(j).add(null);
				}
			}
		}
		return this.dnPerfReqs.get(i1).get(j1).get(k1).get(w1);
	}
	public String getDnPerfReqs(Integer i1,Integer j1){
		for(int i=0;i<this.dnPerfReqs.size();i++){
			while(this.dnPerfReqs.get(i).size()<2){
				this.dnPerfReqs.get(i).add(new ArrayList<>());
			}
			if(this.dnPerfReqs.get(i).get(0).size()<1){
				this.dnPerfReqs.get(i).get(0).add(new ArrayList<>());
			}
			while(this.dnPerfReqs.get(i).get(0).get(0).size()<2){
				this.dnPerfReqs.get(i).get(0).get(0).add(null);
			}
			for(int j=0;j<this.dnPerfReqs.get(i).get(1).size();j++){
				while(this.dnPerfReqs.get(i).get(1).get(j).size()<11){
					this.dnPerfReqs.get(i).get(1).get(j).add(null);
				}
			}
		}
		return this.dnPerfReqs.get(i1).get(0).get(0).get(j1);
	}
	public void addDnPerfReqs(List<List<List<String>>> l){
		if(l==null){
			l = new ArrayList<>();
			List<List<String>> litem = new ArrayList<>();
			List<String> llitem = new ArrayList<>(Arrays.asList(null,null));
			litem.add(llitem);
			l.add(litem);
			l.add(new ArrayList<>());
		}
		this.dnPerfReqs.add(l);
	}
	public void removeDnPerfReqs(Integer i){
		this.dnPerfReqs.remove((int) i);
	}
	public void addDnPerfReqsItem(List<String> item,Integer i1,Integer j1){
		while(this.dnPerfReqs.size()<i1+1){
			this.dnPerfReqs.add(new ArrayList<>());
		}
		for(int i=0;i<this.dnPerfReqs.size();i++){
			while(this.dnPerfReqs.get(i).size()<2){
				this.dnPerfReqs.get(i).add(new ArrayList<>());
			}
			if(this.dnPerfReqs.get(i).get(0).size()<1){
				this.dnPerfReqs.get(i).get(0).add(new ArrayList<>());
			}
			while(this.dnPerfReqs.get(i).get(0).get(0).size()<2){
				this.dnPerfReqs.get(i).get(0).get(0).add(null);
			}
			for(int j=0;j<this.dnPerfReqs.get(i).get(1).size();j++){
				while(this.dnPerfReqs.get(i).get(1).get(j).size()<11){
					this.dnPerfReqs.get(i).get(1).get(j).add(null);
				}
			}
		}
		if(item == null){
			if(j1==1){
				item = new ArrayList<>(Arrays.asList(null,null,null,null,null,null,null,null,null,null,null));
			}
			else{
				item = new ArrayList<>(Arrays.asList(null,null));
			}
		}
		this.dnPerfReqs.get(i1).get(j1).add(item);
	}
	public void removeDnPerfReqsItem(Integer i1,Integer j1, Integer k1){
		for(int i=0;i<this.dnPerfReqs.size();i++){
			while(this.dnPerfReqs.get(i).size()<2){
				this.dnPerfReqs.get(i).add(new ArrayList<>());
			}
			if(this.dnPerfReqs.get(i).get(0).size()<1){
				this.dnPerfReqs.get(i).get(0).add(new ArrayList<>());
			}
			while(this.dnPerfReqs.get(i).get(0).get(0).size()<2){
				this.dnPerfReqs.get(i).get(0).get(0).add(null);
			}
			for(int j=0;j<this.dnPerfReqs.get(i).get(1).size();j++){
				while(this.dnPerfReqs.get(i).get(1).get(j).size()<11){
					this.dnPerfReqs.get(i).get(1).get(j).add(null);
				}
			}
		}
		this.dnPerfReqs.get(i1).get(j1).remove((int) k1);
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
	public void addNetworkAreaItem(List<String> item,Integer i) {
		while(this.networkArea.size()<4){
			this.networkArea.add(new ArrayList<List<String>>());
		}
		if(item == null){
			if(i==0||i==1||i==3){
				this.networkArea.get(i).add(new ArrayList<>(Arrays.asList(null,null,null,null)));
			}
			if(i==2){
				this.networkArea.get(i).add(new ArrayList<>(Arrays.asList(null,null,null,null,null,null,null,null,null,null)));
			}
		}
		else{
			this.networkArea.get(i).add(item);
		}
	}
	public void removeNetworkArea(Integer i,Integer j) {
		this.networkArea.get(i).remove((int)j);
	}

	public String getQosRequ(Integer i) {
		return this.qosRequ.get(i);
	}
	public void setQosRequ(String item,Integer i) {
		this.qosRequ.set(i, item);
	}

	public void addExptUeBehav(String item,Integer i1){
		initExptUeBehav();
		this.exptUeBehav.get(i1).get(0).get(0).get(0).get(0).add(item);
	}
	public void addExptUeBehav(List<List<String>> item,Integer i1,Integer j1){
		initExptUeBehav();
		this.exptUeBehav.get(3).get(i1).get(j1).add(item);
	}
	public void addExptUeBehav(String item,Integer i1,Integer j1,Integer k1){
		initExptUeBehav();
		this.exptUeBehav.get(3).get(i1).get(j1).get(k1).get(1).add(item);
		this.exptUeBehav.get(3).get(i1).get(j1).get(k1).get(2).add(item);
	}
	public void addExptUeBehavItem(List<List<List<List<String>>>> item,Integer i1){
		initExptUeBehav();
		this.exptUeBehav.get(i1).add(item);
	}
	public void addExptUeBehavItem(List<String> item,Integer i1,Integer j1){
		initExptUeBehav();
		this.exptUeBehav.get(3).get(i1).get(0).get(j1).add(item);
	}
	public void addExptUeBehavItem(List<String> item,Integer i1,Integer j1,Integer k1){
		initExptUeBehav();
		this.exptUeBehav.get(3).get(i1).get(j1).get(k1).add(item);
	}
	public void removeExptUeBehav(Integer i1,Integer j1){
		initExptUeBehav();
		this.exptUeBehav.get(i1).get(0).get(0).get(0).get(0).remove((int)j1);
	}
	public void removeExptUeBehav(Integer i1,Integer j1,Integer k1){
		initExptUeBehav();
		this.exptUeBehav.get(3).get(i1).get(j1).remove((int)k1);
	}
	public void removeExptUeBehav(Integer i1,Integer j1,Integer k1,Integer l1){
		initExptUeBehav();
		this.exptUeBehav.get(3).get(i1).get(j1).get(k1).get(1).remove((int) l1);
		this.exptUeBehav.get(3).get(i1).get(j1).get(k1).get(2).remove((int) l1);
	}
	public void removeExptUeBehavItem(Integer i1,Integer j1){
		initExptUeBehav();
		this.exptUeBehav.get(i1).remove((int) j1);
	}
	public void removeExptUeBehavItem(Integer i1,Integer j1,Integer k1){
		initExptUeBehav();
		this.exptUeBehav.get(3).get(i1).get(0).get(j1).remove((int) k1);
	}
	public void removeExptUeBehavItem(Integer i1,Integer j1,Integer k1,Integer l1){
		initExptUeBehav();
		this.exptUeBehav.get(3).get(i1).get(j1).get(k1).remove((int) l1);
	}
	public String getExptUeBehav(Integer i1,Integer j1,Integer k1){
		initExptUeBehav();
		return this.exptUeBehav.get(i1).get(0).get(0).get(0).get(j1).get(k1);
	}
	public List<String> getExptUeBehavList(Integer i1){
		initExptUeBehav();
		return this.exptUeBehav.get(i1).get(0).get(0).get(0).get(0);
	}
	public List<List<List<List<List<String>>>>> getExptUeBehavListUmts(Integer i1){
		initExptUeBehav();
		return this.exptUeBehav.get(i1);
	}
	public List<List<String>> getExptUeBehavList(Integer i1,Integer j1,Integer k1){
		initExptUeBehav();
		return this.exptUeBehav.get(3).get(i1).get(j1).get(k1);
	}
	public List<String> getExptUeBehavList(Integer i1,Integer j1,Integer k1,Integer l1){
		initExptUeBehav();
		return this.exptUeBehav.get(i1).get(j1).get(k1).get(l1).get(1);
	}
	public List<List<List<String>>> getExptUeBehavList(Integer i1,Integer j1){
		initExptUeBehav();
		return this.exptUeBehav.get(3).get(i1).get(j1);
	}
	public String getExptUeBehavItem(Integer i1,Integer j1,Integer k1,Integer l1,Integer m1,Integer n1){
		initExptUeBehav();
		return this.exptUeBehav.get(i1).get(j1).get(k1).get(l1).get(m1).get(n1);
	}
	public void initExptUeBehav(){
		while(this.exptUeBehav.size()<4){
			this.exptUeBehav.add(new ArrayList<>());
		}
		if(this.exptUeBehav.get(0).size()<1){
			this.exptUeBehav.get(0).add(new ArrayList<>());
		}
		if(this.exptUeBehav.get(0).get(0).size()<1){
			this.exptUeBehav.get(0).get(0).add(new ArrayList<>());
		}
		if(this.exptUeBehav.get(0).get(0).get(0).size()<1){
			this.exptUeBehav.get(0).get(0).get(0).add(new ArrayList<>());
		}
		if(this.exptUeBehav.get(0).get(0).get(0).get(0).size()<1){
			this.exptUeBehav.get(0).get(0).get(0).get(0).add(new ArrayList<>(Arrays.asList(null,null,null,null,null,null)));
		}
		if(this.exptUeBehav.get(1).size()<1){
			this.exptUeBehav.get(1).add(new ArrayList<>());
		}
		if(this.exptUeBehav.get(1).get(0).size()<1){
			this.exptUeBehav.get(1).get(0).add(new ArrayList<>());
		}
		if(this.exptUeBehav.get(1).get(0).get(0).size()<1){
			this.exptUeBehav.get(1).get(0).get(0).add(new ArrayList<>());
		}
		while(this.exptUeBehav.get(1).get(0).get(0).get(0).size()<2){
			this.exptUeBehav.get(1).get(0).get(0).get(0).add(new ArrayList<>());
		}
		while(this.exptUeBehav.get(1).get(0).get(0).get(0).get(1).size()<2){
			this.exptUeBehav.get(1).get(0).get(0).get(0).get(1).add(null);
		}
		if(this.exptUeBehav.get(2).size()<1){
			this.exptUeBehav.get(2).add(new ArrayList<>());
		}
		if(this.exptUeBehav.get(2).get(0).size()<1){
			this.exptUeBehav.get(2).get(0).add(new ArrayList<>());
		}
		if(this.exptUeBehav.get(2).get(0).get(0).size()<1){
			this.exptUeBehav.get(2).get(0).get(0).add(new ArrayList<>());
		}
		if(this.exptUeBehav.get(2).get(0).get(0).get(0).size()<1){
			this.exptUeBehav.get(2).get(0).get(0).get(0).add(new ArrayList<>(Arrays.asList(null,null,null)));
		}
		for(int i=0;i<this.exptUeBehav.get(3).size();i++){
			if(this.exptUeBehav.get(3).get(i)==null){
				this.exptUeBehav.get(3).set(i,new ArrayList<>());
			}
			while(this.exptUeBehav.get(3).get(i).size()<4){
				this.exptUeBehav.get(3).get(i).add(new ArrayList<>());
			}
			while(this.exptUeBehav.get(3).get(i).get(0).size()<4){
				this.exptUeBehav.get(3).get(i).get(0).add(new ArrayList<>());
			}
			for(int j=0;j<this.exptUeBehav.get(3).get(i).get(0).get(0).size();j++){
				if(this.exptUeBehav.get(3).get(i).get(0).get(0).get(j)==null){
					this.exptUeBehav.get(3).get(i).get(0).get(0).set(j,new ArrayList<>());
				}
				while(this.exptUeBehav.get(3).get(i).get(0).get(0).get(j).size()<4){
					this.exptUeBehav.get(3).get(i).get(0).get(0).get(j).add(null);
				}
			}
			for(int j=0;j<this.exptUeBehav.get(3).get(i).get(0).get(1).size();j++){
				if(this.exptUeBehav.get(3).get(i).get(0).get(1).get(j)==null){
					this.exptUeBehav.get(3).get(i).get(0).get(1).set(j,new ArrayList<>());
				}
				while(this.exptUeBehav.get(3).get(i).get(0).get(1).get(j).size()<4){
					if(this.exptUeBehav.get(3).get(i).get(0).get(1).get(j)==null){
						this.exptUeBehav.get(3).get(i).get(0).get(1).set(j,new ArrayList<>());
					}
					this.exptUeBehav.get(3).get(i).get(0).get(1).get(j).add(null);
				}
			}
			for(int j=0;j<this.exptUeBehav.get(3).get(i).get(0).get(2).size();j++){
				if(this.exptUeBehav.get(3).get(i).get(0).get(2).get(j)==null){
					this.exptUeBehav.get(3).get(i).get(0).get(2).set(j,new ArrayList<>());
				}
				while(this.exptUeBehav.get(3).get(i).get(0).get(2).get(j).size()<10){
					this.exptUeBehav.get(3).get(i).get(0).get(2).get(j).add(null);
				}
			}
			for(int j=0;j<this.exptUeBehav.get(3).get(i).get(0).get(3).size();j++){
				if(this.exptUeBehav.get(3).get(i).get(0).get(3).get(j)==null){
					this.exptUeBehav.get(3).get(i).get(0).get(3).set(j,new ArrayList<>());
				}
				while(this.exptUeBehav.get(3).get(i).get(0).get(3).get(j).size()<4){
					this.exptUeBehav.get(3).get(i).get(0).get(3).get(j).add(null);
				}
			}
			if(this.exptUeBehav.get(3).get(i).get(1).size()<1){
				this.exptUeBehav.get(3).get(i).get(1).add(new ArrayList<>());
			}
			if(this.exptUeBehav.get(3).get(i).get(1).get(0).size()<1){
				this.exptUeBehav.get(3).get(i).get(1).get(0).add(new ArrayList<>());
			}
			while(this.exptUeBehav.get(3).get(i).get(1).get(0).get(0).size()<2){
				this.exptUeBehav.get(3).get(i).get(1).get(0).get(0).add(null);
			}
			for(int j=0;j<this.exptUeBehav.get(3).get(i).get(2).size();j++){
				if(this.exptUeBehav.get(3).get(i).get(2).get(j)==null){
					this.exptUeBehav.get(3).get(i).get(2).set(j,new ArrayList<>());
				}
				while(this.exptUeBehav.get(3).get(i).get(2).get(j).size()<3){
					this.exptUeBehav.get(3).get(i).get(2).get(j).add(new ArrayList<>());
				}
				while(this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).size()<9){
					this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).add(null);
				}
			}
			if(this.exptUeBehav.get(3).get(i).get(3).size()<1){
				this.exptUeBehav.get(3).get(i).get(3).add(new ArrayList<>());
			}
			for(int j=0;j<this.exptUeBehav.get(3).get(i).get(3).get(0).size();j++){
				if(this.exptUeBehav.get(3).get(i).get(3).get(0).get(j)==null){
					this.exptUeBehav.get(3).get(i).get(3).get(0).set(j,new ArrayList<>());
				}
				while(this.exptUeBehav.get(3).get(i).get(3).get(0).get(j).size()<34){
					this.exptUeBehav.get(3).get(i).get(3).get(0).get(j).add(null);
				}
			}
		}
	}

	public String getUpfInfo(Integer i) {
		return this.upfInfo.get(i);
	}
	public void setUpfInfo(String item,Integer i) {
		this.upfInfo.set(i, item);
	}

    public RequestEventModel fromEventObject(EventSubscription e) {
		if(e!=null){
			if(e.getEvent()!=null){
				this.setEvent(ParserUtil.safeParseString(e.getEvent().getEvent()));
			}
			if(e.getNotificationMethod()!=null){
				this.setNotificationMethod((ParserUtil.safeParseString(e.getNotificationMethod().getNotifMethod())));
			}
			if(e.getMatchingDir()!=null){
				this.setMatchingDir(e.getMatchingDir().getMatchingDir());
			}
			if(e.getExtraReportReq()!=null){
				this.optionals.set(0, ParserUtil.safeParseString(e.getExtraReportReq().getMaxObjectNbr()));
				this.optionals.set(1, ParserUtil.safeParseString(e.getExtraReportReq().getMaxSupiNbr()));
				this.optionals.set(2, ParserUtil.safeParseString(e.getExtraReportReq().getStartTs()));
				this.optionals.set(3, ParserUtil.safeParseString(e.getExtraReportReq().getEndTs()));
				if(e.getExtraReportReq().getAccuracy()!=null){
					this.optionals.set(4, ParserUtil.safeParseString(e.getExtraReportReq().getAccuracy().getAccuracy()));
				}
				this.optionals.set(5, ParserUtil.safeParseString(e.getExtraReportReq().getTimeAnaNeeded()));
				this.optionals.set(6, ParserUtil.safeParseString(e.getExtraReportReq().getOffsetPeriod()));
				if(e.getExtraReportReq().getAnaMeta()!=null){
					for(int i=0;i<e.getExtraReportReq().getAnaMeta().size();i++){
						if(e.getExtraReportReq().getAnaMeta().get(i)!=null){
						this.anaMeta.add(ParserUtil.safeParseString(e.getExtraReportReq().getAnaMeta().get(i).getAnaMeta()));
						}
					}
				}
				if(e.getExtraReportReq().getAnaMetaInd()!=null){
					if(e.getExtraReportReq().getAnaMetaInd().getDataWindow()!=null){
						this.anaMetaInd.set(0,ParserUtil.safeParseString(e.getExtraReportReq().getAnaMetaInd().getDataWindow().getStartTime()));
						this.anaMetaInd.set(1,ParserUtil.safeParseString(e.getExtraReportReq().getAnaMetaInd().getDataWindow().getStopTime()));
					}
					if(e.getExtraReportReq().getAnaMetaInd().getStrategy()!=null){
						this.anaMetaInd.set(2,ParserUtil.safeParseString(e.getExtraReportReq().getAnaMetaInd().getStrategy().getStrategy()));
					}
					if(e.getExtraReportReq().getAnaMetaInd().getAggrNwdafIds()!=null){
						for(int i=0;i<e.getExtraReportReq().getAnaMetaInd().getAggrNwdafIds().size();i++){
							this.aggrNwdafIds.add(ParserUtil.safeParseString(e.getExtraReportReq().getAnaMetaInd().getAggrNwdafIds().get(i)));
						}
					}
					if(e.getExtraReportReq().getAnaMetaInd().getDataStatProps()!=null){
						for(int i=0;i<e.getExtraReportReq().getAnaMetaInd().getDataStatProps().size();i++){
							if(e.getExtraReportReq().getAnaMetaInd().getDataStatProps().get(i)!=null){
								this.dataStatProps.add(ParserUtil.safeParseString(e.getExtraReportReq().getAnaMetaInd().getDataStatProps().get(i).getDataStatProps()));
							}
						}
					}
				}
				if(e.getExtraReportReq().getAccPerSubset()!=null){
					for(int i=0;i<e.getExtraReportReq().getAccPerSubset().size();i++){
						if(e.getExtraReportReq().getAccPerSubset().get(i)!=null){
							this.accPerSubset.add(ParserUtil.safeParseString(e.getExtraReportReq().getAccPerSubset().get(i).getAccuracy()));
						}
					}
				}
			}
			if(e.getTgtUe()!=null){
				this.setSupis(e.getTgtUe().getSupis());
				this.setGpsis(e.getTgtUe().getGpsis());
				this.setIntGroupIds(e.getTgtUe().getIntGroupIds());
				this.args.set(0, ParserUtil.safeParseString(e.getTgtUe().isAnyUe()));
			}
			if(e.getNfInstanceIds()!=null){
				for(int i=0;i<e.getNfInstanceIds().size();i++){
					this.nfInstanceIds.add(ParserUtil.safeParseString(e.getNfInstanceIds().get(i)));
				}
			}
			if(e.getNfSetIds()!=null){
				for(int i=0;i<e.getNfSetIds().size();i++){
					this.nfSetIds.add(e.getNfSetIds().get(i));
				}
			}
			if(e.getAppIds()!=null){
				for(int i=0;i<e.getAppIds().size();i++){
					this.nfSetIds.add(e.getAppIds().get(i));
				}
			}
			if(e.getDnns()!=null){
				for(int i=0;i<e.getDnns().size();i++){
					this.nfSetIds.add(e.getDnns().get(i));
				}
			}
			if(e.getDnais()!=null){
				for(int i=0;i<e.getDnais().size();i++){
					this.nfSetIds.add(e.getDnais().get(i));
				}
			}
			if(e.getLadnDnns()!=null){
				for(int i=0;i<e.getLadnDnns().size();i++){
					this.nfSetIds.add(e.getLadnDnns().get(i));
				}
			}
			if(e.getNfTypes()!=null){
				for(int i=0;i<e.getNfTypes().size();i++){
					if(e.getNfTypes().get(i)!=null){
						this.nfInstanceIds.add(ParserUtil.safeParseString(e.getNfTypes().get(i).getNfType()));
					}
				}
			}
			if(e.getNfLoadLvlThds()!=null){
				for(int i=0;i<e.getNfLoadLvlThds().size();i++){
					if(e.getNfLoadLvlThds().get(i)!=null){
						this.nfLoadLvlThds.add(new ArrayList<>(Arrays.asList(ParserUtil.safeParseString(e.getNfLoadLvlThds().get(i).getCongLevel()),
						ParserUtil.safeParseString(e.getNfLoadLvlThds().get(i).getNfLoadLevel()),
						ParserUtil.safeParseString(e.getNfLoadLvlThds().get(i).getNfCpuUsage()),
						ParserUtil.safeParseString(e.getNfLoadLvlThds().get(i).getNfMemoryUsage()),
						ParserUtil.safeParseString(e.getNfLoadLvlThds().get(i).getNfStorageUsage()),
						ParserUtil.safeParseString(e.getNfLoadLvlThds().get(i).getAvgTrafficRate()),
						ParserUtil.safeParseString(e.getNfLoadLvlThds().get(i).getMaxTrafficRate()),
						ParserUtil.safeParseString(e.getNfLoadLvlThds().get(i).getAvgPacketDelay()),
						ParserUtil.safeParseString(e.getNfLoadLvlThds().get(i).getMaxPacketDelay()),
						ParserUtil.safeParseString(e.getNfLoadLvlThds().get(i).getAvgPacketLossRate()),
						ParserUtil.safeParseString(e.getNfLoadLvlThds().get(i).getSvcExpLevel())
						)));
					}
				}
			}
			if(e.getVisitedAreas()!=null){
				for(int i=0;i<e.getVisitedAreas().size();i++){
					if(e.getVisitedAreas().get(i)!=null){
						if(e.getVisitedAreas().get(i).getEcgis()!=null){
							for(int j=0;j<e.getVisitedAreas().get(i).getEcgis().size();j++){
								if(e.getVisitedAreas().get(i).getEcgis().get(j)!=null){
									if(e.getVisitedAreas().get(i).getEcgis().get(j).getPlmnId()!=null){
										this.addVisitedAreasItem(new ArrayList<>(Arrays.asList(e.getVisitedAreas().get(i).getEcgis().get(j).getPlmnId().getMcc(),
										e.getVisitedAreas().get(i).getEcgis().get(j).getPlmnId().getMnc(),
										e.getVisitedAreas().get(i).getEcgis().get(j).getEutraCellId(),
										e.getVisitedAreas().get(i).getEcgis().get(j).getNid())), i, 0);
									}
								}
							}
							for(int j=0;j<e.getVisitedAreas().get(i).getNcgis().size();j++){
								if(e.getVisitedAreas().get(i).getNcgis().get(j)!=null){
									if(e.getVisitedAreas().get(i).getNcgis().get(j).getPlmnId()!=null){
										this.addVisitedAreasItem(new ArrayList<>(Arrays.asList(e.getVisitedAreas().get(i).getNcgis().get(j).getPlmnId().getMcc(),
										e.getVisitedAreas().get(i).getNcgis().get(j).getPlmnId().getMnc(),
										e.getVisitedAreas().get(i).getNcgis().get(j).getNrCellId(),
										e.getVisitedAreas().get(i).getNcgis().get(j).getNid())), i, 1);
									}
								}
							}
							for(int j=0;j<e.getVisitedAreas().get(i).getGRanNodeIds().size();j++){
								if(e.getVisitedAreas().get(i).getGRanNodeIds().get(j)!=null){
									String bitlength=null,gnbvalue=null,mcc=null,mnc=null;
									if(e.getVisitedAreas().get(i).getGRanNodeIds().get(j).getGNbId()!=null){
										bitlength = ParserUtil.safeParseString(e.getVisitedAreas().get(i).getGRanNodeIds().get(j).getGNbId().getBitLength());
										gnbvalue = ParserUtil.safeParseString(e.getVisitedAreas().get(i).getGRanNodeIds().get(j).getGNbId().getGNBValue());
									}
									if(e.getVisitedAreas().get(i).getGRanNodeIds().get(j).getPlmnId()!=null){
										mcc = e.getVisitedAreas().get(i).getGRanNodeIds().get(j).getPlmnId().getMcc();
										mnc = e.getVisitedAreas().get(i).getGRanNodeIds().get(j).getPlmnId().getMnc();
									}
									this.addVisitedAreasItem(new ArrayList<>(Arrays.asList(mcc,mnc,
									e.getVisitedAreas().get(i).getGRanNodeIds().get(j).getN3IwfId(),
									e.getVisitedAreas().get(i).getGRanNodeIds().get(j).getNgeNbId(),
									e.getVisitedAreas().get(i).getGRanNodeIds().get(j).getWagfId(),
									e.getVisitedAreas().get(i).getGRanNodeIds().get(j).getTngfId(),
									e.getVisitedAreas().get(i).getGRanNodeIds().get(j).getNid(),
									e.getVisitedAreas().get(i).getGRanNodeIds().get(j).getENbId(),
									bitlength, gnbvalue)), i, 2);
								}
							}
							for(int j=0;j<e.getVisitedAreas().get(i).getTais().size();j++){
								if(e.getVisitedAreas().get(i).getTais().get(j)!=null){
									if(e.getVisitedAreas().get(i).getTais().get(j).getPlmnId()!=null){
										this.addVisitedAreasItem(new ArrayList<>(Arrays.asList(e.getVisitedAreas().get(i).getTais().get(j).getPlmnId().getMcc(),
										e.getVisitedAreas().get(i).getTais().get(j).getPlmnId().getMnc(),
										e.getVisitedAreas().get(i).getTais().get(j).getTac(),
										e.getVisitedAreas().get(i).getTais().get(j).getNid())), i, 3);
									}
								}
							}
						}
					}
				}
			}
			if(e.getNsiIdInfos()!=null){
				for(int i=0;i<e.getNsiIdInfos().size();i++){
					if(e.getNsiIdInfos().get(i)!=null){
						this.addNsiIdInfos(null);
						if(e.getNsiIdInfos().get(i).getSnssai()!=null){
							this.setNsiIdInfos(ParserUtil.safeParseString(e.getNsiIdInfos().get(i).getSnssai().getSst()), i, 0);
							this.setNsiIdInfos(ParserUtil.safeParseString(e.getNsiIdInfos().get(i).getSnssai().getSd()), i, 1);
						}
						this.nsiIdInfos.get(i).set(1, e.getNsiIdInfos().get(i).getNsiIds());
				}
				}
			}
			if(e.getNsiLevelThrds()!=null){
				for(int i=0;i<e.getNsiLevelThrds().size();i++){
					this.nsiLevelThrds.add(e.getNsiLevelThrds().get(i));
				}
			}
			if(e.getQosFlowRetThds()!=null){
				for(int i=0;i<e.getQosFlowRetThds().size();i++){
					if(e.getQosFlowRetThds().get(i)!=null){
						this.addQosFlowRetThds(null);
						this.setQosFlowRetThds(ParserUtil.safeParseString(e.getQosFlowRetThds().get(i).getRelFlowNum()), i, 0);
						this.setQosFlowRetThds(ParserUtil.safeParseString(e.getQosFlowRetThds().get(i).getRelFlowRatio()), i, 1);
						if(e.getQosFlowRetThds().get(i).getRelTimeUnit()!=null){
							this.setQosFlowRetThds(ParserUtil.safeParseString(e.getQosFlowRetThds().get(i).getRelTimeUnit().getRelTimeUnit()), i, 2);
						}
					}
				}
			}
			if(e.getRanUeThrouThds()!=null){
				for(int i=0;i<e.getRanUeThrouThds().size();i++){
					this.ranUeThrouThds.add(e.getRanUeThrouThds().get(i));
				}
			}
			if(e.getSnssaia()!=null){
				for(int i=0;i<e.getSnssaia().size();i++){
					if(e.getSnssaia().get(i)!=null){
						this.addSnssaia(null);
						this.setSnssaia(ParserUtil.safeParseString(e.getSnssaia().get(i).getSst()), i, 0);
						this.setSnssaia(ParserUtil.safeParseString(e.getSnssaia().get(i).getSd()), i, 1);
					}
				}
			}
			if(e.getCongThresholds()!=null){
				for(int i=0;i<e.getCongThresholds().size();i++){
					if(e.getCongThresholds().get(i)!=null){
						this.congThresholds.add(new ArrayList<>(Arrays.asList(ParserUtil.safeParseString(e.getCongThresholds().get(i).getCongLevel()),
						ParserUtil.safeParseString(e.getCongThresholds().get(i).getNfLoadLevel()),
						ParserUtil.safeParseString(e.getCongThresholds().get(i).getNfCpuUsage()),
						ParserUtil.safeParseString(e.getCongThresholds().get(i).getNfMemoryUsage()),
						ParserUtil.safeParseString(e.getCongThresholds().get(i).getNfStorageUsage()),
						ParserUtil.safeParseString(e.getCongThresholds().get(i).getAvgTrafficRate()),
						ParserUtil.safeParseString(e.getCongThresholds().get(i).getMaxTrafficRate()),
						ParserUtil.safeParseString(e.getCongThresholds().get(i).getAvgPacketDelay()),
						ParserUtil.safeParseString(e.getCongThresholds().get(i).getMaxPacketDelay()),
						ParserUtil.safeParseString(e.getCongThresholds().get(i).getAvgPacketLossRate()),
						ParserUtil.safeParseString(e.getCongThresholds().get(i).getSvcExpLevel())
						)));
					}
				}
			}
			if(e.getNwPerfRequs()!=null){
				for(int i=0;i<e.getNwPerfRequs().size();i++){
					if(e.getNwPerfRequs().get(i)!=null){
						this.addNwPerfRequs(null);
						if(e.getNwPerfRequs().get(i)!=null){
							if(e.getNwPerfRequs().get(i).getNwPerfType()!=null){
								this.setNwPerfRequs(ParserUtil.safeParseString(e.getNwPerfRequs().get(i).getNwPerfType().getnwPerfType()), i, 0);
							}
							this.setSnssaia(ParserUtil.safeParseString(e.getNwPerfRequs().get(i).getRelativeRatio()), i, 1);
							this.setSnssaia(ParserUtil.safeParseString(e.getNwPerfRequs().get(i).getAbsoluteNum()), i, 2);
						}
					}
				}
			}
			if(e.getExcepRequs()!=null){
				for(int i=0;i<e.getExcepRequs().size();i++){
					if(e.getExcepRequs().get(i)!=null){
						this.addExcepRequs(null);
						if(e.getExcepRequs().get(i)!=null){
							if(e.getExcepRequs().get(i).getExcepId()!=null){
								this.setExcepRequs(ParserUtil.safeParseString(e.getExcepRequs().get(i).getExcepId().getExcepId()), i, 0);
							}
							this.setExcepRequs(ParserUtil.safeParseString(e.getExcepRequs().get(i).getExcepLevel()), i, 1);
							if(e.getExcepRequs().get(i).getExcepTrend()!=null){
								this.setExcepRequs(ParserUtil.safeParseString(e.getExcepRequs().get(i).getExcepTrend().getExcepTrend()), i, 2);
							}
						}
					}
				}
			}
			if(e.getRatFreqs()!=null){
				for(int i=0;i<e.getRatFreqs().size();i++){
					if(e.getRatFreqs().get(i)!=null){
						this.addRatFreqs(null);
						this.setRatFreqs(ParserUtil.safeParseString(e.getRatFreqs().get(i).isAllFreq()), i, 0);
						this.setRatFreqs(ParserUtil.safeParseString(e.getRatFreqs().get(i).isAllRat()), i, 1);
						this.setRatFreqs(ParserUtil.safeParseString(e.getRatFreqs().get(i).getFreq()), i, 2);
						if(e.getRatFreqs().get(i).getRatType()!=null){
							this.setRatFreqs(ParserUtil.safeParseString(e.getRatFreqs().get(i).getRatType().getRatType()), i, 3);
						}
						if(e.getRatFreqs().get(i).getMatchingDir()!=null){
								this.setRatFreqs(ParserUtil.safeParseString(e.getRatFreqs().get(i).getMatchingDir().getMatchingDir()), i, 4);
						}
						if(e.getRatFreqs().get(i).getSvcExpThreshold()!=null){
							this.ratFreqs.get(i).set(1,ParserUtil.safeParseListString(new ArrayList<>(Arrays.asList(e.getNfLoadLvlThds().get(i).getCongLevel(),
							e.getNfLoadLvlThds().get(i).getNfLoadLevel(),
							e.getNfLoadLvlThds().get(i).getNfCpuUsage(),
							e.getNfLoadLvlThds().get(i).getNfMemoryUsage(),
							e.getNfLoadLvlThds().get(i).getNfStorageUsage(),
							e.getNfLoadLvlThds().get(i).getAvgTrafficRate(),
							e.getNfLoadLvlThds().get(i).getMaxTrafficRate(),
							e.getNfLoadLvlThds().get(i).getAvgPacketDelay(),
							e.getNfLoadLvlThds().get(i).getMaxPacketDelay(),
							e.getNfLoadLvlThds().get(i).getAvgPacketLossRate(),
							e.getNfLoadLvlThds().get(i).getSvcExpLevel()
							))));
						}
					}
				}
			}
			if(e.getListOfAnaSubsets()!=null){
				for(int i=0;i<e.getListOfAnaSubsets().size();i++){
					if(e.getListOfAnaSubsets().get(i)!=null){
						this.ranUeThrouThds.add(ParserUtil.safeParseString(e.getListOfAnaSubsets().get(i).getAnaSubset()));
					}
				}
			}
			if(e.getDisperReqs()!=null){
				for(int i=0;i<e.getDisperReqs().size();i++){
					if(e.getDisperReqs().get(i)!=null){
						this.addDisperReqs(null);
						if(e.getDisperReqs().get(i).getDisperType()!=null){
							this.disperReqs.get(i).get(0).get(0).set(0,ParserUtil.safeParseString(e.getDisperReqs().get(i).getDisperType().getDisperType()));
						}
						if(e.getDisperReqs().get(i).getDispOrderCriter()!=null){
							this.disperReqs.get(i).get(0).get(0).set(1,ParserUtil.safeParseString(e.getDisperReqs().get(i).getDispOrderCriter().getDispOrderCriter()));
						}
						if(e.getDisperReqs().get(i).getOrder()!=null){
							this.disperReqs.get(i).get(0).get(0).set(2,ParserUtil.safeParseString(e.getDisperReqs().get(i).getOrder().getMatchingDir()));
						}
						if(e.getDisperReqs().get(i).getClassCriters()!=null){
							for(int j=0;j<e.getDisperReqs().get(i).getClassCriters().size();j++){
								if(e.getDisperReqs().get(i).getClassCriters().get(j)!=null){
									String disperClass=null,classThreshold=null,thresMatch=null;
									if(e.getDisperReqs().get(i).getClassCriters().get(j).getDisperClass()!=null){
										disperClass = ParserUtil.safeParseString(e.getDisperReqs().get(i).getClassCriters().get(j).getDisperClass().getDisperClass());
									}
									classThreshold = ParserUtil.safeParseString(e.getDisperReqs().get(i).getClassCriters().get(j).getClassThreshold());
									if(e.getDisperReqs().get(i).getClassCriters().get(j).getThresMatch()!=null){
										thresMatch = ParserUtil.safeParseString(e.getDisperReqs().get(i).getClassCriters().get(j).getThresMatch().getMatchingDir());
									}
									this.addDisperReqsItem(new ArrayList<>(Arrays.asList(disperClass,classThreshold,thresMatch)), i, 1);
								}
							}
							for(int j=0;j<e.getDisperReqs().get(i).getRankCriters().size();j++){
								if(e.getDisperReqs().get(i).getRankCriters().get(j)!=null){
									this.addDisperReqsItem(new ArrayList<>(Arrays.asList(ParserUtil.safeParseString(e.getDisperReqs().get(i).getRankCriters().get(j).getHighBase()),
										ParserUtil.safeParseString(e.getDisperReqs().get(i).getRankCriters().get(j).getLowBase()))), i, 2);
								}
							}
						}
					}
				}
			}
			if(e.getRedTransReqs()!=null){
				for(int i=0;i<e.getRedTransReqs().size();i++){
					if(e.getRedTransReqs().get(i)!=null){
						this.addRedTransReqs(null);
						if(e.getRedTransReqs().get(i).getRedTOrderCriter()!=null){
							this.setRedTransReqs(ParserUtil.safeParseString(e.getRedTransReqs().get(i).getRedTOrderCriter().getRedTOrderCriter()), i, 0);
						}
						if(e.getRedTransReqs().get(i).getOrder()!=null){
							this.setRedTransReqs(ParserUtil.safeParseString(e.getRedTransReqs().get(i).getOrder().getMatchingDir()), i, 1);
						}
					}		
				}
			}
			if(e.getWlanReqs()!=null){
				for(int i=0;i<e.getWlanReqs().size();i++){
					if(e.getWlanReqs().get(i)!=null){
						this.addWlanReqs(null);
						if(e.getWlanReqs().get(i).getWlanOrderCriter()!=null){
							this.setWlanReqs(ParserUtil.safeParseString(e.getWlanReqs().get(i).getWlanOrderCriter().getWlanOrderCriter()), i, 0);
						}
						if(e.getWlanReqs().get(i).getWlanOrderCriter()!=null){
							this.setWlanReqs(ParserUtil.safeParseString(e.getWlanReqs().get(i).getOrder().getMatchingDir()), i, 1);
						}
						this.wlanReqs.get(i).set(1,e.getWlanReqs().get(i).getSsIds());
						this.wlanReqs.get(i).set(2,e.getWlanReqs().get(i).getBssIds());
					}
				}
			}
			if(e.getAppServerAddrs()!=null){
				for(int i=0;i<e.getAppServerAddrs().size();i++){
					if(e.getAppServerAddrs().get(i)!=null){
						this.setAppServerAddrs(e.getAppServerAddrs().get(i).getIpAddr().getIpv4Addr(), i, 0);
						this.setAppServerAddrs(e.getAppServerAddrs().get(i).getIpAddr().getIpv6Addr(), i, 1);
						this.setAppServerAddrs(e.getAppServerAddrs().get(i).getIpAddr().getIpv6Prefix(), i, 2);
						this.setAppServerAddrs(e.getAppServerAddrs().get(i).getFqdn(), i, 3);
					}
				}
			}
			if(e.getDnPerfReqs()!=null){
				for(int i=0;i<e.getDnPerfReqs().size();i++){
					if(e.getDnPerfReqs().get(i)!=null){
						this.addDnPerfReqs(null);
						if(e.getDnPerfReqs().get(i).getDnPerfOrderCriter()!=null){
							this.dnPerfReqs.get(i).get(0).get(0).set(0,ParserUtil.safeParseString(e.getDnPerfReqs().get(i).getDnPerfOrderCriter().getDnPerfOrderCriter()));
						}
						if(e.getDnPerfReqs().get(i).getOrder()!=null){
							this.dnPerfReqs.get(i).get(0).get(0).set(1,ParserUtil.safeParseString(e.getDnPerfReqs().get(i).getOrder().getMatchingDir()));
						}
						if(e.getDnPerfReqs().get(i).getReportThresholds()!=null){
							for(int j=0;j<e.getDnPerfReqs().get(i).getReportThresholds().size();j++){
								this.addDnPerfReqsItem(ParserUtil.safeParseListString(new ArrayList<>(Arrays.asList(e.getDnPerfReqs().get(i).getReportThresholds().get(j).getCongLevel(),
							e.getDnPerfReqs().get(i).getReportThresholds().get(j).getNfLoadLevel(),
							e.getDnPerfReqs().get(i).getReportThresholds().get(j).getNfCpuUsage(),
							e.getDnPerfReqs().get(i).getReportThresholds().get(j).getNfMemoryUsage(),
							e.getDnPerfReqs().get(i).getReportThresholds().get(j).getNfStorageUsage(),
							e.getDnPerfReqs().get(i).getReportThresholds().get(j).getAvgTrafficRate(),
							e.getDnPerfReqs().get(i).getReportThresholds().get(j).getMaxTrafficRate(),
							e.getDnPerfReqs().get(i).getReportThresholds().get(j).getAvgPacketDelay(),
							e.getDnPerfReqs().get(i).getReportThresholds().get(j).getMaxPacketDelay(),
							e.getDnPerfReqs().get(i).getReportThresholds().get(j).getAvgPacketLossRate(),
							e.getDnPerfReqs().get(i).getReportThresholds().get(j).getSvcExpLevel()
							))), i, 1);
							}
						}
					}
				}
			}
			if(e.getNetworkArea()!=null){
				if(e.getNetworkArea().getEcgis()!=null){
				for(int j=0;j<e.getNetworkArea().getEcgis().size();j++){
					if(e.getNetworkArea().getEcgis().get(j)!=null){
						if(e.getNetworkArea().getEcgis().get(j).getPlmnId()!=null){
							this.addNetworkAreaItem(new ArrayList<>(Arrays.asList(e.getNetworkArea().getEcgis().get(j).getPlmnId().getMcc(),
							e.getNetworkArea().getEcgis().get(j).getPlmnId().getMnc(),
							e.getNetworkArea().getEcgis().get(j).getEutraCellId(),
							e.getNetworkArea().getEcgis().get(j).getNid())), 0);
						}
					}
				}
				}
				if(e.getNetworkArea().getNcgis()!=null){
				for(int j=0;j<e.getNetworkArea().getNcgis().size();j++){
					if(e.getNetworkArea().getNcgis().get(j)!=null){
						if(e.getNetworkArea().getNcgis().get(j).getPlmnId()!=null){
							this.addNetworkAreaItem(new ArrayList<>(Arrays.asList(e.getNetworkArea().getNcgis().get(j).getPlmnId().getMcc(),
							e.getNetworkArea().getNcgis().get(j).getPlmnId().getMnc(),
							e.getNetworkArea().getNcgis().get(j).getNrCellId(),
							e.getNetworkArea().getNcgis().get(j).getNid())), 1);
						}
					}
				}
				}
				if(e.getNetworkArea().getGRanNodeIds()!=null){
				for(int j=0;j<e.getNetworkArea().getGRanNodeIds().size();j++){
					if(e.getNetworkArea().getGRanNodeIds().get(j)!=null){
						String bitlength=null,gnbvalue=null,mcc=null,mnc=null;
						if(e.getNetworkArea().getGRanNodeIds().get(j).getGNbId()!=null){
							bitlength = ParserUtil.safeParseString(e.getNetworkArea().getGRanNodeIds().get(j).getGNbId().getBitLength());
							gnbvalue = ParserUtil.safeParseString(e.getNetworkArea().getGRanNodeIds().get(j).getGNbId().getGNBValue());
						}
						if(e.getNetworkArea().getGRanNodeIds().get(j).getPlmnId()!=null){
							mcc = e.getNetworkArea().getGRanNodeIds().get(j).getPlmnId().getMcc();
							mnc = e.getNetworkArea().getGRanNodeIds().get(j).getPlmnId().getMnc();
						}
						this.addNetworkAreaItem(new ArrayList<>(Arrays.asList(mcc,mnc,
						e.getNetworkArea().getGRanNodeIds().get(j).getN3IwfId(),
						e.getNetworkArea().getGRanNodeIds().get(j).getNgeNbId(),
						e.getNetworkArea().getGRanNodeIds().get(j).getWagfId(),
						e.getNetworkArea().getGRanNodeIds().get(j).getTngfId(),
						e.getNetworkArea().getGRanNodeIds().get(j).getNid(),
						e.getNetworkArea().getGRanNodeIds().get(j).getENbId(),
						bitlength, gnbvalue)), 2);
					}
				}
				}
				if(e.getNetworkArea().getTais()!=null){
				for(int j=0;j<e.getNetworkArea().getTais().size();j++){
					if(e.getNetworkArea().getTais().get(j)!=null){
						if(e.getNetworkArea().getTais().get(j).getPlmnId()!=null){
							this.addNetworkAreaItem(new ArrayList<>(Arrays.asList(e.getNetworkArea().getTais().get(j).getPlmnId().getMcc(),
							e.getNetworkArea().getTais().get(j).getPlmnId().getMnc(),
							e.getNetworkArea().getTais().get(j).getTac(),
							e.getNetworkArea().getTais().get(j).getNid())), 3);
						}
					}
				}
				}				
			}
			if(e.getQosRequ()!=null){
				this.setQosRequ(ParserUtil.safeParseString(e.getQosRequ().get5qi()), 0);
				this.setQosRequ(ParserUtil.safeParseString(e.getQosRequ().getGfbrUl()), 1);
				this.setQosRequ(ParserUtil.safeParseString(e.getQosRequ().getGfbrDl()), 2);
				if(e.getQosRequ().getResType()!=null){
					this.setQosRequ(ParserUtil.safeParseString(e.getQosRequ().getResType().getResType()), 3);
				}
				this.setQosRequ(ParserUtil.safeParseString(e.getQosRequ().getPdb()), 4);
				this.setQosRequ(ParserUtil.safeParseString(e.getQosRequ().getPer()), 5);
			}
			if(e.getExptUeBehav()!=null){
				this.initExptUeBehav();
				if(e.getExptUeBehav().getStationaryIndication()!=null){
					this.exptUeBehav.get(0).get(0).get(0).get(0).get(0).set(0,ParserUtil.safeParseString(e.getExptUeBehav().getStationaryIndication().getStationaryIndication()));
				}
				this.exptUeBehav.get(0).get(0).get(0).get(0).get(0).set(1,ParserUtil.safeParseString(e.getExptUeBehav().getCommunicationDurationTime()));
				this.exptUeBehav.get(0).get(0).get(0).get(0).get(0).set(2,ParserUtil.safeParseString(e.getExptUeBehav().getPeriodicTime()));
				if(e.getExptUeBehav().getScheduledCommunicationType()!=null){
					this.exptUeBehav.get(0).get(0).get(0).get(0).get(0).set(3,ParserUtil.safeParseString(e.getExptUeBehav().getScheduledCommunicationType().getScheduledCommunicationType()));
				}
				if(e.getExptUeBehav().getTrafficProfile()!=null){
					this.exptUeBehav.get(0).get(0).get(0).get(0).get(0).set(4,ParserUtil.safeParseString(e.getExptUeBehav().getTrafficProfile().getTrafficProfile()));
				}
				this.exptUeBehav.get(0).get(0).get(0).get(0).get(0).set(5,ParserUtil.safeParseString(e.getExptUeBehav().getValidityTime()));
				if(e.getExptUeBehav().getScheduledCommunicationTime()!=null){
					if(e.getExptUeBehav().getScheduledCommunicationTime().getDaysOfWeek()!=null){
						for(int i=0;i<e.getExptUeBehav().getScheduledCommunicationTime().getDaysOfWeek().size();i++){
							this.exptUeBehav.get(1).get(0).get(0).get(0).get(0).add(ParserUtil.safeParseString(e.getExptUeBehav().getScheduledCommunicationTime().getDaysOfWeek().get(i)));
						}
					}
					this.exptUeBehav.get(1).get(0).get(0).get(0).get(1).set(0,e.getExptUeBehav().getScheduledCommunicationTime().getTimeOfDayStart());
					this.exptUeBehav.get(1).get(0).get(0).get(0).get(1).set(1,e.getExptUeBehav().getScheduledCommunicationTime().getTimeOfDayEnd());
				}
				this.exptUeBehav.get(2).get(0).get(0).get(0).get(0).set(0,ParserUtil.safeParseString(e.getExptUeBehav().getBatteryIndication().isBatteryInd()));
				this.exptUeBehav.get(2).get(0).get(0).get(0).get(0).set(1,ParserUtil.safeParseString(e.getExptUeBehav().getBatteryIndication().isReplaceableInd()));
				this.exptUeBehav.get(2).get(0).get(0).get(0).get(0).set(2,ParserUtil.safeParseString(e.getExptUeBehav().getBatteryIndication().isRechargeableInd()));
				if(e.getExptUeBehav().getExpectedUmts()!=null){
					for(int i=0;i<e.getExptUeBehav().getExpectedUmts().size();i++){
						if(e.getExptUeBehav().getExpectedUmts().get(i)!=null){
							if(e.getExptUeBehav().getExpectedUmts().get(i)!=null){
								if(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo()!=null){
									if(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getEcgis()!=null){
									for(int j=0;j<e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getEcgis().size();j++){
										if(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getEcgis().get(j)!=null){
											if(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getEcgis().get(j).getPlmnId()!=null){
												this.addExptUeBehavItem(new ArrayList<>(Arrays.asList(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getEcgis().get(j).getPlmnId().getMcc(),
												e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getEcgis().get(j).getPlmnId().getMnc(),
												e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getEcgis().get(j).getEutraCellId(),
												e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getEcgis().get(j).getNid())), i, 0);
											}
										}
									}
									}
									if(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getNcgis()!=null){
									for(int j=0;j<e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getNcgis().size();j++){
										if(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getNcgis().get(j)!=null){
											if(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getNcgis().get(j).getPlmnId()!=null){
												this.addExptUeBehavItem(new ArrayList<>(Arrays.asList(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getNcgis().get(j).getPlmnId().getMcc(),
												e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getNcgis().get(j).getPlmnId().getMnc(),
												e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getNcgis().get(j).getNrCellId(),
												e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getNcgis().get(j).getNid())), i, 1);
											}
										}
									}
									}
									if(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds()!=null){
									for(int j=0;j<e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds().size();j++){
										if(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds().get(j)!=null){
											String bitlength=null,gnbvalue=null,mcc=null,mnc=null;
											if(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds().get(j).getGNbId()!=null){
												bitlength = ParserUtil.safeParseString(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds().get(j).getGNbId().getBitLength());
												gnbvalue = ParserUtil.safeParseString(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds().get(j).getGNbId().getGNBValue());
											}
											if(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds().get(j).getPlmnId()!=null){
												mcc = e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds().get(j).getPlmnId().getMcc();
												mnc = e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds().get(j).getPlmnId().getMnc();
											}
											this.addExptUeBehavItem(new ArrayList<>(Arrays.asList(mcc,mnc,
											e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds().get(j).getN3IwfId(),
											e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds().get(j).getNgeNbId(),
											e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds().get(j).getWagfId(),
											e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds().get(j).getTngfId(),
											e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds().get(j).getNid(),
											e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getGRanNodeIds().get(j).getENbId(),
											bitlength, gnbvalue)), i, 2);
										}
									}
									}
									if(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getTais()!=null){
									for(int j=0;j<e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getTais().size();j++){
										if(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getTais().get(j)!=null){
											if(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getTais().get(j).getPlmnId()!=null){
												this.addExptUeBehavItem(new ArrayList<>(Arrays.asList(e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getTais().get(j).getPlmnId().getMcc(),
												e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getTais().get(j).getPlmnId().getMnc(),
												e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getTais().get(j).getTac(),
												e.getExptUeBehav().getExpectedUmts().get(i).getNwAreaInfo().getTais().get(j).getNid())), i, 3);
											}
										}
									}
									}
								}
								if(e.getExptUeBehav().getExpectedUmts().get(i).getUmtTime()!=null){
									this.exptUeBehav.get(3).get(i).get(1).get(0).get(0).set(0,e.getExptUeBehav().getExpectedUmts().get(i).getUmtTime().getTimeOfDay());
									this.exptUeBehav.get(3).get(i).get(1).get(0).get(0).set(0,ParserUtil.safeParseString(e.getExptUeBehav().getExpectedUmts().get(i).getUmtTime().getDayOfWeek()));
								}
								if(e.getExptUeBehav().getExpectedUmts().get(i).getGeographicAreas()!=null){
									for(int j=0;j<e.getExptUeBehav().getExpectedUmts().get(i).getGeographicAreas().size();j++){
										GeographicArea g = e.getExptUeBehav().getExpectedUmts().get(i).getGeographicAreas().get(j);
										if(g!=null && g.getType()!=null){
											this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(0,g.getType());
											if(g.getType().equals("Point") && ((Point) g).getPoint()!=null){
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(1,ParserUtil.safeParseString(((Point) g).getPoint().getLon()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(2,ParserUtil.safeParseString(((Point) g).getPoint().getLat()));
											}
											else if(g.getType().equals("PointUncertaintyCircle") && ((PointUncertaintyCircle) g).getPoint()!=null){
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(1,ParserUtil.safeParseString(((PointUncertaintyCircle) g).getPoint().getLon()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(2,ParserUtil.safeParseString(((PointUncertaintyCircle) g).getPoint().getLat()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(3,ParserUtil.safeParseString(((PointUncertaintyCircle) g).getUncertainty()));
											
											}
											else if(g.getType().equals("PointUncertaintyEllipse") && ((PointUncertaintyEllipse) g).getPoint()!=null){
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(1,ParserUtil.safeParseString(((PointUncertaintyEllipse) g).getPoint().getLon()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(2,ParserUtil.safeParseString(((PointUncertaintyEllipse) g).getPoint().getLat()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(3,ParserUtil.safeParseString(((PointUncertaintyEllipse) g).getConfidence()));
												if(((PointUncertaintyEllipse) g).getUncertaintyEllipse()!=null){
													this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(4,ParserUtil.safeParseString(((PointUncertaintyEllipse) g).getUncertaintyEllipse().getOrientationMajor()));
													this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(5,ParserUtil.safeParseString(((PointUncertaintyEllipse) g).getUncertaintyEllipse().getSemiMajor()));
													this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(6,ParserUtil.safeParseString(((PointUncertaintyEllipse) g).getUncertaintyEllipse().getSemiMinor()));
												}
											}
											else if(g.getType().equals("Polygon") && ((Polygon) g).getPointList()!=null){
												if(((Polygon) g).getPointList()!=null){
													for(int k=0;k<((Polygon) g).getPointList().size();k++){
														if(((Polygon) g).getPointList().get(k)!=null){
														this.exptUeBehav.get(3).get(i).get(2).get(j).get(1).add(ParserUtil.safeParseString(((Polygon) g).getPointList().get(k).getLon()));
														this.exptUeBehav.get(3).get(i).get(2).get(j).get(2).add(ParserUtil.safeParseString(((Polygon) g).getPointList().get(k).getLat()));
														}
													}
												}
											}
											else if(g.getType().equals("PointAltitude") && ((PointAltitude) g).getPoint()!=null){
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(1,ParserUtil.safeParseString(((PointAltitude) g).getPoint().getLon()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(2,ParserUtil.safeParseString(((PointAltitude) g).getPoint().getLat()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(3,ParserUtil.safeParseString(((PointAltitude) g).getAltitude()));
											}
											else if(g.getType().equals("PointAltitudeUncertainty") && ((PointAltitudeUncertainty) g).getPoint()!=null){
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(1,ParserUtil.safeParseString(((PointAltitudeUncertainty) g).getPoint().getLon()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(2,ParserUtil.safeParseString(((PointAltitudeUncertainty) g).getPoint().getLat()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(3,ParserUtil.safeParseString(((PointAltitudeUncertainty) g).getConfidence()));
												if(((PointAltitudeUncertainty) g).getUncertaintyEllipse()!=null){
													this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(4,ParserUtil.safeParseString(((PointAltitudeUncertainty) g).getUncertaintyEllipse().getOrientationMajor()));
													this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(5,ParserUtil.safeParseString(((PointAltitudeUncertainty) g).getUncertaintyEllipse().getSemiMajor()));
													this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(6,ParserUtil.safeParseString(((PointAltitudeUncertainty) g).getUncertaintyEllipse().getSemiMinor()));
												}
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(7,ParserUtil.safeParseString(((PointAltitudeUncertainty) g).getAltitude()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(8,ParserUtil.safeParseString(((PointAltitudeUncertainty) g).getUncertaintyAltitude()));
											}
											else if(g.getType().equals("EllipsoidArc") && ((EllipsoidArc) g).getPoint()!=null){
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(1,ParserUtil.safeParseString(((EllipsoidArc) g).getPoint().getLon()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(2,ParserUtil.safeParseString(((EllipsoidArc) g).getPoint().getLat()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(3,ParserUtil.safeParseString(((EllipsoidArc) g).getConfidence()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(4,ParserUtil.safeParseString(((EllipsoidArc) g).getInnerRadius()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(5,ParserUtil.safeParseString(((EllipsoidArc) g).getUncertaintyRadius()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(6,ParserUtil.safeParseString(((EllipsoidArc) g).getOffsetAngle()));
												this.exptUeBehav.get(3).get(i).get(2).get(j).get(0).set(7,ParserUtil.safeParseString(((EllipsoidArc) g).getIncludedAngle()));
											}
										}
									}
								}
								if(e.getExptUeBehav().getExpectedUmts().get(i).getCivicAddresses()!=null){
									for(int j=0;j<e.getExptUeBehav().getExpectedUmts().get(i).getCivicAddresses().size();j++){
										CivicAddress c = e.getExptUeBehav().getExpectedUmts().get(i).getCivicAddresses().get(j);
										if(c!=null){
											this.exptUeBehav.get(3).get(i).get(3).get(0).set(j,new ArrayList<>(Arrays.asList(
											c.getCountry(),
											c.getA1(),
											c.getA2(),
											c.getA3(),
											c.getA4(),
											c.getA5(),
											c.getA6(),
											c.getPRD(),
											c.getPOD(),
											c.getSTS(),
											c.getHNO(),
											c.getHNS(),
											c.getLMK(),
											c.getLOC(),
											c.getNAM(),
											c.getPC(),
											c.getBLD(),
											c.getUNIT(),
											c.getFLR(),
											c.getROOM(),
											c.getPLC(),
											c.getPCN(),
											c.getPOBOX(),
											c.getADDCODE(),
											c.getSEAT(),
											c.getRD(),
											c.getRDSEC(),
											c.getRDBR(),
											c.getRDSUBBR(),
											c.getPRM(),
											c.getPOM(),
											c.getUsageRules(),
											c.getMethod(),
											c.getProvidedBy()
											)));
										}
									}
								}
							}							
						}
					}
				}
				
			}
			if(e.getUpfInfo()!=null){
				this.setUpfInfo(e.getUpfInfo().getUpfId(), 0);
				if(e.getUpfInfo().getUpfAddr()!=null){
					if(e.getUpfInfo().getUpfAddr().getIpAddr()!=null){
						this.setUpfInfo(e.getUpfInfo().getUpfAddr().getIpAddr().getIpv4Addr(),1);
						this.setUpfInfo(e.getUpfInfo().getUpfAddr().getIpAddr().getIpv6Addr(), 2);
						this.setUpfInfo(e.getUpfInfo().getUpfAddr().getIpAddr().getIpv6Prefix(), 3);
					}
					if(e.getUpfInfo().getUpfAddr().getFqdn()!=null){
						this.setUpfInfo(e.getUpfInfo().getUpfAddr().getFqdn(), 4);
					}
				}
			}
			this.anySlice = e.isAnySlice();
			this.loadLevelThreshold = e.getLoadLevelThreshold();
			if(e.getMatchingDir()!=null){
				this.matchingDir = e.getMatchingDir().getMatchingDir();
			}
			this.maxTopAppUlNbr = e.getMaxTopAppUlNbr();
			this.maxTopAppDlNbr = e.getMaxTopAppDlNbr();
			this.repetitionPeriod = e.getRepetitionPeriod();
			if(e.getExptAnaType()!=null){
				this.exptAnaType = e.getExptAnaType().getExptAnaType();
			}
		}
        return this;
    }
	
	
}
