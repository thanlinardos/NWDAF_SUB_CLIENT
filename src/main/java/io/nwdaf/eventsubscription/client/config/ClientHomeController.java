package io.nwdaf.eventsubscription.client.config;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@Controller
public class ClientHomeController {
	@Autowired
	private Environment env;
    @Autowired
    private RestTemplate restTemplate;
    private Map<Long,NnwdafEventsSubscription> currentSubs = new HashMap<Long,NnwdafEventsSubscription>();
    private Map<Long,RequestSubscriptionModel> currentSubRequests = new HashMap<Long,RequestSubscriptionModel>();
    private Map<Long,NnwdafEventsSubscriptionNotification> currentSubNotifications = new HashMap<Long,NnwdafEventsSubscriptionNotification>();
    private OffsetDateTime lastNotif = null;
    @GetMapping(value="/client")
    public String client() {
    	return "client";
    }    
    @GetMapping(value="/client/form")
    public String get(ModelMap model,@RequestParam(value="id",defaultValue="-1") Optional<Long> id){
    	RequestSubscriptionModel object;
    	NnwdafEventsSubscription result;
    	NnwdafEventsSubscriptionNotification notification;
    	Long idVal = id.orElse(null);
    	if(idVal==null || currentSubRequests.get(-1l)==null) {
    		object = new RequestSubscriptionModel();
    		result = null;
    		notification = null;
    	}
    	else if(idVal==-1) {
    		object = currentSubRequests.get(-1l);
    		result = null;
    		notification = null;
    	}
    	else {
    		object = currentSubRequests.get(idVal);
    		result = currentSubs.get(idVal);
    		notification = currentSubNotifications.get(idVal);
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
        model.put("notification",notification);
        model.addAttribute("serverTime", OffsetDateTime.now());
        return "form";

    }
    @GetMapping(value="/client/formSuccess/{id}")
    public String getSub(@PathVariable("id") Long id, ModelMap model){
    	RequestSubscriptionModel object;
    	NnwdafEventsSubscription result;
    	NnwdafEventsSubscriptionNotification notification;
    	if(id==null || currentSubRequests.get(-1l)==null) {
    		object = new RequestSubscriptionModel();
    		result = null;
    		notification = null;
    	}
    	else if(id==-1l) {
    		object = currentSubRequests.get(-1l);
    		result = null;
    		notification = null;
    	}
    	else {
    		object = currentSubRequests.get(id);
    		result = currentSubs.get(id);
    		notification = currentSubNotifications.get(id);
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
        model.put("notification",notification);
        model.addAttribute("serverTime", OffsetDateTime.now());
        return "formSuccess";

    }
    @PostMapping(value="/client/formSuccess/{id}")
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
    @PostMapping(value="/client/form")
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
			restTemplate.delete(apiURI, req);
		}catch(Exception e){
			return "redirect:/client/formSuccess/"+id;
		}
		return "redirect:/client/form";
	}
    @PostMapping(value="/client/notify")
    public ResponseEntity<NnwdafEventsSubscriptionNotification> post(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody NnwdafEventsSubscriptionNotification notification){
    	currentSubNotifications.put(Long.parseLong(notification.getSubscriptionId()), notification);
    	lastNotif = OffsetDateTime.now();
    	ResponseEntity<NnwdafEventsSubscriptionNotification> response = ResponseEntity.status(HttpStatus.OK).body(notification);
        return response;

    }

