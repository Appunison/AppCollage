package com.appunison.appcollage.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.appunison.appcollage.constants.MessageType;
import com.appunison.appcollage.constants.SortEnum;
import com.appunison.appcollage.model.pojo.response.Inbox;

/**
 * SortUtils sed to iterate Create List and then Sort
 * @author appunison
 *
 */
public class SortUtils {

	public static List<Inbox> sortByGroups(List<Inbox>  inboxs,SortEnum sortType,Comparator<Inbox> topComparator,Comparator<Inbox> belowComparator)
	{
		
		List<Inbox> topMessages=new ArrayList<Inbox>();
		List<Inbox> belowMessages=new ArrayList<Inbox>();
		for(Inbox inbox:inboxs)
		{
			boolean fallinCriteria=Boolean.FALSE;
			switch (sortType) {
			case BYGROUP:
				if(isGroupMessage(inbox))
				{
					topMessages.add(inbox);
					fallinCriteria=Boolean.TRUE;
				}
				
				break;
			case BYMISED:
				if(isMissed(inbox))
				{
					topMessages.add(inbox);
					fallinCriteria=Boolean.TRUE;
				}
				break;
			case BYPARTICIPATED:
				if(isPartiCipated(inbox))
				{
					topMessages.add(inbox);
					fallinCriteria=Boolean.TRUE;
				}
				break;
			case BYATTACHMENT:
				if(isAttachment(inbox))
				{
					topMessages.add(inbox);
					fallinCriteria=Boolean.TRUE;
				}
				break;
			case BYINVITATION:
				if(isInvited(inbox))
				{
					topMessages.add(inbox);
					fallinCriteria=Boolean.TRUE;
				}
				break;
			
			default:
				break;
				
			}
			
			if(!fallinCriteria)
			{
				belowMessages.add(inbox);
			}
		}
		
		Collections.sort(topMessages,topComparator);
		Collections.sort(belowMessages,belowComparator);
		topMessages.addAll(belowMessages);
		return topMessages;
		
	}
	
	
	private static  boolean isGroupMessage(Inbox inbox)
	{
		if(inbox.getMessage_type().equals(MessageType.COLLAGE.getDesc())||inbox.getMessage_type().equals(MessageType.AppCollage_REQUEST.getDesc()))
		{
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	
	private static boolean isMissed(Inbox inbox)
	{
		if(inbox.getMessage_type().equals(MessageType.AppCollage_REQUEST.getDesc())&&inbox.getRead_status().equals("0")&& !Comparators.isActive(inbox))
		{
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
		
	}
	
	private static boolean isPartiCipated(Inbox inbox)
	{
	
		if(inbox.getMessage_type().equals(MessageType.AppCollage_REQUEST.getDesc())&&inbox.getRead_status().equals("1"))
		{
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
		
	}

	private static boolean isInvited(Inbox inbox)
	{
		if(inbox.getMessage_type().equals(MessageType.INVITATION.getDesc()))
		{
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
		
	}
	
	private static boolean isAttachment(Inbox inbox)
	{
		if(inbox.getMessage_type().equals(MessageType.COLLAGE.getDesc()))
		{
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
		
	}
}
