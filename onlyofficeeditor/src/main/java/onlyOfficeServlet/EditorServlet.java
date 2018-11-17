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

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import main.java.entities.FileModel; 

@WebServlet(name = "EditorServlet", urlPatterns = {"/EditorServlet"})
public class EditorServlet extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	request.setCharacterEncoding("UTF-8");
    	response.setContentType("application/json; charset=utf-8");
        //response.setContentType("text/html");
        String fileName = "";
        if (request.getParameterMap().containsKey("fileName"))
        {
             fileName = request.getParameter("fileName");
        }
        System.out.println("EditorServlet.processRequest(),fileName="+ fileName);
   
        String fileExt = null;
        if (request.getParameterMap().containsKey("fileExt"))
        {
             fileExt = request.getParameter("fileExt");
        }    
        String fileNameT = null;
        if (request.getParameterMap().containsKey("newFileName"))
        {
        	fileNameT = request.getParameter("newFileName");
        }  
//       创建新文档的时候，这个fileExt不为空，fileName=null。
//       编辑已有文档的时候，fileName不为空，fileExt=null
        if (fileExt != null)
        {
            try
            {
                DocumentManager.Init(request);
               // fileName = DocumentManager.CreateDemo(fileExt);
                fileName = DocumentManager.CreateDemoChangeName(fileNameT,fileExt);
                System.out.println("@@@@@@@@@@@@@@@@@in EditorServlet, after CreateDemo, fileName="+fileName);
            }
            catch (Exception ex)
            {
            	System.out.println("@@@@@@@@@@@@@@@@@in EditorServlet, in CreateDemo exception catch!!!!!!--->"+ex.toString());
                response.getWriter().write("Error: " + ex.getMessage());    
            }
        }
        System.out.println("EditorServlet.processRequest(),after init,fileName="+fileName+",fileExt="+fileExt);
        String mode = "";
        if (request.getParameterMap().containsKey("mode"))
        {
             mode = request.getParameter("mode");
             System.out.println("我们的"+mode);
        }
        Boolean desktopMode = !"embedded".equals(mode);
        
//	    wsy:create json data for ajax request
		JSONObject fileJson =new JSONObject();
        JSONObject responseData =new JSONObject();

//      create file model,for following function,such as GetCallbackUrl、GetServerUrlNew...
        FileModel file = new FileModel();
        //------by liuyy, to avoid request null issue when edit a doc
        file.Init(request);
        
        file.SetFileName(fileName);
        file.SetTypeDesktop(desktopMode);
        
        try {
        	fileJson.put("fileUri", file.GetFileUri());
	        fileJson.put("desktopMode", desktopMode);
	        fileJson.put("fileName", fileName);
	        fileJson.put("key", file.GetKey());
	        fileJson.put("documentType", file.GetDocumentType());
	        fileJson.put("fileType", FileUtility.GetFileExtension(fileName).replace(".", ""));
	        fileJson.put("callBackUrl", file.GetCallbackUrl());
	        fileJson.put("userID", file.CurUserHostAddress());
	        fileJson.put("gobackUrl", file.GetServerUrlNew()+"/IndexServlet");

	        responseData.put("file", fileJson);
	        responseData.put("mode", mode);
	        responseData.put("curUserHostAddress",file.CurUserHostAddress());
	        responseData.put("editorConfigMode", DocumentManager.GetEditedExts().contains(FileUtility.GetFileExtension(file.GetFileName())) && !"view".equals(request.getAttribute("mode")) ? "edit" : "view");
	        responseData.put("permissionsEdit", Boolean.toString(DocumentManager.GetEditedExts().contains(FileUtility.GetFileExtension(file.GetFileName()))).toLowerCase());
	        responseData.put("createdTime", new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
	        responseData.put("type",desktopMode ? "desktop" : "embedded");
	        
	        responseData.put("docserviceApiUrl",ConfigManager.GetProperty("files.docservice.url.api"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("EditorServlet.processRequest(),before sent request, fileName="+fileName);
        //向前台的页面输出结果
        System.out.println("EditorServlet.processRequest(),return responseData="+responseData);
        PrintWriter out=response.getWriter();
        out.print(responseData); //或者        out.print(responseData.toString());
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
        return "Editor page";
    }
}
