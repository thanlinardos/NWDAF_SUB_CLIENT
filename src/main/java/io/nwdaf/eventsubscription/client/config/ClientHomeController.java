package io.nwdaf.eventsubscription.client.config;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import io.nwdaf.eventsubscription.client.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.client.model.NnwdafEventsSubscriptionNotification;
import io.nwdaf.eventsubscription.client.requestbuilders.CreateSubscriptionRequestBuilder;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@Controller
public class ClientHomeController {
	@Autowired
	private Environment env;
    @Autowired
    private RestTemplate restTemplate;
    private Map<Long,NnwdafEventsSubscription> currentSubs = new HashMap<Long,NnwdafEventsSubscription>();
    private Map<Long,RequestSubscriptionModel> currentSubRequests = new HashMap<Long,RequestSubscriptionModel>();
    private Map<Long,NnwdafEventsSubscriptionNotification> currentSubNotifications = new HashMap<Long,NnwdafEventsSubscriptionNotification>();
    
   
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
    				object.setNotifMethod(result.getEvtReq().getNotifMethod().getNotifMethod());
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
    				object.setNotifMethod(result.getEvtReq().getNotifMethod().getNotifMethod());
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
            object.setId(Long.parseLong(arr[arr.length-1]));
            NwdafSubClientApplication.getLogger().info(res.getBody().toString());
        	currentSubRequests.put(object.getId(),object);
        	currentSubRequests.put(-1l,object);
        	currentSubs.put(object.getId(), res.getBody());
        	map.addAttribute("location",res.getHeaders().getFirst("Location"));
        	map.put("result",res.getBody().toString());
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
            NwdafSubClientApplication.getLogger().info(res.getBody().toString());
        	currentSubRequests.put(object.getId(),object);
        	currentSubRequests.put(-1l,object);
        	currentSubs.put(object.getId(), res.getBody());
        	map.addAttribute("location",res.getHeaders().getFirst("Location"));
        	map.put("result",res.getBody().toString());
        }
        
        map.addAttribute("nnwdafEventsSubscription",object);
        map.addAttribute("serverTime", OffsetDateTime.now());
        return "redirect:/client/formSuccess/"+id;
    }
    @PostMapping(value="/client/notify")
    public ResponseEntity<NnwdafEventsSubscriptionNotification> post(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody NnwdafEventsSubscriptionNotification notification){
    	currentSubNotifications.put(Long.parseLong(notification.getSubscriptionId()), notification);
    	ResponseEntity<NnwdafEventsSubscriptionNotification> response = ResponseEntity.status(HttpStatus.OK).body(notification);
        return response;

    }
    
    @RequestMapping(value="/client/form", params={"addRow"})
    public String addRow(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id) {
    	object.addPartitionCriteria(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    @RequestMapping(value="/client/form", params={"addRowtaiList"})
    public String addRowtaiList(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id) {
    	object.addTaiList(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    @RequestMapping(value="/client/form", params={"addRowueAnaEvents"})
    public String addRowueAnaEvents(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id) {
    	object.addUeAnaEvents(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    @RequestMapping(value="/client/form", params={"addRowueAnaEventsItem"})
    public String addRowueAnaEventsItem(RequestSubscriptionModel object, ModelMap map,final HttpServletRequest req,@RequestParam(value="id",defaultValue="-1") Optional<Long> id) {
    	final Integer rowId = Integer.valueOf(req.getParameter("addRowueAnaEventsItem"));
    	object.addUeAnaEventsItem(rowId);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
    	return "form";
    }
    @RequestMapping(value="/client/form", params={"addRownfAnaEvents"})
    public String addRownfAnaEvents(RequestSubscriptionModel object, ModelMap map,@RequestParam(value="id",defaultValue="-1") Optional<Long> id) {
    	object.addNfAnaEvents(null);
    	object.setNotificationURI(env.getProperty("nnwdaf-eventsubscription.client.dev-url"));
    	currentSubRequests.put(-1l,object);
    	if(id.orElse(-1l)!=-1l) {
    		currentSubRequests.put(id.orElse(-1l),object);
    	}
    	map.addAttribute("nnwdafEventsSubscription",object);
    	map.addAttribute("serverTime", OffsetDateTime.now());
        return "form";
    }
    
    @RequestMapping(value="/client/form", params={"removeRow"})
    public String removeRow(
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
    
}
