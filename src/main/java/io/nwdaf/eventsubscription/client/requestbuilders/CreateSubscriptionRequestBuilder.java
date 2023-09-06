package io.nwdaf.eventsubscription.client.requestbuilders;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.nwdaf.eventsubscription.model.Accuracy;
import io.nwdaf.eventsubscription.model.Accuracy.AccuracyEnum;
import io.nwdaf.eventsubscription.model.AddrFqdn;
import io.nwdaf.eventsubscription.model.AnalyticsMetadata;
import io.nwdaf.eventsubscription.model.AnalyticsMetadata.AnalyticsMetadataEnum;
import io.nwdaf.eventsubscription.model.AnalyticsMetadataIndication;
import io.nwdaf.eventsubscription.model.AnalyticsSubset;
import io.nwdaf.eventsubscription.model.AnalyticsSubset.AnalyticsSubsetEnum;
import io.nwdaf.eventsubscription.model.BatteryIndication;
import io.nwdaf.eventsubscription.model.BwRequirement;
import io.nwdaf.eventsubscription.model.CivicAddress;
import io.nwdaf.eventsubscription.model.ClassCriterion;
import io.nwdaf.eventsubscription.model.ConsumerNfInformation;
import io.nwdaf.eventsubscription.model.DatasetStatisticalProperty;
import io.nwdaf.eventsubscription.model.DatasetStatisticalProperty.DatasetStatisticalPropertyEnum;
import io.nwdaf.eventsubscription.model.DispersionClass;
import io.nwdaf.eventsubscription.model.DispersionClass.DispersionClassEnum;
import io.nwdaf.eventsubscription.model.DispersionOrderingCriterion;
import io.nwdaf.eventsubscription.model.DispersionOrderingCriterion.DispersionOrderingCriterionEnum;
import io.nwdaf.eventsubscription.model.DispersionRequirement;
import io.nwdaf.eventsubscription.model.DispersionType;
import io.nwdaf.eventsubscription.model.DispersionType.DispersionTypeEnum;
import io.nwdaf.eventsubscription.model.DnPerfOrderingCriterion;
import io.nwdaf.eventsubscription.model.DnPerfOrderingCriterion.DnPerfOrderingCriterionEnum;
import io.nwdaf.eventsubscription.model.DnPerformanceReq;
import io.nwdaf.eventsubscription.model.Ecgi;
import io.nwdaf.eventsubscription.model.EllipsoidArc;
import io.nwdaf.eventsubscription.model.EventReportingRequirement;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.ExceptionId;
import io.nwdaf.eventsubscription.model.ExceptionId.ExceptionIdEnum;
import io.nwdaf.eventsubscription.model.ExceptionTrend;
import io.nwdaf.eventsubscription.model.ExceptionTrend.ExceptionTrendEnum;
import io.nwdaf.eventsubscription.model.ExpectedAnalyticsType;
import io.nwdaf.eventsubscription.model.ExpectedAnalyticsType.ExpectedAnalyticsTypeEnum;
import io.nwdaf.eventsubscription.model.ExpectedUeBehaviourData;
import io.nwdaf.eventsubscription.model.GNbId;
import io.nwdaf.eventsubscription.model.GeographicalCoordinates;
import io.nwdaf.eventsubscription.model.GlobalRanNodeId;
import io.nwdaf.eventsubscription.model.IpAddr;
import io.nwdaf.eventsubscription.model.LocationArea;
import io.nwdaf.eventsubscription.model.MatchingDirection;
import io.nwdaf.eventsubscription.model.MatchingDirection.MatchingDirectionEnum;
import io.nwdaf.eventsubscription.model.NFType;
import io.nwdaf.eventsubscription.model.NFType.NFTypeEnum;
import io.nwdaf.eventsubscription.model.Ncgi;
import io.nwdaf.eventsubscription.model.NetworkAreaInfo;
import io.nwdaf.eventsubscription.model.NetworkPerfRequirement;
import io.nwdaf.eventsubscription.model.NetworkPerfType;
import io.nwdaf.eventsubscription.model.NetworkPerfType.NetworkPerfTypeEnum;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NotificationFlag;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.model.NsiIdInfo;
import io.nwdaf.eventsubscription.model.NwdafEvent;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.model.OutputStrategy;
import io.nwdaf.eventsubscription.model.OutputStrategy.OutputStrategyEnum;
import io.nwdaf.eventsubscription.model.PartitioningCriteria;
import io.nwdaf.eventsubscription.model.PartitioningCriteria.PartitioningCriteriaEnum;
import io.nwdaf.eventsubscription.model.PlmnId;
import io.nwdaf.eventsubscription.model.Point;
import io.nwdaf.eventsubscription.model.PointAltitude;
import io.nwdaf.eventsubscription.model.PointAltitudeUncertainty;
import io.nwdaf.eventsubscription.model.PointList;
import io.nwdaf.eventsubscription.model.PointUncertaintyCircle;
import io.nwdaf.eventsubscription.model.PointUncertaintyEllipse;
import io.nwdaf.eventsubscription.model.Polygon;
import io.nwdaf.eventsubscription.model.PrevSubInfo;
import io.nwdaf.eventsubscription.model.QosRequirement;
import io.nwdaf.eventsubscription.model.QosResourceType;
import io.nwdaf.eventsubscription.model.QosResourceType.QosResourceTypeEnum;
import io.nwdaf.eventsubscription.model.RankingCriterion;
import io.nwdaf.eventsubscription.model.RatFreqInformation;
import io.nwdaf.eventsubscription.model.RatType;
import io.nwdaf.eventsubscription.model.RatType.RatTypeEnum;
import io.nwdaf.eventsubscription.model.RedTransExpOrderingCriterion;
import io.nwdaf.eventsubscription.model.RedTransExpOrderingCriterion.RedTransExpOrderingCriterionEnum;
import io.nwdaf.eventsubscription.model.RedundantTransmissionExpReq;
import io.nwdaf.eventsubscription.model.ReportingInformation;
import io.nwdaf.eventsubscription.model.RetainabilityThreshold;
import io.nwdaf.eventsubscription.model.ScheduledCommunicationTime1;
import io.nwdaf.eventsubscription.model.ScheduledCommunicationType;
import io.nwdaf.eventsubscription.model.ScheduledCommunicationType.ScheduledCommunicationTypeEnum;
import io.nwdaf.eventsubscription.model.Snssai;
import io.nwdaf.eventsubscription.model.StationaryIndication;
import io.nwdaf.eventsubscription.model.StationaryIndication.StationaryIndicationEnum;
import io.nwdaf.eventsubscription.model.SupportedGADShapes;
import io.nwdaf.eventsubscription.model.SupportedGADShapes.SupportedGADShapesEnum;
import io.nwdaf.eventsubscription.model.Tai;
import io.nwdaf.eventsubscription.model.TargetUeInformation;
import io.nwdaf.eventsubscription.model.ThresholdLevel;
import io.nwdaf.eventsubscription.model.TimeUnit;
import io.nwdaf.eventsubscription.model.TimeUnit.TimeUnitEnum;
import io.nwdaf.eventsubscription.model.TimeWindow;
import io.nwdaf.eventsubscription.model.TrafficProfile;
import io.nwdaf.eventsubscription.model.TrafficProfile.TrafficProfileEnum;
import io.nwdaf.eventsubscription.model.UeAnalyticsContextDescriptor;
import io.nwdaf.eventsubscription.model.UmtTime;
import io.nwdaf.eventsubscription.model.UncertaintyEllipse;
import io.nwdaf.eventsubscription.model.UpfInformation;
import io.nwdaf.eventsubscription.model.WlanOrderingCriterion;
import io.nwdaf.eventsubscription.model.WlanOrderingCriterion.WlanOrderingCriterionEnum;
import io.nwdaf.eventsubscription.utilities.CheckUtil;
import io.nwdaf.eventsubscription.utilities.ParserUtil;
import io.nwdaf.eventsubscription.model.WlanPerformanceReq;
import io.nwdaf.eventsubscription.model.Exception;


