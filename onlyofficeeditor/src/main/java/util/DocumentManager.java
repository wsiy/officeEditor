/*
 *
 * (c) Copyright Ascensio System Limited 2010-2017
 *
 * The MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
*/

package main.java.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.sun.xml.internal.txw2.Document;

import main.java.entities.FileType;


public class DocumentManager
{
    private static HttpServletRequest request;

    public static void Init(HttpServletRequest req)
    {
        request = req;
    }

    public static long GetMaxFileSize()
    {
        long size;
        
        try
        {
            size = Long.parseLong(ConfigManager.GetProperty("filesize-max"));
        }
        catch (Exception ex)
        {
            size = 0;
        }

        return size > 0 ? size : 5 * 1024 * 1024;
    }

    public static List<String> GetFileExts()
    {
        List<String> res = new ArrayList<>();
        
        res.addAll(GetViewedExts());
        res.addAll(GetEditedExts());
        res.addAll(GetConvertExts());
        return  res;
    }

    public static List<String> GetViewedExts()
    {
        String exts = ConfigManager.GetProperty("files.docservice.viewed-docs");
        return Arrays.asList(exts.split("\\|"));
    }

    public static List<String> GetEditedExts()
    {
        String exts = ConfigManager.GetProperty("files.docservice.edited-docs");
        return Arrays.asList(exts.split("\\|"));
    }

    public static List<String> GetConvertExts()
    {
        String exts = ConfigManager.GetProperty("files.docservice.convert-docs");
        return Arrays.asList(exts.split("\\|"));
    }

    public static String CurUserHostAddress(String userAddress)
    {
        if(userAddress == null)
        {
            try
            {
                userAddress = InetAddress.getLocalHost().getHostAddress();
            }
            catch (Exception ex)
            {
                userAddress = "";
            }
        }
        System.out.println("DocumentManager.CurUserHostAddress() userAddress "+userAddress);
        return userAddress.replaceAll("[^0-9a-zA-Z.=]", "_");
    }

    public static String StoragePath(String fileName, String userAddress)
    {
        String serverPath = request.getSession().getServletContext().getRealPath("");
        String storagePath = ConfigManager.GetProperty("storage-folder");
        String hostAddress = CurUserHostAddress(userAddress);
        String directory = serverPath  + storagePath + "\\";  
        File file = new File(directory);
        
        if (!file.exists())
        {
            file.mkdir();
        }
        directory = directory + hostAddress + "\\";
        file = new File(directory);

        if (!file.exists())
        {
            file.mkdir();
        }
        return directory + fileName;
    }



    
    /*
     * by liuyy
     * 2018-08-07
     * Get doc storage path with url encoded
     * The method is used by editor.jsp to interact with document server only
     * */
    public static String EncodedStoragePath(String fileName, String userAddress)
    {
        String serverPath = request.getSession().getServletContext().getRealPath("");
        String storagePath = ConfigManager.GetProperty("storage-folder");
        String hostAddress = CurUserHostAddress(userAddress);
        String directory = serverPath  + storagePath + "\\";  
        File file = new File(directory);
        
        if (!file.exists())
        {
            file.mkdir();
        }
        directory = directory + hostAddress + "\\";
        file = new File(directory);
        if (!file.exists())
        {
            file.mkdir();
        }
        String realPath = directory + fileName;
        return realPath.replaceAll("\\\\","/");
    }
    
    public static String GetCorrectName(String fileName)
    {
        String baseName = FileUtility.GetFileNameWithoutExtension(fileName);
        String ext = FileUtility.GetFileExtension(fileName);
        String name = baseName + ext;

        File file = new File(StoragePath(name, null));

        for (int i = 1; file.exists(); i++)
        {
            name = baseName + " (" + i + ")" + ext;
            file = new File(StoragePath(name, null));
        }

        return name;
    }

