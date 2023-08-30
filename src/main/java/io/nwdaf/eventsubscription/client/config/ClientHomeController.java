package io.nwdaf.eventsubscription.client.config;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.nwdaf.eventsubscription.client.NwdafSubClientApplication;
import io.nwdaf.eventsubscription.client.model.EventSubscription;
import io.nwdaf.eventsubscription.client.model.GADShape;
import io.nwdaf.eventsubscription.client.model.LocationArea;
import io.nwdaf.eventsubscription.client.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.client.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.client.model.SupportedGADShapes;
import io.nwdaf.eventsubscription.client.model.SupportedGADShapes.SupportedGADShapesEnum;
import io.nwdaf.eventsubscription.client.requestbuilders.CreateSubscriptionRequestBuilder;
import io.nwdaf.eventsubscription.client.requestbuilders.ParserUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.val;

import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;

@Controller
public class ClientHomeController {
	@Autowired
	private Environment env;
    private RestTemplate restTemplate;
    private Map<Long,NnwdafEventsSubscription> currentSubs = new HashMap<Long,NnwdafEventsSubscription>();
    private Map<Long,RequestSubscriptionModel> currentSubRequests = new HashMap<Long,RequestSubscriptionModel>();
    private Map<String,NnwdafEventsSubscriptionNotification> currentSubNotifications = new HashMap<String,NnwdafEventsSubscriptionNotification>();
    private OffsetDateTime lastNotif = null;
	@Value("${trust.store}")
    private Resource trustStore;
    @Value("${trust.store.password}")
    private String trustStorePassword;

