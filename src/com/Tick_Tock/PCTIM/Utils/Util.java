package com.Tick_Tock.PCTIM.Utils;
import com.Tick_Tock.PCTIM.*;
import com.Tick_Tock.PCTIM.Message.*;
import com.Tick_Tock.PCTIM.Sdk.*;
import io.netty.buffer.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;
import javax.imageio.*;
import javax.net.ssl.*;
import org.json.*;

class textImage
{
	public textImage getobj()
	{
		return new textImage();
	}
	private int minIndex;
	private int maxIndex;
	private List<String> textImage = new ArrayList<String>();
	public textImage()
	{

	}

	public void addLine(String data)
	{
		textImage.add(data);
		if (this.getMaxIndex(data) != -1 && this.maxIndex < this.getMaxIndex(data))
		{
			this.maxIndex = this.getMaxIndex(data);
		}
		if (this.getMinIndex(data) != -1 && this.minIndex > this.getMinIndex(data))
		{
			this.minIndex = this.getMinIndex(data);
		}

	}

	public int getMinIndex(String data)
	{
		int index =0;
		while (index < data.length())
		{
			if (!String.valueOf(data.charAt(index)).equals(" "))
			{
				return index;
			}
			index += 1;
		}
		return -1;
	}

	public int getMaxIndex(String data)
	{
		int index =data.length() - 1;
		while (index > -1)
		{
			if (!String.valueOf(data.charAt(index)).equals(" "))
			{
				return index;
			}
			index -= 1;
		}
		return -1;
	}

	public String getResultString()
	{
		String result = "";
		for (int i=0;i < this.textImage.size();i += 1)
		{
			result += this.textImage.get(i).substring(this.minIndex, this.maxIndex) + "\n";
		}
		return result.replaceAll("\n$", "");
	}
}


public class Util
{
	public static String ua = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36";

	public static byte[] readByteFromFile(String file_name)
	{

		try
		{
			InputStream in = new FileInputStream(file_name);

			byte[] data = Util.toByteArray(in);
			in.close();

			return data;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;

	}

	private static byte[] toByteArray(InputStream in) throws IOException
	{

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 4];
		int n = 0;
		while ((n = in.read(buffer)) != -1)
		{
			out.write(buffer, 0, n);
		}
		return out.toByteArray();
	}

	public static byte[] bufToBytes(ByteBuf buf)
	{
		byte[] h =new byte[buf.readableBytes()];
		buf.readBytes(h);
		buf.readerIndex(0);//必须把指针调回0
		return h;
	}
	
	
	public static void getFriendList(QQUser user)
	{
		try
		{  
		    URL lll = new URL("https://qun.qq.com/cgi-bin/qun_mgr/get_friend_list");
			HttpURLConnection connection = (HttpURLConnection) lll.openConnection();// 打开连接  
			connection.setRequestMethod("POST");
			connection.setInstanceFollowRedirects(true);
			connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
			connection.setRequestProperty("Referer",  "http://qun.qq.com/member.html");
			connection.setRequestProperty("X-Requested-With",  "XMLHttpRequest");
			connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setRequestProperty("User-Agent",  ua);
			connection.setRequestProperty("Cookie", user.quncookie.replaceAll("[_A-Za-z0-9]*=;", "").replaceAll("  ", " "));
            OutputStream outStream = connection.getOutputStream();
			outStream.write(("bkn=" + user.bkn).getBytes());

            outStream.flush();
            outStream.close();
            //读取返回内容
      		String line = null;


			// 获取输入流  
			BufferedReader br= new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));  

