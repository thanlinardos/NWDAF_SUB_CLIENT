package io.nwdaf.eventsubscription.client;

public class Regex {
	public static String nf_set_id_plmn = "set[A-Za-z0-9\\-]+[A-Za-z0-9]\\.[A-Za-z]+set\\.5gc\\.mnc([0-9]){3}\\.mcc([0-9]){3}";
	public static String nf_set_id_snpn = "set[A-Za-z0-9\\-]+[A-Za-z0-9]\\.[A-Za-z]+set\\.5gc\\.nid([A-Za-z0-9]){11}\\.mnc([0-9]){3}\\.mcc([0-9]){3}";
	public static String uuid = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
	public static String tac = "(^[A-Fa-f0-9]{4}$)|(^[A-Fa-f0-9]{6}$)";
	public static String nid = "^[A-Fa-f0-9]{11}$";
	public static String mcc = "^\\d{3}$";
	public static String mnc = "^\\d{2,3}$";
	public static String supi = "^(imsi-[0-9]{5,15}|nai-.+|gci-.+|gli-.+|.+)$";
	public static String correlation_id = "[0-9a-fA-F]{2}\\b-[0-9a-fA-F]{2}\\b-[0-9a-fA-F]{2}\\b-[0-9a-fA-F]{2}";
	public static String group_id = "^[A-Fa-f0-9]{8}-[0-9]{3}-[0-9]{2,3}-([A-Fa-f0-9][A-Fa-f0-9]){1,10}$";
	public static String dnn = "\\.nid([A-Za-z0-9]){11}\\.mnc([0-9]){3}\\.mcc([0-9]){3}\\.gprs";
	public static String nr_cell_id = "^[A-Fa-f0-9]{9}$";
	public static String n3IwfId = "^[A-Fa-f0-9]+$";
	public static String wagfId = "^[A-Fa-f0-9]+$";
	public static String tngfId = "^[A-Fa-f0-9]+$";
	public static String ngeNbId = "^('MacroNGeNB-[A-Fa-f0-9]{5}|LMacroNGeNB-[A-Fa-f0-9]{6}|SMacroNGeNB-[A-Fa-f0-9]{5})$";
	public static String eNbId = "^(MacroeNB-[A-Fa-f0-9]{5}|LMacroeNB-[A-Fa-f0-9]{6}|SMacroeNB-[A-Fa-f0-9]{5}|HomeeNB-[A-Fa-f0-9]{7})$";
	public static String gNbId_bitLength = "2[2-9]|3[0-2]";
	public static String gNBValue = "^[A-Fa-f0-9]{6,8}$";
	//	String text = "setxyz.amfset.5gc.mnc012.mcc345";
//	String text2 = "setxyz.smfset.5gc.nid000007ed9d5.mnc012.mcc345";
}