    @GetMapping(value="/client")
    public String client() {
    	return "client";
    }    
    @GetMapping(value="/client/form")
    public String get(ModelMap model,@RequestParam(value="id",defaultValue="-1") Optional<Long> id){
    	RequestSubscriptionModel object;
    	NnwdafEventsSubscription result;
    	List<NnwdafEventsSubscriptionNotification> notifications;
    	Long idVal = id.orElse(null);
    	if(idVal==null || currentSubRequests.get(-1l)==null) {
    		object = new RequestSubscriptionModel();
    		result = null;
    		notifications = null;
    	}
    	else if(idVal==-1) {
    		object = currentSubRequests.get(-1l);
    		result = null;
    		notifications = null;
    	}
    	else {
    		object = currentSubRequests.get(idVal);
    		result = currentSubs.get(idVal);
    		notifications = new ArrayList<>();
			for(int i=0;i<result.getEventSubscriptions().size();i++){
				notifications.add(currentSubNotifications.get(idVal+","+i));
			}
    		if(object==null) {
    			object = new RequestSubscriptionModel();
    			if(result!=null) {
    				object.setId(idVal);
    				object.setNotificationMethod(result.getEvtReq().getNotifMethod().getNotifMethod());
    				object.setRepPeriod(result.getEvtReq().getRepPeriod());
    				for(int i=0;i<result.getEventSubscriptions().size();i++) {
    					RequestEventModel e = new RequestEventModel();
    					e.setEvent(result.getEventSubscriptions().get(i).getEvent().getEvent().toString());
        				object.addEventList(e);
    				}
    				
    			}
    			
    		}
    	}
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        model.addAttribute("nnwdafEventsSubscription",object);
        model.put("result",result);
        model.put("notifications",notifications);
        model.addAttribute("serverTime", OffsetDateTime.now());
        return "form";

    }
    @GetMapping(value="/client/formSuccess/{id}")
    public String getSub(@PathVariable("id") Long id, ModelMap model){
    	RequestSubscriptionModel object;
    	NnwdafEventsSubscription result;
    	List<NnwdafEventsSubscriptionNotification> notifications;
    	if(id==null || currentSubRequests.get(-1l)==null) {
    		object = new RequestSubscriptionModel();
    		result = null;
    		notifications = null;
    	}
    	else if(id==-1l) {
    		object = currentSubRequests.get(-1l);
    		result = null;
    		notifications = null;
    	}
    	else {
    		object = currentSubRequests.get(id);
    		result = currentSubs.get(id);
			notifications = new ArrayList<>();
			for(int i=0;i<result.getEventSubscriptions().size();i++){
				notifications.add(currentSubNotifications.get(id+","+i));
			}
    		if(object==null) {
    			object = new RequestSubscriptionModel();
    			if(result!=null) {
    				object.setId(id);
    				object.setNotificationMethod(result.getEvtReq().getNotifMethod().getNotifMethod());
    				object.setRepPeriod(result.getEvtReq().getRepPeriod());
    				for(int i=0;i<result.getEventSubscriptions().size();i++) {
    					RequestEventModel e = new RequestEventModel();
    					e.setEvent(result.getEventSubscriptions().get(i).getEvent().getEvent().toString());
        				object.addEventList(e);
    				}
    				
    			}
    			
    		}
    	}
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        model.addAttribute("nnwdafEventsSubscription",object);
        model.put("result",result);
        long delay;
        if(lastNotif!=null) {
        	delay = ChronoUnit.MILLIS.between(lastNotif, OffsetDateTime.now());
        }
        else {
        	delay=-1l;
        }
        model.put("delay", delay);
        model.put("notifications",notifications);
        model.addAttribute("serverTime", OffsetDateTime.now());
        return "formSuccess";

    }
    @PostMapping(value="/client/formSuccess/{id}", params ={"updateSub"})
    public String postSub(RequestSubscriptionModel object, ModelMap map,@PathVariable("id") Long id) throws JsonProcessingException{
    	String apiURI = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url")+"/nwdaf-eventsubscription/v1/subscriptions/";
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	if(id!=null) {
    		apiURI+=id.toString();
    	}
    	else {
    		return "form";
    	}
    	NwdafSubClientApplication.getLogger().info(object.getId().toString());
        object.setAllLists();
        CreateSubscriptionRequestBuilder subBuilder = new CreateSubscriptionRequestBuilder();
        NnwdafEventsSubscription sub = subBuilder.SubscriptionRequestBuilder(env.getProperty("nnwdaf-eventsubscription.client.dev-url"),object);
        
        for(int i=0;i<object.getEventList().size();i++) {
        	RequestEventModel e = object.getEventList().get(i);
        	e.setAllLists();
        	sub = subBuilder.AddEventToSubscription(sub,e);
        }
        
        HttpEntity<NnwdafEventsSubscription> req = new HttpEntity<>(sub);
		restTemplate = new RestTemplate(createRestTemplateFactory());
        ResponseEntity<NnwdafEventsSubscription> res = restTemplate.exchange(apiURI,HttpMethod.PUT, req, NnwdafEventsSubscription.class);
        if(res.getStatusCode().is2xxSuccessful()) {
        	System.out.println("Location:"+res.getHeaders().getFirst("Location"));
            String[] arr = res.getHeaders().getFirst("Location").split("/");
			RequestSubscriptionModel newReq = new RequestSubscriptionModel().fromSubObject(res.getBody());
            newReq.setId(Long.parseLong(arr[arr.length-1]));
            NwdafSubClientApplication.getLogger().info(res.getBody().toString());
        	currentSubRequests.put(newReq.getId(),newReq);
        	currentSubRequests.put(-1l,newReq);
        	currentSubs.put(newReq.getId(), res.getBody());
        	map.addAttribute("location",res.getHeaders().getFirst("Location"));
        	map.put("result",res.getBody());
        }
        
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "formSuccess";
    }
    @PostMapping(value="/client/form", params={"createSub"})
    public String post(RequestSubscriptionModel object, ModelMap map) throws JsonProcessingException{
    	String apiURI = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url")+"/nwdaf-eventsubscription/v1/subscriptions";
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.setAllLists();
        CreateSubscriptionRequestBuilder subBuilder = new CreateSubscriptionRequestBuilder();
        NnwdafEventsSubscription sub = subBuilder.SubscriptionRequestBuilder(env.getProperty("nnwdaf-eventsubscription.client.dev-url"),object);
        sub = subBuilder.AddOptionalsToSubscription(sub, object);
        for(int i=0;i<object.getEventList().size();i++) {
        	RequestEventModel e = object.getEventList().get(i);
        	e.setAllLists();
        	sub = subBuilder.AddEventToSubscription(sub,e);
        }
        
        HttpEntity<NnwdafEventsSubscription> req = new HttpEntity<>(sub);
		restTemplate = new RestTemplate(createRestTemplateFactory());
        ResponseEntity<NnwdafEventsSubscription> res = restTemplate.postForEntity(apiURI, req, NnwdafEventsSubscription.class);

        String id = "";
        if(res.getStatusCode().is2xxSuccessful()) {
        	System.out.println("Location:"+res.getHeaders().getFirst("Location"));
            String[] arr = res.getHeaders().getFirst("Location").split("/");
            id = arr[arr.length-1];
            object.setId(Long.parseLong(arr[arr.length-1]));
			NnwdafEventsSubscription subRes = setupShapes(res.getBody());
            NwdafSubClientApplication.getLogger().info(subRes.toString());
        	currentSubRequests.put(object.getId(),object);
        	currentSubRequests.put(-1l,object);
        	currentSubs.put(object.getId(), subRes);
        	map.addAttribute("location",res.getHeaders().getFirst("Location"));
        	map.put("result",subRes.toString());
        }
        
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "redirect:/client/formSuccess/"+id;
    }
	@RequestMapping(value="/client/formSuccess/{id}", params={"deleteSub"})
	public String deleteSub(RequestSubscriptionModel object,@PathVariable("id") Long id, ModelMap map){
		String apiURI = env.getProperty("nnwdaf-eventsubscription.openapi.dev-url")+"/nwdaf-eventsubscription/v1/subscriptions/"+id.toString();
		HttpEntity<NnwdafEventsSubscription> req = new HttpEntity<>(null);
		try{
			restTemplate = new RestTemplate(createRestTemplateFactory());
			restTemplate.delete(apiURI, req);
		}catch(Exception e){
			return "redirect:/client/formSuccess/"+id;
		}
		return "redirect:/client/form";
	}
    @PostMapping(value="/client/notify")
    public ResponseEntity<NnwdafEventsSubscriptionNotification> post(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody NnwdafEventsSubscriptionNotification notification){
    	currentSubNotifications.put(ParserUtil.safeParseLong(notification.getSubscriptionId())+","+ParserUtil.safeParseLong(notification.getNotifCorrId()), notification);
    	lastNotif = OffsetDateTime.now();
    	ResponseEntity<NnwdafEventsSubscriptionNotification> response = ResponseEntity.status(HttpStatus.OK).body(notification);
        return response;

    }

