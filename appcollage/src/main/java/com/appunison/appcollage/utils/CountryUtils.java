package com.appunison.appcollage.utils;

import java.util.Arrays;
import java.util.Locale;

import com.appunison.appcollage.R;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.EditText;
import android.widget.Spinner;

public class CountryUtils {
	public static String countryFlagImage[]={"afghanistan","albania","algeria","american_samoa","andorra","angola","anguilla","antarctica","antigua_and_barbuda","argentina","armenia","aruba",
		"asean","australia","austria","azerbaijan","bahamas","bahrain","bangladesh","barbados","belarus","belgium","belize",
		"benin","bermuda","bhutan","bolivia","bosnia_and_herzegovina","botswana","brazil","virgin_islands_british","brunei","burkinafaso",
		"burundi","cambodja","cameroon","canada","capeverde","caymanislands","centralafrican_republic","chad","chile","china","colombia","comoros","congo_brazzaville",//"congo_kinshasa,
		"cook_islands","costarica",
		"coted_ivoire","croatia","cuba","cyprus","czechrepublic","denmark","djibouti","dominica","dominicanrepublic",
		"ecuador","egypt","el_salvador","equatorialguinea","eritrea","estonia",
		"ethiopia","faroes","fiji","finland","france","gabon","gambia","georgia","germany","ghana",
		"gibraltar","greece","greenland","guadeloupe","guam","guatemala","guinea_bissau","guinea","guyana","haiti","honduras","hong_kong",
		"hungary","iceland","india","indonesia","iran","iraq","ireland","israel","italy","jamaica","japan","jordan","kazakhstan","kenya","kiribati",
		"kuwait","laos","latvia","lebanon","lesotho","liberia","libya","liechtenstein","lithuania","luxembourg","macao",
		"macedonia","madagascar","malawi","malaysia","maldives","mali","malta","marshall_islands","martinique","mauritania","mauritius",
		"mexico","micronesia","moldova","monaco","mongolia","montenegro","montserrat","morocco","mozambique",
		"myanmar",	"namibia","nauru","nepal","netherlands","netherlands_antilles","new_caledonia","newzealand",
		"nicaragua","niger","nigeria","norway","oman","pakistan","palau","palestine","panama","papua_new_guinea","paraguay","peru",
		"philippines","poland","portugal","puerto_rico","qatar","reunion","romania","russian_federation","rwanda","st_kitts_and_nevis",
		"stvincent_and_the_grenadines","samoa","san_marino","sao_tome_and_principe","saudi_arabia","senegal","serbia","seychelles",
		"sierra_leone","singapore","slovakia","slovenia","solomon_islands","somalia","south_africa","spain","sri_lanka","sudan","suriname",
		"swaziland","sweden","switzerland","syria",
		"taiwan","tajikistan","tanzania","thailand","timor_leste","togo",
		"tonga","trinidad_and_tobago","tunisia","turkey","turkmenistan","turks_and_caicos_islands","tuvalu","uganda","ukraine","united_arab_emirates",
		"united_kingdom","united_states_of_america","virgin_islands_us","uruguay","uzbekistan","vanutau","vatican_city","venezuela","viet_nam",
		"yemen","zambia","zimbabwe"};

	public static String countryNameValues[]={"Afghanistan","Albania","Algeria","American Samoa","Andorra ","Angola ","Anguilla ","Antarctica","Antigua","Argentina","Armenia ","Aruba"
		,"Ascension","Australia ","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize",
		"Benin","Bermuda","Bhutan","Bolivia ","Bosnia & Herzegovina ","Botswana","Brazil ","British Virgin Islands","Brunei Darussalam","Burkina Faso ",
		"Burundi","Cambodia","Cameroon","Canada","Cape Verde Islands","Cayman Islands","Central African Republic","Chad ","Chile ","China (PRC)","Colombia ","Comoros","Congo","Cook Islands","Costa Rica",
		"Côte d'Ivoire (Ivory Coast)","Croatia","Cuba","Cyprus","Czech Republic","Denmark","Djibouti","Dominica","Dominican Republic",
		"Ecuador ","Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia",
		"Ethiopia","Faroe Islands","Fiji Islands","Finland","France","Gabonese Republic","Gambia","Georgia","Germany","Ghana ",
		"Gibraltar ","Greece ","Greenland ","Guadeloupe","Guam","Guatemala ","Guinea-Bissau ","Guinea","Guyana","Haiti ","Honduras","Hong Kong",
		"Hungary ","Iceland","India","Indonesia","Iran","Iraq","Ireland","Israel ","Italy ","Jamaica ","Japan ","Jordan","Kazakhstan","Kenya","Kiribati ",
		"Kuwait ","Laos","Latvia ","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania ","Luxembourg","Macao",
		"Macedonia (Former Yugoslav Rep of.)","Madagascar","Malawi ","Malaysia","Maldives","Mali Republic","Malta","Marshall Islands","Martinique","Mauritania","Mauritius",
		"Mexico","Micronesia, (Federal States of)","Moldova ","Monaco","Mongolia","Montenegro","Montserrat ","Morocco","Mozambique",
		"Myanmar","Namibia","Nauru","Nepal ","Netherlands","Netherlands Antilles","New Caledonia","New Zealand",
		"Nicaragua","Niger","Nigeria","Norway ","Oman","Pakistan","Palau","Palestinian Settlements","Panama","Papua New Guinea","Paraguay","Peru",
		"Philippines","Poland","Portugal","Puerto Rico","Qatar","Réunion Island","Romania","Russia","Rwandese Republic","St. Kitts/Nevis",
		"St. Vincent & Grenadines","Samoa","San Marino","São Tomé and Principe","Saudi Arabia","Senegal ","Serbia","Seychelles Republic",
		"Sierra Leone","Singapore","Slovak Republic","Slovenia ","Solomon Islands","Somali Democratic Republic","South Africa","Spain","Sri Lanka","Sudan","Suriname ",
		"Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan","Tanzania","Thailand","Timor Leste","Togolese Republic",
		"Tonga Islands","Trinidad & Tobago","Tunisia","Turkey","Turkmenistan","Turks and Caicos Islands","Tuvalu","Uganda","Ukraine","United Arab Emirates",
		"United Kingdom","United States of America","US Virgin Islands","Uruguay","Uzbekistan","Vanuatu","Vatican City","Venezuela","Vietnam",
		"Yemen","Zambia","Zimbabwe"};

