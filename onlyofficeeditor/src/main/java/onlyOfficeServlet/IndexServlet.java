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


package main.java.onlyOfficeServlet;

import main.java.util.ConfigManager;
import main.java.util.DocumentManager;
import main.java.util.FileUtility;
import main.java.util.ServiceConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import main.java.daoImpl.RichTextDao;
import main.java.entities.FileType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@WebServlet(name = "IndexServlet", urlPatterns = {"/IndexServlet"})
@MultipartConfig
public class IndexServlet extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("type");
        if(action == null)
        {
        	response.sendRedirect("./index.html");
            return;
        }

        DocumentManager.Init(request);
        PrintWriter writer = response.getWriter();
        
        switch (action.toLowerCase())
        {
            case "upload":                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
            	UploadToLocal(request, response, writer);
                break;
            case "convert":
                Convert(request, response, writer);
                break;
            case "track":
                Track(request, response, writer);
                break;
        }
    }


    /*
	 * Added by liuyy on 2018-08-15.
	 * when import file in onlineWriting page,Put uploading file to local file specified in 'settings.properties'
	 */
	private static void UploadToLocal(HttpServletRequest request, HttpServletResponse response, PrintWriter writer) {
		response.setContentType("text/plain");

		try {
			// 将文件的本地位置赋给此变量
			request.setCharacterEncoding("UTF-8");
			Part httpPostedFile = request.getPart("file");
			String fileName = "";

			for (String content : httpPostedFile.getHeader("content-disposition").split(";")) {
				if (content.trim().startsWith("filename")) {
					fileName = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");

				}
			}
			System.out.println("IndexServlet.UploadToLocal()打印文件名：" + fileName);

			long curSize = httpPostedFile.getSize();
			if (DocumentManager.GetMaxFileSize() < curSize || curSize < 0) {
				writer.write("{ \"error\": \"File size is incorrect\"}");
				return;
			}
			String curExt = FileUtility.GetFileExtension(fileName);
			if (!DocumentManager.GetFileExts().contains(curExt)) {
				writer.write("{ \"error\": \"File type is not supported\"}");
				return;
			}

			InputStream fileStream = httpPostedFile.getInputStream();

			// edited by huhaonan 2018-8-20
			String fileStoragePath = "";
			fileName = DocumentManager.GetCorrectNameFromLocal(fileName);
			fileStoragePath = DocumentManager.StoragePathLocal(fileName, null);
			System.out.println("IndexServlet.UploadToLocal() , storage path=" + fileStoragePath);
			System.out.println("IndexServlet.UploadToLocal() GetFileUri="+DocumentManager.GetFileUri(fileName));

			File file = new File(fileStoragePath);

			try (FileOutputStream out = new FileOutputStream(file)) {
				int read;
				final byte[] bytes = new byte[1024];
				while ((read = fileStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}

				out.flush();
			}

			writer.write("{ \"filename\": \"" + fileName + "\"}");

		} catch (IOException |

				ServletException e) {
			writer.write("{ \"error\": \"" + e.getMessage() + "\"}");
		} catch (Exception e) {
			writer.write("{ \"error\": \"" + e.getMessage() + "\"}");
		}
	}
	
    private static void Convert(HttpServletRequest request, HttpServletResponse response, PrintWriter writer)
    {
        response.setContentType("text/plain;charset=utf-8");
        try
        {
            String fileName = request.getParameter("filename");
            String fileUri = DocumentManager.GetFileUri(fileName);
            String fileExt = FileUtility.GetFileExtension(fileName);
            FileType fileType = FileUtility.GetFileType(fileName);
            String internalFileExt = DocumentManager.GetInternalExtension(fileType);
            
            if (DocumentManager.GetConvertExts().contains(fileExt))
            {
                String key = ServiceConverter.GenerateRevisionId(fileUri);

                String newFileUri = ServiceConverter.GetConvertedUri(fileUri, fileExt, internalFileExt, key, true);
                
                if (newFileUri == "")
                {
                    writer.write("{ \"step\" : \"0\", \"filename\" : \"" + fileName + "\"}");
                    return;
                }

                String correctName = DocumentManager.GetCorrectName(FileUtility.GetFileNameWithoutExtension(fileName) + internalFileExt);
                System.out.println("IndexServlet.Convert()  correctName："+correctName);

                URL url = new URL(newFileUri);
                java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();

                if (stream == null)
                {
                    throw new Exception("服务器错误：文档流是空的");
                }

                File convertedFile = new File(DocumentManager.StoragePath(correctName, null));
                try (FileOutputStream out = new FileOutputStream(convertedFile))
                {
                    int read;
                    final byte[] bytes = new byte[1024];
                    while ((read = stream.read(bytes)) != -1)
                    {
                        out.write(bytes, 0, read);
                    }
                    
                    out.flush();
                }

                connection.disconnect();

                //remove source file ?
                //File sourceFile = new File(DocumentManager.StoragePath(fileName, null));
                //sourceFile.delete();

                fileName = correctName;
            }

            writer.write("{ \"filename\" : \"" + fileName + "\"}");

        }
        catch (Exception ex)
        {
            writer.write("{ \"error\": \"" + ex.getMessage() + "\"}");
        }
    }

    private static void Track(HttpServletRequest request, HttpServletResponse response, PrintWriter writer)
    {
        String userAddress = request.getParameter("userAddress");
        String fileName = request.getParameter("fileName");
        System.out.println("IndexServlet.Track(): fileName ="+fileName);
        String storagePath = DocumentManager.StoragePath(fileName, userAddress);
        String body = "";

        try
        {
            Scanner scanner = new Scanner(request.getInputStream());
            scanner.useDelimiter("\\A");
            body = scanner.hasNext() ? scanner.next() : "";
            scanner.close();
        }
        catch (Exception ex)
        {
            writer.write("get request.getInputStream error:" + ex.getMessage());
            return;
        }

        if (body.isEmpty())
        {
            writer.write("empty request.getInputStream");
            return;
        }
 
        JSONParser parser = new JSONParser();
        JSONObject jsonObj;

        try
        {
        	System.out.println("IndexServlet.Track()  body="+body);
            Object obj = parser.parse(body);
            jsonObj = (JSONObject) obj;
        }
        catch (Exception ex)
        {
            writer.write("JSONParser.parse error:" + ex.getMessage());
            return;
        }

        long status = (long) jsonObj.get("status");

        int saved = 0;
        if(status == 2 || status == 3)//MustSave, Corrupted
        {
            String downloadUri = (String) jsonObj.get("url");
            System.out.println("IndexServlet.Track(): downloadUri："+downloadUri);

            try
            {
                URL url = new URL(downloadUri);
                java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();

                if (stream == null)
                {
                    throw new Exception("Stream is null");
                }

                File savedFile = new File(storagePath);
                try (FileOutputStream out = new FileOutputStream(savedFile))
                {
                    int read;
                    final byte[] bytes = new byte[1024];
                    while ((read = stream.read(bytes)) != -1)
                    {
                        out.write(bytes, 0, read);
                    }
                    
                    out.flush();
                }

                connection.disconnect();

            }
            catch (Exception ex)
            {
                saved = 1;
            }
        }
        /*
		 * Added by huhaonan on 2018-08-17 Refresh the changed files to local directory
		 * 2:直接关闭文本编辑器的status返回值 6:保存后的status返回值为2
		 */
		if (status == 2 || status == 6)// MustSave, Corrupted
		{
			System.out.println("保存文本或者直接关闭编辑界面！");
			String downloadUri = (String) jsonObj.get("url");
			System.out.println(downloadUri);

			try {
				URL url = new URL(downloadUri);
				java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
				InputStream stream = connection.getInputStream();

				if (stream == null) {
					throw new Exception("Stream is null");
				}

				String[] myFileNames = storagePath.split("/");
				int len = myFileNames.length;
				System.out.println("文件名：" + myFileNames[len - 1]);

//				String storagePathToLocal = ConfigManager.GetProperty("files.local.storage.path") + myFileNames[len - 1];
				String storagePathToLocal = ConfigManager.GetProperty("files.local.storage.path") + fileName;
				File savedFile = new File(storagePathToLocal);

				System.out.println("IndexServlet.Track()  storagePathToLocal:" + storagePathToLocal);

				try (FileOutputStream out = new FileOutputStream(savedFile)) {
					int read;
					final byte[] bytes = new byte[1024];
					while ((read = stream.read(bytes)) != -1) {
						out.write(bytes, 0, read);
					}

					out.flush();
				}
				connection.disconnect();
				
				//--Add by liuyy, updated doc index after file updating has been saved!
				RichTextDao richTextDao=new RichTextDao();
				richTextDao.updateSingleRichTextDocument(storagePathToLocal);

			} catch (Exception ex) {
				System.out.println("cause error"+ex.getMessage()+"\n"+ex.getStackTrace());
				saved = 1;
			}

		}
        System.out.println("before write,saved="+saved);
        writer.write("{\"error\":" + saved + "}");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo()
    {
        return "Handler";
    }
}
