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


package main.java.entities;

import javax.servlet.http.HttpServletRequest;

import main.java.util.DocumentManager;
import main.java.util.FileUtility;
import main.java.util.ServiceConverter;

/**
 * used in onlyOffice API
 * @author wsy
 *
 */
public class FileModel
{
    private String FileName;
    private Boolean TypeDesktop;

    private static HttpServletRequest request;
    
    public static void Init(HttpServletRequest req)
    {
        request = req;
    }
    
    public String GetFileName()
    {
        return FileName;
    }

    public void SetFileName(String fileName)
    {
        this.FileName = fileName;
    }
    
    public Boolean GetTypeDesktop()
    {
        return TypeDesktop;
    }

    public void SetTypeDesktop(Boolean typeDesktop)
    {
        this.TypeDesktop = typeDesktop;
    }

    public String GetFileUri() throws Exception
    {
    	//--------add by liuyy, to avoid null request issue when edit a doc
    	DocumentManager.Init(request);
    	
        return DocumentManager.GetFileUri(FileName);
    }

    /*
     * Added by liuyy on 2018-07-31
     * 
     * Get File storage url
     * */
    public String GetFileStorageURL() throws Exception
    {	
    	String fileStorageURL = DocumentManager.EncodedStoragePath(FileName, null);
    	System.out.println("FileModel.GetFileStorageURL(),GetFileStorageURL with encoded:"+fileStorageURL);
        return fileStorageURL;
    }
    
    public String CurUserHostAddress()
    {
        return DocumentManager.CurUserHostAddress(null);
    }

    public String GetDocumentType()
    {
        return FileUtility.GetFileType(FileName).toString().toLowerCase();
    }

    public String GetKey()
    {
    	String key = ServiceConverter.GenerateRevisionId(DocumentManager.CurUserHostAddress(null) + "/" + FileName);
    	System.out.println("FileModel.GetKey(),return "+key);
        return key;
    }

    public String GetCallbackUrl()
    {
        return DocumentManager.GetCallback(FileName);
    }

    public String GetServerUrl()
    {
        return DocumentManager.GetServerUrl();
    }
    
    public String GetServerUrlNew()
    {
        return DocumentManager.GetServerUrlNew();
    }
}
