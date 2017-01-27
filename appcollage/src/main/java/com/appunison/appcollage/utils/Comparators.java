package com.appunison.appcollage.utils;

import java.util.Comparator;
import java.util.Date;

import com.appunison.appcollage.constants.MessageType;
import com.appunison.appcollage.model.pojo.response.Inbox;
/**
 * 
 * @author appunison
 *
 */
public class Comparators {

	public static Comparator<Inbox> groupNameComparator()
	{
		return new Comparator<Inbox>() {

			public int compare(Inbox lhs, Inbox rhs) {
				
				return lhs.getFrom().toLowerCase().trim().compareTo(rhs.getFrom().toLowerCase().trim());
			}
		};
	}
	
	/**
	 * 
	 * @return 
	 */
	
	public static Comparator<Inbox> dateComparator()
	{
		return new Comparator<Inbox>() {

			public int compare(Inbox lhs, Inbox rhs) {
				
				return (int) (Long.parseLong(rhs.getTime())-Long.parseLong(lhs.getTime()));  
			}
		};	
	}
	
	public static Comparator<Inbox> nameComparator()
	{
		return new Comparator<Inbox>() {

			public int compare(Inbox lhs, Inbox rhs) {
				
				return (int) (lhs.getTo().compareTo(rhs.getTo()));  
			}
		};
	}
	
	/**
	 * 
	 * @return
	 */
	public static Comparator<Inbox> getAttachmentComparator()
	{
		return new Comparator<Inbox>() {

			public int compare(Inbox lhs, Inbox rhs) {
				
				if(lhs.getMessage_type().equals(MessageType.COLLAGE.getDesc()))
				{
					return -1;
				}
				else
				{
					return 0;
				}
				
			}
		};

	}
	/**
	 * 
	 * @return
	 */
	public static Comparator<Inbox> getGroupComparator()
	{
		return new Comparator<Inbox>() {

			public int compare(Inbox lhs, Inbox rhs) {
				
				if(lhs.getMessage_type().equals(MessageType.COLLAGE.getDesc())||lhs.getMessage_type().equals(MessageType.AppCollage_REQUEST.getDesc()))
				{
					return -1;
				}
				else
				{
					return 0;
				}
				
			}
		};

	}
	
	/**
	 * 
	 * @return
	 */
	public static Comparator<Inbox> getInvitationComparator()
	{
		return new Comparator<Inbox>() {

			public int compare(Inbox lhs, Inbox rhs) {
				
				if(lhs.getMessage_type().equals(MessageType.INVITATION.getDesc()))
				{
					return -1;
				}
				else
				{
					return 0;
				}
				
			}
		};

	}
	/**
	 * 
	 * @return
	 */
	public static Comparator<Inbox> getParticipatedComparator()
	{
		return new Comparator<Inbox>() {

			public int compare(Inbox lhs, Inbox rhs) {
				
				if(lhs.getMessage_type().equals(MessageType.AppCollage_REQUEST.getDesc())&&lhs.getRead_status().equals("1"))
				{
					return -1;
				}
				else
				{
					return 0;
				}
				
			}
		};

	}
	
	/**
	 * 
	 * @return
	 */
	public static Comparator<Inbox> getMissedComparator()
	{
		return new Comparator<Inbox>() {

			public int compare(Inbox lhs, Inbox rhs) {
				
				if(lhs.getMessage_type().equals(MessageType.AppCollage_REQUEST.getDesc())&&lhs.getRead_status().equals("0")&& !isActive(lhs))
				{
					return -1;
				}
				else
				{
					return 0;
				}
				
			}
		};

	}
	/**
	 * This is a Utility Method Used to check whether Request in 
	 * Inbvox is expired or not 
	 * @param inbox
	 * @return
	 */
	public static boolean isActive(Inbox inbox)
	{
		long serverTime = Long.parseLong(inbox.getTime())*1000;

		long systemTime = new Date().getTime();
		long endTime =0;
		/*if(serverTime > systemTime)
		{
			endTime = systemTime + 5*60*1000;
		}
		else
		{*/
			endTime = serverTime + 5*60*1000;				
		//}
		return ((endTime - systemTime)>0);

	}
	
	
}