	public static  String countryIsdCode[]={"+93","+355","+213","+1684","+376","+244","+1264","+672","+1268","+54","+374","+297","+247","+61","+43","+994","+1242","+973","+880",
		"+1246","+375","+32","+501","+229","+1441","+975","+591","+387","+267","+55","+1284","+673","+226","+257","+855","+237","+1","+238"
		,"+1345","+236","+235","+56","+86","+57","+269","+242","+682","+506","+225","+385","+53","+357","+420 ","+45",
		"+253","+1767","+1809","+593","+20","+503","+240","+291","+372","+251","+298","+679","+358","+33","+241","+220","+995","+49","+233","+350","+30","+299","+590","+1671","+502","+245","+224","+592","+509","+504","+852","+36","+354 ",
		"+91","+62","+98","+964","+353","+972","+39","+1876","+81","+962","+7","+254","+686","+965","+856","+371","+961","+266","+231",
		"+218","+423","+370","+352","+853","+389","+261","+265","+60","+960","+223","+356","+692","+596","+222","+230","+52","+691","+373","+377","+976","+382","+1664",
		"+212","+258","+95","+264","+674","+977","+31","+599","+687","+64","+505","+227","+234 ","+47","+968","+92","+680","+970","+507","+675","+595","+51","+63",
		"+48","+351","+1787","+974","+262","+40","+7","+250","+1869","+1784","+685","+378","+239","+966","+221","+381","+248","+232","+65","+421","+386","+677","+252",
		"+27","+34","+94","+249","+597","+268","+46","+41","+963","+886","+992","+255","+66","+670","+228","+676","+1868","+216","+90 ","+993","+1649","+688","+256","+380","+971",
		"+44","+1","+1340","+598","+998","+678","+39","+58","+84","+967","+260","+263"};

	public static void setPhoneNoWithoutCode(Spinner spinner, EditText editText, String number)
	{
		if(number.length()>5)
		{
			int index = number.indexOf(" ");
			if(index>=1 && index < number.length()-1)
			{
				spinner.setSelection(Arrays.asList(countryIsdCode).indexOf(number.substring(0, index)));
				editText.setText(number.substring(index+1, number.length()));
				return;
			}
			else
			{
				String prefix = number.substring(0, 5);
				int size = prefix.length();
				for (int i = 0; i < size; i++) {
					if(Arrays.asList(countryIsdCode).contains(prefix.substring(0, size-i)))
					{
						spinner.setSelection(Arrays.asList(countryIsdCode).indexOf(prefix.substring(0, size-i)));
						editText.setText(number.substring(size-i, number.length()));
						return;
					}
				}
			}
		}
		editText.setText(number);
	}
	public static void setPhoneNoAndCode(Spinner spinner, EditText editText, String code, String phoneNumber)
	{
		if(Arrays.asList(countryIsdCode).contains(code))
		{
			spinner.setSelection(Arrays.asList(countryIsdCode).indexOf(code));
		}
		editText.setText(phoneNumber);
	}
	
	public static void setDefaultCountry(Context context, Spinner spinner)
	{
		String countryCode = getUserCountry(context); // Ex IN
		String isdCode = getCountryZipCode(context, countryCode); // Ex- +91
		int firstIndex = Arrays.asList(countryIsdCode).indexOf(isdCode);
		int lastIndex = Arrays.asList(countryIsdCode).lastIndexOf(isdCode);
		if(firstIndex == lastIndex)
		{
			spinner.setSelection(firstIndex);
		}
		else // Some countries have same country code, like us and canada (+1)
		{
			if(countryNameValues[firstIndex].charAt(0) == countryCode.charAt(0))
			{
				spinner.setSelection(firstIndex);
			}
			else if(countryNameValues[lastIndex].charAt(0) == countryCode.charAt(0))
			{
				spinner.setSelection(lastIndex);
			}
		}
	}
	
	public static String getCountryZipCode(Context context, String CountryID){
		
	    String CountryZipCode="";

	    String[] rl=context.getResources().getStringArray(R.array.CountryCodes);
	    for(int i=0;i<rl.length;i++){
	        String[] g=rl[i].split(",");
	        if(g[1].trim().equals(CountryID.trim())){
	            CountryZipCode=g[0];
	            break;  
	        }
	    }
	    return CountryZipCode;
	}
	public static String getUserCountry(Context context) {
	    try {
	        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	        final String simCountry = tm.getSimCountryIso();
	        if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
	            return simCountry.toUpperCase(Locale.US);
	        }
	        else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
	            String networkCountry = tm.getNetworkCountryIso();
	            if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
	                return networkCountry.toUpperCase(Locale.US);
	            }
	        }
	    }
	    catch (Exception e) { }
   	 Locale locale = context.getResources().getConfiguration().locale;
     return locale.getCountry().toUpperCase(Locale.US);
	}
}