    public static String CreateDemo(String fileExt) throws Exception
    {
        String demoName = "新建DOCX文档." + fileExt;
        String fileName = GetCorrectName(demoName);

        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(demoName);
        if (stream == null) {
			System.out.println("stream is null");
		}else{
			System.out.println("not null");
		}
        System.out.println("DocumentManager.CreateDemo() getContextClassLoader.path="+Thread.currentThread().getContextClassLoader().toString());
        File file = new File(StoragePath(fileName, null));
        System.out.println("DocumentManager.CreateDemo() ,after new File, filename="+file.getAbsolutePath());
//        try (FileOutputStream out = new FileOutputStream(file))
//        {
//            int read;
//            final byte[] bytes = new byte[1024];
//            System.out.println("%%%%%%%%%%%%%%%%%%%%%in DocumentManager.CreateDemo, in try get input stream!!!!!"); 
//            while ((read = stream.read(bytes)) != -1)
//            {
//                out.write(bytes, 0, read);
//            }
//            out.flush();
//        }
        if(!file.exists()){
        	try {
        		file.createNewFile();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
        System.out.println("DocumentManager.CreateDemo() fileName="+fileName);
        return fileName;
    }
    
    public static String CreateDemoChangeName(String name, String fileExt) throws Exception
    {
        String demoName = name+"." + fileExt;
        String fileName = GetCorrectName(demoName);

        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(demoName);
        if (stream == null) {
			System.out.println("stream is null");
		}else{
			System.out.println("not null");
		}
        System.out.println("DocumentManager.CreateDemo() getContextClassLoader.path="+Thread.currentThread().getContextClassLoader().toString());
        File file = new File(StoragePath(fileName, null));
        System.out.println("DocumentManager.CreateDemo() ,after new File, filename="+file.getAbsolutePath());
        if(!file.exists()){
        	try {
        		file.createNewFile();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
        System.out.println("DocumentManager.CreateDemo() fileName="+fileName);
        return fileName;
    }
    
    public static String GetFileUri(String fileName) throws Exception
    {
        try
        {
            String serverPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            String storagePath = ConfigManager.GetProperty("storage-folder");
            String hostAddress = CurUserHostAddress(null);
            /*
             * by liuyy
             * 2018-08-07
             * Removed URL encoding for fileName, as for the case sample (1).docx -->sample+%281%29.docx, the encoded url cannot be 
             * recognized by document server
             * 
             * */
            // String filePath = serverPath + "/" + storagePath + "/" + hostAddress + "/" + URLEncoder.encode(fileName, java.nio.charset.StandardCharsets.UTF_8.toString()); 
            String filePath = serverPath + "/" + storagePath + "/" + hostAddress + "/" +fileName;//remove encoding
            System.out.println("DocumentManager.GetFileUri(), return "+filePath);
            return filePath;
        }
        catch (Exception e)
        {
            throw new AssertionError("Exception occured when return filepath in GetFileUri!");
        }
    }

    public static String GetServerUrl()
    {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    public static String GetServerUrlNew()
    {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
    }
    
    public static String GetCallback(String fileName)
    {
        String serverPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        String hostAddress = CurUserHostAddress(null);
        try
        {
            String query = "?type=track&fileName=" + URLEncoder.encode(fileName, java.nio.charset.StandardCharsets.UTF_8.toString()) + "&userAddress=" + URLEncoder.encode(hostAddress, java.nio.charset.StandardCharsets.UTF_8.toString());
		    ////////////////liuyy
            System.out.println("DocumentManager.GetCallback() return "+serverPath + "/IndexServlet" + query);
            return serverPath + "/IndexServlet" + query;
        }
        catch (UnsupportedEncodingException e)
        {
            throw new AssertionError("UTF-8 is unknown");
        }
    }

    public static String GetInternalExtension(FileType fileType)
    {
        if(fileType.equals(FileType.Text))
            return ".docx";

        if(fileType.equals(FileType.Spreadsheet))
            return ".xlsx";

        if(fileType.equals(FileType.Presentation))
            return ".pptx";

        return ".docx";
    }
    
	/*
	 * Added by liuyy on 2018-08-15 Get correct file name from local storage path
	 * specified in settings.properties
	 */
	public static String GetCorrectNameFromLocal(String fileName) {
		String baseName = FileUtility.GetFileNameWithoutExtension(fileName);
		String ext = FileUtility.GetFileExtension(fileName);
		String name = baseName + ext;

		File file = new File(StoragePathLocal(name, null));

		for (int i = 1; file.exists(); i++) {
			name = baseName + "(" + i + ")" + ext;//the space before '(' must be added,or can't upload file with same name.
			file = new File(StoragePathLocal(name, null));
		}
		System.out.println("DocumentManager.GetCorrectNameFromLocal(), return "+name);
		return name;
	}

	/*
	 * Added by liuyy on 2018-08-15 Get local storage path specified in
	 * settings.properties
	 */
	public static String StoragePathLocal(String fileName, String userAddress) {
		String storagePath = ConfigManager.GetProperty("files.local.storage.path");
		String directory = storagePath;
		File file = new File(directory);

		if (!file.exists()) {
			file.mkdir();
		}
		System.out.println("DocumentManager.StoragePathLocal(), local storage path=" + directory + fileName);
		return directory + "/"+ fileName;
	}
	
}