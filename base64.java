package mailtest;

public class base64 {
	public static final byte BAD=(byte) 0xFF;
	public static final char base64Encode[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',  
	                              'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',  
	                              'U', 'V', 'W', 'X', 'Y', 'Z',  
	                              'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',  
	                              'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',  
	                              'u', 'v', 'w', 'x', 'y', 'z',  
	                              '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',  
	                              '+', '/'  
	                            };  
	public static final byte base64Decode[] = { 62, BAD, BAD, BAD, 63,   /* + starts at 0x2B */  
	                              52, 53, 54, 55, 56, 57, 58, 59, 60, 61,  
	                              BAD, BAD, BAD, BAD, BAD, BAD, BAD,  
	                              0, 1, 2, 3, 4, 5, 6, 7, 8, 9,  
	                              10, 11, 12, 13, 14, 15, 16, 17, 18, 19,  
	                              20, 21, 22, 23, 24, 25,  
	                              BAD, BAD, BAD, BAD, BAD, BAD,  
	                              26, 27, 28, 29, 30, 31, 32, 33, 34, 35,  
	                              36, 37, 38, 39, 40, 41, 42, 43, 44, 45,  
	                              46, 47, 48, 49, 50, 51  
	                            };  
	
	
	
	public static byte[] decryptBASE64(String str) throws Exception{
		//计算等号
		int len;
		for(len=str.length();len>0&&str.charAt(len-1)=='=';--len){}
		byte [] result=new byte[len/4*3+((len%4<=1?len%4:len%4-1))];
		byte cur;
		int j=0;	//bytearray_top
		for(int i=0;i<str.length();i++){
			if(j<result.length){
				switch(i%4){
				case 0:
					cur= (byte) ((base64Decode[str.charAt(i)- 0x2B]<<2)|((base64Decode[str.charAt(i+1)- 0x2B]&0x30)>>4));
					result[j]=cur;
					++j;
					break;
				case 1:
					cur=(byte) (((base64Decode[str.charAt(i)- 0x2B]&0xf)<<4)|((base64Decode[str.charAt(i+1)- 0x2B]&0x3c)>>2));
					result[j]=cur;
					++j;
					break;
				case 2:
					cur=(byte) (((base64Decode[str.charAt(i)- 0x2B]&0x3)<<6)|(base64Decode[str.charAt(i+1)- 0x2B]));
					result[j]=cur;
					++j;
					break;
				}
			}else{
				break;
			}
		}
		return result;
	}
	
	public static String encryptBASE64(byte[] data) throws Exception{
		String result="";
		byte cur;
		for(int i=0;i<data.length;i=i+1){
			switch(i%3){
			case 0:
				cur=(byte) ((data[i]&0xfc)>>2);
				result+=base64Encode[cur];
				if(i==data.length-1){
					cur=(byte) ((data[i]&0x03)<<4);
					result+=base64Encode[cur];
				}
				break;
			case 1:
				cur=(byte) ( ((data[i-1]&0x03)<<4)|((data[i]&0xf0)>>4) );
				result+=base64Encode[cur];
				if(i==data.length-1){
					cur=(byte) ((data[i]&0x0f)<<2) ;
					result+=base64Encode[cur];
				}
				break;
			case 2:
				cur=(byte) ( ((data[i-1]&0x0f)<<2)|((data[i]&0xc0)>>6) );
				result+=base64Encode[cur];
				cur=(byte) (data[i]&0x3f);
				result+=base64Encode[cur];
				break;
			}
		}
		int len=4-result.length()%4;
		if(len!=4){
			for(int i=0;i<len;++i){
				result+='=';
			}
		}
		return result;
	}
}