			StringBuilder sb = new StringBuilder();  
			while ((line = br.readLine()) != null)
			{// 循环读取流  
				sb.append(line);  
			}  
			br.close();// 关闭流
			user.friendList = Util.parseFriendData(sb.toString());
			connection.disconnect();// 断开连接


		}
		catch (Exception e)
		{  
			e.printStackTrace();
		}

	}

	public static FriendList parseFriendData(String data)
	{
		FriendList friendList = new FriendList();
		try
		{
			JSONObject root = new JSONObject(data);
			JSONArray root_result_0_mems = root.getJSONObject("result").getJSONObject("0").getJSONArray("mems");
			if (root_result_0_mems != null)
			{
				for (int i=0;i < root_result_0_mems.length();i += 1)
				{
					JSONObject root_result_0_memsx = root_result_0_mems.getJSONObject(i);
					friendList.members.add(friendList.getFriendObj().setFriendName(root_result_0_memsx.getString("name")).setFriendUin(root_result_0_memsx.getLong("uin")));
				}
			}

			return friendList;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
	}


	public static void getQunList(QQUser user)
	{

		try
		{  
		    URL lll = new URL("https://qun.qq.com/cgi-bin/qun_mgr/get_group_list");
			HttpURLConnection connection = (HttpURLConnection) lll.openConnection();// 打开连接  
			connection.setRequestMethod("POST");
			connection.setInstanceFollowRedirects(true);
			connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
			connection.setRequestProperty("Referer",  "http://qun.qq.com/member.html");
			connection.setRequestProperty("X-Requested-With",  "XMLHttpRequest");
			connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setRequestProperty("User-Agent",  ua);
			connection.setRequestProperty("Cookie", user.quncookie.replaceAll("[_A-Za-z0-9]*=;", "").replaceAll("  ", " "));
            OutputStream outStream = connection.getOutputStream();
			outStream.write(("bkn=" + user.bkn).getBytes());
            outStream.flush();
            outStream.close();
            //读取返回内容
      		String line = null;
			// 获取输入流  
			BufferedReader br= new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));  
			StringBuilder sb = new StringBuilder();  
			while ((line = br.readLine()) != null)
			{// 循环读取流  
				sb.append(line);  
			}  
			br.close();// 关闭流
			user.groupList = Util.parseGroupData(sb.toString());
			connection.disconnect();// 断开连接
		}
		catch (Exception e)
		{  
			e.printStackTrace();
		}


	}


	public static GroupList parseGroupData(String data)
	{
		GroupList grouplist = new GroupList();
		try
		{
			JSONObject root = new JSONObject(data);
			JSONArray root_join = root.getJSONArray("join");
			if (root_join != null)
			{
				for (int i=0;i < root_join.length();i += 1)
				{
					JSONObject root_joinx = root_join.getJSONObject(i);
					grouplist.joinedGroup.add(grouplist.getgroupobj().setGroupName(root_joinx.getString("gn")).setGroupUin(root_joinx.getLong("gc")).setOwnerUin(root_joinx.getLong("owner")));
				}
			}
			try
			{
				JSONArray root_manage = root.getJSONArray("manage");
				if (root_manage != null)
				{
					for (int i=0;i < root_manage.length();i += 1)
					{
						JSONObject root_managex = root_manage.getJSONObject(i);
						grouplist.managedGroup.add(grouplist.getgroupobj().setGroupName(root_managex.getString("gn")).setGroupUin(root_managex.getLong("gc")).setOwnerUin(root_managex.getLong("owner")));
					}
				}
			}
			catch (JSONException e)
			{

			}
			try
			{
				JSONArray root_create = root.getJSONArray("create");
				if (root_create != null)
				{
					for (int i=0;i < root_create.length();i += 1)
					{
						JSONObject root_createx = root_create.getJSONObject(i);
						grouplist.createdGroup.add(grouplist.getgroupobj().setGroupName(root_createx.getString("gn")).setGroupUin(root_createx.getLong("gc")).setOwnerUin(root_createx.getLong("owner")));
					}
				}
			}
			catch (JSONException e)
			{
				
			}
			return grouplist;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static void getQunCookie(QQUser user)
	{
		try
		{  
			URL lll = new URL("https://ssl.ptlogin2.qq.com/jump?pt_clientver=5509&pt_src=1&keyindex=9&clientuin=" + user.uin + "&clientkey=" + Util.byte2HexString(user.txprotocol.serviceTicketHttp).replaceAll(" ", "") + "&u1=http%3A%2F%2Fqun.qq.com%2Fmember.html%23gid%3D168209441");
			HttpURLConnection connection = (HttpURLConnection) lll.openConnection();// 打开连接  
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", ua);
			connection.connect();// 连接会话  
			// 获取输入流  
			BufferedReader br= new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));  
			String line;  
			StringBuilder sb = new StringBuilder();  
			while ((line = br.readLine()) != null)
			{// 循环读取流  
				sb.append(line);  
			}  
			br.close();// 关闭流
			List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
			user.skey = "";
			for (String cookie : cookies)
			{
				if (cookie.matches("skey=.*"))
				{
					user.skey = cookie.replaceAll("skey=", "").replaceAll(";.*", "");
					user.bkn = Util.getBkn(user.skey);
				}
				if (cookie.matches("p_skey=.*"))
				{
					user.pskey = cookie.replaceAll("p_skey=", "").replaceAll(";.*", "");
					user.qungtk = Util.getGTK(user.pskey);
				}
				user.quncookie += cookie.replaceAll("Path=.*$", "").replaceAll("Expires=.*$", "") + " " ;
			}
			String url = connection.getHeaderField("Location");
			fuck(url, user);
			connection.disconnect();// 断开连接  
		}
		catch (Exception e)
		{  
			e.printStackTrace();
		}
	}

	public static void fuck(String url, QQUser user)
	{
		try
		{  
		    URL lll = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) lll.openConnection();// 打开连接  
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", ua);
			connection.setInstanceFollowRedirects(false);
			connection.connect();// 连接会话  
			// 获取输入流  
			BufferedReader br= new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));  
			String line;  
			StringBuilder sb = new StringBuilder();  
			while ((line = br.readLine()) != null)
			{// 循环读取流  
				sb.append(line);  
			}  
			br.close();// 关闭流
			List<String> cookies = connection.getHeaderFields().get("Set-Cookie");

			for (String cookie : cookies)
			{
				if (cookie.matches("skey=.*"))
				{
					user.skey = cookie.replaceAll("skey=", "").replaceAll(";.*", "");
					user.bkn = Util.getBkn(user.skey);
				}
				if (cookie.matches("p_skey=.*"))
				{
					user.pskey = cookie.replaceAll("p_skey=", "").replaceAll(";.*", "");
					user.qungtk = Util.getGTK(user.pskey);
				}
				user.quncookie += cookie.replaceAll("Path=.*$", "").replaceAll("Expires=.*$", "") + " " ;
			}
			connection.disconnect();// 断开连接  
		}
		catch (Exception e)
		{  
			e.printStackTrace();
		}
	}


	public static void getCookie(QQUser user)
	{

		try
		{  

			Util.trustAllHttpsCertificates();
			HttpsURLConnection.setDefaultHostnameVerifier(Util.hv);

			URL lll = new URL("https://ssl.ptlogin2.qq.com/jump?pt_clientver=5593&pt_src=1&keyindex=9&ptlang=2052&clientuin=" + user.uin + "&clientkey=" + Util.byte2HexString(user.txprotocol.serviceTicketHttp).replaceAll(" ", "") + "&u1=https:%2F%2Fuser.qzone.qq.com%2F417085811%3FADUIN=417085811%26ADSESSION=" + (new Date().getTime() + 28800000) + "%26ADTAG=CLIENT.QQ.5593_MyTip.0%26ADPUBNO=26841&source=namecardhoverstar");
			HttpURLConnection connection = (HttpURLConnection) lll.openConnection();// 打开连接  
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", ua);
			connection.connect();// 连接会话  
			// 获取输入流  
			BufferedReader br= new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));  
			String line;  
			StringBuilder sb = new StringBuilder();  
			while ((line = br.readLine()) != null)
			{// 循环读取流  
				sb.append(line);  
			}  
			br.close();// 关闭流
			String cookie = connection.getHeaderField("Set-Cookie");
			System.out.println(cookie);
			connection.disconnect();// 断开连接  
		}
		catch (Exception e)
		{  
			e.printStackTrace();
		}
	}

	public static String gettextimg(byte[] data)
	{
		final String base = "@#&$%*o!;.";// 字符串由复杂到简单
		BufferedImage image = null;
		textImage timg = new textImage();
		try
		{
			image = Util.byteArrayToImage(data);
		}
		catch (IOException e)
		{
			return "验证码错误";
		}
		for (int y = 0; y < image.getHeight(); y += 2)
		{
			String t="";
			for (int x = 0; x < image.getWidth(); x++)
			{
				final int pixel = image.getRGB(x, y);
				final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
				final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
				final int index = Math.round(gray * (base.length() + 1) / 255);
				t += (index >= base.length() ? " " : String.valueOf(base.charAt(index)));
			}
			timg.addLine(t);
		}
		return timg.getResultString();
	}


	public static boolean isvalidimg(byte[] data)
	{
		if (data.length % 700 == 0)
		{
			return false;
		}
		BufferedImage image = null;
		try
		{
			image = Util.byteArrayToImage(data);
		}
		catch (IOException e)
		{
			return false;
		}

		return true;
	}

	public static boolean display_verifpic(byte[] data)
	{
		final String base = "@#&$%*o!;.";// 字符串由复杂到简单
		BufferedImage image = null;
		try
		{
			image = Util.byteArrayToImage(data);
		}
		catch (IOException e)
		{
			return false;
		}
		for (int y = 0; y < image.getHeight(); y += 2)
		{
			for (int x = 0; x < image.getWidth(); x++)
			{
				final int pixel = image.getRGB(x, y);
				final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
				final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
				final int index = Math.round(gray * (base.length() + 1) / 255);
				System.out.print(index >= base.length() ? " " : String.valueOf(base.charAt(index)));
			}
			System.out.print("\n");
		}

		return true;
	}


	public static void saveFile(String filepath, byte [] data)
	{   
		if (data != null)
		{   
			File file  = new File(filepath);   
			if (file.exists())
			{   
				file.delete();   
			}   
			try
			{
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(data, 0, data.length);   
				fos.flush();   
				fos.close();   
			}
			catch (Exception e)
			{}   
		}   
	}
	
	public static BufferedImage byteArrayToImage(byte[] data) throws IOException
	{
		InputStream stream = new ByteArrayInputStream(data);
		return ImageIO.read(stream);
	}


	public static String readRecord(String key)
	{
		File property_file = new File(Util.get_root_path() + "/config/record.conf");
		Properties properties = new Properties();

		// 使用InPutStream流读取properties文件
		try
		{
			if (!property_file.exists())
			{
				property_file.createNewFile();
			}

			BufferedReader bufferedReader = new BufferedReader(new FileReader(property_file));

			properties.load(bufferedReader);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		// 获取key对应的value值
		return properties.getProperty(key);

	}
	public static String readConfig(String key)
	{
		File property_file = new File(Util.get_root_path() + "/config/settings.conf");
		Properties properties = new Properties();

		// 使用InPutStream流读取properties文件
		try
		{
			if (!property_file.exists())
			{
				property_file.createNewFile();
			}

			BufferedReader bufferedReader = new BufferedReader(new FileReader(property_file));

			properties.load(bufferedReader);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		// 获取key对应的value值
		return properties.getProperty(key);

	}

	public static String readPluginConfig(String key)
	{
		File property_file = new File(Util.get_root_path() + "/config/plugin.conf");
		Properties properties = new Properties();

		// 使用InPutStream流读取properties文件
		try
		{
			if (!property_file.exists())
			{
				property_file.createNewFile();
			}

			BufferedReader bufferedReader = new BufferedReader(new FileReader(property_file));

			properties.load(bufferedReader);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		// 获取key对应的value值
		return properties.getProperty(key);

	}


	public static void writeRecord(String key, String value)
	{
		File property_file = new File(Util.get_root_path() + "/config/record.conf");
		Properties properties = new Properties();
		// 使用InPutStream流读取properties文件
		try
		{
			if (!property_file.exists())
			{
				property_file.createNewFile();
			}
			BufferedReader bufferedReader = new BufferedReader(new FileReader(property_file));

			properties.load(bufferedReader);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(property_file)));

			properties.setProperty(key, value);
			properties.store(bw, value);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public static void writeConfig(String key, String value)
	{
		File property_file = new File(Util.get_root_path() + "/config/settings.conf");
		Properties properties = new Properties();
		// 使用InPutStream流读取properties文件
		try
		{
			if (!property_file.exists())
			{
				property_file.createNewFile();
			}
			BufferedReader bufferedReader = new BufferedReader(new FileReader(property_file));

			properties.load(bufferedReader);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(property_file)));

			properties.setProperty(key, value);
			properties.store(bw, value);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public static void write_pluginconfig(String key, String value)
	{
		File property_file = new File(Util.get_root_path() + "/config/plugin.conf");
		Properties properties = new Properties();
		// 使用InPutStream流读取properties文件
		try
		{
			if (!property_file.exists())
			{
				property_file.createNewFile();
			}
			BufferedReader bufferedReader = new BufferedReader(new FileReader(property_file));

			properties.load(bufferedReader);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(property_file)));

			properties.setProperty(key, value);
			properties.store(bw, value);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public static String get_root_path()
	{
		File directory = new File("");
		String exact_directory = "";
		try
		{
			exact_directory  = directory.getCanonicalPath();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return exact_directory;
	}




	

	public static byte[] reverse_byte(byte[] data)
	{
		byte[] Fuck = new byte[data.length];
		for (int time = 0;time < data.length;time++)
		{
			Fuck[time] = data[data.length - time - 1];

		}

		return Fuck;
	}

	public static long getfilelength(String file)
	{
		File f= new File(file);
		return f.length();
	}

	public static byte[] get_crc32(byte[] data)
	{
		CRC32 crc32 = new CRC32();
		crc32.update(data);
		return reverse_byte(str_to_byte(Long.toHexString(crc32.getValue())));
	}

	public static String getMD5(File file)
	{
		FileInputStream fileInputStream = null;
		try
		{
			MessageDigest MD5 = MessageDigest.getInstance("MD5");
			fileInputStream = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fileInputStream.read(buffer)) != -1)
			{
				MD5.update(buffer, 0, length);
			}
			return byte2HexString(MD5.digest()).replace(" ", "");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			try
			{
				if (fileInputStream != null)
				{
					fileInputStream.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static String GetMD5HashFromFile(String fileName)
	{

		File file = new File(fileName);

		return getMD5(file);
	}

	public static String GetMD5ToGuidHashFromFile(String fileName)
	{
		String md5 = GetMD5HashFromFile(fileName);
		return md5.substring(0, 8) + "-" + md5.substring(8, 12) + "-" + md5.substring(12, 16) + "-" + md5.substring(16, 20) + "-" + md5.substring(20, md5.length());
	}



	public static String http_dns(String host)
	{  
		try
		{  
			URL lll = new URL("http://119.29.29.29/d?dn=" + host);
			HttpURLConnection connection = (HttpURLConnection) lll.openConnection();// 打开连接  
			connection.setRequestMethod("GET");
			connection.connect();// 连接会话  
			// 获取输入流  
			BufferedReader br= new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));  
			String line;  
			StringBuilder sb = new StringBuilder();  
			while ((line = br.readLine()) != null)
			{// 循环读取流  
				sb.append(line);  
			}  
			br.close();// 关闭流
			connection.disconnect();// 断开连接  
			String hosts = sb.toString();
			if (hosts.contains(";"))
			{
				return hosts.split(";")[0];
			}
			else
			{
				return hosts;
			}
		}
		catch (Exception e)
		{  
			e.printStackTrace();
		}
		return null;
	}


	public static PictureStore uploadImage(PictureKeyStore keystore, QQUser user, int pictureid)
	{
		PictureStore store = null;

		for (PictureStore onestore: user.imageStoreCache)
		{


			if (onestore.pictureId == pictureid)
			{

				store = onestore;
				user.imageStoreCache.remove(onestore);

				break;
			}

		}
		String file = store.fileName;
	    URL u = null;
        HttpURLConnection con = null;
        //尝试发送请求
        try
		{
			u = new URL("http://" + Util.http_dns(Util.readConfig("upload_img_address")) + "/cgi-bin/httpconn?htcmd=0x6ff0071&ver=5515&term=pc&ukey=" + Util.byte2HexString(keystore.ukey).replace(" ", "") + "&filesize=" + getfilelength(file) + "&range=0&uin=" + user.uin + "&groupcode=" + store.groupUin);
			con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "binary/octet-stream");
            con.setRequestProperty("User-Agent", "QQClient");
            OutputStream outStream = con.getOutputStream();

			DataInputStream in = new DataInputStream(new FileInputStream(file));
            byte[] bufferOut = new byte[1024];
			int bytes = 0;
			// 每次读1KB数据,并且将文件数据写入到输出流中
			while ((bytes = in.read(bufferOut)) != -1)
			{
				outStream.write(bufferOut, 0, bytes);
			}
            outStream.flush();
            outStream.close();
            //读取返回内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;

			while ((line = reader.readLine()) != null)
			{
				System.out.println(line);
			}

        }
		catch (Exception e)
		{
            e.printStackTrace();
        } 

		return store;

	}



	public static long GetQQNumRetUint(String six)
	{
		return Long.parseLong(six.replace(" ", ""), 16);
	}


	public static byte[] Bufferedimg_tobytes(BufferedImage img, String type)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			ImageIO.write(img, type, baos);

			baos.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		byte[] imageInByte = baos.toByteArray();
		try
		{
			baos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return imageInByte;
	}

	public static BufferedImage get_img(String file_name)
	{
		BufferedImage img_to_send = null;
		File directory = new File("");
		String exact_directory = "";


		try
		{
			exact_directory  = directory.getCanonicalPath();
			img_to_send = ImageIO.read(new File(file_name));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return img_to_send;
	}

	public static byte[] constructxmlmessage(QQUser user, byte[] data)
	{

		ByteBuilder builder = new ByteBuilder();
		builder.writeBytes(Util.RandomKey(4));
		builder.writeBytes(Util.str_to_byte("0000000009008600"));
		builder.writeBytes(new byte[]{0x00,0x0c});
		builder.writeBytes(Util.str_to_byte("E5BEAEE8BDAFE99B85E9BB91"));
		builder.writeBytes(new byte[] { 0x00, 0x00, 0x14 });
		builder.writeShort(data.length + 11);
		builder.writeByte((byte) 0x01);
		builder.writeShort(data.length + 1);
		builder.writeByte((byte) 0x01);
		builder.writeBytes(data);
		builder.writeBytes(new byte[] { 0x02, 0x00, 0x04, 0x00, 0x00, 0x00, 0x4D });
		return builder.getDataAndDestroy();
	}

	public static byte[] constructmessage(QQUser user, byte[] data)
	{
		ByteBuilder builder = new ByteBuilder();
		builder.writeByte((byte)0x01);
		builder.writeShort((data.length + 3));
		builder.writeByte((byte)0x01);
		builder.writeShort(data.length);
		builder.writeBytes(data);

		return builder.getDataAndDestroy();
	}
	public static long ConvertQQGroupId(long code)
	{
		String group = String.valueOf(code);
		long left = Long.parseLong(group.substring(0, group.length() - 6));

		String right = "", gid = "";
		if (left >= 1 && left <= 10)
		{
			right = group.substring(group.length() - 6, group.length());
			gid = String.valueOf(left + 202) + right;
		}
		else if (left >= 11 && left <= 19)
		{
			right = group.substring(group.length() - 6, group.length());
			gid = String.valueOf(left + 469) + right;
		}
		else if (left >= 20 && left <= 66)
		{
			left = Long.parseLong(String.valueOf(left).substring(0, 1));
			right = group.substring(group.length() - 7, group.length());
			gid = String.valueOf(left + 208) + right;
		}
		else if (left >= 67 && left <= 156)
		{
			right = group.substring(group.length() - 6, group.length());
			gid = String.valueOf(left + 1943) + right;
		}
		else if (left >= 157 && left <= 209)
		{
			left = Long.parseLong(String.valueOf(left).substring(0, 2));
			right = group.substring(group.length() - 7, group.length());
			gid = String.valueOf(left + 199) + right;
		}
		else if (left >= 210 && left <= 309)
		{


			left = Long.parseLong(String.valueOf(left).substring(0, 2));

			right = group.substring(group.length() - 7, group.length());

			gid = String.valueOf(left + 389) + right;
		}
		else if (left >= 310 && left <= 499)
		{
			left = Long.parseLong(String.valueOf(left).substring(0, 2));
			right = group.substring(group.length() - 7, group.length());
			gid = String.valueOf(left + 349) + right;
		}
		else
		{
			return code;
		}

		return Long.parseLong(gid);
	}

	public static String getHostIP()
	{

		String hostIp = null;
		try
		{
			Enumeration nis = NetworkInterface.getNetworkInterfaces();
			InetAddress ia = null;
			while (nis.hasMoreElements())
			{
				NetworkInterface ni = (NetworkInterface) nis.nextElement();
				Enumeration<InetAddress> ias = ni.getInetAddresses();
				while (ias.hasMoreElements())
				{
					ia = ias.nextElement();
					if (ia instanceof Inet6Address)
					{
						continue;// skip ipv6
					}
					String ip = ia.getHostAddress();
					if (!"127.0.0.1".equals(ip))
					{
						hostIp = ia.getHostAddress();
						break;
					}
				}
			}
		}
		catch (SocketException e)
		{

			e.printStackTrace();
		}
		return hostIp;
	}

	public static byte[] random_byte(int size)
	{
		byte [] b=new byte[size];
		Random random=new Random();
		random.nextBytes(b);

		return b;
	}
	
	public static String getIpStringFromBytes(byte[] input)
	{
		String u1 = String.valueOf((int)input[0] & 0xff);
		String u2 = String.valueOf((int)input[1] & 0xff);
		String u3 = String.valueOf((int)input[2] & 0xff);
		String u4 = String.valueOf((int)input[3] & 0xff);
		return u1 + "." + u2 + "." + u3 + "." + u4;
	}

	public static String getGTK(String skey)
	{
		String arg = "tencentQQVIP123443safde&!%^%1282";
		List<Integer> list = new ArrayList<Integer>();
		int num = 5381;
		list.add(172192);
		int i = 0;
		for (int length = skey.length(); i < length; i++)
		{
			int num2 = (skey).charAt(i);
			list.add((num << 5) + num2);
			num = num2;
		}

		StringBuilder stringBuilder = new StringBuilder();
		for (i = 0; i < list.size(); i++)
		{
			stringBuilder.append(list.get(i).toString());
		}

		return Md5(stringBuilder + arg);
	}
	
	public static String Md5(String text)
	{

		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");

			byte[] bytes = md.digest(text.getBytes());
			String result = "";
			for (byte b : bytes)
			{
				result += String.format("%02x", b);
			}

			return result;
		}
		catch (NoSuchAlgorithmException e)
		{}
		return null;
	}

	public static byte[] MD5(String arg)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");


			byte[] bytes = md.digest(arg.getBytes());
			return bytes;
		}
		catch (NoSuchAlgorithmException e)
		{}
		return null;
	}

	public static byte[] MD5(byte[] arg)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");


			byte[] bytes = md.digest(arg);
			return bytes;
		}
		catch (NoSuchAlgorithmException e)
		{}
		return null;
	}

	public static String getBkn(String skey)
	{
		int hash = 5381;

		for (int i = 0, len = skey.length(); i < len; ++i)

			hash += (hash << 5) + skey.charAt(i);

		int gtkOrbkn = hash & 2147483647;

		return String.valueOf(gtkOrbkn);
	}


	public static byte[] iPStringToByteArray(String ip)
	{
		byte[] array = new byte[4];
		String[] array2 = ip.split("[.]");
		if (array2.length == 4)
		{
			for (int i = 0; i < 4; i++)
			{
				array[i] = (byte) Integer.parseInt(array2[i]);
			}
		}

		return array;
	}


	public static byte[] RandomKey()
	{
		byte[] key = new byte[16];
		new Random().nextBytes(key);
		return key;
	}
	public static byte[] RandomKey(int size)
	{
		byte[] key = new byte[size];
		new Random().nextBytes(key);
		return key;
	}

	public static byte[] str_to_byte(String str)
	{
        String replaceAll = str.replaceAll(" ", "");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(replaceAll.length() >> 1);
        for (int i = 0; i <= replaceAll.length() - 2; i += 2)
		{
            byteArrayOutputStream.write(Integer.parseInt(replaceAll.substring(i, i + 2), 16) & 255);
        }
        return byteArrayOutputStream.toByteArray();
    }



	public static String byte2HexString(byte[] bytes)
	{
        String hex= "";
        if (bytes != null)
		{
            for (Byte b : bytes)
			{
                hex += String.format("%02X", b.intValue() & 0xFF) + " ";
            }
        }
        return hex;

    }



	public static HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session)
		{
            return true;
        }
    };

	public static void trustAllHttpsCertificates() throws Exception
	{
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
			.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
																	.getSocketFactory());
	}

	public static class miTM implements javax.net.ssl.TrustManager,
	javax.net.ssl.X509TrustManager
	{
		public java.security.cert.X509Certificate[] getAcceptedIssuers()
		{
			return null;
		}

		public static boolean isServerTrusted(
			java.security.cert.X509Certificate[] certs)
		{
			return true;
		}

		public static boolean isClientTrusted(
			java.security.cert.X509Certificate[] certs)
		{
			return true;
		}

		public void checkServerTrusted(
			java.security.cert.X509Certificate[] certs, String authType)
		throws java.security.cert.CertificateException
		{
			return;
		}

		public void checkClientTrusted(
			java.security.cert.X509Certificate[] certs, String authType)
		throws java.security.cert.CertificateException
		{
			return;
		}
	}





}