public class CreateSubscriptionRequestBuilder {
	

	public NnwdafEventsSubscription InitSubscriptionRequest(String clientURI) {
		NnwdafEventsSubscription bodyObject = new NnwdafEventsSubscription();
		bodyObject.notificationURI(clientURI);
		
		return bodyObject;
	}

	public NnwdafEventsSubscription AddEventToSubscription(NnwdafEventsSubscription sub,RequestEventModel object) {
		EventSubscription eventSub = new EventSubscription();
		if(object==null) {
			return sub.addEventSubscriptionsItem(eventSub);
		}
		eventSub.event(new NwdafEvent().event(NwdafEventEnum.fromValue(object.getEvent())));
		eventSub.notificationMethod(new NotificationMethod().notifMethod(NotificationMethodEnum.fromValue(object.getNotificationMethod())));
		if(object.getMatchingDir()!=null){
			eventSub.matchingDir(new MatchingDirection().matchingDir(object.getMatchingDir()));
		}
		EventReportingRequirement extraRepReq = new EventReportingRequirement();
		if(object.getOptionals().size()>0) {
			extraRepReq.maxObjectNbr(ParserUtil.safeParseInteger(object.getOptionals().get(0)));
			extraRepReq.maxSupiNbr(ParserUtil.safeParseInteger(object.getOptionals().get(1)));
			extraRepReq.startTs(ParserUtil.safeParseOffsetDateTime(object.getOptionals().get(2)));
			extraRepReq.endTs(ParserUtil.safeParseOffsetDateTime(object.getOptionals().get(3)));
			extraRepReq.accuracy(new Accuracy().accuracy(AccuracyEnum.fromValue(object.getOptionals().get(4))));
			extraRepReq.timeAnaNeeded(ParserUtil.safeParseOffsetDateTime(object.getOptionals().get(5)));
			extraRepReq.offsetPeriod(ParserUtil.safeParseInteger(object.getOptionals().get(6)));
		}
		if(object.getAnaMeta().size()>0) {
			List<AnalyticsMetadata> l = new ArrayList<AnalyticsMetadata>();
			for(int i =0;i<object.getAnaMeta().size();i++) {
				l.add(new AnalyticsMetadata().anaMeta(AnalyticsMetadataEnum.fromValue(object.getAnaMeta().get(i))));
			}
			extraRepReq.anaMeta(l);
		}
		if(object.getAnaMetaInd().size()>0) {
			AnalyticsMetadataIndication anametaind = new AnalyticsMetadataIndication();
			TimeWindow tw = new TimeWindow();
			tw.startTime(ParserUtil.safeParseOffsetDateTime(object.getAnaMetaInd().get(0) + ZonedDateTime.now().getOffset().getId()));
			tw.stopTime(ParserUtil.safeParseOffsetDateTime(object.getAnaMetaInd().get(1) + ZonedDateTime.now().getOffset().getId()));
			if((object.getAnaMetaInd().get(0)!=null)||(object.getAnaMetaInd().get(1)!=null)) {
				anametaind.dataWindow(tw);
			}
			
			if(object.getDataStatProps().size()>0) {
				for(int i=0;i<object.getDataStatProps().size();i++) {
					anametaind.addDataStatPropsItem(new DatasetStatisticalProperty().dataStatProps(DatasetStatisticalPropertyEnum.fromValue(object.getDataStatProps().get(i))));
				}
			}
			anametaind.strategy(new OutputStrategy().strategy(OutputStrategyEnum.fromValue(object.getAnaMetaInd().get(2))));
			if(object.getAggrNwdafIds()!=null) {
				for(int i=0;i<object.getAggrNwdafIds().size();i++){
					anametaind.addAggrNwdafIdsItem(ParserUtil.safeParseUUID(object.getAggrNwdafIds().get(i)));
				}
			}
			extraRepReq.anaMetaInd(anametaind);
		}
		if(object.getAccPerSubset().size()>0) {
			for(int i=0;i<object.getAccPerSubset().size();i++) {
				extraRepReq.addAccPerSubsetItem(new Accuracy().accuracy(AccuracyEnum.fromValue(object.getAccPerSubset().get(i))));
			}
		}
		if(object.getOptionals().size()>0 || object.getAnaMeta().size()>0 || object.getAnaMetaInd().size()>0 || object.getAccPerSubset().size()>0) {
			eventSub.extraReportReq(extraRepReq);
		}
		TargetUeInformation tgtUe = new TargetUeInformation();
		if(object.getSupis().size()>0||object.getIntGroupIds().size()>0||object.getGpsis().size()>0) {
			tgtUe.supis(object.getSupis()).intGroupIds(object.getIntGroupIds()).gpsis(object.getGpsis());
		}

		tgtUe.anyUe(ParserUtil.safeParseBoolean(object.getArgs().get(0)));
		if(object.getSupis().size()>0||object.getIntGroupIds().size()>0||object.getArgs().get(0)!=null||object.getGpsis().size()>0) {
			eventSub.tgtUe(tgtUe);
		}
		if(object.getNfInstanceIds().size()>0) {
			for(int i=0;i<object.getNfInstanceIds().size();i++) {
				eventSub.addNfInstanceIdsItem(ParserUtil.safeParseUUID(object.getNfInstanceIds().get(i)));
			}
		}
		if(object.getNfSetIds().size()>0) {
			eventSub.nfSetIds(object.getNfSetIds());
		}
		if(object.getAppIds().size()>0) {
			eventSub.appIds(object.getAppIds());
		}
		if(object.getDnns().size()>0) {
			eventSub.dnns(object.getDnns());
		}
		if(object.getDnais().size()>0) {
			eventSub.dnais(object.getDnais());
		}
		if(object.getLadnDnns().size()>0) {
			eventSub.ladnDnns(object.getLadnDnns());
		}
		if(object.getNfTypes().size()>0) {
			for(int i=0;i<object.getNfTypes().size();i++) {
				eventSub.addNfTypesItem(new NFType().nfType(NFTypeEnum.fromValue(object.getNfTypes().get(i))));
			}
		}
		if(object.getNfLoadLvlThds().size()>0) {
			for(int i=0;i<object.getNfLoadLvlThds().size();i++) {
				eventSub.addNfLoadLvlThdsItem(new ThresholdLevel().congLevel(ParserUtil.safeParseInteger(object.getNfLoadLvlThds().get(i).get(0))).nfLoadLevel(ParserUtil.safeParseInteger(object.getNfLoadLvlThds().get(i).get(1))).nfCpuUsage(ParserUtil.safeParseInteger(object.getNfLoadLvlThds().get(i).get(2))).nfMemoryUsage(ParserUtil.safeParseInteger(object.getNfLoadLvlThds().get(i).get(3))).nfStorageUsage(ParserUtil.safeParseInteger(object.getNfLoadLvlThds().get(i).get(4))).avgTrafficRate(object.getNfLoadLvlThds().get(i).get(5)).maxTrafficRate(object.getNfLoadLvlThds().get(i).get(6)).avgPacketDelay(ParserUtil.safeParseInteger(object.getNfLoadLvlThds().get(i).get(7))).maxPacketDelay(ParserUtil.safeParseInteger(object.getNfLoadLvlThds().get(i).get(8))).avgPacketLossRate(ParserUtil.safeParseInteger(object.getNfLoadLvlThds().get(i).get(9))).svcExpLevel(ParserUtil.safeParseFloat(object.getNfLoadLvlThds().get(i).get(10))));
			}
			
		}
		if(object.getVisitedAreas().size()>0) {
			for(int n=0;n<object.getVisitedAreas().size();n++) {
				NetworkAreaInfo area = new NetworkAreaInfo();
				for(int i=0;i<object.getVisitedAreas().get(n).get(0).size();i++) {
					Ecgi ecgi = new Ecgi();
					if(object.getVisitedAreas().get(n).get(0).get(i).get(0)!=null&&object.getVisitedAreas().get(n).get(0).get(i).get(1)!=null) {
						ecgi.plmnId(new PlmnId().mcc(object.getVisitedAreas().get(n).get(0).get(i).get(0)).mnc(object.getVisitedAreas().get(n).get(0).get(i).get(1)));
					}
					ecgi.eutraCellId(object.getVisitedAreas().get(n).get(0).get(i).get(2)).nid(object.getVisitedAreas().get(n).get(0).get(i).get(3));
					area.addEcgisItem(ecgi);
				}
				
				for(int i=0;i<object.getVisitedAreas().get(n).get(1).size();i++) {
					Ncgi ncgi = new Ncgi();
					if(object.getVisitedAreas().get(n).get(1).get(i).get(1)!=null&&object.getVisitedAreas().get(n).get(1).get(i).get(1)!=null) {
						ncgi.plmnId(new PlmnId().mcc(object.getVisitedAreas().get(n).get(1).get(i).get(0)).mnc(object.getVisitedAreas().get(n).get(1).get(i).get(1)));
					}
					ncgi.nrCellId(object.getVisitedAreas().get(n).get(1).get(i).get(2)).nid(object.getVisitedAreas().get(n).get(1).get(i).get(3));
					area.addNcgisItem(ncgi);
				}
				
				for(int i=0;i<object.getVisitedAreas().get(n).get(2).size();i++) {
					GlobalRanNodeId gRanNodeId = new GlobalRanNodeId();
					if(object.getVisitedAreas().get(n).get(2).get(i).get(1)!=null&&object.getVisitedAreas().get(n).get(2).get(i).get(1)!=null) {
						gRanNodeId.plmnId(new PlmnId().mcc(object.getVisitedAreas().get(n).get(2).get(i).get(0)).mnc(object.getVisitedAreas().get(n).get(2).get(i).get(1)));
					}
					gRanNodeId.n3IwfId(object.getVisitedAreas().get(n).get(2).get(i).get(2)).ngeNbId(object.getVisitedAreas().get(n).get(2).get(i).get(3))
																		.wagfId(object.getVisitedAreas().get(n).get(2).get(i).get(4))
																		.tngfId(object.getVisitedAreas().get(n).get(2).get(i).get(5))
																		.nid(object.getVisitedAreas().get(n).get(2).get(i).get(6))
																		.eNbId(object.getVisitedAreas().get(n).get(2).get(i).get(7));
					if(object.getVisitedAreas().get(n).get(2).get(i).get(8)!=null&&object.getVisitedAreas().get(n).get(2).get(i).get(9)!=null) {
						gRanNodeId.gNbId(new GNbId().bitLength(ParserUtil.safeParseInteger(object.getVisitedAreas().get(n).get(2).get(i).get(8))).gNBValue(object.getVisitedAreas().get(n).get(2).get(i).get(9)));
					}
					area.addGRanNodeIdsItem(gRanNodeId);
				}
				
				for(int i=0;i<object.getVisitedAreas().get(n).get(3).size();i++) {
					Tai tai = new Tai();
					if(object.getVisitedAreas().get(n).get(3).get(i).get(1)!=null&&object.getVisitedAreas().get(n).get(3).get(i).get(1)!=null) {
						tai.plmnId(new PlmnId().mcc(object.getVisitedAreas().get(n).get(3).get(i).get(0)).mnc(object.getVisitedAreas().get(n).get(3).get(i).get(1)));
					}
					tai.tac(object.getVisitedAreas().get(n).get(3).get(i).get(2)).nid(object.getVisitedAreas().get(n).get(3).get(i).get(3));
					area.addTaisItem(tai);
				}
				eventSub.addVisitedAreasItem(area);
			}
		}
		if(object.getNsiIdInfos().size()>0) {
			for(int n=0;n<object.getNsiIdInfos().size();n++) {
				NsiIdInfo nsiIdInfo = new NsiIdInfo();
				nsiIdInfo.snssai(new Snssai().sst(ParserUtil.safeParseInteger(object.getNsiIdInfos().get(n).get(0).get(0))).sd(object.getNsiIdInfos().get(n).get(0).get(1)));
				nsiIdInfo.nsiIds(object.getNsiIdInfos().get(n).get(1));
				eventSub.addNsiIdInfosItem(nsiIdInfo);
			}
		}
		if(object.getNsiLevelThrds().size()>0) {
			eventSub.nsiLevelThrds(object.getNsiLevelThrds());
		}
		if(object.getQosFlowRetThds().size()>0) {
			for(int i=0;i<object.getQosFlowRetThds().size();i++) {
				if(object.getQosFlowRetThds().get(i).get(0)!=null&&object.getQosFlowRetThds().get(i).get(1)!=null&&object.getQosFlowRetThds().get(i).get(2)!=null) {
					eventSub.addQosFlowRetThdsItem(new RetainabilityThreshold().relFlowNum(ParserUtil.safeParseInteger(object.getQosFlowRetThds().get(i).get(0))).relFlowRatio(ParserUtil.safeParseInteger(object.getQosFlowRetThds().get(i).get(1))).relTimeUnit(new TimeUnit().relTimeUnit(TimeUnitEnum.fromValue(object.getQosFlowRetThds().get(i).get(2)))));
				}
			}
		}
		if(object.getRanUeThrouThds().size()>0) {
			eventSub.ranUeThrouThds(object.getRanUeThrouThds());
		}
		if(object.getSnssaia().size()>0) {
			for(int i=0;i<object.getSnssaia().size();i++) {
				eventSub.addSnssaiaItem(new Snssai().sst(ParserUtil.safeParseInteger(object.getSnssaia().get(i).get(0))).sd(object.getSnssaia().get(i).get(1)));
			}
		}
		if(object.getCongThresholds().size()>0) {
			for(int i=0;i<object.getCongThresholds().size();i++) {
				eventSub.addCongThresholdsItem(new ThresholdLevel().congLevel(ParserUtil.safeParseInteger(object.getCongThresholds().get(i).get(0))).nfLoadLevel(ParserUtil.safeParseInteger(object.getCongThresholds().get(i).get(1))).nfCpuUsage(ParserUtil.safeParseInteger(object.getCongThresholds().get(i).get(2))).nfMemoryUsage(ParserUtil.safeParseInteger(object.getCongThresholds().get(i).get(3))).nfStorageUsage(ParserUtil.safeParseInteger(object.getCongThresholds().get(i).get(4))).avgTrafficRate(object.getCongThresholds().get(i).get(5)).maxTrafficRate(object.getCongThresholds().get(i).get(6)).avgPacketDelay(ParserUtil.safeParseInteger(object.getCongThresholds().get(i).get(7))).maxPacketDelay(ParserUtil.safeParseInteger(object.getCongThresholds().get(i).get(8))).avgPacketLossRate(ParserUtil.safeParseInteger(object.getCongThresholds().get(i).get(9))).svcExpLevel(ParserUtil.safeParseFloat(object.getCongThresholds().get(i).get(10))));
			}
			
		}
		if(object.getNwPerfRequs().size()>0) {
			for(int i=0;i<object.getNwPerfRequs().size();i++) {
				if(object.getNwPerfRequs().get(i).get(0)!=null&&object.getNwPerfRequs().get(i).get(1)!=null&&object.getNwPerfRequs().get(i).get(2)!=null) {
					eventSub.addNwPerfRequsItem(new NetworkPerfRequirement().nwPerfType(new NetworkPerfType().nwPerfType(NetworkPerfTypeEnum.fromValue(object.getNwPerfRequs().get(i).get(0)))).relativeRatio(ParserUtil.safeParseInteger(object.getNwPerfRequs().get(i).get(1))).absoluteNum(ParserUtil.safeParseInteger(object.getNwPerfRequs().get(i).get(2))));
				}
			}
		}
		if(object.getBwRequs().size()>0) {
			for(int i=0;i<object.getBwRequs().size();i++) {
				eventSub.addBwRequsItem(new BwRequirement().appId(object.getBwRequs().get(i).get(0)).marBwDl(object.getBwRequs().get(i).get(1)).marBwUl(object.getBwRequs().get(i).get(2)).mirBwDl(object.getBwRequs().get(i).get(3)).mirBwUl(object.getBwRequs().get(i).get(4)));
			}
		}
		if(object.getExcepRequs().size()>0) {
			for(int i=0;i<object.getExcepRequs().size();i++) {
				if(object.getExcepRequs().get(i).get(0)!=null&&object.getExcepRequs().get(i).get(1)!=null&&object.getExcepRequs().get(i).get(2)!=null) {
					eventSub.addExcepRequsItem(new Exception().excepId(new ExceptionId().excepId(ExceptionIdEnum.fromValue(object.getExcepRequs().get(i).get(0))))
															.excepLevel(ParserUtil.safeParseInteger(object.getExcepRequs().get(i).get(1)))
															.excepTrend(new ExceptionTrend().excepTrend(ExceptionTrendEnum.fromValue(object.getExcepRequs().get(i).get(2)))));
				}
			}
		}
		if(object.getRatFreqs().size()>0) {
			for(int i=0;i<object.getRatFreqs().size();i++) {
				RatFreqInformation ratFreq = new RatFreqInformation();
					ratFreq.allFreq(ParserUtil.safeParseBoolean(object.getRatFreqs().get(i).get(0).get(0)));
					ratFreq.allRat(ParserUtil.safeParseBoolean(object.getRatFreqs().get(i).get(0).get(1)));
					ratFreq.freq(ParserUtil.safeParseInteger(object.getRatFreqs().get(i).get(0).get(2)));
					ratFreq.ratType(new RatType().ratType(RatTypeEnum.fromValue(object.getRatFreqs().get(i).get(0).get(3))));
					ratFreq.matchingDir(new MatchingDirection().matchingDir(MatchingDirectionEnum.fromValue(object.getRatFreqs().get(i).get(0).get(4))));
				if(object.getRatFreqs().get(i).get(1).size()>0) {
					ratFreq.svcExpThreshold(new ThresholdLevel().congLevel(ParserUtil.safeParseInteger(object.getRatFreqs().get(i).get(1).get(0))).nfLoadLevel(ParserUtil.safeParseInteger(object.getRatFreqs().get(i).get(1).get(1))).nfCpuUsage(ParserUtil.safeParseInteger(object.getRatFreqs().get(i).get(1).get(2))).nfMemoryUsage(ParserUtil.safeParseInteger(object.getRatFreqs().get(i).get(1).get(3))).nfStorageUsage(ParserUtil.safeParseInteger(object.getRatFreqs().get(i).get(1).get(4))).avgTrafficRate(object.getRatFreqs().get(i).get(1).get(5)).maxTrafficRate(object.getRatFreqs().get(i).get(1).get(6)).avgPacketDelay(ParserUtil.safeParseInteger(object.getRatFreqs().get(i).get(1).get(7))).maxPacketDelay(ParserUtil.safeParseInteger(object.getRatFreqs().get(i).get(1).get(8))).avgPacketLossRate(ParserUtil.safeParseInteger(object.getRatFreqs().get(i).get(1).get(9))).svcExpLevel(ParserUtil.safeParseFloat(object.getRatFreqs().get(i).get(1).get(10))));
				}
				eventSub.addRatFreqsItem(ratFreq);
			}
		}
		if(object.getListOfAnaSubsets().size()>0) {
			for(int i=0;i<object.getListOfAnaSubsets().size();i++) {
				eventSub.addListOfAnaSubsetsItem(new AnalyticsSubset().anaSubset(AnalyticsSubsetEnum.fromValue(object.getListOfAnaSubsets().get(i))));
			}
		}
		if(object.getDisperReqs().size()>0) {
			for(int n=0;n<object.getDisperReqs().size();n++) {
				DispersionRequirement disperReq = new DispersionRequirement();
					disperReq.disperType(new DispersionType().disperType(DispersionTypeEnum.fromValue(object.getDisperReqs().get(n).get(0).get(0).get(0))));
					disperReq.dispOrderCriter(new DispersionOrderingCriterion().dispOrderCriter(DispersionOrderingCriterionEnum.fromValue(object.getDisperReqs().get(n).get(0).get(0).get(1))));
					disperReq.order(new MatchingDirection().matchingDir(MatchingDirectionEnum.fromValue(object.getDisperReqs().get(n).get(0).get(0).get(2))));
				for(int i=0;i<object.getDisperReqs().get(n).get(1).size();i++) {
					disperReq.addClassCritersItem(new ClassCriterion().disperClass(new DispersionClass().disperClass(DispersionClassEnum.fromValue(object.getDisperReqs().get(n).get(1).get(i).get(0))))
																	.classThreshold(ParserUtil.safeParseInteger(object.getDisperReqs().get(n).get(1).get(i).get(1)))
																	.thresMatch(new MatchingDirection().matchingDir(MatchingDirectionEnum.fromValue(object.getDisperReqs().get(n).get(1).get(i).get(2)))));
				}
				for(int i=0;i<object.getDisperReqs().get(n).get(2).size();i++) {
					disperReq.addRankCritersItem(new RankingCriterion().highBase(ParserUtil.safeParseInteger(object.getDisperReqs().get(n).get(2).get(i).get(0)))
																	.lowBase(ParserUtil.safeParseInteger(object.getDisperReqs().get(n).get(2).get(i).get(1))));
				}
				eventSub.addDisperReqsItem(disperReq);
			}
		}
		if(object.getRedTransReqs().size()>0) {
			for(int  i=0;i<object.getRedTransReqs().size();i++) {
				if(object.getRedTransReqs().get(i).get(0)!=null&&object.getRedTransReqs().get(i).get(1)!=null) {
					eventSub.addRedTransReqsItem(new RedundantTransmissionExpReq().redTOrderCriter(new RedTransExpOrderingCriterion().redTOrderCriter(RedTransExpOrderingCriterionEnum.fromValue(object.getRedTransReqs().get(i).get(0))))
							.order(new MatchingDirection().matchingDir(MatchingDirectionEnum.fromValue(object.getRedTransReqs().get(i).get(1)))));
				}
			}
		}
		if(object.getWlanReqs().size()>0) {
			for(int n=0;n<object.getWlanReqs().size();n++) {
				WlanPerformanceReq wlanReq = new WlanPerformanceReq();
					wlanReq.wlanOrderCriter(new WlanOrderingCriterion().wlanOrderCriter(WlanOrderingCriterionEnum.fromValue(object.getWlanReqs().get(n).get(0).get(0))));
					wlanReq.order(new MatchingDirection().matchingDir(MatchingDirectionEnum.fromValue(object.getWlanReqs().get(n).get(0).get(1))));
				wlanReq.ssIds(object.getWlanReqs().get(n).get(1));
				wlanReq.bssIds(object.getWlanReqs().get(n).get(2));
				eventSub.addWlanReqsItem(wlanReq);
			}
		}
		if(object.getAppServerAddrs().size()>0) {
			for(int i=0;i<object.getAppServerAddrs().size();i++) {
				eventSub.addAppServerAddrsItem(new AddrFqdn().ipAddr(new IpAddr().ipv4Addr(object.getAppServerAddrs().get(i).get(0)).ipv6Addr(object.getAppServerAddrs().get(i).get(1)).ipv6Prefix(object.getAppServerAddrs().get(i).get(2)))
															.fqdn(object.getAppServerAddrs().get(i).get(3)));
			}
		}
		if(object.getDnPerfReqs().size()>0) {
			for(int n=0;n<object.getDnPerfReqs().size();n++) {
				DnPerformanceReq dnPerfReq = new DnPerformanceReq();
					dnPerfReq.dnPerfOrderCriter(new DnPerfOrderingCriterion().dnPerfOrderCriter(DnPerfOrderingCriterionEnum.fromValue(object.getDnPerfReqs().get(n).get(0).get(0).get(0))));
					dnPerfReq.order(new MatchingDirection().matchingDir(MatchingDirectionEnum.fromValue(object.getDnPerfReqs().get(n).get(0).get(0).get(1))));
				for(int i=0;i<object.getDnPerfReqs().get(n).get(1).size();i++) {
					dnPerfReq.addReportThresholdsItem(new ThresholdLevel().congLevel(ParserUtil.safeParseInteger(object.getDnPerfReqs().get(n).get(1).get(i).get(0))).nfLoadLevel(ParserUtil.safeParseInteger(object.getDnPerfReqs().get(n).get(1).get(i).get(1))).nfCpuUsage(ParserUtil.safeParseInteger(object.getDnPerfReqs().get(n).get(1).get(i).get(2))).nfMemoryUsage(ParserUtil.safeParseInteger(object.getDnPerfReqs().get(n).get(1).get(i).get(3))).nfStorageUsage(ParserUtil.safeParseInteger(object.getDnPerfReqs().get(n).get(1).get(i).get(4))).avgTrafficRate(object.getDnPerfReqs().get(n).get(1).get(i).get(5)).maxTrafficRate(object.getDnPerfReqs().get(n).get(1).get(i).get(6)).avgPacketDelay(ParserUtil.safeParseInteger(object.getDnPerfReqs().get(n).get(1).get(i).get(7))).maxPacketDelay(ParserUtil.safeParseInteger(object.getDnPerfReqs().get(n).get(1).get(i).get(8))).avgPacketLossRate(ParserUtil.safeParseInteger(object.getDnPerfReqs().get(n).get(1).get(i).get(9))).svcExpLevel(ParserUtil.safeParseFloat(object.getDnPerfReqs().get(n).get(1).get(i).get(10))));
				}
				eventSub.addDnPerfReqsItem(dnPerfReq);
			}
		}
		if(object.getNetworkArea().size()>0) {
			NetworkAreaInfo area = new NetworkAreaInfo();
			for(int i=0;i<object.getNetworkArea().get(0).size();i++) {
				Ecgi ecgi = new Ecgi();
				if(CheckUtil.checkNotNullNorEmptyString(object.getNetworkArea().get(0).get(i).get(0))&&CheckUtil.checkNotNullNorEmptyString(object.getNetworkArea().get(0).get(i).get(1))) {
					ecgi.plmnId(new PlmnId().mcc(object.getNetworkArea().get(0).get(i).get(0)).mnc(object.getNetworkArea().get(0).get(i).get(1)));
				}
				ecgi.eutraCellId(object.getNetworkArea().get(0).get(i).get(2)).nid(object.getNetworkArea().get(0).get(i).get(3));
				area.addEcgisItem(ecgi);
			}
			
			for(int i=0;i<object.getNetworkArea().get(1).size();i++) {
				Ncgi ncgi = new Ncgi();
				if(object.getNetworkArea().get(1).get(i).get(0)!=null&&object.getNetworkArea().get(1).get(i).get(1)!=null) {
					ncgi.plmnId(new PlmnId().mcc(object.getNetworkArea().get(1).get(i).get(0)).mnc(object.getNetworkArea().get(1).get(i).get(1)));
				}
				ncgi.nrCellId(object.getNetworkArea().get(1).get(i).get(2)).nid(object.getNetworkArea().get(1).get(i).get(3));
				area.addNcgisItem(ncgi);
			}
			
			for(int i=0;i<object.getNetworkArea().get(2).size();i++) {
				GlobalRanNodeId gRanNodeId = new GlobalRanNodeId();
				if(object.getNetworkArea().get(2).get(i).get(0)!=null&&object.getNetworkArea().get(2).get(i).get(1)!=null) {
					gRanNodeId.plmnId(new PlmnId().mcc(object.getNetworkArea().get(2).get(i).get(0)).mnc(object.getNetworkArea().get(2).get(i).get(1)));
				}
				gRanNodeId.n3IwfId(object.getNetworkArea().get(2).get(i).get(2)).ngeNbId(object.getNetworkArea().get(2).get(i).get(3))
																	.wagfId(object.getNetworkArea().get(2).get(i).get(4))
																	.tngfId(object.getNetworkArea().get(2).get(i).get(5))
																	.nid(object.getNetworkArea().get(2).get(i).get(6))
																	.eNbId(object.getNetworkArea().get(2).get(i).get(7));
				if(object.getNetworkArea().get(2).get(i).get(8)!=null&&CheckUtil.checkNotNullNorEmptyString(object.getNetworkArea().get(2).get(i).get(9))) {
					gRanNodeId.gNbId(new GNbId().bitLength(ParserUtil.safeParseInteger(object.getNetworkArea().get(2).get(i).get(8))).gNBValue(object.getNetworkArea().get(2).get(i).get(9)));
				}
				area.addGRanNodeIdsItem(gRanNodeId);
			}
			
			for(int i=0;i<object.getNetworkArea().get(3).size();i++) {
				Tai tai = new Tai();
				if(object.getNetworkArea().get(3).get(i).get(0)!=null&&object.getNetworkArea().get(3).get(i).get(1)!=null) {
					tai.plmnId(new PlmnId().mcc(object.getNetworkArea().get(3).get(i).get(0)).mnc(object.getNetworkArea().get(3).get(i).get(1)));
				}
				tai.tac(object.getNetworkArea().get(3).get(i).get(2)).nid(object.getNetworkArea().get(3).get(i).get(3));
				area.addTaisItem(tai);
			}
			eventSub.networkArea(area);
		}
		if(object.getQosRequ().size()>0) {
			QosRequirement qosR = new QosRequirement();
				qosR._5qi(ParserUtil.safeParseInteger(object.getQosRequ().get(0)));
			qosR.gfbrUl(object.getQosRequ().get(1)).gfbrDl(object.getQosRequ().get(2)).per(object.getQosRequ().get(5));
			qosR.resType(new QosResourceType().resType(QosResourceTypeEnum.fromValue(object.getQosRequ().get(3))));
			qosR.pdb(ParserUtil.safeParseInteger(object.getQosRequ().get(4)));
			eventSub.qosRequ(qosR);
		}
		if(object.getExptUeBehav().size()>0) {
			ExpectedUeBehaviourData expUe = new ExpectedUeBehaviourData();
			expUe.stationaryIndication(new StationaryIndication().stationaryIndication(StationaryIndicationEnum.fromValue(object.getExptUeBehav().get(0).get(0).get(0).get(0).get(0).get(0))));
			expUe.communicationDurationTime(ParserUtil.safeParseInteger(object.getExptUeBehav().get(0).get(0).get(0).get(0).get(0).get(1)));
			expUe.periodicTime(ParserUtil.safeParseInteger(object.getExptUeBehav().get(0).get(0).get(0).get(0).get(0).get(2)));
			expUe.scheduledCommunicationType(new ScheduledCommunicationType().scheduledCommunicationType(ScheduledCommunicationTypeEnum.fromValue(object.getExptUeBehav().get(0).get(0).get(0).get(0).get(0).get(3))));
			expUe.trafficProfile(new TrafficProfile().trafficProfile(TrafficProfileEnum.fromValue(object.getExptUeBehav().get(0).get(0).get(0).get(0).get(0).get(4))));
			expUe.validityTime(ParserUtil.safeParseOffsetDateTime(object.getExptUeBehav().get(0).get(0).get(0).get(0).get(0).get(5) + ZonedDateTime.now().getOffset().getId()));
			ScheduledCommunicationTime1 schTime = new ScheduledCommunicationTime1();
			if(object.getExptUeBehav().get(1).get(0).get(0).get(0).get(0).size()>0) {
				for(int i=0;i<object.getExptUeBehav().get(1).get(0).get(0).get(0).get(0).size();i++) {
				schTime.addDaysOfWeekItem(ParserUtil.safeParseInteger(object.getExptUeBehav().get(1).get(0).get(0).get(0).get(0).get(i)));
				}
			}
		    schTime.timeOfDayStart(object.getExptUeBehav().get(1).get(0).get(0).get(0).get(1).get(0));
		    schTime.timeOfDayEnd(object.getExptUeBehav().get(1).get(0).get(0).get(0).get(1).get(1));
		    expUe.scheduledCommunicationTime(schTime);
		    BatteryIndication bttrInd = new BatteryIndication();
		    bttrInd.batteryInd(ParserUtil.safeParseBoolean(object.getExptUeBehav().get(2).get(0).get(0).get(0).get(0).get(0)));
			bttrInd.replaceableInd(ParserUtil.safeParseBoolean(object.getExptUeBehav().get(2).get(0).get(0).get(0).get(0).get(1)));
		    bttrInd.rechargeableInd(ParserUtil.safeParseBoolean(object.getExptUeBehav().get(2).get(0).get(0).get(0).get(0).get(2)));
		    expUe.batteryIndication(bttrInd);
		    if(object.getExptUeBehav().get(3).size()>0) {
		    	for(int n=0;n<object.getExptUeBehav().get(3).size();n++) {
		    		LocationArea area = new LocationArea();
		    		if(object.getExptUeBehav().get(3).get(n).get(0).size()>0) {
		    			NetworkAreaInfo netArea = new NetworkAreaInfo();
		    			for(int i=0;i<object.getExptUeBehav().get(3).get(n).get(0).get(0).size();i++) {
		    				Ecgi ecgi = new Ecgi();
		    				if(object.getExptUeBehav().get(3).get(n).get(0).get(0).get(i).get(0)!=null&&object.getExptUeBehav().get(3).get(n).get(0).get(0).get(i).get(1)!=null) {
		    					ecgi.plmnId(new PlmnId().mcc(object.getExptUeBehav().get(3).get(n).get(0).get(0).get(i).get(0)).mnc(object.getExptUeBehav().get(3).get(n).get(0).get(0).get(i).get(1)));
		    				}
		    				ecgi.eutraCellId(object.getExptUeBehav().get(3).get(n).get(0).get(0).get(i).get(2)).nid(object.getExptUeBehav().get(3).get(n).get(0).get(0).get(i).get(3));
		    				netArea.addEcgisItem(ecgi);
		    			}
		    			
		    			for(int i=0;i<object.getExptUeBehav().get(3).get(n).get(0).get(1).size();i++) {
		    				Ncgi ncgi = new Ncgi();
		    				if(object.getExptUeBehav().get(3).get(n).get(0).get(1).get(i).get(0)!=null&&object.getExptUeBehav().get(3).get(n).get(0).get(1).get(i).get(1)!=null) {
		    					ncgi.plmnId(new PlmnId().mcc(object.getExptUeBehav().get(3).get(n).get(0).get(1).get(i).get(0)).mnc(object.getExptUeBehav().get(3).get(n).get(0).get(1).get(i).get(1)));
		    				}
		    				ncgi.nrCellId(object.getExptUeBehav().get(3).get(n).get(0).get(1).get(i).get(2)).nid(object.getExptUeBehav().get(3).get(n).get(0).get(1).get(i).get(3));
		    				netArea.addNcgisItem(ncgi);
		    			}
		    			
		    			for(int i=0;i<object.getExptUeBehav().get(3).get(n).get(0).get(2).size();i++) {
		    				GlobalRanNodeId gRanNodeId = new GlobalRanNodeId();
		    				if(object.getExptUeBehav().get(3).get(n).get(0).get(2).get(i).get(0)!=null&&object.getExptUeBehav().get(3).get(n).get(0).get(2).get(i).get(1)!=null) {
		    					gRanNodeId.plmnId(new PlmnId().mcc(object.getExptUeBehav().get(3).get(n).get(0).get(2).get(i).get(0)).mnc(object.getExptUeBehav().get(3).get(n).get(0).get(2).get(i).get(1)));
		    				}
		    				gRanNodeId.n3IwfId(object.getExptUeBehav().get(3).get(n).get(0).get(2).get(i).get(2)).ngeNbId(object.getExptUeBehav().get(3).get(n).get(0).get(2).get(i).get(3))
		    																	.wagfId(object.getExptUeBehav().get(3).get(n).get(0).get(2).get(i).get(4))
		    																	.tngfId(object.getExptUeBehav().get(3).get(n).get(0).get(2).get(i).get(5))
		    																	.nid(object.getExptUeBehav().get(3).get(n).get(0).get(2).get(i).get(6))
		    																	.eNbId(object.getExptUeBehav().get(3).get(n).get(0).get(2).get(i).get(7));
		    				gRanNodeId.gNbId(new GNbId().bitLength(ParserUtil.safeParseInteger(object.getExptUeBehav().get(3).get(n).get(0).get(2).get(i).get(8))).gNBValue(object.getExptUeBehav().get(3).get(n).get(0).get(2).get(i).get(9)));
		    				netArea.addGRanNodeIdsItem(gRanNodeId);
		    			}
		    			
		    			for(int i=0;i<object.getExptUeBehav().get(3).get(n).get(0).get(3).size();i++) {
		    				Tai tai = new Tai();
		    				if(object.getExptUeBehav().get(3).get(n).get(0).get(3).get(i).get(1)!=null&&object.getExptUeBehav().get(3).get(n).get(0).get(3).get(i).get(1)!=null) {
		    					tai.plmnId(new PlmnId().mcc(object.getExptUeBehav().get(3).get(n).get(0).get(3).get(i).get(0)).mnc(object.getExptUeBehav().get(3).get(n).get(0).get(3).get(i).get(1)));
		    				}
		    				tai.tac(object.getExptUeBehav().get(3).get(n).get(0).get(3).get(i).get(2)).nid(object.getExptUeBehav().get(3).get(n).get(0).get(3).get(i).get(3));
		    				netArea.addTaisItem(tai);
		    			}
		    			area.nwAreaInfo(netArea);
		    		}
		    		if(object.getExptUeBehav().get(3).get(n).get(1).size()>0) {
		    			UmtTime t = new UmtTime();
		    			t.timeOfDay(object.getExptUeBehav().get(3).get(n).get(1).get(0).get(0).get(0));
		    			t.dayOfWeek(ParserUtil.safeParseInteger(object.getExptUeBehav().get(3).get(n).get(1).get(0).get(0).get(1)));
		    			area.umtTime(t);
		    		}
		    		if(object.getExptUeBehav().get(3).get(n).get(2).size()>0) {
		    			for(int i=0;i<object.getExptUeBehav().get(3).get(n).get(2).size();i++) {
		    				if(CheckUtil.safeCheckEquals(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(0),"Point")) {
		    					Point p=new Point();
				    			p.shape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.fromValue("Point")));
				    			p.point(new GeographicalCoordinates().lon(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(1)))
				    												.lat(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(2))));
				    			area.addGeographicAreasItem(p);
		    				}
		    				if(CheckUtil.safeCheckEquals(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(0),"PointUncertaintyCircle")) {
		    					PointUncertaintyCircle p=new PointUncertaintyCircle();
				    			p.shape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.fromValue("PointUncertaintyCircle")));
				    			p.point(new GeographicalCoordinates().lon(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(1)))
				    												.lat(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(2))));
				    			p.uncertainty(ParserUtil.safeParseFloat(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(3)));
								area.addGeographicAreasItem(p);
		    				}
		    				if(CheckUtil.safeCheckEquals(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(0),"PointUncertaintyEllipse")) {
		    					PointUncertaintyEllipse p=new PointUncertaintyEllipse();
				    			p.shape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.fromValue("PointUncertaintyEllipse")));
				    			p.point(new GeographicalCoordinates().lon(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(1)))
				    												.lat(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(2))));
				    			p.confidence(ParserUtil.safeParseInteger(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(3)));
				    			p.uncertaintyEllipse(new UncertaintyEllipse().orientationMajor(ParserUtil.safeParseInteger(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(4)))
				    														.semiMajor(ParserUtil.safeParseFloat(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(5)))
				    														.semiMinor(ParserUtil.safeParseFloat(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(6))));
				    			area.addGeographicAreasItem(p);
		    				}
		    				if(CheckUtil.safeCheckEquals(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(0),"Polygon")) {
		    					Polygon p = new Polygon();
		    					p.shape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.fromValue("Polygon")));
		    					PointList pl = new PointList();
		    					for(int j=0;j<object.getExptUeBehav().get(3).get(n).get(2).get(i).get(1).size();j++) {
		    						pl.add(new GeographicalCoordinates().lon(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(1).get(j)))
		    															.lat(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(2).get(j))));
		    					}
		    					p.pointList(pl);
		    					area.addGeographicAreasItem(p);
		    				}
		    				if(CheckUtil.safeCheckEquals(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(0),"PointAltitude")) {
		    					PointAltitude p=new PointAltitude();
				    			p.shape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.fromValue("PointAltitude")));
				    			p.point(new GeographicalCoordinates().lon(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(1)))
				    												.lat(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(2))));
				    			p.altitude(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(3)));
				    			area.addGeographicAreasItem(p);
		    				}
		    				if(CheckUtil.safeCheckEquals(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(0),"PointAltitudeUncertainty")) {
		    					PointAltitudeUncertainty p=new PointAltitudeUncertainty();
				    			p.shape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.fromValue("PointAltitudeUncertainty")));
				    			p.point(new GeographicalCoordinates().lon(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(1)))
				    												.lat(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(2))));
				    			p.confidence(ParserUtil.safeParseInteger(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(3)));
				    			p.uncertaintyEllipse(new UncertaintyEllipse().orientationMajor(ParserUtil.safeParseInteger(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(4)))
				    														.semiMajor(ParserUtil.safeParseFloat(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(5)))
				    														.semiMinor(ParserUtil.safeParseFloat(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(6))));
				    			p.altitude(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(7)));
				    			p.uncertaintyAltitude(ParserUtil.safeParseFloat(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(8)));
				    			area.addGeographicAreasItem(p);
		    				}
		    				if(CheckUtil.safeCheckEquals(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(0),"EllipsoidArc")) {
		    					EllipsoidArc p=new EllipsoidArc();
				    			p.shape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.fromValue("EllipsoidArc")));
				    			p.point(new GeographicalCoordinates().lon(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(1)))
				    												.lat(ParserUtil.safeParseDouble(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(2))));
				    			p.confidence(ParserUtil.safeParseInteger(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(3)));
				    			p.innerRadius(ParserUtil.safeParseInteger(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(4)));
				    			p.uncertaintyRadius(ParserUtil.safeParseFloat(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(5)));
				    			p.offsetAngle(ParserUtil.safeParseInteger(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(6)));
				    			p.includedAngle(ParserUtil.safeParseInteger(object.getExptUeBehav().get(3).get(n).get(2).get(i).get(0).get(7)));
				    			area.addGeographicAreasItem(p);
		    				}
		    				
		    			}
		    			
		    		}
		    		if(object.getExptUeBehav().get(3).get(n).get(3).get(0).size()>0) {
		    			for(int i=0;i<object.getExptUeBehav().get(3).get(n).get(3).get(0).size();i++) {
			    			CivicAddress civicadd = new CivicAddress();
			    			civicadd.country(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(0))
			    			.a1(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(1))
			    			.a2(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(2))
			    			.a3(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(3))
			    			.a4(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(4))
			    			.a5(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(5))
			    			.a6(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(6))
			    			.PRD(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(7))
			    			.POD(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(8))
			    			.STS(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(9))
			    			.HNO(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(10))
			    			.HNS(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(11))
			    			.LMK(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(12))
			    			.LOC(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(13))
			    			.NAM(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(14))
			    			.PC(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(15))
			    			.BLD(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(16))
			    			.UNIT(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(17))
			    			.FLR(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(18))
			    			.ROOM(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(19))
			    			.PLC(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(20))
			    			.PCN(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(21))
			    			.POBOX(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(22))
			    			.ADDCODE(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(23))
			    			.SEAT(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(24))
			    			.RD(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(25))
			    			.RDSEC(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(26))
			    			.RDBR(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(27))
			    			.RDSUBBR(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(28))
			    			.PRM(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(29))
			    			.POM(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(30))
			    			.usageRules(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(31))
			    			.method(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(32))
			    			.providedBy(object.getExptUeBehav().get(3).get(n).get(3).get(0).get(i).get(33));
			    			area.addCivicAddressesItem(civicadd);
		    			}
		    		}
		    		expUe.addExpectedUmtsItem(area);
		    	}
		    }
		    eventSub.exptUeBehav(expUe);
		}
		if(object.getUpfInfo().size()>0) {
			UpfInformation upfInf = new UpfInformation();
			upfInf.upfId(object.getUpfInfo().get(0));
			if(object.getUpfInfo().get(1)!=null||object.getUpfInfo().get(2)!=null||object.getUpfInfo().get(4)!=null) {
				upfInf.upfAddr(new AddrFqdn().ipAddr(new IpAddr().ipv4Addr(object.getUpfInfo().get(1)).ipv6Addr(object.getUpfInfo().get(2)).ipv6Prefix(object.getUpfInfo().get(3))).fqdn(object.getUpfInfo().get(4)));
			}
		}
		eventSub.anySlice(ParserUtil.safeParseBoolean(object.getArgs().get(1)));
		eventSub.loadLevelThreshold(ParserUtil.safeParseInteger(object.getArgs().get(2)));
		eventSub.matchingDir(new MatchingDirection().matchingDir(MatchingDirectionEnum.fromValue(object.getArgs().get(3))));
		eventSub.maxTopAppUlNbr(ParserUtil.safeParseInteger(object.getArgs().get(4)));
		eventSub.maxTopAppDlNbr(ParserUtil.safeParseInteger(object.getArgs().get(5)));
		eventSub.repetitionPeriod(ParserUtil.safeParseInteger(object.getArgs().get(6)));
		eventSub.exptAnaType(new ExpectedAnalyticsType().exptAnaType(ExpectedAnalyticsTypeEnum.fromValue(object.getArgs().get(7))));
		
		sub.addEventSubscriptionsItem(eventSub);
		return sub;
	}
	public NnwdafEventsSubscription AddOptionalsToSubscription(NnwdafEventsSubscription sub,RequestSubscriptionModel object) {
		ReportingInformation evtReq = new ReportingInformation();
		PrevSubInfo prevSub = new PrevSubInfo();
		ConsumerNfInformation consNfInfo = new ConsumerNfInformation();
			evtReq.immRep(ParserUtil.safeParseBoolean(object.getOptionals().get(0)));
			evtReq.notifMethod(new NotificationMethod().notifMethod(NotificationMethodEnum.fromValue(object.getOptionals().get(1))));
			evtReq.maxReportNbr(ParserUtil.safeParseInteger(object.getOptionals().get(2)));
			evtReq.monDur(ParserUtil.safeParseOffsetDateTime(object.getOptionals().get(3)));
			evtReq.repPeriod(ParserUtil.safeParseInteger(object.getOptionals().get(4)));
			evtReq.sampRatio(ParserUtil.safeParseInteger(object.getOptionals().get(5)));
			evtReq.grpRepTime(ParserUtil.safeParseInteger(object.getOptionals().get(6)));
			evtReq.notifFlag(new NotificationFlag().notifFlag(NotificationFlagEnum.fromValue(object.getOptionals().get(7))));
		if(object.getPartitionCriteria().size()>0) {
			for(int i=0;i<object.getPartitionCriteria().size();i++) {
				evtReq.addPartitionCriteriaItem(new PartitioningCriteria().partitionCriteria(PartitioningCriteriaEnum.fromValue(object.getPartitionCriteria().get(i))));
			}
		}
		if((object.getOptionals().get(0)!=null) ||(object.getOptionals().get(1)!=null)||(object.getOptionals().get(2)!=null) ||(object.getOptionals().get(3)!=null) ||(object.getOptionals().get(4)!=null) ||(object.getOptionals().get(5)!=null) ||(object.getOptionals().get(6)!=null) ||(object.getOptionals().get(7)!=null)){
			sub.evtReq(evtReq);
		}
			prevSub.producerId(ParserUtil.safeParseUUID(object.getOptionals().get(8)));
			prevSub.producerSetId(object.getOptionals().get(9));
			prevSub.subscriptionId(object.getOptionals().get(10));
		if(object.getNfAnaEvents().size()>0) {
			for(int i=0;i<object.getNfAnaEvents().size();i++) {
				prevSub.addNfAnaEventsItem(new NwdafEvent().event(NwdafEventEnum.fromValue(object.getNfAnaEvents().get(i))));
			}
		}
		if(object.getUeAnaEvents().size()>0) {
			for(int i=0;i<object.getUeAnaEvents().size();i++) {
				if(object.getUeAnaEvents().get(i).size()==2) {
				UeAnalyticsContextDescriptor ueAnaEvent = new UeAnalyticsContextDescriptor();
				ueAnaEvent.supi(object.getUeAnaEvents().get(i).get(0).get(0));
				for(int j=0;j<object.getUeAnaEvents().get(i).get(1).size();j++) {
					ueAnaEvent.addAnaTypesItem(new NwdafEvent().event(NwdafEventEnum.fromValue(object.getUeAnaEvents().get(i).get(1).get(j))));
				}
				prevSub.addUeAnaEventsItem(ueAnaEvent);
				}
			}
		}
		if(object.getOptionals().get(8)!=null||object.getOptionals().get(9)!=null||object.getOptionals().get(10)!=null||object.getNfAnaEvents().size()>0||object.getUeAnaEvents().size()>0) {
			sub.prevSub(prevSub);
		}
		sub.notifCorrId(object.getOptionals().get(11));
		consNfInfo.nfId(ParserUtil.safeParseUUID(object.getOptionals().get(12)));
		consNfInfo.nfSetId(object.getOptionals().get(13));
		if(object.getTaiList().size()>0) {
			for(int i=0;i<object.getTaiList().size();i++) {
				consNfInfo.addTaiListItem(new Tai().plmnId(new PlmnId().mcc(object.getTaiList().get(i).get(0)).mnc(object.getTaiList().get(i).get(1))).tac(object.getTaiList().get(i).get(2)).nid(object.getTaiList().get(i).get(3)));
			}
		}
		if((object.getOptionals().get(12)!=null)||(object.getOptionals().get(13)!=null)||object.getTaiList().size()>0) {
			sub.consNfInfo(consNfInfo);
		}
		return sub;
	}
	public NnwdafEventsSubscription SubscriptionRequestBuilder(String clientURI,
			RequestSubscriptionModel object) throws JsonProcessingException {
		
		NnwdafEventsSubscription bodyObject = InitSubscriptionRequest(clientURI);
		if(object!=null) {
			if(object.getSupportedFeatures()!=null){
				bodyObject.setSupportedFeatures(object.getSupportedFeatures());
			}
		}
		
		System.out.println("Request body:");
		System.out.println(bodyObject.toString());
		return bodyObject;
	}
}