    @RequestMapping(value="/client/form")
    public String buttonHandler(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="action",required = true) String action,@RequestParam(value="id",defaultValue="-1") Optional<Long> id) {
    	String[] actionList = action.split(",");
		List<Integer> values = new ArrayList<>();
		for(int i=1;i<actionList.length;i++){
			values.add(ParserUtil.safeParseInteger(actionList[i]));
		}
		String command = actionList[0];
		buttonActionMapper(object, command, values);
		initFormAction(object,map,id);
        return "form";
    }

    //Update Mappings
	@RequestMapping(value="/client/formSuccess/{id}")
    public String buttonHandler(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="action",required = true) String action,@PathVariable("id") Long id) {
    	String[] actionList = action.split(",");
		List<Integer> values = new ArrayList<>();
		for(int i=1;i<actionList.length;i++){
			values.add(ParserUtil.safeParseInteger(actionList[i]));
		}
		String command = actionList[0];
		buttonActionMapper(object, command, values);
    	initFormActionUpdate(object,map,id);
        return "redirect:/client/formSuccess/"+id.toString();
    }

	private void initFormAction(RequestSubscriptionModel object,ModelMap map,Optional<Long> id){
		object.setAllLists();
		object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
	}
	private void initFormActionUpdate(RequestSubscriptionModel object,ModelMap map,Long id){
		object.setAllLists();
		object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id!=-1l) {
    		currentSubRequests.put(id,object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
	}
	private void buttonActionMapper(RequestSubscriptionModel object, String command, List<Integer> values){
		RequestEventModel e = null;
		if(object.getEventList().size()>0 && values.size()>0){
			e = object.getEventList().get(values.get(0));
		}
		switch(command){
			case "addPartitionCriteria":
				object.addPartitionCriteria(null);
				break;
			case "addRowtaiList":
				object.addTaiList(null);
				break;
			case "addRowueAnaEvents":
				object.addUeAnaEvents(null);
				break;
			case "addRowueAnaEventsItem":
				object.addUeAnaEventsItem(values.get(0));
				break;
			case "addRownfAnaEvents":
				object.addNfAnaEvents(null);
				break;

			case "removePartitionCriteria":
				object.removePartitionCriteria(values.get(0).intValue());
				break;
			case "removeRownfAnaEvents":
				object.removeNfAnaEvents(values.get(0).intValue());
				break;
			case "removeRowtaiList":
				object.removeTaiList(values.get(0).intValue());
				break;
			case "removeRowueAnaEvents":
				object.removeUeAnaEvents(values.get(0).intValue());
				break;
			case "removeRowueAnaEventsItem":
				if(object.getUeAnaEvents().get(values.get(0)).size()>1) {
					object.removeUeAnaEvents(values.get(0).intValue(),values.get(1).intValue());
				}
				break;
			//Event mappings
			case "addEventRow":
				object.addEventList(null);
				break;
			case "removeEventRow":
				object.removeEventList(values.get(0).intValue());
				break;
			case "addAnaMeta":
				e.addAnaMeta(null);
				break;
			case "removeAnaMeta":
				e.removeAnaMeta(values.get(1));
				break;
			case "addAggrNwdafIds":
				e.addAggrNwdafIds(null);
				break;
			case "removeAggrNwdafIds":
				e.removeAggrNwdafIds(values.get(1));
				break;
			case "addDataStatProps":
				e.addDataStatProps(null);
				break;
			case "removeDataStatProps":
				e.removeDataStatProps(values.get(1));
				break;
			case "addAccPerSubset":
				e.addAccPerSubset(null);
				break;
			case "removeAccPerSubset":
				e.removeAccPerSubset(values.get(1));
				break;
			case "addSupis":
				e.addSupis(null);
				break;
			case "removeSupis":
				e.removeSupis(values.get(1));
				break;
			case "addGpsis":
				e.addGpsis(null);
				break;
			case "removeGpsis":
				e.removeGpsis(values.get(1));
				break;
			case "addIntGroupIds":
				e.addIntGroupIds(null);
				break;
			case "removeIntGroupIds":
				e.removeIntGroupIds(values.get(1));
				break;
			case "addNfInstanceIds":
				e.addNfInstanceIds(null);
				break;
			case "removeNfInstanceIds":
				e.removeNfInstanceIds(values.get(1));
				break;
			case "addNfSetIds":
				e.addNfSetIds(null);
				break;
			case "removeNfSetIds":
				e.removeNfSetIds(values.get(1));
				break;
			case "addAppIds":
				e.addAppIds(null);
				break;
			case "removeAppIds":
				e.removeAppIds(values.get(1));
				break;
			case "addDnns":
				e.addDnns(null);
				break;
			case "removeDnns":
				e.removeDnns(values.get(1));
				break;
			case "addDnais":
				e.addDnais(null);
				break;
			case "removeDnais":
				e.removeDnais(values.get(1));
				break;
			case "addLadnDnns":
				e.addLadnDnns(null);
				break;
			case "removeLadnDnns":
				e.removeLadnDnns(values.get(1));
				break;
			case "addNfTypes":
				e.addNfTypes(null);
				break;
			case "removeNfTypes":
				e.removeNfTypes(values.get(1));
				break;
			case "addNfLoadLvlThds":
				e.addNfLoadLvlThds(null);
				break;
			case "removeNfLoadLvlThds":
				e.removeNfLoadLvlThds(values.get(1));
				break;
			case "addEcgis":
				e.addNetworkAreaItem(0);
				break;
			case "removeEcgis":
				e.removeNetworkArea(0,values.get(1));
				break;
			case "addNcgis":
				e.addNetworkAreaItem(1);
				break;
			case "removeNcgis":
				e.removeNetworkArea(1,values.get(1));
				break;
			case "addGRanNodeIds":
				e.addNetworkAreaItem(2);
				break;
			case "removeGRanNodeIds":
				e.removeNetworkArea(2,values.get(1));
				break;
			case "addTais":
				e.addNetworkAreaItem(3);
				break;
			case "removeTais":
				e.removeNetworkArea(3,values.get(1));
				break;
			case "addNsiIdInfos":
				e.addNsiIdInfos(null);
				break;
			case "removeNsiIdInfos":
				e.removeNsiIdInfos(values.get(1));
				break;
			case "addNsiId":
				e.addNsiIdInfosItem(values.get(1));
				break;
			case "removeNsiId":
				e.removeNsiIdInfos(values.get(1),values.get(2));
				break;
			case "addNsiLevelThrds":
				e.addNsiLevelThrds(null);
				break;
			case "removeNsiLevelThrds":
				e.removeNsiLevelThrds(values.get(1));
				break;
			case "addQosFlowRetThds":
				e.addQosFlowRetThds(null);
				break;
			case "removeQosFlowRetThds":
				e.removeQosFlowRetThds(values.get(1));
				break;
			case "addRanUeThrouThds":
				e.addRanUeThrouThds(null);
				break;
			case "removeRanUeThrouThds":
				e.removeRanUeThrouThds(values.get(1));
				break;
			case "addSnssaia":
				e.addSnssaia(null);
				break;
			case "removeSnssaia":
				e.removeSnssaia(values.get(1));
				break;
			case "addCongThresholds":
				e.addCongThresholds(null);
				break;
			case "removeCongThresholds":
				e.removeCongThresholds(values.get(1));
				break;

			case "addNwPerfRequs":
				e.addNwPerfRequs(null);
				break;
			case "removeNwPerfRequs":
				e.removeNwPerfRequs(values.get(1));
				break;

			case "addBwRequs":
				e.addBwRequs(null);
				break;
			case "removeBwRequs":
				e.removeBwRequs(values.get(1));
				break;
			case "addExcepRequs":
				e.addExcepRequs(null);
				break;
			case "removeExcepRequs":
				e.removeExcepRequs(values.get(1));
				break;
			case "addListOfAnaSubsets":
				e.addListOfAnaSubsets(null);
				break;
			case "removeListOfAnaSubsets":
				e.removeListOfAnaSubsets(values.get(1));
				break;
			case "addRedTransReqs":
				e.addRedTransReqs(null);
				break;
			case "removeRedTransReqs":
				e.removeRedTransReqs(values.get(1));
				break;
			case "addAppServerAddrs":
				e.addAppServerAddrs(null);
				break;
			case "removeAppServerAddrs":
				e.removeAppServerAddrs(values.get(1));
				break;
			case "addRatFreqs":
				e.addRatFreqs(null);
				break;
			case "removeRatFreqs":
				e.removeRatFreqs(values.get(1));
			case "addWlanReqs":
				e.addWlanReqs(null);
				break;
			case "removeWlanReqs":
				e.removeWlanReqs(values.get(1));
				break;
			case "addSsIds":
				e.addWlanReqsItem(values.get(1));
				break;
			case "removeSsIds":
				e.removeWlanReqs(values.get(1),values.get(2));
				break;
			case "addBssIds":
				e.addWlanReqsItem2(values.get(1));
				break;
			case "removeBssIds":
				e.removeWlanReqs2(values.get(1),values.get(2));
				break;
			case "addVisitedAreas":
				e.addVisitedAreas(null);
				break;
			case "removeVisitedAreas":
				e.removeVisitedAreas(values.get(1));
				break;
			case "addVisitedAreasEcgis":
				e.addVisitedAreasItem(null,values.get(1),0);
				break;
			case "removeVisitedAreasEcgis":
				e.removeVisitedAreasItem(values.get(1),0,values.get(2));
				break;
			case "addVisitedAreasNcgis":
				e.addVisitedAreasItem(null,values.get(1),1);
				break;
			case "removeVisitedAreasNcgis":
				e.removeVisitedAreasItem(values.get(1),1,values.get(2));
				break;
			case "addVisitedAreasGRanNodeIds":
				e.addVisitedAreasItem(null,values.get(1),2);
				break;
			case "removeVisitedAreasGRanNodeIds":
				e.removeVisitedAreasItem(values.get(1),2,values.get(2));
				break;
			case "addVisitedAreasTais":
				e.addVisitedAreasItem(null,values.get(1),3);
				break;
			case "removeVisitedAreasTais":
				e.removeVisitedAreasItem(values.get(1),3,values.get(2));
				break;
			case "addDisperReqs":
				e.addDisperReqs(null);
				break;
			case "removeDisperReqs":
				e.removeDisperReqs(values.get(1));
				break;
			case "addDisperReqsClassCriters":
				e.addDisperReqsItem(null,values.get(1),1);
				break;
			case "removeDisperReqsClassCriters":
				e.removeDisperReqsItem(values.get(1),1,values.get(2));
				break;
			case "addDisperReqsRankCriters":
				e.addDisperReqsItem(null,values.get(1),2);
				break;
			case "removeDisperReqsRankCriters":
				e.removeDisperReqsItem(values.get(1),2,values.get(2));
				break;
			case "addDnPerfReqs":
				e.addDnPerfReqs(null);
				break;
			case "removeDnPerfReqs":
				e.removeDnPerfReqs(values.get(1));
				break;
			case "addDnPerfReqsReportThresholds":
				e.addDnPerfReqsItem(null,values.get(1),1);
				break;
			case "removeDnPerfReqsReportThresholds":
				e.removeDnPerfReqsItem(values.get(1),1,values.get(2));
				break;
			case "addExptUeBehavScheduledCommunicationTimeDaysOfWeek":
				e.addExptUeBehav(null,1);
				break;
			case "removeExptUeBehavScheduledCommunicationTimeDaysOfWeek":
				e.removeExptUeBehav(1,values.get(1));
				break;
			case "addExptUeBehavExpectedUmts":
				e.addExptUeBehavItem(null,3);
				break;
			case "removeExptUeBehavExpectedUmts":
				e.removeExptUeBehavItem(3,values.get(1));
				break;
			case "addExptUeBehavEcgis":
				e.addExptUeBehavItem(null,values.get(1),0);
				break;
			case "removeExptUeBehavEcgis":
				e.removeExptUeBehavItem(values.get(1),0,values.get(2));
				break;
			case "addExptUeBehavNcgis":
				e.addExptUeBehavItem(null,values.get(1),1);
				break;
			case "removeExptUeBehavNcgis":
				e.removeExptUeBehavItem(values.get(1),1,values.get(2));
				break;
			case "addExptUeBehavGRanNodeIds":
				e.addExptUeBehavItem(null,values.get(1),2);
				break;
			case "removeExptUeBehavGRanNodeIds":
				e.removeExptUeBehavItem(values.get(1),2,values.get(2));
				break;
			case "addExptUeBehavTais":
				e.addExptUeBehavItem(null,values.get(1),3);
				break;
			case "removeExptUeBehavTais":
				e.removeExptUeBehavItem(values.get(1),3,values.get(2));
				break;
			case "addExptUeBehavGeographicalAreas":
				e.addExptUeBehav(null,values.get(1),2);
				break;
			case "removeExptUeBehavGeographicalAreas":
				e.removeExptUeBehav(values.get(1),2,values.get(2));
				break;
			case "addExptUeBehavGeographicalAreasPolygon":
				e.addExptUeBehav(null,values.get(1),2,values.get(2));
				break;
			case "removeExptUeBehavGeographicalAreasPolygon":
				e.removeExptUeBehav(values.get(1),2,values.get(2),values.get(3));
				break;
			case "addExptUeBehavCivicAddresses":
				e.addExptUeBehavItem(null,values.get(1),3,0);
				break;
			case "removeExptUeBehavCivicAddresses":
				e.removeExptUeBehav(values.get(1),3,0,values.get(2));
				break;

			case "update":
				break;
			default:
				break;
		}
    	
	}
	private NnwdafEventsSubscription setupShapes(NnwdafEventsSubscription s){
		if(s!=null){
			if(s.getEventSubscriptions()!=null){
				for(int i=0;i<s.getEventSubscriptions().size();i++){
					s.getEventSubscriptions().set(i,setShapes(s.getEventSubscriptions().get(i)));
				}
			}
		}
		return s;
	}
	private EventSubscription setShapes(EventSubscription e){
		if(e.getExptUeBehav()!=null){
			if(e.getExptUeBehav().getExpectedUmts()!=null){
				for(int j=0;j<e.getExptUeBehav().getExpectedUmts().size();j++){
					LocationArea area = e.getExptUeBehav().getExpectedUmts().get(j);
					if(area.getGeographicAreas()!=null){
						for(int k=0;k<area.getGeographicAreas().size();k++){
    						String shapeType = area.getGeographicAreas().get(k).getType();
							if(shapeType.equals("Point")){
								((GADShape)area.getGeographicAreas().get(k)).setShape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.Point));
							}
							else if(shapeType.equals("PointAltitude")){
								((GADShape)area.getGeographicAreas().get(k)).setShape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.PointAltitude));
							}
							else if(shapeType.equals("PointAltitudeUncertainty")){
								((GADShape)area.getGeographicAreas().get(k)).setShape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.PointAltitudeUncertainty));
							}
							else if(shapeType.equals("PointUncertaintyCircle")){
								((GADShape)area.getGeographicAreas().get(k)).setShape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.PointUncertaintyCircle));
							}
							else if(shapeType.equals("PointUncertaintyEllipse")){
								((GADShape)area.getGeographicAreas().get(k)).setShape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.PointUncertaintyEllipse));
							}
							else if(shapeType.equals("Polygon")){
								((GADShape)area.getGeographicAreas().get(k)).setShape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.Polygon));
							}
							else if(shapeType.equals("EllipsoidArc")){
								((GADShape)area.getGeographicAreas().get(k)).setShape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.EllipsoidArc));
							}
						}
					}
				}
			}
		}
		return e;	
	}

	private ClientHttpRequestFactory createRestTemplateFactory(){
		SSLContext sslContext;
		try {
			sslContext = new SSLContextBuilder()
			  .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).build();
			SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
			.register("https", sslConFactory)
			.register("http", new PlainConnectionSocketFactory())
			.build();
			BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
			CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
			ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			return requestFactory;
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | CertificateException
				| IOException e) {
			e.printStackTrace();
		}
        return null;
	}
}
