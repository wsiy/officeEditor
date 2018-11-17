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

import main.java.util.DocumentManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DownloadServlet", urlPatterns = {"/DownloadServlet"})
public class DownloadServlet extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception
    {
	   request.setCharacterEncoding("UTF-8");
	   String fileNameWithExt = "";
	   if (request.getParameterMap().containsKey("fileNameWithExt"))
		{
			fileNameWithExt = request.getParameter("fileNameWithExt");
		}
	   System.out.println("DownloadServlet.processRequest(),fileNameWithExt="+ fileNameWithExt);
   
	   OutputStream o = response.getOutputStream();  
	   byte b[] = new byte[1024];  
	   
	   DocumentManager.Init(request);
	   // the file to download.  
	   File fileLoad = new File(DocumentManager.StoragePath(fileNameWithExt, null));
       if(!fileLoad.exists()){
    	   System.out.println("--in DownloadServlet, the file fileNameWithExt to be downloaded does not exist!");
    	   return;
	   }
	   System.out.println("--in DownloadServlet, fileLoad.getAbsolutePath()="+fileLoad.getAbsolutePath());
	   response.setHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileNameWithExt,"UTF-8"));
	   response.setContentType("multipart/form-data");  
	   // get the file length.  
	   long fileLength = fileLoad.length();  
	   String length = String.valueOf(fileLength);  
	   response.setHeader("Content_Length", length);  
	   FileInputStream in = new FileInputStream(fileLoad);  
	   int n = 0;  
	   while ((n = in.read(b)) != -1) {  
	       o.write(b, 0, n);  
	   }  
	   in.close();
     } 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try{
        	processRequest(request, response);
        }catch(Exception e){
			System.out.println(e.getMessage());
		}
    }

    @Override
    public String getServletInfo()
    {
        return "Editor page";
    }
}