    @RequestMapping(value="/client/form")
    public String buttonHandler(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="action") String action,@RequestParam(value="id",defaultValue="-1") Optional<Long> id) {
    	String[] values = action.split(",");
		action = values[0];

		switch(action){
			case "addRow":
				object.addPartitionCriteria(null);
				break;
		}
    	initFormAction(object,map,id);
        return "form";
    }
    @RequestMapping(value="/client/form", params={"addRow"})
    public String addRow(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id) {
    	object.addPartitionCriteria(null);
    	initFormAction(object,map,id);
        return "form";
    }
    @RequestMapping(value="/client/form", params={"addRowtaiList"})
    public String addRowtaiList(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id) {
    	object.addTaiList(null);
    	initFormAction(object,map,id);
        return "form";
    }
    @RequestMapping(value="/client/form", params={"addRowueAnaEvents"})
    public String addRowueAnaEvents(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id) {
    	object.addUeAnaEvents(null);
    	initFormAction(object,map,id);
        return "form";
    }
    @RequestMapping(value="/client/form", params={"addRowueAnaEventsItem"})
    public String addRowueAnaEventsItem(RequestSubscriptionModel object, ModelMap map,final HttpServletRequest req,@RequestParam(value="id",defaultValue="-1") Optional<Long> id) {
    	final Integer rowId = Integer.valueOf(req.getParameter("addRowueAnaEventsItem"));
    	object.addUeAnaEventsItem(rowId);
    	initFormAction(object,map,id);
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"addRownfAnaEvents"})
    public String addRownfAnaEvents(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id) {
    	object.addNfAnaEvents(null);
    	initFormAction(object,map,id);
        return "form";
    }
    
    @RequestMapping(value="/client/form", params={"removeRow"})
    public String removeRow(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
		object.removePartitionCriteria(rowId.intValue());
        initFormAction(object,map,id);
        return "form";
    }
    @RequestMapping(value="/client/form", params={"removeRownfAnaEvents"})
    public String removeRownfAnaEvents(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeRownfAnaEvents"));
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.removeNfAnaEvents(rowId.intValue());
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    @RequestMapping(value="/client/form", params={"removeRowtaiList"})
    public String removeRowtaiList(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeRowtaiList"));
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.removeTaiList(rowId.intValue());
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    @RequestMapping(value="/client/form", params={"removeRowueAnaEvents"})
    public String removeRowueAnaEvents(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
    		final HttpServletRequest req) {
    	final Integer rowId = Integer.valueOf(req.getParameter("removeRowueAnaEvents"));
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	object.removeUeAnaEvents(rowId.intValue());
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeRowueAnaEventsItem"})
    public String removeRowueAnaEventsItem(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
    		final HttpServletRequest req) {
    	NwdafSubClientApplication.getLogger().info(req.getParameter("removeRowueAnaEventsItem"));
    	final Integer rowId = Integer.valueOf(req.getParameter("removeRowueAnaEventsItem").split(",")[0]);
    	final Integer eventId = Integer.valueOf(req.getParameter("removeRowueAnaEventsItem").split(",")[1]);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	if(object.getUeAnaEvents().get(rowId).size()>1) {
    		object.removeUeAnaEvents(rowId.intValue(),eventId.intValue());
    	}
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"update"})
    public String update(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    
    //Event mappings
    @RequestMapping(value="/client/form", params={"addEventRow"})
    public String addEventRow(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id) {
    	object.addEventList(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeEventRow"})
    public String removeEventRow(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeEventRow"));
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.removeEventList(rowId.intValue());
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addAnaMeta"})
    public String addAnaMeta(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addAnaMeta"));
    	object.getEventList().get(rowId).addAnaMeta(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeAnaMeta"})
    public String removeAnaMeta(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeAnaMeta").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeAnaMeta").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeAnaMeta(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addAggrNwdafIds"})
    public String addAggrNwdafIds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addAggrNwdafIds"));
    	object.getEventList().get(rowId).addAggrNwdafIds(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeAggrNwdafIds"})
    public String removeAggrNwdafIds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeAggrNwdafIds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeAggrNwdafIds").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeAggrNwdafIds(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addDataStatProps"})
    public String addDataStatProps(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addDataStatProps"));
    	object.getEventList().get(rowId).addDataStatProps(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeDataStatProps"})
    public String removeDataStatProps(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeDataStatProps").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeDataStatProps").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeDataStatProps(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addAccPerSubset"})
    public String addAccPerSubset(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addAccPerSubset"));
    	object.getEventList().get(rowId).addAccPerSubset(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeAccPerSubset"})
    public String removeAccPerSubset(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeAccPerSubset").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeAccPerSubset").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeAccPerSubset(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addSupis"})
    public String addSupis(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addSupis"));
    	object.getEventList().get(rowId).addSupis(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeSupis"})
    public String removeSupis(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeSupis").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeSupis").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeSupis(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addGpsis"})
    public String addGpsis(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addGpsis"));
    	object.getEventList().get(rowId).addGpsis(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeGpsis"})
    public String removeGpsis(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeGpsis").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeGpsis").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeGpsis(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addIntGroupIds"})
    public String addIntGroupIds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addIntGroupIds"));
    	object.getEventList().get(rowId).addIntGroupIds(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeIntGroupIds"})
    public String removeIntGroupIds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeIntGroupIds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeIntGroupIds").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeIntGroupIds(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addNfInstanceIds"})
    public String addNfInstanceIds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addNfInstanceIds"));
    	object.getEventList().get(rowId).addNfInstanceIds(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeNfInstanceIds"})
    public String removeNfInstanceIds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeNfInstanceIds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeNfInstanceIds").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeNfInstanceIds(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addNfSetIds"})
    public String addNfSetIds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addNfSetIds"));
    	object.getEventList().get(rowId).addNfSetIds(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeNfSetIds"})
    public String removeNfSetIds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeNfSetIds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeNfSetIds").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeNfSetIds(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addAppIds"})
    public String addAppIds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addAppIds"));
    	object.getEventList().get(rowId).addAppIds(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeAppIds"})
    public String removeAppIds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeAppIds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeAppIds").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeAppIds(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addDnns"})
    public String addDnns(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addDnns"));
    	object.getEventList().get(rowId).addDnns(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeDnns"})
    public String removeDnns(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeDnns").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeDnns").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeDnns(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addDnais"})
    public String addDnais(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addDnais"));
    	object.getEventList().get(rowId).addDnais(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeDnais"})
    public String removeDnais(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeDnais").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeDnais").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeDnais(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addLadnDnns"})
    public String addLadnDnns(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addLadnDnns"));
    	object.getEventList().get(rowId).addLadnDnns(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeLadnDnns"})
    public String removeLadnDnns(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeLadnDnns").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeLadnDnns").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeLadnDnns(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addNfTypes"})
    public String addNfTypes(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addNfTypes"));
    	object.getEventList().get(rowId).addNfTypes(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeNfTypes"})
    public String removeNfTypes(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeNfTypes").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeNfTypes").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeNfTypes(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addNfLoadLvlThds"})
    public String addNfLoadLvlThds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addNfLoadLvlThds"));
    	object.getEventList().get(rowId).addNfLoadLvlThds(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeNfLoadLvlThds"})
    public String removeNfLoadLvlThds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeNfLoadLvlThds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeNfLoadLvlThds").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeNfLoadLvlThds(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addEcgis"})
    public String addEcgis(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addEcgis"));
    	object.getEventList().get(rowId).addNetworkAreaItem(0);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeEcgis"})
    public String removeEcgis(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeEcgis").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeEcgis").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeNetworkArea(0, rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addNcgis"})
    public String addNcgis(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addNcgis"));
    	object.getEventList().get(rowId).addNetworkAreaItem(1);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeNcgis"})
    public String removeNcgis(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeNcgis").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeNcgis").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeNetworkArea(1, rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addGRanNodeIds"})
    public String addGRanNodeIds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addGRanNodeIds"));
    	object.getEventList().get(rowId).addNetworkAreaItem(2);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeGRanNodeIds"})
    public String removeGRanNodeIds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeGRanNodeIds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeGRanNodeIds").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeNetworkArea(2, rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addTais"})
    public String addTais(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("addTais"));
    	object.getEventList().get(rowId).addNetworkAreaItem(3);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeTais"})
    public String removeTais(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeTais").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeTais").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeNetworkArea(3, rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addNsiIdInfos"})
    public String addNsiIdInfos(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addNsiIdInfos"));
    	object.getEventList().get(eventId).addNsiIdInfos(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeNsiIdInfos"})
    public String removeNsiIdInfos(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeNsiIdInfos").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeNsiIdInfos").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeNsiIdInfos(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addNsiId"})
    public String addNsiId(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addNsiId").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("addNsiId").split(",")[1]);
    	object.getEventList().get(eventId).addNsiIdInfosItem(rowId);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeNsiId"})
    public String removeNsiId(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeNsiId").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeNsiId").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeNsiId").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeNsiIdInfos(rowId,colId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addNsiLevelThrds"})
    public String addNsiLevelThrds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addNsiLevelThrds"));
    	object.getEventList().get(eventId).addNsiLevelThrds(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeNsiLevelThrds"})
    public String removeNsiLevelThrds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeNsiLevelThrds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeNsiLevelThrds").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeNsiLevelThrds(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addQosFlowRetThds"})
    public String addQosFlowRetThds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addQosFlowRetThds"));
    	object.getEventList().get(eventId).addQosFlowRetThds(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeQosFlowRetThds"})
    public String removeQosFlowRetThds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeQosFlowRetThds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeQosFlowRetThds").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeQosFlowRetThds(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addRanUeThrouThds"})
    public String addRanUeThrouThds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addRanUeThrouThds"));
    	object.getEventList().get(eventId).addRanUeThrouThds(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeRanUeThrouThds"})
    public String removeRanUeThrouThds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeRanUeThrouThds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeRanUeThrouThds").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeRanUeThrouThds(rowId);
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addSnssaia"})
    public String addSnssaia(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addSnssaia"));
    	object.getEventList().get(eventId).addSnssaia(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeSnssaia"})
    public String removeSnssaia(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeSnssaia").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeSnssaia").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeSnssaia(rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addCongThresholds"})
    public String addCongThresholds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addCongThresholds"));
    	object.getEventList().get(eventId).addCongThresholds(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeCongThresholds"})
    public String removeCongThresholds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeCongThresholds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeCongThresholds").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeCongThresholds(rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addNwPerfRequs"})
    public String addNwPerfRequs(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addNwPerfRequs"));
    	object.getEventList().get(eventId).addNwPerfRequs(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeNwPerfRequs"})
    public String removeNwPerfRequs(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeNwPerfRequs").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeNwPerfRequs").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeNwPerfRequs(rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addBwRequs"})
    public String addBwRequs(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addBwRequs"));
    	object.getEventList().get(eventId).addBwRequs(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeBwRequs"})
    public String removeBwRequs(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeBwRequs").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeBwRequs").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeBwRequs(rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addExcepRequs"})
    public String addExcepRequs(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addExcepRequs"));
    	object.getEventList().get(eventId).addExcepRequs(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeExcepRequs"})
    public String removeExcepRequs(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeExcepRequs").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeExcepRequs").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeExcepRequs(rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addListOfAnaSubsets"})
    public String addListOfAnaSubsets(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addListOfAnaSubsets"));
    	object.getEventList().get(eventId).addListOfAnaSubsets(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeListOfAnaSubsets"})
    public String removeListOfAnaSubsets(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeListOfAnaSubsets").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeListOfAnaSubsets").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeListOfAnaSubsets(rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addRedTransReqs"})
    public String addRedTransReqs(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addRedTransReqs"));
    	object.getEventList().get(eventId).addRedTransReqs(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeRedTransReqs"})
    public String removeRedTransReqs(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeRedTransReqs").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeRedTransReqs").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeRedTransReqs(rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addAppServerAddrs"})
    public String addAppServerAddrs(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addAppServerAddrs"));
    	object.getEventList().get(eventId).addAppServerAddrs(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeAppServerAddrs"})
    public String removeAppServerAddrs(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeAppServerAddrs").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeAppServerAddrs").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeAppServerAddrs(rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addRatFreqs"})
    public String addRatFreqs(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addRatFreqs"));
    	object.getEventList().get(eventId).addRatFreqs(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeRatFreqs"})
    public String removeRatFreqs(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeRatFreqs").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeRatFreqs").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeRatFreqs(rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addWlanReqs"})
    public String addWlanReqs(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addWlanReqs"));
    	object.getEventList().get(eventId).addWlanReqs(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeWlanReqs"})
    public String removeWlanReqs(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeWlanReqs").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeWlanReqs").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeWlanReqs(rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addSsIds"})
    public String addSsIds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addSsIds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("addSsIds").split(",")[1]);
    	object.getEventList().get(eventId).addWlanReqsItem(rowId);;
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeSsIds"})
    public String removeSsIds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeSsIds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeSsIds").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeSsIds").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeWlanReqs(rowId, colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addBssIds"})
    public String addBBssIds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addBssIds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("addBssIds").split(",")[1]);
    	object.getEventList().get(eventId).addWlanReqsItem2(rowId);;
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeBssIds"})
    public String removeBssIds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeBssIds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeBssIds").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeBssIds").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeWlanReqs2(rowId, colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addVisitedAreas"})
    public String addVisitedAreas(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addVisitedAreas"));
    	object.getEventList().get(eventId).addVisitedAreas(null);;
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeVisitedAreas"})
    public String removeVisitedAreas(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeVisitedAreas").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeVisitedAreas").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeVisitedAreas(rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addVisitedAreasEcgis"})
    public String addVisitedAreasEcgis(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addVisitedAreasEcgis").split(",")[0]);
		final Integer rowId = Integer.valueOf(req.getParameter("addVisitedAreasEcgis").split(",")[1]);
    	object.getEventList().get(eventId).addVisitedAreasItem(null,rowId,0);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeVisitedAreasEcgis"})
    public String removeVisitedAreasEcgis(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeVisitedAreasEcgis").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeVisitedAreasEcgis").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeVisitedAreasEcgis").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeVisitedAreasItem(rowId,0,colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addVisitedAreasNcgis"})
    public String addVisitedAreasNcgis(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addVisitedAreasNcgis").split(",")[0]);
		final Integer rowId = Integer.valueOf(req.getParameter("addVisitedAreasNcgis").split(",")[1]);
    	object.getEventList().get(eventId).addVisitedAreasItem(null,rowId,1);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeVisitedAreasNcgis"})
    public String removeVisitedAreasNcgis(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeVisitedAreasNcgis").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeVisitedAreasNcgis").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeVisitedAreasNcgis").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeVisitedAreasItem(rowId,1,colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addVisitedAreasGRanNodeIds"})
    public String addVisitedAreasGRanNodeIds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addVisitedAreasGRanNodeIds").split(",")[0]);
		final Integer rowId = Integer.valueOf(req.getParameter("addVisitedAreasGRanNodeIds").split(",")[1]);
    	object.getEventList().get(eventId).addVisitedAreasItem(null,rowId,2);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeVisitedAreasGRanNodeIds"})
    public String removeVisitedAreasGRanNodeIds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeVisitedAreasGRanNodeIds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeVisitedAreasGRanNodeIds").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeVisitedAreasGRanNodeIds").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeVisitedAreasItem(rowId,2,colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addVisitedAreasTais"})
    public String addVisitedAreasTais(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addVisitedAreasTais").split(",")[0]);
		final Integer rowId = Integer.valueOf(req.getParameter("addVisitedAreasTais").split(",")[1]);
    	object.getEventList().get(eventId).addVisitedAreasItem(null,rowId,3);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeVisitedAreasTais"})
    public String removeVisitedAreasTais(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeVisitedAreasTais").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeVisitedAreasTais").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeVisitedAreasTais").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeVisitedAreasItem(rowId,3,colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addDisperReqs"})
    public String addDisperReqs(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addDisperReqs"));
    	object.getEventList().get(eventId).addDisperReqs(null);;
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeDisperReqs"})
    public String removeDisperReqs(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeDisperReqs").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeDisperReqs").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeDisperReqs(rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addDisperReqsClassCriters"})
    public String addDisperReqsClassCriters(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addDisperReqsClassCriters").split(",")[0]);
		final Integer rowId = Integer.valueOf(req.getParameter("addDisperReqsClassCriters").split(",")[1]);
    	object.getEventList().get(eventId).addDisperReqsItem(null, rowId, 1);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeDisperReqsClassCriters"})
    public String removeDisperReqsClassCriters(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeDisperReqsClassCriters").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeDisperReqsClassCriters").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeDisperReqsClassCriters").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeDisperReqsItem(rowId,1,colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addDisperReqsRankCriters"})
    public String addDisperReqsRankCriters(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addDisperReqsRankCriters").split(",")[0]);
		final Integer rowId = Integer.valueOf(req.getParameter("addDisperReqsRankCriters").split(",")[1]);
    	object.getEventList().get(eventId).addDisperReqsItem(null,rowId,2);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeDisperReqsRankCriters"})
    public String removeDisperReqsRankCriters(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeDisperReqsRankCriters").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeDisperReqsRankCriters").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeDisperReqsRankCriters").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeDisperReqsItem(rowId,2,colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addDnPerfReqs"})
    public String addDnPerfReqs(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addDnPerfReqs"));
    	object.getEventList().get(eventId).addDnPerfReqs(null);;
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeDnPerfReqs"})
    public String removeDnPerfReqs(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeDnPerfReqs").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeDnPerfReqs").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeDnPerfReqs(rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addDnPerfReqsReportThresholds"})
    public String addDnPerfReqsReportThresholds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addDnPerfReqsReportThresholds").split(",")[0]);
		final Integer rowId = Integer.valueOf(req.getParameter("addDnPerfReqsReportThresholds").split(",")[1]);
    	object.getEventList().get(eventId).addDnPerfReqsItem(null,rowId,1);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeDnPerfReqsReportThresholds"})
    public String removeDnPerfReqsReportThresholds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeDnPerfReqsReportThresholds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeDnPerfReqsReportThresholds").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeDnPerfReqsReportThresholds").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeDnPerfReqsItem(rowId,1,colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addExptUeBehavScheduledCommunicationTimeDaysOfWeek"})
    public String addExptUeBehavScheduledCommunicationTimeDaysOfWeek(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addExptUeBehavScheduledCommunicationTimeDaysOfWeek"));
    	object.getEventList().get(eventId).addExptUeBehav(null,1);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeExptUeBehavScheduledCommunicationTimeDaysOfWeek"})
    public String removeExptUeBehavScheduledCommunicationTimeDaysOfWeek(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeExptUeBehavScheduledCommunicationTimeDaysOfWeek").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeExptUeBehavScheduledCommunicationTimeDaysOfWeek").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeExptUeBehav(1,rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addExptUeBehavExpectedUmts"})
    public String addExptUeBehavExpectedUmts(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addExptUeBehavExpectedUmts"));
    	object.getEventList().get(eventId).addExptUeBehavItem(null,3);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeExptUeBehavExpectedUmts"})
    public String removeExptUeBehavExpectedUmts(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeExptUeBehavExpectedUmts").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeExptUeBehavExpectedUmts").split(",")[1]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeExptUeBehavItem(3,rowId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addExptUeBehavEcgis"})
    public String addExptUeBehavEcgis(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addExptUeBehavEcgis").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("addExptUeBehavEcgis").split(",")[1]);
    	object.getEventList().get(eventId).addExptUeBehavItem(null,rowId,0);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeExptUeBehavEcgis"})
    public String removeExptUeBehavEcgis(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeExptUeBehavEcgis").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeExptUeBehavEcgis").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeExptUeBehavEcgis").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeExptUeBehavItem(rowId,0,colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addExptUeBehavNcgis"})
    public String addExptUeBehavNcgis(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addExptUeBehavNcgis").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("addExptUeBehavNcgis").split(",")[1]);
    	object.getEventList().get(eventId).addExptUeBehavItem(null,rowId,1);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeExptUeBehavNcgis"})
    public String removeExptUeBehavNcgis(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeExptUeBehavNcgis").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeExptUeBehavNcgis").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeExptUeBehavNcgis").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeExptUeBehavItem(rowId,1,colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addExptUeBehavGRanNodeIds"})
    public String addExptUeBehavGRanNodeIds(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addExptUeBehavGRanNodeIds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("addExptUeBehavGRanNodeIds").split(",")[1]);
    	object.getEventList().get(eventId).addExptUeBehavItem(null,rowId,2);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeExptUeBehavGRanNodeIds"})
    public String removeExptUeBehavGRanNodeIds(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeExptUeBehavGRanNodeIds").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeExptUeBehavGRanNodeIds").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeExptUeBehavGRanNodeIds").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeExptUeBehavItem(rowId,2,colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addExptUeBehavTais"})
    public String addExptUeBehavTais(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addExptUeBehavTais").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("addExptUeBehavTais").split(",")[1]);
    	object.getEventList().get(eventId).addExptUeBehavItem(null,rowId,3);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeExptUeBehavTais"})
    public String removeExptUeBehavTais(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeExptUeBehavTais").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeExptUeBehavTais").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeExptUeBehavTais").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeExptUeBehavItem(rowId,3,colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addExptUeBehavGeographicalAreas"})
    public String addExptUeBehavGeographicalAreas(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addExptUeBehavGeographicalAreas").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("addExptUeBehavGeographicalAreas").split(",")[1]);
    	object.getEventList().get(eventId).addExptUeBehav(null,rowId,2);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeExptUeBehavGeographicalAreas"})
    public String removeExptUeBehavGeographicalAreas(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeExptUeBehavGeographicalAreas").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeExptUeBehavGeographicalAreas").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeExptUeBehavGeographicalAreas").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeExptUeBehav(rowId,2,colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addExptUeBehavGeographicalAreasPolygon"})
    public String addExptUeBehavGeographicalAreasPolygon(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addExptUeBehavGeographicalAreasPolygon").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("addExptUeBehavGeographicalAreasPolygon").split(",")[1]);
		final Integer colId = Integer.valueOf(req.getParameter("addExptUeBehavGeographicalAreasPolygon").split(",")[2]);
    	object.getEventList().get(eventId).addExptUeBehav(null,rowId,2,colId);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeExptUeBehavGeographicalAreasPolygon"})
    public String removeExptUeBehavGeographicalAreasPolygon(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeExptUeBehavGeographicalAreasPolygon").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeExptUeBehavGeographicalAreasPolygon").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeExptUeBehavGeographicalAreasPolygon").split(",")[2]);
    	final Integer xcolId = Integer.valueOf(req.getParameter("removeExptUeBehavGeographicalAreasPolygon").split(",")[3]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeExptUeBehav(rowId,2,colId,xcolId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
	@RequestMapping(value="/client/form", params={"addExptUeBehavCivicAddresses"})
    public String addExptUeBehavCivicAddresses(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
		final Integer eventId = Integer.valueOf(req.getParameter("addExptUeBehavCivicAddresses").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("addExptUeBehavCivicAddresses").split(",")[1]);
    	object.getEventList().get(eventId).addExptUeBehavItem(null,rowId,3,0);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
		object.setAllLists();
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"removeExptUeBehavCivicAddresses"})
    public String removeExptUeBehavCivicAddresses(
    		RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id, 
            final HttpServletRequest req) {
        final Integer eventId = Integer.valueOf(req.getParameter("removeExptUeBehavCivicAddresses").split(",")[0]);
    	final Integer rowId = Integer.valueOf(req.getParameter("removeExptUeBehavCivicAddresses").split(",")[1]);
    	final Integer colId = Integer.valueOf(req.getParameter("removeExptUeBehavCivicAddresses").split(",")[2]);
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.getEventList().get(eventId).removeExptUeBehavItem(rowId,3,0,colId);
		object.setAllLists();
        currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    //Update Mappings

    @RequestMapping(value="/client/formSuccess/{id}", params={"addRow"})
    public String addRowUpdate(RequestSubscriptionModel object, ModelMap map,@PathVariable("id") Long id) {
    	object.addPartitionCriteria(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id!=-1l) {
    		currentSubRequests.put(id,object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
        return "redirect:/client/formSuccess/"+id.toString();
    }
    @RequestMapping(value="/client/formSuccess/{id}", params={"addRowtaiList"})
    public String addRowtaiListUpdate(RequestSubscriptionModel object, ModelMap map,@PathVariable("id") Long id) {
    	object.addTaiList(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id!=-1l) {
    		currentSubRequests.put(id,object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
        return "redirect:/client/formSuccess/"+id.toString();
    }
    @RequestMapping(value="/client/formSuccess/{id}", params={"addRowueAnaEvents"})
    public String addRowueAnaEventsUpdate(RequestSubscriptionModel object, ModelMap map,@PathVariable("id") Long id) {
    	object.addUeAnaEvents(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id!=-1l) {
    		currentSubRequests.put(id,object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
        return "redirect:/client/formSuccess/"+id.toString();
    }
    @RequestMapping(value="/client/formSuccess/{id}", params={"addRowueAnaEventsItem"})
    public String addRowueAnaEventsItemUpdate(RequestSubscriptionModel object, ModelMap map,final HttpServletRequest req,@PathVariable("id") Long id) {
    	final Integer rowId = Integer.valueOf(req.getParameter("addRowueAnaEventsItem"));
    	object.addUeAnaEventsItem(rowId);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id!=-1l) {
    		currentSubRequests.put(id,object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "redirect:/client/formSuccess/"+id.toString();
    }
    @RequestMapping(value="/client/formSuccess/{id}", params={"addRownfAnaEvents"})
    public String addRownfAnaEventsUpdate(RequestSubscriptionModel object, ModelMap map,@PathVariable("id") Long id) {
    	object.addNfAnaEvents(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id!=-1l) {
    		currentSubRequests.put(id,object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
        return "redirect:/client/formSuccess/"+id.toString();
    }
    
    @RequestMapping(value="/client/formSuccess/{id}", params={"removeRow"})
    public String removeRowUpdate(
    		RequestSubscriptionModel object, ModelMap map,@PathVariable("id") Long id, 
            final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.removePartitionCriteria(rowId.intValue());
        currentSubRequests.put(-1l,object);
    	if(id!=-1l) {
    		currentSubRequests.put(id,object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "redirect:/client/formSuccess/"+id.toString();
    }
    @RequestMapping(value="/client/formSuccess/{id}", params={"removeRownfAnaEvents"})
    public String removeRownfAnaEventsUpdate(
    		RequestSubscriptionModel object, ModelMap map,@PathVariable("id") Long id, 
            final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeRownfAnaEvents"));
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.removeNfAnaEvents(rowId.intValue());
        currentSubRequests.put(-1l,object);
    	if(id!=-1l) {
    		currentSubRequests.put(id,object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "redirect:/client/formSuccess/"+id.toString();
    }
    @RequestMapping(value="/client/formSuccess/{id}", params={"removeRowtaiList"})
    public String removeRowtaiListUpdate(
    		RequestSubscriptionModel object, ModelMap map,@PathVariable("id") Long id, 
            final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeRowtaiList"));
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.removeTaiList(rowId.intValue());
        currentSubRequests.put(-1l,object);
    	if(id!=-1l) {
    		currentSubRequests.put(id,object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "redirect:/client/formSuccess/"+id.toString();
    }
    @RequestMapping(value="/client/formSuccess/{id}", params={"removeRowueAnaEvents"})
    public String removeRowueAnaEventsUpdate(
    		RequestSubscriptionModel object, ModelMap map,@PathVariable("id") Long id, 
    		final HttpServletRequest req) {
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	final Integer rowId = Integer.valueOf(req.getParameter("removeRowueAnaEvents"));
    	object.removeUeAnaEvents(rowId.intValue());
    	currentSubRequests.put(-1l,object);
    	if(id!=-1l) {
    		currentSubRequests.put(id,object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "redirect:/client/formSuccess/"+id.toString();
    }
    @RequestMapping(value="/client/formSuccess/{id}", params={"removeRowueAnaEventsItem"})
    public String removeRowueAnaEventsItemUpdate(
    		RequestSubscriptionModel object, ModelMap map,@PathVariable("id") Long id, 
    		final HttpServletRequest req) {
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	NwdafSubClientApplication.getLogger().info(req.getParameter("removeRowueAnaEventsItem"));
    	final Integer rowId = Integer.valueOf(req.getParameter("removeRowueAnaEventsItem").split(",")[0]);
    	final Integer eventId = Integer.valueOf(req.getParameter("removeRowueAnaEventsItem").split(",")[1]);
		System.out.println("rowId="+rowId+", eventId="+eventId);
    	if(object.getUeAnaEvents().get(rowId).size()>1) {
    		object.removeUeAnaEvents(rowId.intValue(),eventId.intValue());
    	}
    	currentSubRequests.put(-1l,object);
    	if(id!=-1l) {
    		currentSubRequests.put(id,object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "redirect:/client/formSuccess/"+id.toString();
    }
    @RequestMapping(value="/client/formSuccess/{id}", params={"update"})
    public String updateUpdate(RequestSubscriptionModel object, ModelMap map,@PathVariable("id") Long id, 
            final HttpServletRequest req) {
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id!=-1l) {
    		currentSubRequests.put(id,object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "redirect:/client/formSuccess/"+id.toString();
    }
  
    //Event mappings
    @RequestMapping(value="/client/formSuccess/{id}", params={"addEventRow"})
    public String addEventRowUpdate(RequestSubscriptionModel object, ModelMap map,@PathVariable("id") Long id) {
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	object.addEventList(null);
    	currentSubRequests.put(-1l,object);
    	if(id!=-1l) {
    		currentSubRequests.put(id,object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "redirect:/client/formSuccess/"+id.toString();
    }
    @RequestMapping(value="/client/formSuccess/{id}", params={"removeEventRow"})
    public String removeEventRowUpdate(
    		RequestSubscriptionModel object, ModelMap map,@PathVariable("id") Long id, 
            final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeEventRow"));
        object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
        object.removeEventList(rowId.intValue());
        currentSubRequests.put(-1l,object);
    	if(id!=-1l) {
    		currentSubRequests.put(id,object);
    	}
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "redirect:/client/formSuccess/"+id.toString();
    }

	private void initFormAction(RequestSubscriptionModel object,ModelMap map,Optional<Long> id){
		object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
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
}
